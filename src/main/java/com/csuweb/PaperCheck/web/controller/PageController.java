package com.csuweb.PaperCheck.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
	
	static List<String> roleList = new ArrayList<>();
	static{
		roleList.add("管理员");
		roleList.add("超级管理员");
		roleList.add("super");
		roleList.add("manager");
		roleList.add("super manager");
		roleList.add("supermanager");
		roleList.add("Super");
		roleList.add("Manager");
		roleList.add("Super Manager");
		roleList.add("super Manager");
		roleList.add("Super manager");
		roleList.add("SuperManager");
		roleList.add("superManager");
		roleList.add("Supermanager");
		roleList.add("admin");
		roleList.add("Admin");
		roleList.add("administrator");
		roleList.add("Administrator");
	}
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@RequestMapping("/404")
	public String error404(){
		return "404";
	}
	
	@RequestMapping("/401")
	public String error401(){
		return "401";
	}
	
	@RequestMapping(value={"/index","/index/*"})
	public String index(){
		return "index";
	}
	
	@RequestMapping("/500")
	public String error500(){
		return "500";
	}
	
	@RequestMapping("/report")
	public String report(){
		return "report";
	}
	
	@RequestMapping("/checkpaper")
	public String checkpaper(){
		return "checkpaper";
	}
	
	@RequiresPermissions(value = { "checkdoc:bashcheck" })//需要checkdoc表的批处理权限
	@RequestMapping("/bashcheckpaper")
	public String bashcheckpaper(){
		return "bashcheckpaper";
	}
	
	@RequiresPermissions(value = { "history:add" })//证据文档（添加查重库文档权限）
	@RequestMapping("/history")
	public String history(){
		return "history";
	}
	
	@RequiresPermissions(value = {"user:select" })//"user:add","user:update","user:delete","user:select"    用户的增删查改权限
	@RequestMapping("/userlist")
	public String userlist(){
		return "userlist";
	}
	
	
	@RequiresPermissions(value = {"role:select" })
	@RequestMapping("/rolelist")
	public String rolelist(){
		boolean state = false;
		Subject subject = SecurityUtils.getSubject();
		boolean[] hasRoles = subject.hasRoles(roleList);
		for (boolean b : hasRoles) {
			if(b==true){
				state = true;
				break;
			}
		}
		if(state==true){
			return "rolelist";
		}else{
			return "401";
		}
	}
	
	@RequiresPermissions(value = {"permission:set" })
	@RequestMapping("/setpermission")
	public String setpermission(){
		return "setpermission";
	}
	
	@RequestMapping("/personalinfo")
	public String personalinfo(){
		return "personalinfo";
	}
	
	@RequestMapping("/uppassword")
	public String uppassword(){
		return "uppassword";
	}
//	
//	@RequiresPermissions(value = { "user:add" })//用户的增权限
//	@RequestMapping("/userlist")
//	public String adduser(){
//		return "adduser";
//	}
}
