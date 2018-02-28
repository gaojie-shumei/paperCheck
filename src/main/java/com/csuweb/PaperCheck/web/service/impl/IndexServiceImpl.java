package com.csuweb.PaperCheck.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csuweb.PaperCheck.core.bean.Article;
import com.csuweb.PaperCheck.core.bean.Sentence;
import com.csuweb.PaperCheck.core.bean.SmallSentence;
import com.csuweb.PaperCheck.core.common.ParagraphAndSentenceAnalyzer;
import com.csuweb.PaperCheck.web.service.IndexService;
import com.csuweb.PaperCheck.web.util.ESUtils;

//import com.nesei.xmccCheck.bean.Article;
//import com.nesei.xmccCheck.bean.Sentence;
//import com.nesei.xmccCheck.bean.SmallSentence;
//import com.nesei.xmccCheck.common.ParagraphAndSentenceAnalyzer;
//import com.nesei.xmccCheck.service.IndexService;
//import com.nesei.xmccCheck.util.ESUtils;

@Service("IndexService")
public class IndexServiceImpl implements IndexService {
	int num=20;
	@Autowired
	private ParagraphAndSentenceAnalyzer paragraphAndSentenceAnalyzer;
	
	public void index(String DCPaperId,List<Article> xsArticles) throws Exception {
		for(int i=0;i<xsArticles.size();i++){
			index(DCPaperId,xsArticles.get(i));			
		}
	
	}
	
	public void index(String DCPaperId,Article xsArticle) throws Exception {
		List<Sentence> sentences=new ArrayList<Sentence>();
		List<Sentence> smallSentences=new ArrayList<Sentence>();
		List<SmallSentence> sentenceAll=new ArrayList<SmallSentence>();	
		sentences=paragraphAndSentenceAnalyzer.splitToList(xsArticle.getId(), xsArticle.getTitle(), xsArticle.getContent());
		if(!sentences.isEmpty()){
			for(Sentence sentence:sentences){
				if(!sentence.getSmallSentences().isEmpty()){
					smallSentences.addAll(sentence.getSmallSentences());
				}
			}
		}
		
		for(int i=0;i<smallSentences.size()-1;i++){
			Sentence smallSentence=smallSentences.get(i);
			SmallSentence HBSentence= new SmallSentence(null,null, smallSentence.getTitle(), smallSentence.getOriginSentence()+smallSentences.get(i+1).getOriginSentence(), smallSentence.getForeSentence(), smallSentences.get(i+1).getBehindSentence(), smallSentence.getLength()+smallSentences.get(i+1).getLength());
			sentenceAll.add(HBSentence);
		}				
			
		for(int i=0;i<sentences.size();i++){
			Sentence sentence=sentences.get(i);
			sentenceAll.add(new SmallSentence(null,null,  sentence.getTitle(), sentence.getOriginSentence(), sentence.getForeSentence(), sentence.getBehindSentence(), sentence.getLength()));
		}
		for(int i=0;i<smallSentences.size();i++){
			Sentence sentence=smallSentences.get(i);
			sentenceAll.add(new SmallSentence(null,null,  sentence.getTitle(), sentence.getOriginSentence(), sentence.getForeSentence(), sentence.getBehindSentence(), sentence.getLength()));
		}
		ESUtils.indexSentence(DCPaperId, "SmallSentence", sentenceAll);
	}


}
