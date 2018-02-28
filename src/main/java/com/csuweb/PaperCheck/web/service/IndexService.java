package com.csuweb.PaperCheck.web.service;

import java.io.IOException;
import java.util.List;

import com.csuweb.PaperCheck.core.bean.Article;


public interface IndexService {
	public void index(String DCId,Article article) throws  Exception;
	public void index(String DCId,List<Article> articles) throws  Exception;
}
