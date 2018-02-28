package com.csuweb.PaperCheck.core.bean;

/**
 * 文章的基本信息类
 */
public class BasicInfo {
	private String XMGUID="";//文章的主键值
	private String XMYEAR;
	private String XMTITLE="";//文章的名称
	private String XMZZLB;
	private String XMAUAR;
	private String XMDANW;
	private String PIZHUN;
	private int totalLength;//文章的总大小
	private int compareLength ;
	private int similarLength ;
	private int singleMaxLength ;
	private int numSection;
	private int numParagraph ;
	private float xsl;
	
	public String getXMGUID() {
		return XMGUID==null?"":XMGUID;
	}
	public void setXMGUID(String xMGUID) {
		XMGUID = xMGUID;
	}
	public String getXMYEAR() {
		return XMYEAR==null?"":XMYEAR;
	}
	public void setXMYEAR(String xMYEAR) {
		XMYEAR = xMYEAR;
	}
	public String getXMTITLE() {
		return XMTITLE==null?"":XMTITLE;
	}
	public void setXMTITLE(String xMTITLE) {
		XMTITLE = xMTITLE;
	}
	public String getXMZZLB() {
		return XMZZLB==null?"":XMZZLB;
	}
	public void setXMZZLB(String xMZZLB) {
		XMZZLB = xMZZLB;
	}
	public String getXMAUAR() {
		return XMAUAR==null?"":XMAUAR;
	}
	public void setXMAUAR(String xMAUAR) {
		XMAUAR = xMAUAR;
	}
	public String getXMDANW() {
		return XMDANW==null?"":XMDANW;
	}
	public void setXMDANW(String xMDANW) {
		XMDANW = xMDANW;
	}
	public String getPIZHUN() {
		return PIZHUN==null?"":PIZHUN;
	}
	public void setPIZHUN(String pIZHUN) {
		PIZHUN = pIZHUN;
	}
	public int getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}
	public int getCompareLength() {
		return compareLength;
	}
	public void setCompareLength(int compareLength) {
		this.compareLength = compareLength;
	}
	public int getSimilarLength() {
		return similarLength;
	}
	public void setSimilarLength(int similarLength) {
		this.similarLength = similarLength;
	}
	public int getSingleMaxLength() {
		return singleMaxLength;
	}
	public void setSingleMaxLength(int singleMaxLength) {
		this.singleMaxLength = singleMaxLength;
	}
	public int getNumSection() {
		return numSection;
	}
	public void setNumSection(int numSection) {
		this.numSection = numSection;
	}
	public int getNumParagraph() {
		return numParagraph;
	}
	public void setNumParagraph(int numParagraph) {
		this.numParagraph = numParagraph;
	}
	public float getXsl() {
		return xsl;
	}
	public void setXsl(float xsl) {
		this.xsl = xsl;
	}

	
}
