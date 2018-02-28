package com.csuweb.PaperCheck.web.service;

import java.util.Hashtable;
import java.util.List;

import com.csuweb.PaperCheck.core.bean.Article;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;


public interface ClusterService {
	public void clusterArticle(CheckDocWithBLOBs checkDoc,Hashtable<String, Article> htArticles,Hashtable<String, Integer> ht) throws Exception; 
}
