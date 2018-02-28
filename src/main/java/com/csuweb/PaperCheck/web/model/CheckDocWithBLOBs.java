package com.csuweb.PaperCheck.web.model;

public class CheckDocWithBLOBs extends CheckDoc {
    private String content;

    private String xmlcontent;

    private String html;

    private String xspaper;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getXmlcontent() {
        return xmlcontent;
    }

    public void setXmlcontent(String xmlcontent) {
        this.xmlcontent = xmlcontent == null ? null : xmlcontent.trim();
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html == null ? null : html.trim();
    }

    public String getXspaper() {
        return xspaper;
    }

    public void setXspaper(String xspaper) {
        this.xspaper = xspaper == null ? null : xspaper.trim();
    }
}