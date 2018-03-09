package com.csuweb.PaperCheck.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;

import com.csuweb.PaperCheck.core.bean.Article;


@Component
public class ExtractTxt {

		private static final String pageStart = "--pageStart--";
		private static final String paraStart = "--paraStart--";
		/**
		 * 提取成证据文档
		 * @param path
		 * @return
		 * @throws Exception
		 */
		public Article extractToArticle(String path) throws Exception {
			String txt = null;
			Article article = new Article();
			
			txt=extractTxt(path);

			article.setContent(txt); 
			String title = path.substring(path.lastIndexOf("/")+1,path.length());
			title=title.substring(0,title.lastIndexOf("."));
			article.setId(UUID.randomUUID().toString());
			article.setTitle(title);
			article.setContent(txt);
			Random random=new Random();
			int t=random.nextInt(10);
			article.setTimes(t);
			
			return article;
		}
		/**
		 * 提取出文档中的文本内容,适用文件类型txt，doc，docx，pdf
		 * 
		 * @return String
		 * @throws Exception
		 */
		public String extractTxt(String path) throws Exception{
			String txt=null;
			// 得到文件的类型，判断pdf文档或者word文档，分别提取内容
			String type = path.substring(path.lastIndexOf(".") + 1);
			if (type.trim().equals("pdf")) {
				txt = getTxtfromPdf(path);
				txt = TxtAnalyse.dealPdfContent(txt);
			} else if (type.trim().equals("doc") || type.trim().equals("docx")) {
				 txt=getTxtfromWord(path);
			} else if (type.trim().equals("txt")) {
				txt = getTxtfromTxt(path);
			} else{
				return null;
			}

			// 消除乱码
			Pattern pattern = Pattern.compile("[<>&'\"\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f\\uD800-\\uDFFF]");
			Matcher matcher = pattern.matcher(txt);
			matcher.replaceAll("");
			if(txt!=null) {
				txt = TxtAnalyse.ReplaceLowOrderASCIICharacters(txt);
			}
			return txt;
		}

		public static String getTxtfromTxt(String filepath) throws Exception {
			String text = null;
			try {
				text = parseFile(filepath);
			}catch(Exception e){
				try {
					Tika tika = new Tika();
					tika.setMaxStringLength(999999999);
					text = tika.parseToString(new File(filepath));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return text;
//			 InputStreamReader reader=new InputStreamReader(new
////			 FileInputStream(filepath),"UTF-8");
//					 FileInputStream(filepath));
//			 BufferedReader br=new BufferedReader(reader);
//			 String lineTxt = null;
//			 while((lineTxt = br.readLine()) != null){
//				 result+=lineTxt;
//			 }
//			 reader.close();
//			return result;
		}

		/*
		 * 引用pdfbox.jar中的方法处理pdf文档，获得返回内容
		 */
		public String getTxtfromPdf(String filepath) throws Exception {
			System.setProperty("org.apache.pdfbox.baseParser.pushBackSize", new Long(100*1024*1024).toString());
			PDDocument doc = null;
			String result = "";

			try {
				FileInputStream fileInputStream = new FileInputStream(filepath);
				PDFParser parser = new PDFParser(fileInputStream);
				parser.parse();
				doc = parser.getPDDocument();
				if (doc.isEncrypted()) {
					return null;
				}
				PDFTextStripper stripper = new PDFTextStripper();
				stripper.setPageEnd(pageStart);
				stripper.setParagraphStart(paraStart);
				result = stripper.getText(doc);

			} catch (Exception e) {
				try {
					result = parseFile(filepath);
				} catch (Exception e1) {
					try {
						Tika tika = new Tika();
						tika.setMaxStringLength(999999999);
						result = tika.parseToString(new File(filepath));
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
			} finally {
				if (doc != null) {
					try {
						doc.close();
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					} finally {
						doc = null;
					}
				}
			}

			return result;
		}
		
		public String getTxtfromWord(String filepath)throws Exception{
			String text = null;
			try {
				text = parseFile(filepath);
			}catch(Exception e){
				try {
					Tika tika = new Tika();
					tika.setMaxStringLength(999999999);
					text = tika.parseToString(new File(filepath));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return text;
		}

		public static String parseFile(String filepath) {
			File file = new File(filepath);
			Parser parser = new AutoDetectParser();
			InputStream input = null;
			try {
				Metadata metadata = new Metadata();
				metadata.set(Metadata.CONTENT_ENCODING, "utf-8");
				metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
				input = new FileInputStream(file);
				ContentHandler handler = new BodyContentHandler(1024*1024*1024);//当文件大于100000时，new BodyContentHandler(1024*1024*10);
				ParseContext context = new ParseContext();
				context.set(Parser.class, parser);
				parser.parse(input, handler, metadata, context);
//				for (String name : metadata.names()) {
//					System.out.println(name + ":" + metadata.get(name));
//				}
//				System.out.println(handler.toString());
//				System.out.println();
				return handler.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (input != null) input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;

		}


}
