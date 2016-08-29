package com.jerry.crawler.components.crawler;

/**
 * 正则处理工具
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月29日
 */
public class RegexHelper {
	
	private static final String URL_REGEX = "";
	
	private static final String CHINA_REGEX = "([\u4e00-\u9fa5]+)";
	
	public static String getString(String dealStr, String regexStr, String splitStr, int n) {
		String result = "";
		
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return result;
		}
		
		splitStr = (splitStr == null) ? "" : splitStr;
			
		
		return result;
	}
}
