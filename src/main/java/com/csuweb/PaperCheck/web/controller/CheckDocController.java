package com.csuweb.PaperCheck.web.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csuweb.PaperCheck.core.common.ExtractTxt;
import com.csuweb.PaperCheck.core.conf.GlobalConfig;
import com.csuweb.PaperCheck.web.biz.CheckDocBiz;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;
import com.csuweb.PaperCheck.web.model.User;

@Controller
@RequestMapping("/checkdoc")
public class CheckDocController {
	
	@Resource
	ExtractTxt extracttxt;
	@Resource
	CheckDocBiz checkDocbiz;
	
	@RequestMapping(value={"/checkpaper"},method=RequestMethod.POST)
	public @ResponseBody String checkPaper(HttpServletRequest request){
//		System.out.println("进入checkpaper");
		User user = (User) request.getSession().getAttribute("user");
		//1.获取从前台传递过来的数据，形成checkDocWithBlobs对象
		String content1 = request.getParameter("content1");//粘贴文本所对应的内容
		String content2 = request.getParameter("content2");//上传文档所对应的内容
		String selectedA = request.getParameter("selectedA");//0表示是粘贴文本，1表示是上传文档
		String title = request.getParameter("title");
		String title2 = request.getParameter("title2");
		String author = request.getParameter("author");
		content1 = checkDocbiz.filterUtf8mb4(content1);
		content2 = checkDocbiz.filterUtf8mb4(content2);
		title = checkDocbiz.filterUtf8mb4(title);
		title2 = checkDocbiz.filterUtf8mb4(title2);
		author = checkDocbiz.filterUtf8mb4(author);
			/*//后续完善增加doctype选择，相似度计算算法选择，证据资源时间限制选择等
			//其中doctype是checkDoc的属性，以后可以分为作业、论文等类型
			//时间限制可以为3~5年之类的限制*/	
		CheckDocWithBLOBs checkdocwithblobs = null;
		Timestamp date = new Timestamp(new Date().getTime());
		date.setNanos(0);
		try {
			if(selectedA.equals("0")){
				if(!content1.equals("")){
					checkdocwithblobs = new CheckDocWithBLOBs();
					checkdocwithblobs.setAuthor(author);
					checkdocwithblobs.setContent(content1);
					checkdocwithblobs.setGuid(UUID.randomUUID().toString());
					checkdocwithblobs.setTitle(title);
					checkdocwithblobs.setTimestamp(date);
					checkdocwithblobs.setUserid(user.getId());
					checkdocwithblobs.setState(GlobalConfig.getClusterStart());
					checkdocwithblobs.setAlgorithm("编辑距离算法");
				}
			}else{
				if(!content2.equals("")){
					checkdocwithblobs = new CheckDocWithBLOBs();
					checkdocwithblobs.setAuthor(author);
					checkdocwithblobs.setContent(content2);
					checkdocwithblobs.setGuid(UUID.randomUUID().toString());
					checkdocwithblobs.setTitle(title2.substring(0,title2.lastIndexOf(".")));
					checkdocwithblobs.setTimestamp(date);
					checkdocwithblobs.setUserid(user.getId());
					checkdocwithblobs.setState(GlobalConfig.getClusterStart());
					checkdocwithblobs.setAlgorithm("编辑距离算法");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "resource/page/index/500";
		}
		//2.在使用本系统自带算法时，则将checkDocWithBlobs对象存储到mysql数据库,服务器自动从数据库中获取需要查重的文章，并进行查重
		//if(选择本系统自带算法){
			//直接存入数据库
		//}else{
			//调用算法接口，传递参数为文章名、作者列表、文章内容，返回一个查重报告
			//对checkdocwithblobs进行相应处理，存入数据库
		//}
		int state = checkDocbiz.addCheckDoc(checkdocwithblobs);
		if(state!=1){
			return "resource/page/index/500";
		}
		
		
		//返回到下载报告页面
		return "resource/page/index/report";//在前台进行跳转，此处只返回字符串
	}
	
	@RequiresPermissions(value = { "checkdoc:bashcheck" })//需要checkdoc表的批处理权限
	@RequestMapping(value={"/bashcheckpaper"},method=RequestMethod.POST)
	public @ResponseBody String bashCheckPaper(HttpServletRequest request){
		String filesnameStr = request.getParameter("filenames");
		String[] filenamearr = filesnameStr.split(":");
		User user = (User) request.getSession().getAttribute("user");
		String path = request.getSession().getServletContext().getRealPath("/uploadFiles");
		Timestamp date = new Timestamp(new Date().getTime());
		date.setNanos(0);
		try {
			for (String filename : filenamearr) {
				File targetFile = new File(path+"/"+user.getLoginname(),filename);
				if(targetFile.exists()){
					String content = extracttxt.extractTxt(path+"/"+user.getLoginname()+"/"+filename);
					content = checkDocbiz.filterUtf8mb4(content);
					filename = checkDocbiz.filterUtf8mb4(filename);
					targetFile.delete();
					CheckDocWithBLOBs checkdoc = new CheckDocWithBLOBs();
					checkdoc.setContent(content);
					checkdoc.setGuid(UUID.randomUUID().toString());
					checkdoc.setTitle(filename.substring(0, filename.lastIndexOf(".")));
					checkdoc.setTimestamp(date);
					checkdoc.setState(GlobalConfig.getClusterStart());
					checkdoc.setAlgorithm("编辑距离算法");
					checkdoc.setUserid(user.getId());
					//2.在使用本系统自带算法时，则将checkDocWithBlobs对象存储到mysql数据库,服务器自动从数据库中获取需要查重的文章，并进行查重
					//if(选择本系统自带算法){
						//直接存入数据库
					//}else{
						//调用算法接口，传递参数为文章名、作者列表、文章内容，返回一个查重报告
						//对checkdocwithblobs进行相应处理，存入数据库
					//}
					int state = checkDocbiz.addCheckDoc(checkdoc);
					if(state!=1){
						return "resource/page/index/500";
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "resource/page/index/500";
		}
		//返回到下载报告页面
		return "resource/page/index/report";//在前台进行跳转，此处只返回字符串
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value={"/showReports"})
	public @ResponseBody JSONArray showReports(HttpServletRequest request){
		JSONArray arr = new JSONArray();
		User user = (User) request.getSession().getAttribute("user");
//		String parentPath = path+"/"+user.getLoginname();
		List<CheckDocWithBLOBs> checkdocs = checkDocbiz.selectCheckDocByUserId(user.getId());
		if(checkdocs.size()>0){
			for (CheckDocWithBLOBs checkdoc : checkdocs) {
				long usefullife = (new Date().getTime()-checkdoc.getTimestamp().getTime())/(1000*60*60*24);
				JSONObject json = new JSONObject();
				json.put("title",checkdoc.getTitle());
				json.put("state", checkdoc.getState().equals(GlobalConfig.getReportEnd())?"检测完成":checkdoc.getState().contains("Wrong")?"检测失败":"检测中");
				json.put("checktime", checkdoc.getTimestamp().toLocaleString());
				json.put("xsl", checkdoc.getState().equals(GlobalConfig.getReportEnd())?checkdoc.getXsl()+"%":"");
				json.put("downloadPath", checkdoc.getPath());
				json.put("usefullife", checkdoc.getState().equals(GlobalConfig.getReportEnd())?(usefullife<=15?"15天":"已失效"):"");
				json.put("algorithm", checkdoc.getAlgorithm());
				json.put("guid", checkdoc.getGuid());
				arr.add(json);
			}
		}
		return arr;
	}
	@RequestMapping(value={"/getReportByGuid"})
	public @ResponseBody JSONObject getReportByGuid(HttpServletRequest request){
		JSONObject json = null;
		String guid = request.getParameter("guid");
		CheckDocWithBLOBs checkdoc = checkDocbiz.selectCheckdocByPrimaryKey(guid);
		if(checkdoc!=null){
			json = new JSONObject();
			long usefullife = (new Date().getTime()-checkdoc.getTimestamp().getTime())/(1000*60*60*24);
			json.put("title",checkdoc.getTitle());
			json.put("state", checkdoc.getState().equals(GlobalConfig.getReportEnd())?"检测完成":checkdoc.getState().contains("Wrong")?"检测失败":"检测中");
			json.put("checktime", checkdoc.getTimestamp().toLocaleString());
			json.put("xsl", checkdoc.getState().equals(GlobalConfig.getReportEnd())?checkdoc.getXsl()+"%":"");
			json.put("downloadPath", checkdoc.getPath());
			json.put("usefullife", checkdoc.getState().equals(GlobalConfig.getReportEnd())?(usefullife<=15?"15天":"已失效"):"");
			json.put("algorithm", checkdoc.getAlgorithm());
			json.put("guid", checkdoc.getGuid());
		}
		return json;
	}
	
	@RequestMapping(value={"/delRecord"},method=RequestMethod.POST)
	public @ResponseBody JSONObject delRecord(HttpServletRequest request){
		JSONObject json = new JSONObject();
		String guid = request.getParameter("guid");
		String downloadPath = request.getParameter("downloadPath");
		String projectPath = request.getServletContext().getRealPath("/");
		System.out.println(projectPath);
		System.out.println(downloadPath);
		int state = checkDocbiz.delRecordByPrimaryKey(guid);
		if(state==1){
			String compeletePath = projectPath+"/"+downloadPath;
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
		    json.put("state", "success");
		}else{
			json.put("state", "failed");
		}
		return json;
	}
}
