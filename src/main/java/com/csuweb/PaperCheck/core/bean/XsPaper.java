package com.csuweb.PaperCheck.core.bean;

public class XsPaper {
	private String originSentence ;
	private float xsl=0; 
	private int xslLength=0; 
	private BasicInfo basic;
	private String url;
	/**
	 * 方便获取，提到外面来
	 */
	private String title;
	/**
	 * true为本地，false为网络
	 */
	private boolean source;
	
	
	public String getOriginSentence() {
		return originSentence;
	}
	public void setOriginSentence(String originSentence) {
		this.originSentence = originSentence;
	}
	public float getXsl() {
		return xsl;
	}
	public XsPaper setXsl(float xsl) {
		this.xsl = xsl;
		return this;
	}
	public BasicInfo getBasic() {
		return basic;
	}
	public void setBasic(BasicInfo basic) {
		this.basic = basic;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isSource() {
		return source;
	}
	public void setSource(boolean source) {
		this.source = source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getXslLength() {
		return xslLength;
	}
	public XsPaper setXslLength(int xslLength) {
		this.xslLength = xslLength;
		return this;
	}
	
}
