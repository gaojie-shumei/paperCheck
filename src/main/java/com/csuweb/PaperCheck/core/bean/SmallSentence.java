package com.csuweb.PaperCheck.core.bean;

import java.util.ArrayList;
import java.util.List;

public class SmallSentence extends Object {
	
	private String id="";
	private String paperId="" ;
	private String paperUrl="" ;
	private String title="" ;
	private String originSentence="" ;	
	private String foreSentence=""  ;
	private String behindSentence=""  ;
	private int length=0;
	
	public SmallSentence(){}
	
	public SmallSentence(String id,String url,String title,String originSentence,String foreSentence,String behindSentence,int length){
		this.id=id;
		this.setPaperUrl(url);
		this.title=title;
		this.foreSentence=foreSentence;
		this.originSentence=originSentence;
		this.behindSentence=behindSentence;
		this.length=length;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPaperId() {
		return paperId;
	}
	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOriginSentence() {
		return originSentence;
	}
	public void setOriginSentence(String originSentence) {
		this.originSentence = originSentence;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getForeSentence() {
		return foreSentence;
	}
	public void setForeSentence(String foreSentence) {
		this.foreSentence = foreSentence;
	}
	public String getBehindSentence() {
		return behindSentence;
	}	
	public void setBehindSentence(String behindSentence) {
		this.behindSentence = behindSentence;
	}

	public String getPaperUrl() {
		return paperUrl;
	}

	public void setPaperUrl(String paperUrl) {
		this.paperUrl = paperUrl;
	}

	
}
