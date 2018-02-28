package com.csuweb.PaperCheck.web.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.BasicInfo;
import com.csuweb.PaperCheck.core.bean.Paragraph;
import com.csuweb.PaperCheck.core.bean.Section;
import com.csuweb.PaperCheck.core.bean.Sentence;
import com.csuweb.PaperCheck.core.bean.XsSenInfo;

//import com.nesei.xmccCheck.bean.ArticleInfo;
//import com.nesei.xmccCheck.bean.BasicInfo;
//import com.nesei.xmccCheck.bean.Paragraph;
//import com.nesei.xmccCheck.bean.Section;
//import com.nesei.xmccCheck.bean.Sentence;
//import com.nesei.xmccCheck.bean.XsSenInfo;

@Component
public class XMLUtil {

	public static ArticleInfo XMLToArticle(String xmlResult) throws DocumentException {
		xmlResult = ReplaceLowOrderASCIICharacters(xmlResult);
		ArticleInfo articleInfo = new ArticleInfo();
		BasicInfo basicInfo = new BasicInfo();
		Document document = DocumentHelper.parseText(xmlResult);
		Element root = document.getRootElement();
		Iterator iterator = root.elementIterator("basic");
		while (iterator.hasNext()) {
			Element basic = (Element) iterator.next();
			String s = basic.elementTextTrim("xmguid").toString();
			basicInfo.setXMGUID(s);
			basicInfo.setPIZHUN(basic.elementTextTrim("pizhun").toString());
			basicInfo.setXMTITLE(basic.elementTextTrim("xmtitle").toString());
			basicInfo.setXMAUAR(basic.elementTextTrim("xmauar").toString());
			basicInfo.setXMYEAR(basic.elementTextTrim("xmyear").toString());
			basicInfo.setXMZZLB(basic.elementTextTrim("xmzzlb").toString());
			basicInfo.setXMDANW(basic.elementTextTrim("xmdanw").toString());
			basicInfo.setXsl(Float.valueOf(basic.elementTextTrim("xsl").toString()));
			basicInfo.setTotalLength(Integer.valueOf(basic.elementTextTrim("totalLength").toString()));
			basicInfo.setCompareLength(Integer.valueOf(basic.elementTextTrim("compareLength").toString()));
			basicInfo.setSimilarLength(Integer.valueOf(basic.elementTextTrim("similarLength").toString()));
			basicInfo.setSingleMaxLength(Integer.valueOf(basic.elementTextTrim("singleMaxLength").toString()));
		}
		articleInfo.setBasic(basicInfo);

		Iterator iterr = root.elementIterator("content");
		while (iterr.hasNext()) {
			Element content = (Element) iterr.next();
			Element section;
			Element para;
			Element sen;
			Element xinfo;
			Element smallSen;

			List<Section> sections = new ArrayList<Section>();
			for (int k = 0; k < content.elements().size(); k++) {
				Section sec = new Section();
				section = (Element) content.elements().get(k);
				List<Paragraph> paragraphs = new ArrayList<Paragraph>();
				for (int i = 0; i < section.elements().size(); i++) {
					Paragraph par = new Paragraph();
					List<Sentence> sentences = new ArrayList<Sentence>();
					para = (Element) section.elements().get(i);
					for (int j = 0; j < para.elements().size(); j++) {
						// System.out.println(para.elements().size());
						Sentence sentence = new Sentence();
						sen = (Element) para.elements().get(j);
						sentence.setId(sen.attributeValue("id"));
						sentence.setOriginSentence(sen.attributeValue("origin"));
						sentence.setExistXs(Boolean.parseBoolean(sen.attributeValue("existXs").toString()));
						sentence.setSmallSenExistXs(
								Boolean.parseBoolean(sen.attributeValue("smallSenExistXs").toString()));
						if (sentence.getExistXs()) {
							List<XsSenInfo> xsSenInfos = new ArrayList<XsSenInfo>();
							for (int h = 0; h < sen.elements().size(); h++) {
								XsSenInfo xsSenInfo = new XsSenInfo();
								xinfo = sen.elements().get(h);
								BasicInfo xsBasicInfo = new BasicInfo();
								xsBasicInfo.setXMGUID(xinfo.elementText("xsxmguid"));
								xsBasicInfo.setXMTITLE(xinfo.elementText("xsxmtitle"));
								xsSenInfo.setBasic(xsBasicInfo);
								xsSenInfo.setOriginSentence(xinfo.elementText("xsorigin"));
								xsSenInfo.setFrontSentence(xinfo.elementText("fore"));
								xsSenInfo.setBehindSentence(xinfo.elementText("behind"));
								xsSenInfo.setXsl(Float.valueOf(xinfo.elementText("xsl")));
								xsSenInfos.add(xsSenInfo);
							}
							sentence.setXsSenInfoList(xsSenInfos);
						} else if (sentence.getSmallSenExistXs()) {
							List<Sentence> smallSentences=new ArrayList<>();
							for (int h = 0; h < sen.elements().size(); h++) {
								Sentence smallSentence = new Sentence();
								smallSen = sen.elements().get(h);
								smallSentence.setId(smallSen.attributeValue("id"));
								smallSentence.setOriginSentence(smallSen.attributeValue("origin").toString());
								smallSentence.setExistXs(
										Boolean.parseBoolean(smallSen.attributeValue("existXs").toString()));
								smallSentence.setSmallSenExistXs(
										Boolean.parseBoolean(smallSen.attributeValue("smallSenExistXs").toString()));
								if (smallSentence.getExistXs()) {
									List<XsSenInfo> xsSenInfos = new ArrayList<XsSenInfo>();
									for (int m = 0; m < smallSen.elements().size(); m++) {
										XsSenInfo xsSenInfo = new XsSenInfo();
										xinfo = smallSen.elements().get(m);
										BasicInfo xsBasicInfo = new BasicInfo();
										xsBasicInfo.setXMGUID(xinfo.elementText("xsxmguid"));
										xsBasicInfo.setXMTITLE(xinfo.elementText("xsxmtitle"));
										xsSenInfo.setBasic(xsBasicInfo);
										xsSenInfo.setOriginSentence(xinfo.elementText("xsorigin"));
										xsSenInfo.setFrontSentence(xinfo.elementText("fore"));
										xsSenInfo.setBehindSentence(xinfo.elementText("behind"));
										xsSenInfo.setXsl(Float.valueOf(xinfo.elementText("xsl")));
										xsSenInfos.add(xsSenInfo);
									}
									smallSentence.setXsSenInfoList(xsSenInfos);
								}
								smallSentences.add(smallSentence);
							}
							sentence.setSmallSentences(smallSentences);
						}
						sentences.add(sentence);
					}
					par.setSentenceList(sentences);
					paragraphs.add(par);					
				}
				sec.setParagraphList(paragraphs);
				sections.add(sec);
			}
			articleInfo.setSectionList(sections);
		}
		return articleInfo;
	}

