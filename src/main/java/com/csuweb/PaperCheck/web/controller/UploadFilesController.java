package com.csuweb.PaperCheck.web.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.csuweb.PaperCheck.web.model.User;

@Controller
@RequestMapping("/upload")
public class UploadFilesController {
	@RequestMapping(value={"/fileUpLoad"},method=RequestMethod.POST)
	public @ResponseBody JSONObject uploadFiles(@RequestParam(value = "files", required = false) MultipartFile[] file, HttpServletRequest request) {  
//        System.out.println("上传文件开始");  
		JSONObject json = new JSONObject();
		boolean state = true;
        String path = request.getSession().getServletContext().getRealPath("/uploadFiles");
        User user = (User) request.getSession().getAttribute("user");
//        System.out.println(user.getLoginname());
        try {
			for (int i = 0; i < file.length; i++) {
				String fileName = file[i].getOriginalFilename();  
				File targetFile = new File(path+"/"+user.getLoginname(),fileName);
			    if(!targetFile.exists()){  
			        targetFile.mkdirs();  
			    }
//        	System.out.println(fileName);
			    //保存    
			    file[i].transferTo(targetFile);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			state = false;
		}
        json.put("state", state);
		return json;
	}
	
	@RequestMapping(value={"/uploadUserImage"},method=RequestMethod.POST)
	public @ResponseBody JSONObject uploadUserImage(@RequestParam(value = "files", required = false) MultipartFile[] file, HttpServletRequest request) {  
//        System.out.println("上传文件开始");  
		JSONObject json = new JSONObject();
		boolean state = true;
        String path = request.getSession().getServletContext().getRealPath("/static/images/userimage");
        System.out.println(path);
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.getLoginname());
        File f = new File(path+"/"+user.getLoginname());
        if(f.exists()&&f.isDirectory()){
        	File[] listFiles = f.listFiles();
        	for(int i=0;i<listFiles.length;i++){
        		if(listFiles[i].exists()){
        			listFiles[i].delete();
        		}
        	}
        }
        try {
			for (int i = 0; i < file.length; i++) {
				String fileName = file[i].getOriginalFilename();  
				File targetFile = new File(path+"/"+user.getLoginname(),fileName);
			    if(!targetFile.exists()){  
			        targetFile.mkdirs();  
			    }
//        	System.out.println(fileName);
			    //保存    
			    file[i].transferTo(targetFile);
			}
		} catch (Exception e) {
//			e.printStackTrace();
			state = false;
		}
        json.put("state", state);
		return json;
	}
	
	@RequestMapping(value={"/fileRemove"})
	public @ResponseBody JSONObject removeFiles(HttpServletRequest request){
		JSONObject json = new JSONObject();
		boolean state = true;
        String path = request.getSession().getServletContext().getRealPath("/uploadFiles");
//        System.out.println(path);
        User user = (User) request.getSession().getAttribute("user");
        File f = new File(path+"/"+user.getLoginname());
        if(f.exists()){
        	f.delete();
        }
        json.put("state", state);
        return json;
	}
}
