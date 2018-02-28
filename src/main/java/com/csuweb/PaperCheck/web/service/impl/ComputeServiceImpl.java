package com.csuweb.PaperCheck.web.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.BasicInfo;
import com.csuweb.PaperCheck.core.bean.Paragraph;
import com.csuweb.PaperCheck.core.bean.Section;
import com.csuweb.PaperCheck.core.bean.Sentence;
import com.csuweb.PaperCheck.core.bean.SmallSentence;
import com.csuweb.PaperCheck.core.bean.XsPaper;
import com.csuweb.PaperCheck.core.bean.XsSenInfo;
import com.csuweb.PaperCheck.web.service.ComputeService;
import com.csuweb.PaperCheck.web.util.ESUtils;
import com.csuweb.PaperCheck.web.util.SimilarityUtil;

//import com.nesei.xmccCheck.bean.ArticleInfo;
//import com.nesei.xmccCheck.bean.BasicInfo;
//import com.nesei.xmccCheck.bean.Paragraph;
//import com.nesei.xmccCheck.bean.Section;
//import com.nesei.xmccCheck.bean.Sentence;
//import com.nesei.xmccCheck.bean.SmallSentence;
//import com.nesei.xmccCheck.bean.XsPaper;
//import com.nesei.xmccCheck.bean.XsSenInfo;
//import com.nesei.xmccCheck.service.ComputeService;
//import com.nesei.xmccCheck.util.ESUtils;
//import com.nesei.xmccCheck.util.SimilarityUtil;

@Service("ComputerService")
public class ComputeServiceImpl implements ComputeService {

	/**
	 * 句子相似度阀值
	 */
	float senXslThreshold = 0.3f;

	/**
	 * 小句查询阀值
	 */
	float smallSenCheck_senXslThreshold = 0.8f;

	/**
	 * 大于等于这个值，相似率算为1
	 */
	float closeTo1XSLThreshold = 0.9f;

	/**
	 * 大句变小句可接受误差
	 */
	float bigToSmallThreshold = 0.1f;

	/**
	 * 相似句子之间长度比阀值
	 */
	float smallCheckThreshold = 2.5f;
	int xsSenNumThreshold = 4;// 相似句子个数

	public List<XsPaper> computer(ArticleInfo articleInfo) throws Exception {
		traverseArticle(articleInfo);
		return getXsl(articleInfo);
	}

	private void traverseArticle(ArticleInfo articleInfo) throws Exception {
		for (Section sec : articleInfo.getSectionList()) {
			for (Paragraph para : sec.getParagraphList()) {
				if (para.isTitleFlag())
					continue;
				for (Sentence sen : para.getSentenceList()) {
					// 返回值为知否进行小句查询
					boolean isSmallCheck = searchSentence(articleInfo.getBasic().getXMGUID(), sen);
					// 小句查询
					if (!sen.getExistXs() || sen.getMaxXsl() < smallSenCheck_senXslThreshold || isSmallCheck) {
						List<Sentence> smallSentences = sen.getSmallSentences();
						for (Sentence smallSentence : smallSentences) {
							if (smallSentence.getOriginSentence().trim().length() < 3)
								continue;
							searchSentence(articleInfo.getBasic().getXMGUID(), smallSentence);
							// 小句相似度高，则取小句查询结果
							if (smallSentence.getMaxXsl() > smallSenCheck_senXslThreshold) {
								sen.setExistXs(false);
								sen.setSmallSenExistXs(true);
							}
						}
					}

				}
			}
		}
	}

	public boolean searchSentence(String xmguid, Sentence sen) throws Exception {
		int xsNum = 0;
		boolean isAdd = false;
		boolean isSmallCheck = false;
		List<Object> xsSentences = new ArrayList<>();

		xsSentences = ESUtils.search(xmguid, "SmallSentence", "originSentence", sen.getOriginSentence(), null, 0, 10,
				SmallSentence.class);
		if (xsSentences == null || xsSentences.size() == 0) {
			sen.setExistXs(false);
			return true;
		}

		ArrayList<XsSenInfo> xsSenInfoList = new ArrayList<XsSenInfo>();
		for (int i = 0; i < xsSentences.size(); i++) {
			SmallSentence xsSentence = (SmallSentence) xsSentences.get(i);
			XsSenInfo xsSenInfo = new XsSenInfo();
			float xsl = (float) SimilarityUtil.similarity(sen.getOriginSentence().trim(),
					xsSentence.getOriginSentence().trim());

			if (xsl >= senXslThreshold) {
				if (xsSentence.getLength() > (sen.getLength() * smallCheckThreshold)) {
					continue;
				}

				BasicInfo basicInfo = new BasicInfo();
				basicInfo.setXMTITLE(xsSentence.getTitle());
				basicInfo.setXMGUID(xsSentence.getPaperId());
				sen.setExistXs(true);
				xsSenInfo.setOriginSentence(xsSentence.getOriginSentence());
				xsSenInfo.setXsl(xsl);
				xsSenInfo.setUrl(xsSentence.getPaperUrl());
				xsSenInfo.setFrontSentence(xsSentence.getForeSentence());
				xsSenInfo.setBehindSentence(xsSentence.getBehindSentence());
				xsSenInfo.setBasic(basicInfo);
				isAdd = true;
				xsNum++;
				xsSenInfoList.add(xsSenInfo);

				if (xsNum >= xsSenNumThreshold)
					break;

			} else if (i < 5 && !isAdd) {// 如果还没找到，继续找前五个
				continue;
			} else {
				break;
			}

		}

		Collections.sort(xsSenInfoList, new Comparator<XsSenInfo>() {
			public int compare(XsSenInfo o1, XsSenInfo o2) {
				if (o1.getXsl() - o2.getXsl() < 0) {
					return 1;
				} else if (o1.getXsl() - o2.getXsl() == 0) {
					return -1;
				} else {
					return -1;
				}
			}
		});
		if (isAdd) {
			if (xsSenInfoList.get(0).getOriginSentence().length() > sen.getOriginSentence().length()
					* smallCheckThreshold)
				isSmallCheck = true;
			sen.setMaxXsl(xsSenInfoList.get(0).getXsl());
			// sen.setMaxXsl(xsSenInfoList.get(0).getXsl2());
			sen.setXsSenInfoList(xsSenInfoList);
		} else {
			// System.out.println("失败"+"....."+sen.getOriginSentence());
			// float xsl=compare(shingle, xsSentences.get(0).getShingle());
			// System.out.println(xsl);
		}
		sen.setExistXs(isAdd);
		return isSmallCheck;
	}