	public static String ArticleInfoToXML(ArticleInfo articleInfo) throws Exception {
		XMLWriter writer = null;
		StringWriter sw=new StringWriter();
		Document doc = DocumentHelper.createDocument();
		OutputFormat format = OutputFormat.createPrettyPrint();

		Element root = doc.addElement("root");
		// basic
		Element basic = root.addElement("basic");
		basic.addElement("xmguid")
				.addText(articleInfo.getBasic().getXMGUID() == null ? "\r" : articleInfo.getBasic().getXMGUID());
		basic.addElement("pizhun")
				.addText(articleInfo.getBasic().getPIZHUN() == null ? "\r" : articleInfo.getBasic().getPIZHUN());
		basic.addElement("xmtitle")
				.addText(articleInfo.getBasic().getXMTITLE() == null ? "\r" : articleInfo.getBasic().getXMTITLE());
		basic.addElement("xmauar")
				.addText(articleInfo.getBasic().getXMAUAR() == null ? "\r" : articleInfo.getBasic().getXMAUAR());
		basic.addElement("xmyear")
				.addText(articleInfo.getBasic().getXMYEAR() == null ? "\r" : articleInfo.getBasic().getXMYEAR());
		basic.addElement("xmzzlb")
				.addText(articleInfo.getBasic().getXMZZLB() == null ? "\r" : articleInfo.getBasic().getXMZZLB());
		basic.addElement("xmdanw")
				.addText(articleInfo.getBasic().getXMDANW() == null ? "\r" : articleInfo.getBasic().getXMDANW());
		basic.addElement("xsl").addText(String.valueOf(articleInfo.getBasic().getXsl()) == null ? "\r"
				: String.valueOf(articleInfo.getBasic().getXsl()));
		basic.addElement("totalLength").addText(String.valueOf(articleInfo.getBasic().getTotalLength()) == null ? "\r"
				: String.valueOf(articleInfo.getBasic().getTotalLength()));
		basic.addElement("compareLength").addText(String.valueOf(articleInfo.getBasic().getCompareLength()) == null
				? "\r" : String.valueOf(articleInfo.getBasic().getCompareLength()));
		basic.addElement("similarLength").addText(String.valueOf(articleInfo.getBasic().getSimilarLength()) == null
				? "\r" : String.valueOf(articleInfo.getBasic().getSimilarLength()));
		basic.addElement("singleMaxLength").addText(String.valueOf(articleInfo.getBasic().getSingleMaxLength()) == null
				? "\r" : String.valueOf(articleInfo.getBasic().getSingleMaxLength()));

		// detail
		Element content = root.addElement("content");
		for (int i = 0; i < articleInfo.getSectionList().size(); i++) {
			Section sec = articleInfo.getSectionList().get(i);
			Element section = content.addElement("section" + i);
			for (int j = 0; j < sec.getParagraphList().size(); j++) {
				Paragraph par = sec.getParagraphList().get(j);
				Element paragraph = section.addElement("paragraph" + j);
				for (int k = 0; k < par.getSentenceList().size(); k++) {
					Sentence sen = par.getSentenceList().get(k);
					Element sentence = paragraph.addElement("sentence" + k);
					sentence.addAttribute("id", String.valueOf(sen.getId()));
					sentence.addAttribute("origin", sen.getOriginSentence());
					sentence.addAttribute("existXs", sen.getExistXs() + "");
					sentence.addAttribute("smallSenExistXs", sen.getSmallSenExistXs() + "");
					if (sen.getExistXs()) {
						for (int h = 0; h < sen.getXsSenInfoList().size(); h++) {
							XsSenInfo xInfo = sen.getXsSenInfoList().get(h);
							Element xsseninfo = sentence.addElement("xsseninfo" + h);
							xsseninfo.addElement("xsxmguid").addText(
									xInfo.getBasic().getXMGUID() == null ? "\r" : xInfo.getBasic().getXMGUID());
							xsseninfo.addElement("xsxmtitle").addText(
									xInfo.getBasic().getXMTITLE() == null ? "\r" : xInfo.getBasic().getXMTITLE());
							Element xsorigin = xsseninfo.addElement("xsorigin");
							xsorigin.addCDATA(xInfo.getOriginSentence());
							Element fore = xsseninfo.addElement("fore");
							fore.addCDATA(xInfo.getFrontSentence());
							Element behind = xsseninfo.addElement("behind");
							behind.addCDATA(xInfo.getBehindSentence());
							xsseninfo.addElement("xsl").addText(String.valueOf(xInfo.getXsl()));
						}
					} else if (sen.getSmallSenExistXs()) {
						for (int h = 0; h < sen.getSmallSentences().size(); h++) {
							Sentence smallSen = sen.getSmallSentences().get(h);
							Element smallSentence = sentence.addElement("smallSentence" + h);
							smallSentence.addAttribute("id", String.valueOf(smallSen.getId()));
							smallSentence.addAttribute("origin", smallSen.getOriginSentence());
							smallSentence.addAttribute("existXs", smallSen.getExistXs() + "");
							smallSentence.addAttribute("smallSenExistXs", smallSen.getSmallSenExistXs() + "");
							if (smallSen.getExistXs()) {
								for (int m = 0; m < smallSen.getXsSenInfoList().size(); m++) {
									XsSenInfo xInfo = smallSen.getXsSenInfoList().get(m);
									Element xsseninfo = smallSentence.addElement("xsseninfo" + m);
									xsseninfo.addElement("xsxmguid").addText(
											xInfo.getBasic().getXMGUID() == null ? "\r" : xInfo.getBasic().getXMGUID());
									xsseninfo.addElement("xsxmtitle").addText(xInfo.getBasic().getXMTITLE() == null
											? "\r" : xInfo.getBasic().getXMTITLE());
									Element xsorigin = xsseninfo.addElement("xsorigin");
									xsorigin.addCDATA(xInfo.getOriginSentence());
									Element fore = xsseninfo.addElement("fore");
									fore.addCDATA(xInfo.getFrontSentence());
									Element behind = xsseninfo.addElement("behind");
									behind.addCDATA(xInfo.getBehindSentence());
									xsseninfo.addElement("xsl").addText(String.valueOf(xInfo.getXsl()));
								}
							}
						}
					}

				}
			}
		}

		format.setEncoding("UTF-8");
		writer=new XMLWriter(format);
		writer.setWriter(sw);
		writer.write(doc);
		//String xml=doc.asXML();
		String result=sw.toString();
		result=filterNoUtf8(result);
		return ReplaceLowOrderASCIICharacters(result);
	}

