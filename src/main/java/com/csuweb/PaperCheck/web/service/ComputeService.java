package com.csuweb.PaperCheck.web.service;

import java.util.List;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.XsPaper;


public interface ComputeService {
	public List<XsPaper> computer(ArticleInfo articleInfo) throws Exception;
}
