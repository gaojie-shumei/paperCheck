package com.csuweb.PaperCheck.core.bean;

import java.util.ArrayList;
import java.util.List;

public class Paragraph {
	private int id;
	private int startid; 
	private int endid;  
	private int length;
	private String originContent;
	boolean titleFlag;	
	private List<Sentence> sentenceList=new ArrayList<Sentence>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStartid() {
		return startid;
	}
	public void setStartid(int startid) {
		this.startid = startid;
	}
	public int getEndid() {
		return endid;
	}
	public void setEndid(int endid) {
		this.endid = endid;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getOriginContent() {
		return originContent;
	}
	public void setOriginContent(String originContent) {
		this.originContent = originContent;
	}

	public boolean isTitleFlag() {
		return titleFlag;
	}
	public void setTitleFlag(boolean titleFlag) {
		this.titleFlag = titleFlag;
	}
	public List<Sentence> getSentenceList() {
		return sentenceList;
	}
	public void setSentenceList(List<Sentence> sentenceList) {
		this.sentenceList = sentenceList;
	}
	
	

}