	/**
	 * XML十六进制无效字符的处理
	 * 
	 * @param tmp
	 * @return
	 */
	public static String ReplaceLowOrderASCIICharacters(String tmp) {
		tmp=tmp.replaceAll("#", "&#35;");
//		tmp=tmp.replaceAll("[", "&#91;");
//		tmp=tmp.replaceAll("]", "&#93;");
		tmp=tmp.replaceAll("&", "&amp;");
//		
//		tmp=tmp.replaceAll("\"", "&quot;");
//		tmp=tmp.replaceAll("&nbsp", "&amp");
		if (tmp == null || tmp == "")
			return "";
		StringBuilder info = new StringBuilder();
		for (int i = 0; i < tmp.length(); i++) {
			char cc = tmp.charAt(i);
			int ss = (int) cc;
			if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss <= 32)) || (ss == 127))
				info.append(" ");// &#x{0:X};
			else
				info.append(cc);
		}
		return info.toString();// .Trim();
	}

	public static String filterNoUtf8(String tmp) {
		if (tmp == null || tmp == "")
			return "";
		StringBuilder info = new StringBuilder();
		for (int i = 0; i < tmp.length(); i++) {
			char c = tmp.charAt(i);
			if (c < 0x0000 || c > 0xffff)
				continue;
			else {
				info.append(c);
			}
		}
		return info.toString();
	}

}