	private List<XsPaper> getXsl(ArticleInfo articleInfo) {
		Map<String, XsPaper> xsPaperMap = new TreeMap<String, XsPaper>();
		int xslLength = 0;
		// int CompareLength=info.getBasic().getCompareLength();
		int CompareLength = 0;
		float xsl = 0.0000f;

		for (Section section : articleInfo.getSectionList()) {
			for (Paragraph paragraph : section.getParagraphList()) {
				for (Sentence sentence : paragraph.getSentenceList()) {
					if (sentence.getSmallSenExistXs()) {
						for (Sentence smallSentence : sentence.getSmallSentences()) {
							xslLength += smallSentence.getLength() * (smallSentence.getMaxXsl() >= closeTo1XSLThreshold
									? 1 : smallSentence.getMaxXsl());							
							CompareLength += smallSentence.getLength();							
							addXsPaperMap(smallSentence, xsPaperMap);
						}
					} else {
						xslLength += sentence.getLength()
								* (sentence.getMaxXsl() >= closeTo1XSLThreshold ? 1 : sentence.getMaxXsl());						
						CompareLength += sentence.getLength();						
						addXsPaperMap(sentence, xsPaperMap);
					}

				}
			}
		}
		//获取相似文本列表
		List<Map.Entry<String, XsPaper>> xsPaperMaps = new ArrayList<Map.Entry<String, XsPaper>>(xsPaperMap.entrySet());
		Collections.sort(xsPaperMaps, new Comparator<Map.Entry<String, XsPaper>>() {
			public int compare(Entry<String, XsPaper> o1, Entry<String, XsPaper> o2) {
				if (o1.getValue().getXslLength() - o2.getValue().getXslLength() < 0) {
					return 1;
				} else if (o1.getValue().getXslLength() - o2.getValue().getXslLength() == 0) {
					return 0;
				} else {
					return -1;
				}
			}
		});

		for (Entry<String, XsPaper> mapping : xsPaperMaps) {
			articleInfo.getBasic().setSingleMaxLength((int) (mapping.getValue().getXsl() * CompareLength));
			break;
		}

		xsl = (float) (xslLength * 1.00 / CompareLength);
		BigDecimal   b   =   new   BigDecimal(xsl);
		xsl =b.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
		articleInfo.getBasic().setSimilarLength(xslLength);
		articleInfo.getBasic().setCompareLength(CompareLength);
		articleInfo.getBasic().setXsl(xsl);

		List<XsPaper> xsPapers = new ArrayList<>();
		for (int i = 0; i < xsPaperMaps.size(); i++) {
			XsPaper xsPaper = xsPaperMaps.get(i).getValue();
			xsPaper.setXsl((float) (xsPaper.getXslLength() * 1.00 / CompareLength));
			xsPapers.add(xsPaperMaps.get(i).getValue());
		}
		return xsPapers;
	}

	public void addXsPaperMap(Sentence sentence, Map<String, XsPaper> xsPaperMap) {
		if (sentence.getXsSenInfoList() == null || sentence.getXsSenInfoList().isEmpty())
			return;
		String title = sentence.getXsSenInfoList().get(0).getBasic().getXMTITLE();
		if (xsPaperMap.containsKey(title)) {
			int newXslLength = (int) (xsPaperMap.get(title).getXslLength()
					+ sentence.getXsSenInfoList().get(0).getXsl() * sentence.getLength());
			XsPaper xsPaper=xsPaperMap.get(title);
			xsPaper.setXslLength(newXslLength);
			xsPaperMap.remove(title);
			xsPaperMap.put(title,xsPaper);

			// xsPaperMap.replace(title, xsPaperMap.get(title),
			// xsPaperMap.get(title).setXslLength(newXslLength));
		} else {
			XsPaper xsPaper = new XsPaper();
			xsPaper.setXslLength((int) (sentence.getXsSenInfoList().get(0).getXsl() * sentence.getLength()));
			xsPaper.setTitle(sentence.getXsSenInfoList().get(0).getBasic().getXMTITLE());
			xsPaper.setUrl(sentence.getXsSenInfoList().get(0).getUrl());
			xsPaper.setBasic(sentence.getXsSenInfoList().get(0).getBasic());
			xsPaper.setSource(sentence.getXsSenInfoList().get(0).getUrl() == null ? true : false);// url为空，是本地，否则是网络
			xsPaperMap.put(title, xsPaper);
		}

	}

}
