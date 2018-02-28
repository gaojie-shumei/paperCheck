package com.csuweb.PaperCheck.web.biz.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.core.conf.GlobalConfig;
import com.csuweb.PaperCheck.web.biz.CheckDocBiz;
import com.csuweb.PaperCheck.web.dao.CheckDocMapper;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;
import com.csuweb.PaperCheck.web.util.ClusterPool;
import com.csuweb.PaperCheck.web.util.ComputePool;

@Service
public class CheckDocBizImpl implements CheckDocBiz {

	@Resource
	private CheckDocMapper checkdocmapper;
	@Autowired
	private ClusterPool clusterPool;
	@Autowired
	private ComputePool computePool;
	
	/**
	 * 与聚类相关的全局变量
	 */
	int xsArticleNum = 40;// 所取相似文章个数
	int maxIndexNum = 10;//
	String clusterState = GlobalConfig.getClusterStart();
	String nextState = GlobalConfig.getClusterEnd();
	String wrongState = GlobalConfig.getClusterWrong();
	
	/**
	 * 与计算相关的全局变量
	 */
	String computerState=GlobalConfig.getComputerStart();
	String computenextState=GlobalConfig.getComputerEnd();
	String computewrongState=GlobalConfig.getComputerWrong();
	
	/**
	 * 过滤字符串中4字长UTF-8编码字符
	 * @param str
	 * @return
	 */
	public String filterUtf8mb4(String str) {
        final int LAST_BMP = 0xFFFF;
        if(str==null||str.equals("")){
        	return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            int codePoint = str.codePointAt(i);
            if (codePoint < LAST_BMP) {
                sb.appendCodePoint(codePoint);
            } else {
                i++;
            }
        }
        return sb.toString();
    }
	
	@Override
	public int addCheckDoc(CheckDocWithBLOBs checkDocWithBlobs) {
		// TODO Auto-generated method stub
		return checkdocmapper.insert(checkDocWithBlobs);
	}

	@Override
	public int selectCountCheckDoc(String state, String state2) {
		return checkdocmapper.selectCountByState(state, state2);
	}
	@Override
	public List<CheckDocWithBLOBs> selectCheckDoc(String state,String state2){
		return checkdocmapper.selectCheckDoc(state, state2);
	}
	@Override
	public int updateCheckDoc(CheckDocWithBLOBs checkDoc){
		return checkdocmapper.updateByPrimaryKey(checkDoc);
	}
	
	@Override
	public List<CheckDocWithBLOBs> selectCheckDocByUserId(String userid){
		return checkdocmapper.selectCheckDocByUserId(userid);
	}
	
	@Override
	public CheckDocWithBLOBs selectCheckdocByPrimaryKey(String guid) {
		// TODO Auto-generated method stub
		return checkdocmapper.selectByPrimaryKey(guid);
	}
	
	public int delRecordByPrimaryKey(String guid){
		return checkdocmapper.deleteByPrimaryKey(guid);
	}

	/**
	 * 文本聚类
	 */
	//自己的算法
	@Scheduled(cron = "0/30 * *  * * ? ") // 每30秒执行一次
	public void cluster() {
//		System.out.println("聚类开始");
//		boolean state = true;
		try {
			if (clusterPool.getCount() > 0){
				//如果聚类线程还在运行
				return;
			}
			
			if (selectCountCheckDoc(GlobalConfig.getClusterOn(),GlobalConfig.getComputerStart()) > maxIndexNum){
				//如果索引库个数较多，暂停写入索引
				return;
			}
			List<CheckDocWithBLOBs> checkDocs = selectCheckDoc(clusterState, null);
			if (checkDocs.size() == 0){
//				state = false;
				return;
			}else{
				for (CheckDocWithBLOBs checkdoc : checkDocs) {
					if (checkdoc.getStartstmp() == null){
						checkdoc.setStartstmp(new Date());
					}
					checkdoc.setState(GlobalConfig.getClusterOn());
					updateCheckDoc(checkdoc);
				}
				clusterPool.excute(checkDocs, nextState, wrongState, xsArticleNum);
				System.out.println("聚类成功结束");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return state;
	}

	/**
	 * 文本爬虫，在网络上进行文本聚类，未实现
	 * 
	 */
	@Scheduled(cron = "0/30 * *  * * ? ") // 每30秒执行一次
	public void clawer() {
	
	}

	/**
	 * 文本计算并生成查重报告，在聚类和爬虫之后执行
	 */
	@Scheduled(cron = "0/30 * *  * * ? ") // 每30秒执行一次
	public void compute() {
//		System.out.println("计算开始");
//		boolean state = true;
		try {
			if(computePool.getCount()>0){
				//如果计算线程还在运行
				return;
			}
			List<CheckDocWithBLOBs> checkDocs=selectCheckDoc(computerState,null);
			if(checkDocs.size()==0){
				return;
			}else{
				for(CheckDocWithBLOBs checkdoc:checkDocs){
					checkdoc.setState(GlobalConfig.getComputerOn());
					updateCheckDoc(checkdoc);
				}
				computePool.excute(checkDocs,computenextState,computewrongState);
				System.out.println("计算及生成报告结束");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			state = false;
		}
//		return state;
	}

	/**
	 * 每份查重报告只保存15天
	 */
	@Scheduled(cron = "0/30 * *  * * ? ") // 每30秒执行一次
	public void autoDeleteReport(){
		List<CheckDocWithBLOBs> checkDocs=selectCheckDoc(GlobalConfig.getReportEnd(),null);
		for (CheckDocWithBLOBs checkdoc : checkDocs) {
			Timestamp generateReportTime = checkdoc.getTimestamp();
			Timestamp nowtime = new Timestamp(new Date().getTime());
			if((nowtime.getTime()-generateReportTime.getTime())/(1000*60*60*24)>15){
				String projectPath = this.getClass().getClassLoader().getResource("").getPath();
			    projectPath = projectPath.substring(0,projectPath.lastIndexOf("/WEB-INF"));
			    String compeletePath = projectPath+"/"+checkdoc.getPath();
			    File f = new File(compeletePath);
			    File parentFile = f.getParentFile();
			    File parentParentFile = parentFile.getParentFile();
			    if(f.exists()){
			    	f.delete();
			    }
			    if(parentFile.isDirectory()&&parentFile.listFiles().length==0){
			    	parentFile.delete();
			    }
			    if(parentParentFile.isDirectory()&&parentParentFile.listFiles().length==0){
			    	parentParentFile.delete();
			    }
			}
		}
	}

}
