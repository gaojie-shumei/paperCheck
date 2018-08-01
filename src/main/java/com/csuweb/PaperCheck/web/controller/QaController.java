 /** 
 * Project Name:PaperCheck 
 * File Name:QaController.java 
 * Package Name:com.csuweb.PaperCheck.web.controller 
 * Date:2018年7月31日下午12:57:06 
 * Copyright (c) 2018, taoge@tmd.me All Rights Reserved. 
 * 
 */  
  
package com.csuweb.PaperCheck.web.controller;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csuweb.PaperCheck.web.biz.QaBiz;
import com.csuweb.PaperCheck.web.biz.UserBiz;
import com.csuweb.PaperCheck.web.model.QAWithBLOBs;
import com.csuweb.PaperCheck.web.model.User;


/** 
 * ClassName:QaController <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2018年7月31日 下午12:57:06 <br/> 
 * @author   Administrator 
 * @version   
 * @since    JDK 1.8 
 * @see       
 */
@Controller
@RequestMapping("/qa")
public class QaController {
	@Resource
	QaBiz qabiz;
	
	@Resource
	UserBiz userbiz;
	
	@RequestMapping(value = "/selQaBack",method=RequestMethod.POST)
	public @ResponseBody JSONArray selQaBack(HttpServletRequest request){
		JSONArray json = new JSONArray();
		List<QAWithBLOBs> qalist = qabiz.selQaBack();
		if(qalist!=null&&qalist.size()>0){
			for (QAWithBLOBs qa : qalist) {
				JSONObject j = new JSONObject();
				j.put("id", qa.getId());
				User quser = userbiz.selUserByPrimaryKey(qa.getQuserid());
				j.put("qloginname", quser.getLoginname());
				j.put("qdescription", qa.getQdescription());
//				j.put("qimage", qa.getQimage());
				json.add(j);
			}
		}
		return json;
	}
	
	@RequestMapping(value = "/delQaBack",method=RequestMethod.POST)
	public @ResponseBody JSONObject delQaBack(HttpServletRequest request){//要求主键值
		String qaid = request.getParameter("qaid");
		JSONObject json = new JSONObject();
		boolean state = true;
		int del = qabiz.delQaBack(qaid);
		if(del!=1){
			state = false;
		}
		json.put("state", state);
		return json;
	}
	
	@RequestMapping(value = "/upqa",method=RequestMethod.POST)
	public @ResponseBody JSONObject upQa(HttpServletRequest request){//要求主键值
		String qdescription = request.getParameter("qdescription");
		User user = (User) request.getSession().getAttribute("user");
		QAWithBLOBs qa = new QAWithBLOBs();
		qa.setId(UUID.randomUUID().toString());
		qa.setQdescription(qdescription);
		qa.setQuserid(user.getId());
		JSONObject json = new JSONObject();
		boolean state = true;
		int up = qabiz.upQa(qa);
		if(up!=1){
			state = false;
		}
		json.put("state", state);
		return json;
	}
}
  