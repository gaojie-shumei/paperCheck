package com.csuweb.PaperCheck.core.bean;

public class Senbean {
	
	private String title;
	private String remark;
	private String url;
	
	public Senbean(String title){
		this.title = title;
	}
	
	public Senbean(String title, String remark/*, String url*/){
		this.title = title;
		this.remark = remark;
/*		this.url = url;*/
	}

	public String getTitle() {
		return title;
	}

	public String getRemark() {
		return remark;
	}

	public String getUrl() {
		return url;
	}
	
	

}
