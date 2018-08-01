 /** 
 * Project Name:PaperCheck 
 * File Name:QaBiz.java 
 * Package Name:com.csuweb.PaperCheck.web.biz 
 * Date:2018年7月31日下午12:57:50 
 * Copyright (c) 2018, taoge@tmd.me All Rights Reserved. 
 * 
 */  
  
package com.csuweb.PaperCheck.web.biz;

import java.util.List;

import com.csuweb.PaperCheck.web.model.QA;
import com.csuweb.PaperCheck.web.model.QAWithBLOBs;

/** 
 * ClassName:QaBiz <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2018年7月31日 下午12:57:50 <br/> 
 * @author   Administrator 
 * @version   
 * @since    JDK 1.8 
 * @see       
 */
public interface QaBiz {

	List<QAWithBLOBs> selQaBack();
	int delQaBack(String id);
	int upQa(QAWithBLOBs qa);

}
  