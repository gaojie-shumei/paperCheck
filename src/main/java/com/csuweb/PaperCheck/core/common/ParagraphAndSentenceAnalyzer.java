package com.csuweb.PaperCheck.core.common;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csuweb.PaperCheck.core.bean.ArticleInfo;
import com.csuweb.PaperCheck.core.bean.BasicInfo;
import com.csuweb.PaperCheck.core.bean.Paragraph;
import com.csuweb.PaperCheck.core.bean.Section;
import com.csuweb.PaperCheck.core.bean.Sentence;
import com.csuweb.PaperCheck.web.util.StringUtil;
import com.hankcs.hanlp.HanLP;
//import com.nesei.xmccCheck.bean.ArticleInfo;
//import com.nesei.xmccCheck.bean.BasicInfo;
//import com.nesei.xmccCheck.bean.Paragraph;
//import com.nesei.xmccCheck.bean.Section;
//import com.nesei.xmccCheck.bean.Sentence;
//import com.nesei.xmccCheck.util.StringUtil;

@Component
public class ParagraphAndSentenceAnalyzer {
	private int senNum = 50;
	private int foreBehindLength = 20;

	private static ParagraphAndSentenceAnalyzer paragraphAndSentenceAnalyzer;
	// markHM 分句
	// markHN 二次分句
	/**
	 * 长句分隔符
	 */
	private static Hashtable<Character, Character> markHM = new Hashtable<Character, Character>();
	/**
	 * 短句分隔符
	 */
	private static Hashtable<Character, Character> markHN = new Hashtable<Character, Character>();
	public int par_startId = 0;

	public ParagraphAndSentenceAnalyzer() {
		LoadMark();
	}

	public synchronized static ParagraphAndSentenceAnalyzer getInstance() {
		if (paragraphAndSentenceAnalyzer == null) {
			paragraphAndSentenceAnalyzer = new ParagraphAndSentenceAnalyzer();
		}
		return paragraphAndSentenceAnalyzer;
	}

	/**
	 * 将文章内容分成一句一句用于爬虫
	 * 
	 * @param content
	 * @return
	 */
	public List<String> crawlerSplitToList(String content) {
		return crawlerSplitToList(content, senNum);
	}

	/**
	 * 将文章内容分成一句一句用于爬虫
	 * 
	 * @param content
	 * @return
	 */
	public List<String> crawlerSplitToList(String content, int number) {
		content = StringUtil.deleteChSpace(content);
		List<String> list = new ArrayList<String>();
		List<String> sentences = new ArrayList<String>();
		list = splitSentence(content);
		if (list.size() > number * 1.5) {
			// if(list.size()>1.5){
			int num = 1;
			if (list.size() % number != 0) {
				num = ((int) list.size() / number) + 1;
			} else {
				num = ((int) list.size() / number);
			}
			int index = num;
			while (list.size() >= index) {
				List<String> sList = list.subList(index - num, index);
				String sentence = "";
				for (String string : sList) {
					sentence += string;
				}
				List<String> keywordList = HanLP.extractKeyword(sentence, 30);
				String words = "";
				for (String word : keywordList) {
					words += word;
				}
				sentences.add(words);
				// sentences.add(sentence);
				index += num;
			}
			if (list.size() % num != 0) {
				List<String> sList = list.subList(index - num, list.size());
				String sentence = "";
				for (String string : sList) {
					sentence += string;
				}
				List<String> keywordList = HanLP.extractKeyword(sentence, 20);
				String words = "";
				for (String word : keywordList) {
					words += word;
				}
				sentences.add(words);
			}
		} else {
			return list;
		}
		return sentences;
	}

