package com.csuweb.PaperCheck.web.util;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class StringUtil {
	private static Hashtable<Character, Character> markHM = new Hashtable<Character, Character>();
	private static final Log LOG = LogFactory.getLog(StringUtil.class);

	/**
	 * 载入用于切分句子的标点符号
	 */
	static {
		char[] f = { '.', '。', ';', '；', '？', '?', '！', '!', '．', ',', '，' };// 短句
		for (int i = 0; i < f.length; i++) {
			markHM.put(f[i], f[i]);
		}
	}

	/**
	 * 过滤html标签
	 * 
	 * @param content
	 * @return
	 */
	public static String filter(String content) {
//		 String temp = content.replaceAll("\\<.*?\\>", "").replaceAll("\\\n",
//		 "").replaceAll("\\\t", "").replaceAll("\\{.*?\\}", "");
//		 temp = temp.replaceAll("\\<.*?\\>", "").replaceAll("\\\n",
//		 "").replaceAll("\\\t", "").replaceAll("\\{.*?\\}", "").replaceAll("  ", "");
//		return temp;
				 
		String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
		String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script;
		m_script = p_script.matcher(content);
		content = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(content);
		content = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(content);
		content = m_html.replaceAll(""); // 过滤html标签
		content=filterUnusualCharacter(content);
		Pattern p_n = Pattern.compile("[\\n||\\r\\n||\\r]{2,}", Pattern.CASE_INSENSITIVE);
		Matcher m_n = p_n.matcher(content);
		content = m_n.replaceAll("\n"); // 过滤\n标签

		return content;
	}
	
	public static void main(String[] args)throws Exception{
		String content="\n\n\n\n\n\n我们的\n\n";
		Pattern p_n = Pattern.compile("[\\n||\\r\\n||\\r]{2,}", Pattern.CASE_INSENSITIVE);
		Matcher m_n = p_n.matcher(content);
		String contents = m_n.replaceAll("\n"); // 过滤\n标签
		System.out.println(contents);
		content.replaceAll("#", "\n");
		System.out.println(filter(content));
	}

	public static String filterUnusualCharacter(String content) {
		content = content.replaceAll("&nbsp;", "");
		content = content.replaceAll("&copy;", "");
		content = content.replaceAll("&raquo;", "");
		content = content.replaceAll("&middot;", "");
		content = content.replaceAll("&gt;", "");
		content = content.replaceAll("&quot;", "");
		content = content.replaceAll("&middot;", "");
		content = content.replaceAll(" ", "");
		return content;
	}

	public static String subForeSen(String foreSen) {
		return subForeSen(foreSen, foreSen.length() - 1);
	}

	public static String subForeSen(String foreSen, int index) {
		int start = 0;
		for (int i = index - 1; i >= 0; i--) {
			if (markHM.containsKey(foreSen.charAt(i))) {
				if (index - 1 - i <= 3) {
					continue;
				} else {
					start = i + 1;
					break;
				}

			}
		}

		return foreSen.substring(start);
	}

	/**
	 * 从一段话中提取字符
	 * 
	 * @param sen
	 * @return
	 */
	public static String getfromSen(String sen) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sen.length(); i++) {
			if (isChinese(sen.charAt(i)) || isLetter(sen.charAt(i))
					|| StringUtils.isNumeric(String.valueOf(sen.charAt(i)))) {
				sb.append(sen.charAt(i));
			}
		}
		return sb.toString();
	}

	/**
	 * 删除中文中空格
	 * 
	 * @param string
	 * @return
	 */
	public static String deleteChSpace(String string) {
		if (string == null || string == "")
			return "";
		StringBuilder stringBuilder = new StringBuilder();
		try {
			stringBuilder.append(string.charAt(0));
		} catch (Exception e) {
			System.out.println(string);
			e.printStackTrace();
			LOG.error(string + " " + e);
		}

		for (int i = 1; i < string.length() - 1; i++) {
			if (string.charAt(i) == ' ' && !isLetter(string.charAt(i - 1)) && !isLetter(string.charAt(i + 1))) {
				continue;
			} else {
				stringBuilder.append(string.charAt(i));
			}
		}
		stringBuilder.append(string.charAt(string.length() - 1));
		return stringBuilder.toString();
	}

	/**
	 * 是否为英文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为中文
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		boolean result = false;
		if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
			if (c != '，' && c != '。' && c != '！' && c != '；')
				result = true;
		}

		return result;
	}

	// public static boolean isChinese(char c) {
	// Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	// if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub ==
	// Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	// || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub
	// == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
	// || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub ==
	// Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
	// || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
	// return true;
	// }
	// return false;
	// }

}
