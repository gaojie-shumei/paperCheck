package com.csuweb.PaperCheck.core.bean;

public class XsSenInfo {
	private String originSentence ;
	private float xsl=0; 
	private BasicInfo basic;
	private String url;
	private String frontSentence;
	private String behindSentence;
	public String getOriginSentence() {
		return originSentence;
	}
	public void setOriginSentence(String originSentence) {
		this.originSentence = originSentence;
	}
	public float getXsl() {
		return xsl;
	}
	public void setXsl(float xsl) {
		this.xsl = xsl;
	}
	public BasicInfo getBasic() {
		return basic==null?new BasicInfo():basic;
	}
	public void setBasic(BasicInfo basic) {
		this.basic = basic;
	}
	public String getFrontSentence() {
		return frontSentence==null?"":frontSentence;
	}
	public void setFrontSentence(String frontSentence) {
		this.frontSentence = frontSentence;
	}
	public String getBehindSentence() {
		return behindSentence==null?"":behindSentence;
	}
	public void setBehindSentence(String behindSentence) {
		this.behindSentence = behindSentence;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
