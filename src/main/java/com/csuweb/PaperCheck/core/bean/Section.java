package com.csuweb.PaperCheck.core.bean;

import java.util.ArrayList;
import java.util.List;

public class Section {
	private int id; 
	private int length;
	private String name;
	private String originContent;
	private List<Paragraph> paragraphList=new ArrayList<Paragraph>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginContent() {
		return originContent;
	}
	public void setOriginContent(String originContent) {
		this.originContent = originContent;
	}
	public List<Paragraph> getParagraphList() {
		return paragraphList;
	}
	public void setParagraphList(List<Paragraph> paragraphList) {
		this.paragraphList = paragraphList;
	}
	
}
