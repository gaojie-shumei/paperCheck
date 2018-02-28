package com.csuweb.PaperCheck.core.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleInfo {
	private BasicInfo basic;
	private String content;
	private String xmlContent;
	private String fullshingle="";	
	private List<Section> sectionList=new ArrayList<Section>();//章节

	public BasicInfo getBasic() {
		return basic;
	}
	
	public void setBasic(BasicInfo basic) {
		this.basic = basic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getXmlContent() {
		return xmlContent;
	}
	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}
	public String getFullshingle() {
		return fullshingle;
	}
	public void setFullshingle(String fullshingle) {
		this.fullshingle = fullshingle;
	}
	public List<Section> getSectionList() {
		return sectionList;
	}
	public void setSectionList(List<Section> sectionList) {
		this.sectionList = sectionList;
	}	
	
}
