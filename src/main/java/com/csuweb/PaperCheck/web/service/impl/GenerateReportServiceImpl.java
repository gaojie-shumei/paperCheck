package com.csuweb.PaperCheck.web.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.Paragraph;
import com.csuweb.PaperCheck.core.bean.Section;
import com.csuweb.PaperCheck.core.bean.Sentence;
import com.csuweb.PaperCheck.core.bean.XsPaper;
import com.csuweb.PaperCheck.core.bean.XsSenInfo;
import com.csuweb.PaperCheck.core.conf.GlobalConfig;
import com.csuweb.PaperCheck.web.service.GenerateReportService;
import com.csuweb.PaperCheck.web.util.FileUtil;
import com.csuweb.PaperCheck.web.util.HtmlToPdfUtils;
import com.csuweb.PaperCheck.web.util.StringUtil;

//import com.nesei.xmccCheck.bean.ArticleInfo;
//import com.nesei.xmccCheck.bean.Paragraph;
//import com.nesei.xmccCheck.bean.Section;
//import com.nesei.xmccCheck.bean.Sentence;
//import com.nesei.xmccCheck.bean.XsPaper;
//import com.nesei.xmccCheck.bean.XsSenInfo;
//import com.nesei.xmccCheck.conf.GlobalConfig;
//import com.nesei.xmccCheck.service.GenerateReportService;
//import com.nesei.xmccCheck.util.FileUtil;
//import com.nesei.xmccCheck.util.HtmlToPdfUtils;
//import com.nesei.xmccCheck.util.StringUtil;




@Service("GenerateReportService")
public class GenerateReportServiceImpl implements GenerateReportService {
	private static Log log = LogFactory.getLog(GenerateReportServiceImpl.class);

	/**
	 * 显示报告前后句的长度
	 */
	static int LENGTH = 25;
	static String charset = "UTF-8";
	private ArticleInfo articleInfo;
	private List<XsPaper> xsPapers;
	private String papertitle;

	private static String locallipath = FileUtil.getPath() + "Template/local-li.html";
	private static String networklipath = FileUtil.getPath() + "Template/network-li.html";
	private static String sentencepath = FileUtil.getPath() + "Template/similarbox.html";
	private static String pdfpath = FileUtil.getPath() + "Template/pdf.html";
	private static String indexpath = FileUtil.getPath() + "Template/index.html";
	private static String savepath;
	private static Document indexdoc = null;
	private static Document pdfdoc = null;
	// 获取本地库模板
	File localInput = new File(locallipath);
	// 获取网络库模板
	File networkInput = new File(networklipath);
	private String xmtitle;
	private String submitTime;

	public String writeHtml(String content, ArticleInfo articleInfo, String submitTime, List<XsPaper> xsPapers,String parentPath,String papertitle)
			throws Exception {
		this.submitTime = submitTime;
		this.articleInfo = articleInfo;
		this.xsPapers = xsPapers;
		this.savepath = parentPath;
		this.papertitle = papertitle;

		File input = new File(indexpath);
		File pdfInput = new File(pdfpath);
		indexdoc = Jsoup.parse(input, charset, indexpath);
		pdfdoc = Jsoup.parse(pdfInput, charset, pdfpath);
		createIndexHtml();//创建详细比对
//		createPdfHtml();//创建简易比对结果以及pdf
		return indexdoc.html();
	}

