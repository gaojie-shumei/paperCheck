 /** 
 * Project Name:PaperCheck 
 * File Name:StringTokenizerTest.java 
 * Package Name:com.csuweb.PaperCheck.test 
 * Date:2018年1月3日下午12:36:10 
 * Copyright (c) 2018, taoge@tmd.me All Rights Reserved. 
 * 
 */  
  
package com.csuweb.PaperCheck.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/** 
 * ClassName:StringTokenizerTest <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2018年1月3日 下午12:36:10 <br/> 
 * @author   Administrator 
 * @version   
 * @since    JDK 1.8 
 * @see       
 */
public class StringTokenizerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		StringTokenizer str = new StringTokenizer("hello world i love you");//StringTokenizer依据空格进行分割
//		while(str.hasMoreTokens()){
//			System.out.println(str.nextToken());
//		}
		try {
			File file = new File("F:\\2000-001.caj");
			//String result = new Tika().parseToString(new File("F:\\2000-001.caj"));
			String result = readFileByReader(file);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String readFileByReader(File file){
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line = "";
			while((line=br.readLine())!=null){
				result.append(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.toString();
	}

}
  