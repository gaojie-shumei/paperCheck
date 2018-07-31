 /** 
 * Project Name:PaperCheck 
 * File Name:QaBizImpl.java 
 * Package Name:com.csuweb.PaperCheck.web.biz.impl 
 * Date:2018年7月31日下午1:04:38 
 * Copyright (c) 2018, taoge@tmd.me All Rights Reserved. 
 * 
 */  
  
package com.csuweb.PaperCheck.web.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.web.biz.QaBiz;
import com.csuweb.PaperCheck.web.dao.QAMapper;
import com.csuweb.PaperCheck.web.model.QA;
import com.csuweb.PaperCheck.web.model.QAWithBLOBs;

/** 
 * ClassName:QaBizImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2018年7月31日 下午1:04:38 <br/> 
 * @author   Administrator 
 * @version   
 * @since    JDK 1.8 
 * @see       
 */
@Service
public class QaBizImpl implements QaBiz {

	@Resource
	private QAMapper qamapper;
	@Override
	public List<QAWithBLOBs> selQaBack() {
		// TODO Auto-generated method stub
		return qamapper.selQaBack();
	}

	@Override
	public int delQaBack(String id) {
		// TODO Auto-generated method stub
		return qamapper.deleteByPrimaryKey(id);
	}

}
  