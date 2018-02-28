package com.csuweb.PaperCheck.web.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.XsPaper;
import com.csuweb.PaperCheck.core.common.ParagraphAndSentenceAnalyzer;
import com.csuweb.PaperCheck.core.conf.GlobalConfig;
import com.csuweb.PaperCheck.web.biz.CheckDocBiz;
import com.csuweb.PaperCheck.web.biz.UserBiz;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;
import com.csuweb.PaperCheck.web.model.User;
import com.csuweb.PaperCheck.web.service.ComputeService;
import com.csuweb.PaperCheck.web.service.GenerateReportService;

//import com.nesei.xmccCheck.bean.ArticleInfo;
//import com.nesei.xmccCheck.bean.XsPaper;
//import com.nesei.xmccCheck.common.ParagraphAndSentenceAnalyzer;
//import com.nesei.xmccCheck.conf.GlobalConfig;
//import com.nesei.xmccCheck.model.CheckDocWithBLOBs;
//import com.nesei.xmccCheck.service.CheckDocService;
//import com.nesei.xmccCheck.service.ComputeService;
//import com.nesei.xmccCheck.service.GenerateReportService;
//import com.nesei.xmccCheck.util.CommonUtil;
//import com.nesei.xmccCheck.util.ESUtils;
//import com.nesei.xmccCheck.util.JsonUtil;
//import com.nesei.xmccCheck.util.XMLUtil;

@Component
public class ComputePool {
	String nextState;
	String wrongState;	
	private ThreadPoolExecutor pools=(ThreadPoolExecutor) Executors.newFixedThreadPool(4); 
	private static Log log = LogFactory.getLog(ComputePool.class);
	@Autowired
	private UserBiz userbiz;
	@Autowired
	private CheckDocBiz checkDocService;
	@Autowired
	private ParagraphAndSentenceAnalyzer paragraphAndSentenceAnalyzer;
	@Autowired
	private GenerateReportService	generateReportService;
	@Autowired
	private ComputeService computerService;
	
	public void excute(List<CheckDocWithBLOBs> checkDocs,String state1,String state2){
		this.nextState=state1;
		this.wrongState=state2;
		for(CheckDocWithBLOBs checkDoc:checkDocs){
			pools.execute(new ComputerThread(checkDoc));
//			computer(checkDoc);
		}
	}
	
	public int getCount(){
		return CommonUtil.getCount(pools);
	}
	
	private void computer(CheckDocWithBLOBs checkDoc){
		int updateState;
		String DCPaperid=checkDoc.getGuid();
		ArticleInfo articleInfo=null;
		List<XsPaper> xsPapers=null;
		try{			
			articleInfo=paragraphAndSentenceAnalyzer.DCSplitToArticle(DCPaperid, checkDoc.getTitle(), checkDoc.getContent());						
			xsPapers= computerService.computer(articleInfo);	
//			String xsPaperString=JsonUtil.generateJson(xsPapers);
//			String xmlResult=XMLUtil.ArticleInfoToXML(articleInfo);
//			checkDoc.setXmlcontent(xmlResult);
//			checkDoc.setState(nextState);	
//			checkDoc.setXspaper(xsPaperString);
//			checkDoc.setXsl(String.valueOf(articleInfo.getBasic().getXsl()*100));
			checkDoc.setXsl(String.format("%.2f",articleInfo.getBasic().getXsl()*100));

		}catch(Exception e){
			e.printStackTrace();
			log.error(checkDoc.getTitle()+"计算：错误--"+e);
			checkDoc.setState(wrongState);
			checkDocService.updateCheckDoc(checkDoc);
		}finally {
			ESUtils.deleteIndex(DCPaperid);
		}
		//生成查重报告
		try {
//			ArticleInfo old_articleInfo=paragraphAndSentenceAnalyzer.DCSplitToArticle(checkDoc.getGuid(), checkDoc.getTitle(), checkDoc.getContent());
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");  
			String dateString=sdf.format(checkDoc.getTimestamp());
		    String projectPath = this.getClass().getClassLoader().getResource("").getPath();
		    projectPath = projectPath.substring(0,projectPath.lastIndexOf("/WEB-INF"));
//		    System.out.println(projectPath);
		    User user = userbiz.selUserByPrimaryKey(checkDoc.getUserid());
		    String parentPath = projectPath +"/paperReport/"+user.getLoginname();
			String html= generateReportService.writeHtml(checkDoc.getContent(),articleInfo,dateString,xsPapers,parentPath,checkDoc.getTitle());
			checkDoc.setState(GlobalConfig.getReportEnd());
			checkDoc.setHtml(html);
			checkDoc.setEndstmp(new Date());
			checkDoc.setPath("paperReport/"+user.getLoginname()+"/"+dateString+"/"+articleInfo.getBasic().getXMTITLE()+".html");
			updateState=checkDocService.updateCheckDoc(checkDoc);
			log.info(checkDoc.getTitle()+"生成报告："+(updateState==1?"完成":"错误")+new Date().toLocaleString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(checkDoc.getTitle()+"生成报告：错误--"+e);
			checkDoc.setXspaper("");
			checkDoc.setHtml("");
			checkDoc.setState(GlobalConfig.getReportWrong());
			checkDocService.updateCheckDoc(checkDoc);
		}				
		
	}
	
	class ComputerThread implements Runnable{
		private CheckDocWithBLOBs checkDoc;
		
		public ComputerThread(CheckDocWithBLOBs c){
			this.checkDoc=c;
		}
		
		public void run() {
			computer(checkDoc);	
		}		
	}
}
