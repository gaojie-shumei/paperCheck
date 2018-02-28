package com.csuweb.PaperCheck.core.common;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Profile;



public class TxtAnalyse {
	private static final String pageStart = "--pageStart--";
	private static final String paraStart = "--paraStart--";
	private static final String lineEnd = "\n";
	private static final String endChars = "！？。.!”";
	private static final int titleLenTh = 10;
	private static final int textLenTh = 20;	
	private static TxtAnalyse m_instance=null;
	public static TxtAnalyse getTxtAnalyse() {
		if(m_instance==null){
			m_instance=new TxtAnalyse();
		}
		return m_instance;
	}

	/**
	 * XML十六进制无效字符的处理
	 * @param tmp
	 * @return
	 */
	public static String RemainFW(String str){
		StringBuilder sb=new StringBuilder();
		for(char ss:str.toCharArray()){
			if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss <= 32)) || (ss == 127))
	               sb.append("");//&#x{0:X};
			else if (ss=='。'||ss=='，') {
				sb.append("");//&#x{0:X};
			}
	           else sb.append(ss);
		}
		return sb.toString();
	}
	
	/**
	 * XML十六进制无效字符的处理
	 * @param tmp
	 * @return
	 */
	public static String ReplaceLowOrderASCIICharacters(String tmp)
    {
       StringBuilder info = new StringBuilder();
       for(int i=0;i<tmp.length();i++){
    	   char cc=tmp.charAt(i);    	   
           int ss = (int)cc;
           if (((ss >= 0) && (ss <= 8)) || ((ss >= 11) && (ss <= 12)) || ((ss >= 14) && (ss <= 32)) || (ss == 127))
               info.append(" ");//&#x{0:X};
           else info.append(cc);
       }
       return info.toString();//.Trim();
    }
	
	public static String dealPdfContent(String content) {
		if(content!=null) {
			StringBuffer strBuf = new StringBuffer();
			if (!useParaStart(content)) {
				//return null;
				content = content.replaceAll(pageStart, "");
				content = content.replaceAll(paraStart, "\r\n");
				content = getInfoFromTxt(content);
				return content;
			} else {
//			content=TxtAnalyse.getInfoFromTxt(content);
				String[] lines = reviseParaStart(content);
				String[] res = filterDocument(lines).split(paraStart);
				for (String r : res) {
					strBuf.append(r);
					strBuf.append("\r\n");
				}
				return strBuf.toString();
			}
		}else{
			return content;
		}
			
	}
	
	/**
	 * 消除文章的虚假换行空格
	 * @param data
	 * @param leibie
	 */
	public static String getInfoFromTxt(String data){
		
//		 Pattern pattern=Pattern.compile("[.。）\\);；：:](( )*)\\r\\n");
		Pattern pattern=Pattern.compile("[.。：:](\\s*)\\r\\n");
		 Matcher matcher=pattern.matcher(data);
         data = matcher.replaceAll("#####");
         
         Pattern pattern2=Pattern.compile("\\r\\n");
		 Matcher matcher2=pattern2.matcher(data);
         data = matcher2.replaceAll("");
         data = data.replaceAll("#####", "。\r\n");

		 Pattern pattern4=Pattern.compile("(?<=[^\\x00-\\xff])\\s(?=[^\\x00-\\xff])");
		 Matcher matcher4=pattern4.matcher(data);
		 data = matcher4.replaceAll("");
		return data;
	}
	
	private static boolean useParaStart(String content)
	{
		String[] lines = content.split(lineEnd);
		int sum = 0;
		int count = 0;
		for(int i=0; i<lines.length; i++)
		{
			lines[i] = lines[i].trim();
		}
		for(int i=0; i<lines.length; i++)
		{
			if(!lines[i].isEmpty())
			{
				sum++;
				if(lines[i].startsWith(paraStart))
				{
					count++;
				}
			}
		}
		return count < sum * 0.5;
	}
	//revise the position of paraStart
	private static String[] reviseParaStart(String content)
	{
		String[] lines = content.split(lineEnd);
		for(int i=0; i<lines.length; i++)
		{
			lines[i] = lines[i].trim();
		}
		for(int i=0; i<lines.length; i++)
		{
			String currentLine = lines[i];
			if(currentLine.startsWith(paraStart) && i >= 2)
			{
				String preLine = lines[i-1];
				String prepreLine = lines[i-2];
				if(!preLine.startsWith(paraStart) && !isTitle(currentLine) 
						&& isParagraphFirst(preLine) && isParagraphLast(prepreLine))
				{
					lines[i] = currentLine.substring(paraStart.length());
					lines[i-1] = paraStart + preLine;
				}
			}
		}
		return lines;
	}
	private static boolean isTitle(String line)
	{
		if(line.startsWith(paraStart))
		{
			line = line.substring(paraStart.length());
		}
		return line.length() < titleLenTh;
	}
	private static boolean isParagraphLast(String line)
	{
		if(line.isEmpty())
		{
			return true;
		}
		char end = line.charAt(line.length() - 1);
		if(line.length() < textLenTh || endChars.indexOf(end) >= 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private static boolean isParagraphFirst(String line)
	{
		if(line.startsWith(paraStart))
		{
			line = line.substring(paraStart.length());
		}
		return line.length() > textLenTh;
	}
	
	private static String filterDocument(String[] lines)
	{
		StringBuffer strBuf = new StringBuffer();
		int start = 0;
		for(int i=0; i<lines.length; i++)
		{
			if(lines[i].indexOf(pageStart) >= 0)
			{
				strBuf.append(filterPage(lines, start, i));
				start = i + 1;
			}
		}	
		if(start < lines.length)
		{
			strBuf.append(filterPage(lines, start, lines.length));
		}
		return strBuf.toString();
	}
	
	private static String filterPage(String[] lines, int from, int to)
	{
		StringBuffer strBuf = new StringBuffer();
		int start = from;
		for(int i=from; i<to-1; i++)
		{
			if(lines[i].startsWith(paraStart) && lines[i+1].startsWith(paraStart))
			{
				start = i + 1;
			}
			else
			{
				break;
			}
		}
		for(int i=start; i<to; i++)
		{
			strBuf.append(lines[i]);
		}
		return strBuf.toString().startsWith(paraStart)? strBuf.toString().substring(paraStart.length()) : strBuf.toString();
	}
	
	/**
	 * 获取文章章节信息
	 * @param data 文档中提取出的纯文本内容
	 * @param leibie
	 * @param listsection 单个章节信息被组织为XmInfo节点，存储于该集合中 
	 * @throws Exception 
	 */
	/*public static boolean getSectionInfoFromTxt(String data,String leibie,List<XmInfo> listsection) throws Exception{ 
		List<String> listcode=new ArrayList<String>();
		List<String> titlecode=new ArrayList<String>();
		//获取文档的章节信息
		String code = PropertiesUtil.get("leibie", leibie);//获取项目类别数字代号
		String configkey = PropertiesUtil.get("txt", code);//获取相应类别的txt。		
		Iterator<String> emu=PropertiesUtil.key(configkey);
		
		while(emu.hasNext()){
			String ss= PropertiesUtil.get(configkey, emu.next());
			listcode.add(ss); 
		}		
		
		configkey = PropertiesUtil.get("title", code);
		emu = PropertiesUtil.key(configkey);
		
		while(emu.hasNext()){
			String ss= PropertiesUtil.get(configkey, emu.next());
			titlecode.add(ss);
		}
		
		int start = -1;
        int end = -1;
		for(int i=0;i<listcode.size();i++){
			String[] valueArr=listcode.get(i).split("_");
			String title=valueArr[0];
			String titlekey=valueArr[1];
			
	        XmInfo xInfo=new XmInfo(); 
	        xInfo.content=data;
	        xInfo.secname=listcode.get(i);
	        xInfo.originname = titlecode.get(i); 
	        if(searchTitle(xInfo,data, title, end+1, titlekey)){
	        	xInfo.seccontent=data.substring(xInfo.start, xInfo.end);
	        	end=xInfo.end;
	        	listsection.add(xInfo);
	        }
	        else {
	        	xInfo.seccontent=data.substring(xInfo.start, xInfo.end);
	        	listsection.add(xInfo);
			}
		}
		Collections.sort(listsection);
		
		start = -1;
		end = -1;
		for(int i=0;i<listsection.size();i++){
			if(listsection.get(i).end==0){				
		        String[] valueArr=listsection.get(i).secname.split("_");
		        String title=valueArr[0];
				String titlekey=valueArr[1];
				
				XmInfo xInfo=new XmInfo(); 
		        xInfo.content=data;
		        xInfo.secname=listcode.get(i);
				if(searchTitle(xInfo,data, title, 0, titlekey)){
					start= listsection.get(i).start;
					end= listsection.get(i).end;
					listsection.get(i).seccontent=data.substring(start, end);
				}
			}
		}
		Collections.sort(listsection);		
		int i1 = 0, j1 = 0;//i1用于统计可选项的总数，可选为0，j1用于统计必选项的总数，必选为1  
		int i2 = 0, j2 = 0;//i2记录必选项中value为空的总数，j2用于统计可选项中value为空的总数
		for(int i=0;i<listsection.size();i++){
			String[] valueArr=listsection.get(i).secname.split("_");
			String titlekey=valueArr[1];
			if(titlekey.equals("0")){
				i1++;
			}
			else {
				j1++;
			}
			if(listsection.get(i).seccontent.toString().equals("")&&titlekey.equals("1")){
				i2++;
			}
			if(listsection.get(i).seccontent.toString().equals("")&&titlekey.equals("0")){			
			    j2++;
			}
		}		
		 if ((1 - (float)i2 / i1) <= 0.7)
         {
			 XmInfo xInfo=new XmInfo(); 
			 xInfo.content=data;
			 xInfo.seccontent=data;
			 listsection.clear();
			 listsection.add(xInfo);
         }
		 return true;
	}*/
	
	/**
	 * 根据章节名称（title）检索内容（str）中该章节的起始位置（存入xmInfo.start和xmInfo.end中），返回是否找到的bool值
	 * @param str txt内容
	 * @param title 要在txt中搜索的章节标题
	 * @param inStart 
	 * @param //start
	 * @param //end
	 * @param titlekey
	 * @return
	 */
	/*public static boolean searchTitle(XmInfo xmInfo,String str,String title,int inStart,String titlekey){
		int num=0;	int index=0;int start=-1;int end =-1;
		List<Integer> list=new ArrayList<Integer>();
		
				
		String str2=str.substring(inStart);
		Pattern pattern=Pattern.compile(title);	
		Matcher mc=pattern.matcher(str2);
		
		while(mc.find()){
			num++;
			list.add(mc.start());
			
		}
		if (titlekey.equals("1") && num >= 2){
			index=list.get(1)+inStart;
		}else if(num==0){
			return false;
		}
		else {
			index=list.get(0)+inStart;
		}
		int i = index - 1; boolean startfind = false;
        int j = index + 1; boolean endfind = false;
		
        for (; i >= 0; i--) // 找到标题的头
        {
            if (str.charAt(i)==('\r'))
            {
                startfind = true;
                break;
            }
        }
        if (startfind)
            start = i + 1;
        for (; j < str.length(); j++)// 找到标题的尾
        {
            if (str.charAt(j)==('\r'))
            {
                endfind = true;
                break;
            }
        }
        if (endfind)
            end = j + 1;
        xmInfo.start=start;
        xmInfo.end=end;
        if (startfind && endfind)
            return true;
        else
            return false;  
	}*/
	

}