	/**
	 * 将文章分成ArticleInfo（不含模板）
	 * 
	 * @param paperid
	 * @param title
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public ArticleInfo DCSplitToArticle(String paperid, String title, String content) {
		content = StringUtil.deleteChSpace(content);
		List<Section> sections = new ArrayList<Section>();
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();
		List<String> listParagram = new ArrayList<String>();
		int compareLength = 0;

		listParagram = splitParagraph(content);
		String paraContent = "";// 段落太短为标题，中短则合并
		for (int i = 0; i < listParagram.size(); i++) {
			String listp = listParagram.get(i).trim() + "\r\n";
			paraContent += listp;
			if (paraContent.length() < 20 && paraContent.length() > 3) {// 标题
				Paragraph paragraph = new Paragraph();
				paragraph.setLength(paraContent.length());
				paragraph.setId(par_startId++);
				paragraph.setOriginContent(paraContent);
				paragraph.setTitleFlag(true);
				paragraphs.add(paragraph);
				paraContent = "";
				continue;
			} else if (paraContent.length() < 50 && i < listParagram.size() - 1) {// 中短句合并,且不为最后一句
				continue;
			} else {
				Paragraph paragraph = new Paragraph();
				paragraph.setLength(paraContent.length());
				paragraph.setId(par_startId++);
				paragraph.setOriginContent(paraContent);
				compareLength += paraContent.length();
				List<Sentence> sentences = splitToList(paperid, title, paraContent);
				paragraph.setSentenceList(sentences);
				paragraphs.add(paragraph);
				paraContent = "";
			}
		}

		ArticleInfo articleInfo = new ArticleInfo();

		BasicInfo basicInfo = new BasicInfo();
		basicInfo.setXMGUID(paperid);
		basicInfo.setXMTITLE(title);
		basicInfo.setTotalLength(content.length());
		Section section = new Section();
		section.setId(0);
		section.setParagraphList(paragraphs);
		sections.add(section);
		articleInfo.setContent(content);
		basicInfo.setCompareLength(compareLength);
		articleInfo.setBasic(basicInfo);
		articleInfo.setSectionList(sections);
		return articleInfo;
	}

	/**
	 * 语句分成句子列表，有前后句、小句
	 * 
	 * @param paperid
	 * @param title
	 * @param content
	 * @return
	 */
	public List<Sentence> splitToList(String paperid, String title, String content) {
		content = StringUtil.deleteChSpace(content);
		int sen_startId = 0;
		List<String> listLong = new ArrayList<String>();
		List<Sentence> sentences = new ArrayList<Sentence>();
		listLong = splitSentenceByMark(content, markHM);
		String laString = "";
		for (int i = 0; i < listLong.size(); i++) {
			List<Sentence> smallSentences = new ArrayList<Sentence>();
			String string = laString + listLong.get(i).trim();
			laString = string;
			if (string.length() < 3)
				continue;
			else {
				laString = "";
			}
			Sentence sentence = new Sentence();
			sentence.setId(i + "_" + 0 + "_" + 0);
			sentence.setOriginSentence(string);
			sentence.setPaperId(paperid);
			sentence.setTitle(title);
			sentence.setLength(string.length());
			if (i == 0) {
				sentence.setForeSentence("");
			} else {
				sentence.setForeSentence(listLong.get(i - 1));
			}

			if (i == listLong.size() - 1) {
				sentence.setBehindSentence("");
			} else {
				sentence.setBehindSentence(listLong.get(i + 1));
			}

			List<String> listShort = new ArrayList<String>();
			listShort = splitSentenceByMark(listLong.get(i), markHN);
			for (int j = 0; j < listShort.size(); j++) {
				int h = j + 1;
				Sentence smallSentence = new Sentence();
				String string2 = listShort.get(j);
				smallSentence.setId(i + "_" + h + "_" + 0);
				smallSentence.setOriginSentence(string2);
				smallSentence.setPaperId(paperid);
				smallSentence.setTitle(title);
				smallSentence.setLength(string2.length());
				if (j == 0) {
					if (i == 0) {
						smallSentence.setForeSentence("");
					} else {
						smallSentence.setForeSentence(listLong.get(i - 1).length() > foreBehindLength
								? listLong.get(i - 1).substring(listLong.get(i - 1).length() - 20)
								: listLong.get(i - 1));
					}
				} else {
					smallSentence.setForeSentence(listShort.get(j - 1).length() > foreBehindLength
							? listShort.get(j - 1).substring(listShort.get(j - 1).length() - 20)
							: listShort.get(j - 1));
				}

				if (j == listShort.size() - 1) {
					if (i == listLong.size() - 1) {
						smallSentence.setBehindSentence("");
					} else {
						smallSentence.setBehindSentence(listLong.get(i + 1).length() > foreBehindLength
								? listLong.get(i + 1).substring(0, 20) : listLong.get(i + 1));
					}
				} else {
					smallSentence.setBehindSentence(listShort.get(j + 1).length() > foreBehindLength
							? listShort.get(j + 1).substring(0, 20) : listShort.get(j + 1));
				}
				smallSentences.add(smallSentence);
			}
			sentence.setSmallSentences(smallSentences);
			sentences.add(sentence);
		}
		return sentences;
	}

	public List<String> splitSentenceIsLong(String content, boolean isLong) {
		if (isLong) {
			return splitSentenceByMark(content, markHM);
		} else {
			return splitSentenceByMark(content, markHN);
		}
	}

