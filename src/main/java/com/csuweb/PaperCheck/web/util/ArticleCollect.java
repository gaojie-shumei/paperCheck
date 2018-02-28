package com.csuweb.PaperCheck.web.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("ArticleCollect")
public class ArticleCollect {
/*	private static Log log = LogFactory.getLog(ArticleCollect.class);
	String crawlerOn=GlobalConfig.getCrawlerOn();
	String clusterOn=GlobalConfig.getClusterOn();
	String computerState=GlobalConfig.getComputerOn();
	private int minuteThreshold=30;
	@Autowired
	private CheckDocService checkDocService;
	
//	@Scheduled(cron="0/ 0/3 * * * ? ")   //每30秒执行一次
	public void start(){
		log.info("ArticleCollect begin!");
		reseState(GlobalConfig.getCrawlerOn(),GlobalConfig.getCrawlerStart());
		reseState(GlobalConfig.getClusterOn(),GlobalConfig.getClusterStart());
		reseState(GlobalConfig.getComputerOn(),GlobalConfig.getComputerStart());
	}
	
	public void reseState(String on,String start){
		List<CheckDocWithBLOBs> checkDocs=checkDocService.selectCheckDoc(on,null);
		
		for(CheckDocWithBLOBs checkdoc:checkDocs){
			Date startTime=checkdoc.getStartstmp();
			if(startTime==null)continue;
			Calendar startC=Calendar.getInstance(); 
			startC.setTime(startTime); 
			
			Calendar currentC=Calendar.getInstance();   
			currentC.setTime(new Date()); 
			
			if(startC.get(Calendar.MONTH)!=currentC.get(Calendar.MONTH)||startC.get(Calendar.DATE)!=currentC.get(Calendar.DATE)||
					startC.get(Calendar.HOUR)!=currentC.get(Calendar.HOUR)||(startC.get(Calendar.MINUTE)-currentC.get(Calendar.MINUTE)>minuteThreshold)){
				checkdoc.setState(start);
				checkDocService.updateCheckDoc(checkdoc);
			}
		}
		
	}*/
}
