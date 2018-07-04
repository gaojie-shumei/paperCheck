package com.csuweb.PaperCheck.web.model;

public class QAWithBLOBs extends QA {
    private String qdescription;

    private String adescription;

    public String getQdescription() {
        return qdescription;
    }

    public void setQdescription(String qdescription) {
        this.qdescription = qdescription == null ? null : qdescription.trim();
    }

    public String getAdescription() {
        return adescription;
    }

    public void setAdescription(String adescription) {
        this.adescription = adescription == null ? null : adescription.trim();
    }
}