	public void createPdfHtml() throws IOException {

		// 获取并填写基本信息区域
		Element basicContent = pdfdoc.getElementsByClass("basic-info").get(0);
		Elements generalins = basicContent.getElementsByTag("ins");
		String totalxsl = String.format("%.2f", articleInfo.getBasic().getXsl() * 100) + "%";
		generalins.get(0).appendText(totalxsl);
		generalins.get(1).appendText(articleInfo.getBasic().getXMTITLE());
		generalins.get(2).appendText(articleInfo.getBasic().getTotalLength() + "");
		generalins.get(3).appendText(articleInfo.getBasic().getXMAUAR());

		// 获取index文件中的ol，填写相似文档列表
		Element localol = pdfdoc.getElementsByTag("ol").get(0);
		Element networkol = pdfdoc.getElementsByTag("ol").get(1);
		writeXspapers(localol, networkol, localInput, networkInput);

		Element detailedContent = pdfdoc.getElementsByClass("content").get(0);
		// 初始化相似句子和段落的计数
		int numXssen = 0;
		int numPara = 0;

		for (int i = 0; i < articleInfo.getSectionList().size(); i++) {// 遍历章节
			Section section = articleInfo.getSectionList().get(i);
			for (int j = 0; j < section.getParagraphList().size(); j++) {// 遍历段落
				Paragraph paragraph = section.getParagraphList().get(j);
				numPara++;

				// 添加章节标题<p class="caption">
				if (articleInfo.getSectionList().get(i).getParagraphList().get(j).isTitleFlag()) {// 判断该段落是否有标题
					detailedContent.appendElement("p").addClass("caption").appendText(paragraph.getOriginContent().replace("\r\n", ""));
					continue;
				}

				// 增加一个对应段落
				Element p = detailedContent.appendElement("p");

				for (int k = 0; k < paragraph.getSentenceList().size(); k++) {// 遍历句子
					Sentence sentence = paragraph.getSentenceList().get(k);
					if (sentence.getExistXs()) {// 存在相似句子
						// 增加一个链接
						Element a = p.appendElement("a");
						// 判断相似率给出class
						a.addClass(getSenClass(sentence.getXsSenInfoList().get(0).getXsl()));
						// 指定链接内容
						a.appendText(sentence.getOriginSentence());
						numXssen++;
					} else if (sentence.getSmallSenExistXs()) {// 存在小句相似
						for (int m = 0; m < sentence.getSmallSentences().size(); m++) {// 遍历小句
							Sentence smallSen = sentence.getSmallSentences().get(m);
							if (smallSen.getExistXs()) {
								// 增加一个链接
								Element a = p.appendElement("a");
								// 判断相似率给出class
								a.addClass(getSenClass(smallSen.getXsSenInfoList().get(0).getXsl()));
								// 指定链接内容
								a.appendText(smallSen.getOriginSentence());
								numXssen++;
							} else {
								p.appendText(smallSen.getOriginSentence());
							}
						}
					} else {// 不存在相似句子
							// 直接添加原文句子
						p.appendText(sentence.getOriginSentence());
					}
				}
			}
		}
		writeToIndexHtml("pdf", pdfdoc);
	}

