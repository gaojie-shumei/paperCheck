package com.csuweb.PaperCheck.web.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csuweb.PaperCheck.core.common.ExtractTxt;
import com.csuweb.PaperCheck.web.model.User;

@Controller
@RequestMapping("/extract")
public class ExtractController {
	@Resource
	private ExtractTxt extractTxt;
	@RequestMapping(value={"/extractTxt"},method=RequestMethod.POST)
	public @ResponseBody JSONObject extractTxt(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String content = "";
		try {
			String filename = request.getParameter("filename");
			String path = request.getSession().getServletContext().getRealPath("/uploadFiles");
//       System.out.println(path);
			User user = (User) request.getSession().getAttribute("user");
			File targetFile = new File(path+"/"+user.getLoginname(),filename);
			if(!targetFile.exists()){
				json.put("content", "");
				return json;
			}
			/**
			 * 提取文本内容
			 */
			content = extractTxt.extractTxt(path+"/"+user.getLoginname()+"/"+filename);
//			System.out.println(content);
			/**
			 * 提取完后，销毁文件
			 */
//			targetFile.deleteOnExit();
			targetFile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("提取文本内容出错！");
		}
		json.put("content", content);
		return json;
		
	}
}
