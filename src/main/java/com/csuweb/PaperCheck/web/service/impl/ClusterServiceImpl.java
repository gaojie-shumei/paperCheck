package com.csuweb.PaperCheck.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.TokenStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.core.bean.Article;
import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.Paragraph;
import com.csuweb.PaperCheck.core.bean.Section;
import com.csuweb.PaperCheck.core.common.ParagraphAndSentenceAnalyzer;
import com.csuweb.PaperCheck.web.model.CheckDocWithBLOBs;
import com.csuweb.PaperCheck.web.service.ClusterService;
import com.csuweb.PaperCheck.web.util.ESUtils;
import com.hankcs.hanlp.HanLP;

@Service("ClusterService")
public class ClusterServiceImpl implements ClusterService {
	private int xsPaperNum = 5;
	private int sennum = 2;// 句子合并数
	private int keywordnum = 20;// 一个合并句所取的关键词
	private float keywordSubLength = 0.8f;

	/**
	 * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
	 */
	final static float d = 0.85f;
	/**
	 * 最大迭代次数
	 */
	final static int max_iter = 200;
	final static float min_diff = 0.001f;

	@Autowired
	private ParagraphAndSentenceAnalyzer paragraphAndSentenceAnalyzer;

	public void clusterArticle(CheckDocWithBLOBs checkDoc, Hashtable<String, Article> htArticles,
			Hashtable<String, Integer> ht) throws Exception {
		List<String> listsen = new ArrayList<String>();
		listsen = makePara(checkDoc);
		searchCompareLibfromSen(listsen, xsPaperNum, htArticles, ht, checkDoc.getTitle());
	}

	public List<String> makePara(CheckDocWithBLOBs checkDoc) {
		// List<List<String>> listsen=new ArrayList<List<String>>();
		List<String> listsen = new ArrayList<String>();
		// Map<List<String>,Integer> maps=new HashMap<List<String>,Integer>();
		// List<String> keyWords=new ArrayList<String>();
		String keyWords;
		ArticleInfo articleInfo = paragraphAndSentenceAnalyzer.extractParagraphAndSentence(checkDoc.getGuid(),
				checkDoc.getTitle(), checkDoc.getContent());
		for (Section section : articleInfo.getSectionList()) {
			for (Paragraph paragraph : section.getParagraphList()) {
				if (paragraph.getSentenceList().size() > sennum) {
					String p1 = "";
					for (int i = 1; i < paragraph.getSentenceList().size() / sennum + 1; i++) {
						for (int j = (i - 1) * sennum; j < i * sennum; j++) {
							p1 += paragraph.getSentenceList().get(j).getOriginSentence();
							// if(p1.length()>100)break;
						}
						keyWords = extractKeyWord(p1);
						listsen.add(keyWords);
						// maps.put(keyWords, 1);
						p1 = "";
						keyWords = "";
					}
					if (paragraph.getSentenceList().size() % sennum != 0) {
						for (int h = ((int) paragraph.getSentenceList().size() / sennum) * sennum; h < paragraph
								.getSentenceList().size(); h++) {
							p1 += paragraph.getSentenceList().get(h).getOriginSentence();
						}
						keyWords = extractKeyWord(p1);
						listsen.add(keyWords);
						// maps.put(keyWords, 1);
						p1 = "";
						keyWords = "";
					}
				} else {
					String p1 = "";
					for (int h = 0; h < paragraph.getSentenceList().size(); h++) {
						p1 += paragraph.getSentenceList().get(h).getOriginSentence();
					}
					keyWords = extractKeyWord(p1);
					listsen.add(keyWords);
					// maps.put(keyWords, 1);
					p1 = "";
					keyWords = "";
				}
			}
		}
		return listsen;
	}

	public void searchCompareLibfromSen(List<String> paras,int pagenum,
			final Hashtable<String, Article> htArticles,final Hashtable<String, Integer> ht,String title) throws Exception{
		
		List<String> listsen=new ArrayList<String>();
//		long time=System.currentTimeMillis();
		for(String keyWords:paras){
			int num=0;
			if(keyWords=="")continue;
			String[] aStrings=keyWords.split("#");
			for(String s:aStrings){
				listsen.add(s);
				num++;
				if(num>=aStrings.length*keywordSubLength||num>=keywordnum)break;
			}
			List<Object> articles= ESUtils.search("articles", "article", "content", listsen,title, 0, pagenum,Article.class);
			listsen.clear();
			if(articles==null)continue;
			for(int i=0;i<articles.size();i++){	
				Article article=(Article) articles.get(i);
				String xmguid=article.getId();
				if(!ht.containsKey(xmguid)){
					ht.put(xmguid, 1);
					htArticles.put(xmguid, article);
				}else {
					int num2=(Integer) ht.get(xmguid);
//					ht.replace(xmguid, num2, num2+1);
					ht.remove(xmguid);
					ht.put(xmguid, num2+1);
				}					
			}

		}	
//		System.out.println("查库时间："+(System.currentTimeMillis()-time));
	}

	public String extractKeyWord(String para) {

		Hashtable<String, Integer> ht = new Hashtable<>();
		String result = "";
		TokenStream ts = null;
		StringBuffer sb = null;
		List<String> v = null;
		int num = 0;

		List<String> keywordList = HanLP.extractKeyword(para, keywordnum);// 关键词
		// List<String> keywordList =getKeyword(para);
		for (String s : keywordList) {
			result += s + "#";
		}
		return result;
	}

}