	/*
	 * 创建index html报告
	 */
	public void createIndexHtml() throws IOException {
		// 添加头部信息
		Elements inses = indexdoc.getElementsByTag("ins");
		inses.get(0).appendText(submitTime == null ? "" : submitTime);
		float xsl1 = Float.parseFloat(String.format("%.2f", articleInfo.getBasic().getXsl() * 100));// 处理相似率
		String xsl2 = "";
		xsl2 = xsl1 + "%";

		inses.get(1).appendText(xsl2);

		// 创建左侧详细内容区域
		Element detailedContent = indexdoc.getElementById("detailed-content");// 获取左侧区域元素

		// 初始化相似句子和段落的计数
		int numXssen = 0;
		int numPara = 0;

		for (int i = 0; i < articleInfo.getSectionList().size(); i++) {// 遍历章节
			Section section = articleInfo.getSectionList().get(i);
//			detailedContent.appendElement("p").text("----------" + section.getName() + "----------");
			for (int j = 0; j < section.getParagraphList().size(); j++) {// 遍历段落
				Paragraph paragraph = section.getParagraphList().get(j);
				numPara++;

				// 添加章节标题<p class="caption">
				if (articleInfo.getSectionList().get(i).getParagraphList().get(j).isTitleFlag()) {// 判断该段落是否有标题
					detailedContent.appendElement("p").addClass("caption").appendText(paragraph.getOriginContent().replace("\r\n", ""));
				}

				// 增加一个对应段落
				Element p = detailedContent.appendElement("p");

				for (int k = 0; k < paragraph.getSentenceList().size(); k++) {// 遍历句子
					Sentence sentence = paragraph.getSentenceList().get(k);

					if (sentence.getExistXs()) {// 存在相似句子
						// 增加一个链接
						Element a = p.appendElement("a");
						// 指定锚点
						a.attr("href", numXssen + "");
						// 判断相似率给出class
						a.addClass(getSenClass(sentence.getXsSenInfoList().get(0).getXsl()));
						// 指定链接内容
						a.appendText(sentence.getOriginSentence());
						// 增加similar-box
						addXssenDiv(sentence.getOriginSentence(), sentence.getXsSenInfoList(), numXssen);

						numXssen++;
					} else if (sentence.getSmallSenExistXs()) {// 存在小句相似
						for (int m = 0; m < sentence.getSmallSentences().size(); m++) {// 遍历小句
							Sentence smallSen = sentence.getSmallSentences().get(m);
							if (smallSen.getExistXs()) {
								// 增加一个链接
								Element a = p.appendElement("a");
								// 指定锚点
								a.attr("href", numXssen + "");
								// 判断相似率给出class
								a.addClass(getSenClass(smallSen.getXsSenInfoList().get(0).getXsl()));
								// 指定链接内容
								a.appendText(smallSen.getOriginSentence());
								// 增加similar-box
								addXssenDiv(smallSen.getOriginSentence(), smallSen.getXsSenInfoList(), numXssen);

								numXssen++;
							} else {
								p.appendText(smallSen.getOriginSentence());
							}
						}
					} else {// 不存在相似句子
							// 直接添加原文句子
						p.appendText(sentence.getOriginSentence());
					}
				}
			}
		}
		addLi(xsPapers);
		writeToIndexHtml("index", indexdoc);
	}

	/**
	 * 写综合评估的本地、网络来源
	 * 
	 * @param xsPapers
	 * @throws IOException
	 */
	private void addLi(List<XsPaper> xsPapers) throws IOException {
		Element generalul = indexdoc.getElementsByClass("general-information").get(0);
		Elements generalins = generalul.getElementsByTag("ins");
		String totalxsl = String.format("%.2f", articleInfo.getBasic().getXsl() * 100) + "%";
		generalins.get(0).appendText(totalxsl);
		generalins.get(1).appendText(articleInfo.getBasic().getTotalLength() + "");
		generalins.get(2).appendText(articleInfo.getBasic().getCompareLength() + "");
		generalins.get(3).appendText(articleInfo.getBasic().getSimilarLength() + "");

		// 获取index文件中的ol，填写相似文档列表
		Element localol = indexdoc.getElementsByClass("local-lib").get(0).getElementsByTag("ol").get(0);
		Element networkol = indexdoc.getElementsByClass("network-lib").get(0).getElementsByTag("ol").get(0);
		writeXspapers(localol, networkol, localInput, networkInput);
	}

	private void addXssenDiv(String origin, List<XsSenInfo> xsSenInfo, int no) throws IOException {
		// 获取句子模板
		File input = new File(sentencepath);

		// 初始化变量
		int num = 1;// 相似句子数量计数
		String senstr = null;
		Element sentences = indexdoc.getElementById("detailed-sentences");

		for (XsSenInfo xssen : xsSenInfo) {
			// 模板转化
			Document sendoc = Jsoup.parse(input, charset, sentencepath);

			// 相似句子添加编号
			Element div = sendoc.getElementsByTag("div").get(0).addClass(no + "");

			String fore = changeStyle(xssen.getFrontSentence(), true);
			String behind = changeStyle(xssen.getBehindSentence(), false);

			float xsl1 = Float.parseFloat(String.format("%.2f", xssen.getXsl() * 100));
			String xsl = "";
			xsl = xsl1 + "%";
			String className = getSenClass(xsl1 / 100);

			Elements spans = div.getElementsByTag("span");
			spans.get(0).prependText(String.valueOf(num));
			spans.get(1).prependText(xsl);
			spans.get(2).prependText(fore);
			spans.get(3).prependText(xssen.getOriginSentence());
			spans.get(4).prependText(behind);
			spans.get(5).prependText(xssen.getBasic().getXMTITLE() == null ? "" : xssen.getBasic().getXMTITLE());
			String url = xssen.getUrl() == null ? "本地库" : xssen.getUrl();
			div.getElementsByTag("a").get(0).attr("href", url).appendText(url);
			// spans.get(6).prependText(xssen.getUrl()==null?"":xssen.getUrl());
			num++;

			// 最多显示3个相似句子
			if (num > 3)
				break;
			senstr += sendoc.toString();
			// 将新生成的div挂在sentences下
			sentences.appendChild(div);
		}
	}