	private List<String> splitSentenceByMark(String content, Hashtable<Character, Character> mark) {
		List<String> sentenceList = new ArrayList<String>();
		int start = 0;
		int length = content.length();
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				sentenceList.add(content.substring(start, i + 1));
			} else if (mark.containsKey(content.charAt(i))) {
				if (content.charAt(i) == '.') {
					if ((i == 0) || (!StringUtil.isLetter(content.charAt(i - 1)) || (i - start < 5))) {
						continue;
					}
				}
				if (i - start <= 5) {
					if (content.charAt(i) == '.') {
						String sub = content.substring(i, content.length());
						Pattern pattern = Pattern.compile("^\\.*");
						Matcher matcher = pattern.matcher(sub);
						if (matcher.find()) {
							int len = matcher.group().length();
							if (i - start + len > 10) {
								i = i + len - 1;
							} else {
								continue;
							}
						}
					} else {
						continue;
					}
				}
				sentenceList.add(content.substring(start, i + 1));
				start = i + 1;

			}
		}
		return sentenceList;
	}

	/**
	 * 将文章内容分段分句
	 * 
	 * @param content
	 * @return
	 */
	public ArticleInfo extractParagraphAndSentence(String paperid, String title, String content) {
		content = StringUtil.deleteChSpace(content);//将文章内容中无用的字符去除如中文之间的空格
		int compareLength = 0;//要比较的长度
		ArticleInfo articleInfo = new ArticleInfo();//文章的相关信息

		BasicInfo basicInfo = new BasicInfo();//基本信息
		basicInfo.setXMGUID(paperid);
		basicInfo.setXMTITLE(title);
		basicInfo.setTotalLength(content.length());

		List<String> allSenString = new LinkedList<String>();
		List<String> listParagram = new ArrayList<String>();
		List<String> listSentence = new ArrayList<String>();
		List<Sentence> sentences = new ArrayList<Sentence>();//句子集合
		List<Section> sections = new ArrayList<Section>();//章节集合
		List<Paragraph> paragraphs = new ArrayList<Paragraph>();//段落集合

		String paraString = "";
		int par_startId = 0;//段落开始id
		int sen_startId = 0;//句子开始id

		Section section = new Section();//章节
		section.setId(0);

		listParagram = splitParagraph(content);

		for (int i = 0; i < listParagram.size(); i++) {
			String listp = listParagram.get(i);
			paraString += listp;

			if (paraString.length() < 200) {
				if (i != listParagram.size() - 1)
					continue;
			}
			Paragraph par = new Paragraph();
			par.setLength(paraString.length());
			par.setOriginContent(paraString);
			par.setId(par_startId++);

			listSentence = splitSentence(paraString);
			paraString = "";

			for (int j = 0; j < listSentence.size(); j++) {
				String sen_String = listSentence.get(j);
				// 后一句
				int par_size = paragraphs.size();
				int sen_szie = par.getSentenceList().size();
				if (sen_szie == 0) {// 本段落第一句
					if (par_size > 0) {// 前面还有段落
						int sen_index = paragraphs.get(par_size - 1).getSentenceList().size() - 1;// 得到前面段落最后一句，赋值后一句
						paragraphs.get(par_size - 1).getSentenceList().get(sen_index).setBehindSentence(sen_String);
					}
				} else {// 非本段落第一句，得到本段落前一句赋值
					par.getSentenceList().get(sen_szie - 1).setBehindSentence(sen_String);
				}

				if (sen_String.length() < 5)
					continue;
				// System.out.println(shingle);
				Sentence sentence = new Sentence();
				sentence.setPaperId(paperid);
				sentence.setTitle(title);
				sentence.setOriginSentence(sen_String);
				// 前一句
				allSenString.add(sen_String);
				if (sen_startId > 0) {// 非第一句有前一句
					sentence.setForeSentence(allSenString.get(sen_startId - 1));
				} else {// 第一句无
					sentence.setForeSentence("");
				}
				compareLength += sen_String.length();
				sentence.setId(sen_startId++ + "");
				sentence.setLength(sen_String.length());
				sentences.add(sentence);
				par.getSentenceList().add(sentence);

			}
			paragraphs.add(par);
		}
		section.setParagraphList(paragraphs);
		sections.add(section);
		articleInfo.setContent(content);
		basicInfo.setCompareLength(compareLength);
		articleInfo.setBasic(basicInfo);
		articleInfo.setSectionList(sections);
		return articleInfo;
	}

	/**
	 * 分段
	 * 
	 * @param content
	 * @return
	 */
	private List<String> splitParagraph(String content) {
		String[] sen;
		List<String> list = new ArrayList<String>();
		int start = 0;
		int length = content.length();
		String dealContent="";
//		dealContent=content;
		for (int j=0;j<length;j++){
			if (j == length - 1) {
				dealContent += content.substring(start, length).replaceAll("\\r|\\n","");
			}else{
				if (content.charAt(j) == '.'||content.charAt(j)=='。'||content.charAt(j)=='?'||content.charAt(j)=='？'||content.charAt(j)=='!'||content.charAt(j)=='！'||content.charAt(j)=='；'||content.charAt(j)==';'||content.charAt(j)==','||content.charAt(j)=='，') {
					dealContent += content.substring(start, j+1).replaceAll("\\r|\\n","");
					start = j + 1;
				}else{
					if(content.charAt(j)=='\n'||content.charAt(j)=='\r') {
						boolean panduan = content.charAt(j-1) == '.'||content.charAt(j-1)=='。'||content.charAt(j-1)=='?'||content.charAt(j-1)=='？'||content.charAt(j-1)=='!'||content.charAt(j-1)=='！';
						if(panduan) {
							dealContent += content.substring(start, j+1);
							start = j + 1;
						}else if(j-start>=200){
							dealContent += content.substring(start, j+1);
							start = j + 1;
						}
					}
				}
			}
		}
		String f = "\\r|\\n";
		sen = dealContent.split(f);
		for (int i = 0; i < sen.length; i++) {
			if (!sen[i].isEmpty() && sen[i].length() > 10) {
				list.add(sen[i].toString());
			}
		}
		return list;
	}

	/**
	 * 分句
	 * 
	 * @param content
	 * @return
	 */
	private List<String> splitSentence(String content) {
		List<String> sentenceList = new ArrayList<String>();
		int start = 0;
		int length = content.length();
		String sen = "";
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
//				sen = content.substring(start, length);
				sen = content.substring(start, length).replaceAll("\\r|\\n","");
				addSentence(sen, sentenceList);
			} else if (markHM.containsKey(content.charAt(i))) {
				if (content.charAt(i) == '.'||content.charAt(i)=='。'||content.charAt(i)=='?'||content.charAt(i)=='？'||content.charAt(i)=='!'||content.charAt(i)=='！'||content.charAt(i)=='；'||content.charAt(i)==';') {
					if ((i == 0) || (!StringUtil.isLetter(content.charAt(i - 1)) || (i - start < 10))) {
						continue;
					}
				}
				if (i - start >= 10) {
					if (content.charAt(i) == '.'||content.charAt(i)=='。'||content.charAt(i)=='?'||content.charAt(i)=='？'||content.charAt(i)=='!'||content.charAt(i)=='！'||content.charAt(i)=='；'||content.charAt(i)==';') {
						String sub = content.substring(i, content.length());
						Pattern pattern = Pattern.compile("^\\.*|。*|\\?*|？*|!*|！*");
						Matcher matcher = pattern.matcher(sub);
						if (matcher.find()) {
							int len = matcher.group().length();
							if (i - start + len > 10) {
								i = i + len - 1;
							} else {
								continue;
							}
						}
					} else {
						continue;
					}
				}
//				sen = content.substring(start, i+1);
				sen = content.substring(start, i+1).replaceAll("\\r|\\n","");
				addSentence(sen, sentenceList);
				start = i + 1;

			}
		}
		return sentenceList;
	}

	/**
	 * 处理句子
	 * 
	 * @param sentence
	 * @param sentenceList
	 */
	private void addSentence(String sentence, List<String> sentenceList) {
		if (sentence.length() < 10 && sentenceList.size() > 0) // ���������о��㷨����β�����г��ľ��ӳ���С��10.���ֱ����ӵ���һ�������С�
		{
			String newSen = sentenceList.remove(sentenceList.size() - 1) + sentence;
			sentenceList.add(newSen);

		} else if (sentence.length() < 200) {
			sentenceList.add(sentence);
		} else {
			int middle = sentence.length() / 2;
			for (int i = 0; i < middle - 10; i++) {
				if (markHN.containsKey(sentence.charAt(middle - i))) {
					int breakLength = middle - i + 1;
					addSentence(sentence.substring(0, breakLength), sentenceList);
					addSentence(sentence.substring(breakLength, sentence.length()), sentenceList);
					return;
				} else if (markHN.containsKey(sentence.charAt(middle + i))) {
					int breakLength = middle + i + 1;
					addSentence(sentence.substring(0, breakLength), sentenceList);
					addSentence(sentence.substring(breakLength, sentence.length()), sentenceList);
					return;
				}
			}
			// ʵ��û�ҵ���ֵ㣬���Ӳ��ɲ�֣�ǿ�Ʋ��
			addSentence(sentence.substring(0, middle), sentenceList);
			addSentence(sentence.substring(middle, sentence.length()), sentenceList);
		}
	}

	/**
	 * 加载符号库
	 */
	private void LoadMark() {
		// char[] f = { '.', '。', ';', '；', '？', '?', '！', '!','．',',','，'};//短句
		// char[] fc = {',','，',')','）',':','：',';', '；'};
		char[] fc = { ',', '，', ':', '：', ';', '；', '，' };
		char[] f = { '。', '？', '?', '！', '!', '.', '。' };// 长句
		for (int i = 0; i < f.length; i++) {
			markHM.put(f[i], f[i]);
		}
		for (int i = 0; i < fc.length; i++) {
			markHN.put(fc[i], fc[i]);
		}
	}

}
