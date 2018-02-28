package com.csuweb.PaperCheck.web.service;

import java.util.List;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.XsPaper;


public interface GenerateReportService {
	public String writeHtml(String content,ArticleInfo articleInfo ,String submitTime, List<XsPaper> xsPapers,String parentPath,String papertitle) throws Exception;
}