	public void writeXspapers(Element localol, Element networkol, File localInput, File networkInput)
			throws IOException {
		if(xsPapers==null)return;
		for (XsPaper xsPaper : xsPapers) {
			if (xsPaper.isSource()) {// true 本地
				// 模板转化并获取
				Document localdoc = Jsoup.parse(localInput, charset);
				Element localli = localdoc.getElementsByTag("li").get(0);

				Elements ins = localli.getElementsByTag("ins");
				String xsl = String.format("%.2f", xsPaper.getXsl() * 100) + "%";
				ins.get(0).appendText(xsl);
				ins.get(2).appendText(xsPaper.getTitle());
				ins.get(3).appendText(StringUtils.defaultString(xsPaper.getBasic().getXMDANW()));
				ins.get(4).appendText(StringUtils.defaultString(xsPaper.getBasic().getXMYEAR()));
				ins.get(5).appendText(StringUtils.defaultString(xsPaper.getBasic().getXMAUAR()));

				// 添加到index界面
				localol.appendChild(localli);
			} else {
				// 模板转化并获取
				Document netdoc = Jsoup.parse(networkInput, charset);
				Element netli = netdoc.getElementsByTag("li").get(0);

				Elements ins = netli.getElementsByTag("ins");
				String xsl = String.format("%.2f", xsPaper.getXsl() * 100) + "%";
				ins.get(0).appendText(xsl);
				ins.get(2).appendText(xsPaper.getTitle());
				Elements a = netli.getElementsByTag("a");
				a.get(0).attr("href", StringUtils.defaultString(xsPaper.getUrl()));

				// 添加到index界面
				networkol.appendChild(netli);
			}
		}
	}

	private String changeStyle(String ss, boolean isFore) {
		ss = StringUtils.defaultString(ss);
		if (ss.length() > LENGTH) {
			ss = StringUtil.subForeSen(ss, ss.length() - 1);
		}
		if (isFore)
			ss = "...." + ss;
		else {
			ss = ss + "....";
		}
		return ss;
	}

	private void writeToIndexHtml(String name, Document doc) throws IOException {
		// 如果文章名称为空，则设置为随机字符串
//		this.xmtitle = articleInfo.getBasic().getXMGUID() == null ? UUID.randomUUID().toString()
//				: articleInfo.getBasic().getXMGUID();
//		xmtitle = articleInfo.getBasic().getXMTITLE() + "-" + xmtitle;
		xmtitle = articleInfo.getBasic().getXMTITLE();
		// 保存路径
//		String path = savepath + "\\" + submitTime + "\\" + xmtitle;
		String path = savepath + "/" + submitTime;
		FileUtil.MakeDir(path);
		String file = null;
		file = path + "/" + xmtitle+".html";
//		System.out.println(file);
		File filename = new File(file);

		if (!filename.exists()) {
			filename.createNewFile();
		}

		FileOutputStream fos = new FileOutputStream(filename, false);
		OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
		osw.write(doc.html());
		osw.close();
		if (name.equals("pdf")) {
			HtmlToPdfUtils.htmpToPdf(articleInfo.getBasic().getXMGUID(), file, path);
		}

	}

	public String getSenClass(float xsl) {
		if (xsl >= 0.7) {
			return "high";
		} else {
			return "medium";
		}
	}

}
