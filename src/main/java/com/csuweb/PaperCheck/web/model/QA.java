package com.csuweb.PaperCheck.web.model;

public class QA {
    private String id;

    private String quserid;

    private String qimage;

    private String auserid;

    private String aimage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getQuserid() {
        return quserid;
    }

    public void setQuserid(String quserid) {
        this.quserid = quserid == null ? null : quserid.trim();
    }

    public String getQimage() {
        return qimage;
    }

    public void setQimage(String qimage) {
        this.qimage = qimage == null ? null : qimage.trim();
    }

    public String getAuserid() {
        return auserid;
    }

    public void setAuserid(String auserid) {
        this.auserid = auserid == null ? null : auserid.trim();
    }

    public String getAimage() {
        return aimage;
    }

    public void setAimage(String aimage) {
        this.aimage = aimage == null ? null : aimage.trim();
    }
}