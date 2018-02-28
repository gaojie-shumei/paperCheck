package com.csuweb.PaperCheck.web.biz;

import java.util.List;

import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;

public interface CheckDocBiz {
	int addCheckDoc(CheckDocWithBLOBs checkDocWithBlobs);
	
	public int selectCountCheckDoc(String state,String state2);
	
	public List<CheckDocWithBLOBs> selectCheckDoc(String state,String state2);
	
	public int updateCheckDoc(CheckDocWithBLOBs checkDoc);

	public List<CheckDocWithBLOBs> selectCheckDocByUserId(String userid);

	String filterUtf8mb4(String str);

	int delRecordByPrimaryKey(String guid);//删除主键为guid的checkdoc对象
	
	public CheckDocWithBLOBs selectCheckdocByPrimaryKey(String guid);
	
//	/**
//	 * 文本聚类
//	 */
//	public boolean cluster(User user);
//	
//	/**
//	 * 文本爬虫，在网络上进行文本聚类，未实现
//	 * 
//	 */
//	public boolean clawer(User user);
//	
//	/**
//	 * 文本计算并生成查重报告，在聚类和爬虫之后执行
//	 */
//	public boolean compute(User user);
	
//	/**
//	 * 生成查重报告
//	 */
//	public boolean generateReport(User user);
}
