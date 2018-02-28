package com.csuweb.PaperCheck.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csuweb.PaperCheck.core.bean.Article;
import com.csuweb.PaperCheck.web.biz.CheckDocBiz;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;
import com.csuweb.PaperCheck.web.service.ClusterService;
import com.csuweb.PaperCheck.web.service.IndexService;

@Component
public class ClusterPool {
	int xsArticleNum;
	String nextState;
	String wrongState;	
	private static Log log = LogFactory.getLog(ClusterPool.class);
	private ThreadPoolExecutor pools=(ThreadPoolExecutor) Executors.newFixedThreadPool(3); 
	
	@Autowired
	private CheckDocBiz checkDocService;
	@Autowired
	private ClusterService clusterService;	
	@Autowired
	private IndexService indexService;
		
	public void excute(List<CheckDocWithBLOBs> checkDocs,String state1,String state2,int num){
		this.nextState=state1;
		this.wrongState=state2;
		this.xsArticleNum=num;
		for(CheckDocWithBLOBs checkDoc:checkDocs){	
			pools.execute(new ClusterThread(checkDoc));
//			cluster(checkDoc);
		}
	}
	
	public int getCount(){
		return CommonUtil.getCount(pools);
	}
	
	private void cluster(CheckDocWithBLOBs checkDoc){
		Hashtable<String, Article> htArticles=new Hashtable<String, Article>();
		final Hashtable<String, Integer> ht=new Hashtable<String, Integer>();
		List<Article> xsArticles=new ArrayList<Article>();
		String DCPaperid=checkDoc.getGuid();
		try{
			long time=System.currentTimeMillis();
			clusterService.clusterArticle(checkDoc, htArticles, ht);
//			log.info("聚类:"+(System.currentTimeMillis()-time));
			log.info(checkDoc.getTitle()+"\t聚类:"+new Date().toLocaleString());
			List<String> v = new ArrayList<String>(ht.keySet()); 
			Collections.sort(v, new Comparator<Object>(){			
				public int compare(Object arg0,Object arg1)	{  
					if(ht.get(arg1)-ht.get(arg0)>0){
						return 1;
					}else if(ht.get(arg1)-ht.get(arg0)==0) {
						return 0;
					}else {
						return -1;
					}
			    } 
			  });
			int num=v.size()<xsArticleNum?v.size():xsArticleNum;
			 for(int i=0;i<num;i++){				 
				 String xmguid=v.get(i);
				 xsArticles.add(htArticles.get(xmguid));
		     }	
//			 System.out.println("排序："+(System.currentTimeMillis()-time1));
			 long time2=System.currentTimeMillis();
			indexService.index(DCPaperid, xsArticles);	
//			log.info("cluster-写索引："+(System.currentTimeMillis()-time2));
			log.info("cluster-写索引："+new Date().toLocaleString());
			checkDoc.setState(nextState);
			checkDocService.updateCheckDoc(checkDoc);			
		}catch(Exception e){
			e.printStackTrace();
			log.error(checkDoc.getTitle()+"聚类：错误--"+e);
			checkDoc.setState(wrongState);
			checkDocService.updateCheckDoc(checkDoc);
		}
	}
	
	class ClusterThread implements Runnable{
		private CheckDocWithBLOBs checkDoc;
		
		public ClusterThread(CheckDocWithBLOBs c){
			this.checkDoc=c;
		}
		
		public void run() {
			cluster(checkDoc);		
		}		
	}
}
