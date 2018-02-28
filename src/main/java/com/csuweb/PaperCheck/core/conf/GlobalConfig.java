package com.csuweb.PaperCheck.core.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






public class GlobalConfig {
	private static final String configFile = "application.properties";
	private static final Log LOG = LogFactory.getLog(GlobalConfig.class);
	private static String crawlerStart;
	private static String crawlerOn;
	private static String crawlerEnd;	
	private static String crawlerWrong;
	private static String clusterStart;
	private static String clusterOn;
	private static String clusterEnd;	
	private static String clusterWrong;
	private static String computerStart;
	private static String computerOn;
	private static String computerEnd;
	private static String computerWrong;
	private static String reportStart;
	private static String reportOn;
	private static String reportEnd;	
	private static String reportWrong;
	private static String reportSavePath;
	
	
	
	static
	{
		try {
			init(); 
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error(e);
		}
	}
	
	private static void init() throws IOException{
		String path = GlobalConfig.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		path=path.substring(0, path.lastIndexOf("/")+1);
		Properties prop = new Properties();
		InputStream in= new FileInputStream(new File(path+configFile));
		prop.load(in);
		crawlerStart=prop.getProperty("crawlerStart");
		crawlerOn=prop.getProperty("crawlerOn");
		crawlerEnd=prop.getProperty("crawlerEnd");
		crawlerWrong=prop.getProperty("crawlerWrong");
		clusterStart=prop.getProperty("clusterStart");
		clusterOn=prop.getProperty("clusterOn");
		clusterEnd=prop.getProperty("clusterEnd");
		clusterWrong=prop.getProperty("clusterWrong");
		computerStart=prop.getProperty("computerStart");
		computerOn=prop.getProperty("computerOn");
		computerEnd=prop.getProperty("computerEnd");
		computerWrong=prop.getProperty("computerWrong");
		reportStart=prop.getProperty("reportStart");
		reportOn=prop.getProperty("reportOn");
		reportEnd=prop.getProperty("reportEnd");
		reportWrong=prop.getProperty("reportWrong");			
		reportSavePath=prop.getProperty("reportSavePath");
	}

	public static String getConfigfile() {
		return configFile;
	}

	public static Log getLog() {
		return LOG;
	}

	public static String getCrawlerStart() {
		return crawlerStart;
	}

	public static String getCrawlerOn() {
		return crawlerOn;
	}

	public static String getCrawlerEnd() {
		return crawlerEnd;
	}

	public static String getCrawerlWrong() {
		return crawlerWrong;
	}

	public static String getClusterStart() {
		return clusterStart;
	}

	public static String getClusterOn() {
		return clusterOn;
	}

	public static String getClusterEnd() {
		return clusterEnd;
	}

	public static String getClusterWrong() {
		return clusterWrong;
	}

	public static String getComputerStart() {
		return computerStart;
	}

	public static String getComputerOn() {
		return computerOn;
	}

	public static String getComputerEnd() {
		return computerEnd;
	}

	public static String getComputerWrong() {
		return computerWrong;
	}

	public static String getReportStart() {
		return reportStart;
	}

	public static String getReportOn() {
		return reportOn;
	}

	public static String getReportEnd() {
		return reportEnd;
	}

	public static String getReportWrong() {
		return reportWrong;
	}

	public static String getReportSavePath() {
		return reportSavePath;
	}

	public static void setCrawerlStart(String crawerlStart, String crawlerStart) {
		GlobalConfig.crawlerStart = crawlerStart;
	}

	public static void setCrawerlOn(String crawlerOn) {
		GlobalConfig.crawlerOn = crawlerOn;
	}

	public static void setCrawerlEnd(String crawlerEnd) {
		GlobalConfig.crawlerEnd = crawlerEnd;
	}

	public static void setCrawerlWrong(String crawlerWrong) {
		GlobalConfig.crawlerWrong = crawlerWrong;
	}

	public static void setClusterStart(String clusterStart) {
		GlobalConfig.clusterStart = clusterStart;
	}

	public static void setClusterOn(String clusterOn) {
		GlobalConfig.clusterOn = clusterOn;
	}

	public static void setClusterEnd(String clusterEnd) {
		GlobalConfig.clusterEnd = clusterEnd;
	}

	public static void setClusterWrong(String clusterWrong) {
		GlobalConfig.clusterWrong = clusterWrong;
	}

	public static void setComputerStart(String computerStart) {
		GlobalConfig.computerStart = computerStart;
	}

	public static void setComputerOn(String computerOn) {
		GlobalConfig.computerOn = computerOn;
	}

	public static void setComputerEnd(String computerEnd) {
		GlobalConfig.computerEnd = computerEnd;
	}

	public static void setComputerWrong(String computerWrong) {
		GlobalConfig.computerWrong = computerWrong;
	}

	public static void setReportStart(String reportStart) {
		GlobalConfig.reportStart = reportStart;
	}

	public static void setReportOn(String reportOn) {
		GlobalConfig.reportOn = reportOn;
	}

	public static void setReportEnd(String reportEnd) {
		GlobalConfig.reportEnd = reportEnd;
	}

	public static void setReportWrong(String reportWrong) {
		GlobalConfig.reportWrong = reportWrong;
	}

	public static void setReportSavePath(String reportSavePath) {
		GlobalConfig.reportSavePath = reportSavePath;
	}

}
