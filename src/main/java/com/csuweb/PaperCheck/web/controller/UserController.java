package com.csuweb.PaperCheck.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csuweb.PaperCheck.web.biz.PermissionBiz;
import com.csuweb.PaperCheck.web.biz.RoleBiz;
import com.csuweb.PaperCheck.web.biz.UserBiz;
import com.csuweb.PaperCheck.web.model.Role;
import com.csuweb.PaperCheck.web.model.User;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserBiz userbiz;
	
	@Resource
	private RoleBiz rolebiz;
	
	@Resource
	private PermissionBiz permissionbiz;
	
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	public String login(Model model,HttpServletRequest request){
		String loginname = request.getParameter("username");
		String pwd = request.getParameter("password");
		User user = new User(loginname,pwd);
		//使用shiro进行登录验证
		Subject subject = SecurityUtils.getSubject();
		try {
			User u = (User) request.getSession().getAttribute("user");
			if(u!=null){
				if(u.getLoginname().equals(user.getLoginname())&&u.getPwd().equals(userbiz.makeMD5(user.getPwd()))){
					//判断对象是否已经登录过，登录过直接跳转到首页
					if(subject.isAuthenticated()){
						return "redirect:/resource/page/index";
					}
				}else{
					model.addAttribute("error", "同一个浏览器不允许多个用户登录！");
					return "login";
				}
			}
			subject.login(new UsernamePasswordToken(user.getLoginname(), user.getPwd()));
			request.getSession().setAttribute("user", userbiz.authentication(user));
		} catch (AuthenticationException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("user",user);
            return "login";
		}
		return "redirect:/resource/page/index";
		
		
	}
	
	/**
     * 用户登出
     * 
     * @param session
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session,HttpServletRequest request) {
        session.removeAttribute("user");
        request.getSession().removeAttribute("user");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/resource/page/login";
    }
	
	@RequestMapping(value = "/signup",method=RequestMethod.POST)
	public @ResponseBody JSONObject signup(HttpServletRequest request){
		String loginname = request.getParameter("usernamesignup");
		String pwd = request.getParameter("passwordsignup");
		User user = new User(loginname, userbiz.makeMD5(pwd));
		user.setId(UUID.randomUUID().toString());
		user.setCreatetime(new Date());
		int insert_state = userbiz.addUser(user);
		JSONObject json = new JSONObject();
		if(insert_state==1){
			json.put("state", "success");
		}else{
			json.put("state", "failed");
		}
		return json;
	}
	
	
	@RequestMapping(value = "/uppwd",method=RequestMethod.POST)
	public @ResponseBody JSONObject uppwd(HttpServletRequest request){
		String error = "";
		boolean state = true;
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");
		User user = (User) request.getSession().getAttribute("user");
		if(user.getPwd().equals(userbiz.makeMD5(oldpwd))){
			user.setPwd(userbiz.makeMD5(newpwd));
			int up = userbiz.upUser(user);
			if(up!=1){
				state = false;
				error="系统异常，请稍后再试！";
			}
		}else{
			state = false;
			error = "原密码输入错误！";
		}
		JSONObject json = new JSONObject();
		json.put("state", state);
		json.put("error", error);
		return json;
	}
	
	

	@RequestMapping(value = "/selUser",method=RequestMethod.POST)
	public @ResponseBody JSONObject selUser(HttpServletRequest request){
		String loginname = request.getParameter("loginname");
		User user = userbiz.authentication(new User(loginname));
		JSONObject json = new JSONObject();
		if(user!=null){
			json.put("hasuser", true);
			json.put("id", user.getId());
			json.put("username", user.getUsername());
			json.put("userimage", user.getUserimage());
			json.put("birthdate", user.getBirthdate()==null?user.getBirthdate():user.getBirthdate().toLocaleString().split(" ")[0]);
			json.put("sex", user.getSex());
			json.put("tel", user.getTel());
			json.put("qq", user.getQq());
			json.put("email", user.getEmail());
			json.put("loginname", user.getLoginname());
			json.put("pwd", user.getPwd());
			json.put("createtime", user.getCreatetime());
		}else{
			json.put("hasuser", false);
		}
		return json;
	}
	
	@RequestMapping(value = "/upUser",method=RequestMethod.POST)
	public @ResponseBody JSONObject upUser(User user,HttpServletRequest request){//要求要有主键值
		JSONObject json = new JSONObject();
		boolean state = true;
		int up = 1;
		if("0".equals(user.getRoleid())||"".equals(user.getRoleid())){
			user.setRoleid(null);
		}
		User u = userbiz.selUserByPrimaryKey(user.getId());
		if(user.getRoleid()==null){
			if(u.getRoleid()!=null){
				up = userbiz.upUser(user);
			}
		}else{
			if(u.getRoleid()==null||!user.getRoleid().equals(u.getRoleid())){
				up = userbiz.upUser(user);
			}
		}
		if(up!=1){
			state = false;
		}
		json.put("state", state);
		return json;
	}
	
	@RequestMapping(value = "/upPersonalInfo",method=RequestMethod.POST)
	public @ResponseBody JSONObject upPersonalInfo(HttpServletRequest request){
//		userimage:userimage,
//		username:username,
//		sex:sex,
//		birthdate:birthdate,
//		tel:tel,
//		qq:qq,
//		email:email
		String userimage = request.getParameter("userimage");
		String username = request.getParameter("username");
		String sex = request.getParameter("sex");
		String birthdate = request.getParameter("birthdate");
		String tel = request.getParameter("tel");
		String qq = request.getParameter("qq");
		String email = request.getParameter("email");
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟 
		
		HttpSession session = request.getSession();
		//String userid = request.getParameter("userid");
		User user = (User) request.getSession().getAttribute("user");
		
		try {
			if(!"".equals(birthdate)){
				user.setBirthdate(sdf.parse(birthdate));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setUserimage(userimage);
		user.setUsername(username);
		user.setSex(sex);
		user.setTel(tel);
		user.setQq(qq);
		user.setEmail(email);
		
		JSONObject json = new JSONObject();
		boolean state = true;
		int up = userbiz.upUser(user);
		if(up!=1){
			state = false;
		}else{
			session.setAttribute("user", user);
		}
		json.put("state", state);
		return json;
	}
	
	/**
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delloginUser",method=RequestMethod.POST)
	public @ResponseBody JSONObject delloginUser(HttpServletRequest request){//要求主键值
		HttpSession session = request.getSession();
		//String userid = request.getParameter("userid");
		User currentUser = (User) request.getSession().getAttribute("user");
		JSONObject json = new JSONObject();
		boolean state = true;
		String href = "";
		int del = userbiz.delUser(currentUser.getId());
		if(del!=1){
			state = false;
		}
		json.put("state", state);
		if(state==true){
			href = "resource/page/login";
		}
		json.put("href", href);
		session.removeAttribute("user");
        request.getSession().removeAttribute("user");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
		return json;
	}
	
	/**
	 * 要求操作对象的权限包含了被删除对象的权限才能删除
	 * 如果某个角色被删除时要同时修改用户对象中为该角色的角色id属性为null
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delUser",method=RequestMethod.POST)
	public @ResponseBody JSONObject delUser(HttpServletRequest request){//要求主键值
		String userid = request.getParameter("userid");
		User currentUser = (User) request.getSession().getAttribute("user");
		User deleteUser = userbiz.selUserByPrimaryKey(userid);
		Role currentRole = rolebiz.selRoleByPrimaryKey(currentUser.getRoleid());
		Role doRole = rolebiz.selRoleByPrimaryKey(deleteUser.getRoleid());
		JSONObject json = new JSONObject();
		boolean state = true;
		if(doRole==null){
			int del = userbiz.delUser(userid);
			if(del!=1){
				state = false;
			}
		}else{
			String[] permissionidarr = doRole.getPermissionids().split(",");
			for (String permissionid : permissionidarr) {
				if(!currentRole.getPermissionids().contains(permissionid)){
					state = false;
					break;
				}
			}
			if(state==true){
				int del = userbiz.delUser(userid);
				if(del!=1){
					state = false;
				}
			}
		}
		json.put("state", state);
		return json;
	}
	
	/**
	 * 查找出当前对象的权限范围内可操作的对象
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/selAllowedUser",method=RequestMethod.POST)
	public @ResponseBody JSONArray selAllowedUser(HttpServletRequest request){
		JSONArray arr = new JSONArray();
		User user = (User) request.getSession().getAttribute("user");
		Role currentRole = rolebiz.selRoleByPrimaryKey(user.getRoleid());
		if(currentRole!=null&&currentRole.getPermissionids()!=null&&!currentRole.getPermissionids().equals("")){
			String roleids = "";
			List<Role> selAllowedRole = rolebiz.selAllowedRole(currentRole.getPermissionids());
			for (Role role : selAllowedRole) {
				roleids = roleids + role.getId()+",";
			}
			roleids.substring(0, roleids.length()-1);
			List<User> allowedUser = userbiz.selAllowedUser(roleids);
			for (User u : allowedUser) {
				if(!u.getId().equals(user.getId())){
					JSONObject json = new JSONObject();
					Role role = rolebiz.selRoleByPrimaryKey(u.getRoleid());
					json.put("id", u.getId());
					json.put("loginname", u.getLoginname());
					json.put("rolename", role==null?"普通用户":role.getRolename());
					arr.add(json);
				}
			}
		}
		return arr;
	}
}
