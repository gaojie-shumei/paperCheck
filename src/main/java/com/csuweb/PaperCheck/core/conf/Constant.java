package com.csuweb.PaperCheck.core.conf;

import com.csuweb.PaperCheck.web.util.FileUtil;

public class Constant {
	
	public static String spil = "\\|#\\|";
	 /**
	  * 模板存放位置
	  */
//	public static final String CONTEXT_PATH =FileUtil.getPath()+"/src/main/resources/gen";
//	 public static final  String templatePath =FileUtil.getPath()+"/src/main/resources/templates";
//	 public static final  String fontPath =FileUtil.getPath()+"/src/main/resources/fonts";
//	 public static final  String base =FileUtil.getPath()+"/src/main/resources/templates";
	public static final String CONTEXT_PATH =FileUtil.getPath()+"gen";
	 public static final  String templatePath =FileUtil.getPath()+"templates";
	 public static final  String fontPath =FileUtil.getPath()+"fonts";
	 public static final  String base =FileUtil.getPath()+"templates";
	
	 /**
	  * 1 不是主服务器
	  * 0 主服务器
	  */
	 public static  String isMain ;
	 public static  Integer userId ;
	 public static int limit ;
	 
	public static final Object TITLE = "= XMCC论文检测系统";

	public static final int CHECK_STATUS_DONE = 1;

	public static final String SITE_URL = "";
	
	public static final Integer App_Server = 1;

	public static String SYSTEM_ENCODING = "UTF-8";
	 
//	public static int limit = 25;
	
	public static String pdfName = "PDF简明打印报告.pdf";
	
	public static String pdf_logo = "logo.png";
	
	public static String wordName = "标色辅助修改.doc";
	
	public static String url;

	public static String gcTemplatePath;
	
	public static int baseoption_gc = 4;
	
	public static int baseoption_net = 1;
	
}
