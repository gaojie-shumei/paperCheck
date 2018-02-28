package com.csuweb.PaperCheck.web.controller;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csuweb.PaperCheck.core.bean.Article;
import com.csuweb.PaperCheck.core.common.ExtractTxt;
import com.csuweb.PaperCheck.web.biz.CheckDocBiz;
import com.csuweb.PaperCheck.web.model.User;
import com.csuweb.PaperCheck.web.util.ESUtils;



@Controller
@RequestMapping("/history")
public class ArticleController {
	@Resource
	ExtractTxt extractTxt;
	@Resource
	CheckDocBiz checkDocbiz;
	
	private static ArrayList<Article> articles = new ArrayList<Article>();
	Article article=null;
	private static Log log = LogFactory.getLog(ArticleController.class);
	//添加证据文档
	@RequiresPermissions(value = { "history:add" })//证据文档（添加查重库文档权限）
	@RequestMapping(value={"/bashaddhistory"},method=RequestMethod.POST)
	public @ResponseBody String addHistory(HttpServletRequest request){
//		JSONObject json = new JSONObject();
		boolean state = true;
		String filesnameStr = request.getParameter("filenames");
		String[] filenamearr = filesnameStr.split(":");
		User user = (User) request.getSession().getAttribute("user");
		String path = request.getSession().getServletContext().getRealPath("/uploadFiles");
		
		for (String filename : filenamearr) {
			boolean isnormal = true;
			File targetFile = new File(path+"/"+user.getLoginname(),filename);
			if(targetFile.exists()){
				try {
					article = extractTxt.extractToArticle(path+"/"+user.getLoginname()+"/"+filename);
					article.setContent(checkDocbiz.filterUtf8mb4(article.getContent()));
					article.setTitle(checkDocbiz.filterUtf8mb4(article.getTitle()));
				} catch (Exception e) {
					isnormal = false;	
					log.error("历史文档提取错误："+e);
				}
				
				if (article != null&&article.getContent().length()>10&&isnormal) {
					articles.add(article);
				}
				targetFile.delete();
			}
		}
		try {
			if(articles!=null&&articles.size()>0){
				ESUtils.indexArticle("articles", "article", articles);
			}else{
				state = false;
			}
		} catch (Exception e) {
			log.error("历史文档写入索引错误："+e);
			state = false;
		}
		articles.clear();
		log.info("articles 索引完成！");
//		json.put("state", state);
		if(state==true){
			return "success";
		}
		return "failed";
	}
}
