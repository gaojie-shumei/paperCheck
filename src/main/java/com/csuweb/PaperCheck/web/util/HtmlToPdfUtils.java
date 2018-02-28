package com.csuweb.PaperCheck.web.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import com.csuweb.PaperCheck.core.conf.Constant;

//import com.nesei.xmccCheck.conf.Constant;


public class HtmlToPdfUtils {

	private static Log log = LogFactory.getLog(HtmlToPdfUtils.class);
	
	private static    PD4ML html = new PD4ML();
	
	static{
	    //  html.setPageSize( new Dimension(450, 450) );
	   //   html.setPageInsets( new Insets(20, 50, 10, 10) );
	    //  html.setHtmlWidth( 750 );
	//      html.enableImgSplit( false );
//	      html.useTTF( "c:/fonts/", true );
	      try {
			html.useTTF( Constant.fontPath + "/", true );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	      html.enableDebugInfo();
	}
	
	/*public static void htmpToPdf(String htmlPath, String pdfFilePath){
		OutputStream os = null;
		try {
			os = new FileOutputStream(pdfFilePath + "/" + Constant.pdfName);
			String temp = new File(htmlPath).toURI().toURL().toString();
			org.xhtmlrenderer.pdf.ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(temp);
			org.xhtmlrenderer.pdf.ITextFontResolver fontResolver = renderer
					.getFontResolver();
			fontResolver.addFont(Constant.fontPath +"/SIMSUN.TTC", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			renderer.layout();
			renderer.createPDF(os);
		} catch (Exception e) {
			log.error(e.getMessage());
		}finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}
		}
	}*/
	
	public static void htmpToPdf(String docId, String htmlPath, String pdfFilePath)  {
		java.io.FileOutputStream fos = null;
		java.io.FileInputStream fis = null;
		try{
			File f = new File(pdfFilePath + "/" + Constant.pdfName);
			if(!f.exists())f.createNewFile();
			fos = new java.io.FileOutputStream(f);
	      File fz = new File(htmlPath);
	      fis = new java.io.FileInputStream(fz);
	      InputStreamReader isr = new InputStreamReader( fis, "UTF-8" );
	     /*PD4ML html = new PD4ML();
	      html.setPageSize( new Dimension(450, 450) );
	      html.setPageInsets( new Insets(20, 50, 10, 10) );
	      html.setHtmlWidth( 750 );
	      html.enableImgSplit( false );
//	      html.useTTF( "c:/fonts/", true );
	      html.useTTF( Constant.fontPath + "/", true );
	      html.enableDebugInfo();*/
	      PD4PageMark header = new PD4PageMark();
			header.setAreaHeight( 70 );
			header.setHtmlTemplate( "<img  src='file:///"+ Constant.templatePath + "/pdf/" +  Constant.pdf_logo +"' height='60' width='181' > &nbsp;&nbsp;&nbsp;ID: "+ docId +" &nbsp;&nbsp;"+ Constant.url +"&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${page} / ${total}<hr>" );
//			header.setHtmlTemplate( "<img  src='file:///"+ Constant.templatePath + "/pdf/" +  Constant.pdf_logo +"' height='60' width='181' > &nbsp;&nbsp;&nbsp;ID: "+ docId +" &nbsp;&nbsp;www.paper-pass.cn&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${page} / ${total}<hr>" );
			html.setPageHeader( header );
	      URL base = new URL( "file:" +Constant.base + "/");
	      html.render( isr, fos, base );
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(fos != null){
					fos.close();
				}
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		HtmlToPdfUtils.htmpToPdf("1499212192329", "D:/pdf2.html", "D:/");
	}
}
