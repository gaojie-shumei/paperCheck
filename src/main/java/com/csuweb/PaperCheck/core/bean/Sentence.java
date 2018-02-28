package com.csuweb.PaperCheck.core.bean;

import java.util.ArrayList;
import java.util.List;


public class Sentence extends Object {
	
	private String id="";
	private String paperId="" ;
	private String title="" ;
	private String originSentence="" ;	
	private String foreSentence=""  ;
	private String behindSentence=""  ;
	private int length=0;
	private boolean existXs;
	private boolean smallSenExistXs;
	private List<XsSenInfo> xsSenInfoList=new ArrayList<XsSenInfo>() ;
	private List<Sentence> smallSentences=new ArrayList<Sentence>() ;
	private float maxXsl=0.0f;
	
	
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
	public boolean getExistXs() {
		return existXs;
	}
	public void setExistXs(boolean existXs) {
		this.existXs = existXs;
	}
	public boolean getSmallSenExistXs() {
		return smallSenExistXs;
	}
	public void setSmallSenExistXs(boolean smallSenExistXs) {
		this.smallSenExistXs = smallSenExistXs;
	}
	public List<XsSenInfo> getXsSenInfoList() {
		return xsSenInfoList==null?new ArrayList<XsSenInfo>():xsSenInfoList;
	}
	public void setXsSenInfoList(List<XsSenInfo> xsSenInfoList) {
		this.xsSenInfoList = xsSenInfoList;
	}
	public float getMaxXsl() {
		return maxXsl;
	}
	public void setMaxXsl(float maxXsl) {
		this.maxXsl = maxXsl;
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
	public List<Sentence> getSmallSentences() {
		return smallSentences;
	}
	public void setSmallSentences(List<Sentence> smallSentences) {
		this.smallSentences = smallSentences;
	}
	public void setBehindSentence(String behindSentence) {
		this.behindSentence = behindSentence;
	}

	
}
