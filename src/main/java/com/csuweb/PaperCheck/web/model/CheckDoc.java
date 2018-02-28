package com.csuweb.PaperCheck.web.model;

import java.sql.Timestamp;
import java.util.Date;

public class CheckDoc {
    private String guid;

    private String token;

    private String userid;

    private String title;

    private String keyword;

    private String year;

    private String subclass;

    private String author;

    private String danwei;

    private String doctype;

    private String repotype;

    private String recalladdr;

    private Timestamp timestamp;

    private String path;

    private String xsl;

    private String state;

    private Date startstmp;

    private Date endstmp;

    private String algorithm;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid == null ? null : guid.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass == null ? null : subclass.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public String getDanwei() {
        return danwei;
    }

    public void setDanwei(String danwei) {
        this.danwei = danwei == null ? null : danwei.trim();
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype == null ? null : doctype.trim();
    }

    public String getRepotype() {
        return repotype;
    }

    public void setRepotype(String repotype) {
        this.repotype = repotype == null ? null : repotype.trim();
    }

    public String getRecalladdr() {
        return recalladdr;
    }

    public void setRecalladdr(String recalladdr) {
        this.recalladdr = recalladdr == null ? null : recalladdr.trim();
    }

    

    public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getXsl() {
        return xsl;
    }

    public void setXsl(String xsl) {
        this.xsl = xsl == null ? null : xsl.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getStartstmp() {
        return startstmp;
    }

    public void setStartstmp(Date startstmp) {
        this.startstmp = startstmp;
    }

    public Date getEndstmp() {
        return endstmp;
    }

    public void setEndstmp(Date endstmp) {
        this.endstmp = endstmp;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm == null ? null : algorithm.trim();
    }
}