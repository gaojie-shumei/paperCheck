 /** 
 * Project Name:PaperCheck 
 * File Name:RoleController.java 
 * Package Name:com.csuweb.PaperCheck.web.controller 
 * Date:2017年11月20日下午7:08:50 
 * Copyright (c) 2017, taoge@tmd.me All Rights Reserved. 
 * 
 */  
  
package com.csuweb.PaperCheck.web.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csuweb.PaperCheck.web.biz.PermissionBiz;
import com.csuweb.PaperCheck.web.biz.RoleBiz;
import com.csuweb.PaperCheck.web.model.Permission;
import com.csuweb.PaperCheck.web.model.Role;
import com.csuweb.PaperCheck.web.model.User;

/** 
 * ClassName:RoleController <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017年11月20日 下午7:08:50 <br/> 
 * @author   Administrator 
 * @version   
 * @since    JDK 1.8 
 * @see       
 */
@Controller
@RequestMapping("/role")
public class RoleController {
	@Resource
	private RoleBiz rolebiz;
	@Resource
	private PermissionBiz permissionbiz;
	

	@RequestMapping(value={"/selAllowedRole"},method=RequestMethod.POST)
	public @ResponseBody JSONArray selAllowedRole(HttpServletRequest request){
		JSONArray arr = new JSONArray();
		User user = (User) request.getSession().getAttribute("user");
		Role currentRole = rolebiz.selRoleByPrimaryKey(user.getRoleid());
		if(currentRole!=null&&currentRole.getPermissionids()!=null&&!currentRole.getPermissionids().equals("")){
			List<Role> selAllowedRole = rolebiz.selAllowedRole(currentRole.getPermissionids());
			for (Role role : selAllowedRole) {
				if(role.getPermissionids()==null||!role.getPermissionids().equals(currentRole.getPermissionids())){
					JSONObject json = new JSONObject();
					json.put("id", role==null?"":role.getId());
					json.put("rolename", role==null?"普通用户":role.getRolename());
					arr.add(json);
				}
			}
		}
		return arr;
	}
	
	@RequestMapping(value={"/selAllowedPermission"},method=RequestMethod.POST)
	public @ResponseBody JSONArray selAllowedPermission(HttpServletRequest request){
		JSONArray arr = new JSONArray();
		User user = (User) request.getSession().getAttribute("user");
		Role currentRole = rolebiz.selRoleByPrimaryKey(user.getRoleid());
		String roleid = request.getParameter("roleid");
		Role setrole = rolebiz.selRoleByPrimaryKey(roleid);
		boolean countuser = false;
		boolean countrole = false;
		if(currentRole!=null&&currentRole.getPermissionids()!=null&&!currentRole.getPermissionids().equals("")){
			String[] permissionidarr = currentRole.getPermissionids().split(",");
			for (String id : permissionidarr) {
				Permission p = permissionbiz.selPermissionByPrimaryKey(id);
				if(p.getPermissionevent().contains("user")){
					if(countuser==false){
						countuser = true;
						JSONObject json = new JSONObject();
						json.put("id", 1);
						json.put("pid", 0);
						json.put("name", "用户管理");
						arr.add(json);
					}
					JSONObject json = new JSONObject();
					json.put("id", p.getId());
					json.put("pid", 1);
					json.put("name", p.getPermissionname());
					if(setrole.getPermissionids()!=null&&setrole.getPermissionids().contains(p.getId())){
						json.put("checked", true);
					}
					arr.add(json);
				}else if((p.getPermissionevent().contains("role")||p.getPermissionevent().contains("permission"))){
					if(countrole==false){
						countrole=true;
						JSONObject json = new JSONObject();
						json.put("id", 2);
						json.put("pid", 0);
						json.put("name", "角色管理");
						arr.add(json);
					}
					JSONObject json = new JSONObject();
					json.put("id", p.getId());
					json.put("pid", 2);
					json.put("name", p.getPermissionname());
					if(setrole.getPermissionids()!=null&&setrole.getPermissionids().contains(p.getId())){
						json.put("checked", true);
					}
					arr.add(json);
				}else{
					JSONObject json = new JSONObject();
					json.put("id", p.getId());
					json.put("pid", 0);
					json.put("name", p.getPermissionname());
					if(setrole.getPermissionids()!=null&&setrole.getPermissionids().contains(p.getId())){
						json.put("checked", true);
					}
					arr.add(json);
				}
			}
		}
		return arr;
	}
	
	
	@RequestMapping(value={"/selRoleByRoleName"},method=RequestMethod.POST)
	public @ResponseBody JSONObject selRoleByRoleName(HttpServletRequest request){
		String rolename = request.getParameter("rolename");
		String roleid = request.getParameter("id");
		JSONObject json = new JSONObject();
		boolean hasrole = false;
		Role role = rolebiz.selRoleByRoleName(rolename);
		//System.out.println(rolename+"\t"+roleid+"\t"+role.getId().equals(roleid));
		if((role!=null&&!role.getId().equals(roleid))||rolename.equals("普通用户")){
			hasrole = true;
		}
		json.put("hasrole", hasrole);
		return json;
	}
	
	@RequestMapping(value = "/upRole",method=RequestMethod.POST)
	public @ResponseBody JSONObject upRole(Role role,HttpServletRequest request){//要求要有主键值
		JSONObject json = new JSONObject();
		boolean state = true;
		int up = rolebiz.upRole(role);
		if(up!=1){
			state = false;
		}
		json.put("state", state);
		return json;
	}
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delRole",method=RequestMethod.POST)
	public @ResponseBody JSONObject delRole(HttpServletRequest request){//要求主键值
		String roleid = request.getParameter("roleid");
		//System.out.println(roleid);
		JSONObject json = new JSONObject();
		boolean state = true;
		int del = rolebiz.delRole(roleid);
		if(del!=1){
			state = false;
		}
		json.put("state", state);
		return json;
	}
}
  