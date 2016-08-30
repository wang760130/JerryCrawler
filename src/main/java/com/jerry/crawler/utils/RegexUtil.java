package com.jerry.crawler.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月30日
 */
public class RegexUtil {

	/**
	 * 正则匹配结果，每条记录用splitStr分割
	 * @param dealStr
	 * @param regexStr
	 * @param splitStr
	 * @param n
	 * @return
	 */
	public static String getString(String dealStr, String regexStr, String splitStr, int n) {
		String result = "";
		
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return result;
		}
		
		splitStr = (splitStr == null) ? "" : splitStr;
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			sb.append(matcher.group(n).trim()).append(splitStr);
		}
		result = sb.toString();
		if(!splitStr.equals("") && result.endsWith(splitStr)) {
			result = result.substring(0, result.length() - splitStr.length());
		} 
		
		return result;
	}
	
	/**
	 * 正则匹配结果，将所有匹配记录组装成字符串 
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return
	 */
	public static String getString(String dealStr, String regexStr, int n) {
		return getString(dealStr, regexStr, null, n);
	}
	
	/**
	 * 正则匹配结果，将匹配结果组装成数组 
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return
	 */
	public static List<String> getList(String dealStr, String regexStr, int n) {
		List<String> result = new ArrayList<String>();
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return result;
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL) ;
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()) {
			result.add(matcher.group(n).trim());
		}
		
		return result;
	}
	
	/**
	 * 正则匹配第一条结果 
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return
	 */
	public static String getFirstString(String dealStr, String regexStr, int n) {
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return "";
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()) {
			return matcher.group(n).trim();
		}
		
		return "";
	}
	
	/**
	 * 获取第一个 
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return
	 */
	public static String[] getFirstArray(String dealStr, String regexStr, int[] array) {
		if(dealStr == null || regexStr == null || array == null) {
			return null;
		}
		
		for(int i = 0; i < array.length; i++) {
			if(array[i] < 1) {
				return null;
			}
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()) {
			String[] strArr = new String[array.length];
			for(int i = 0; i < array.length; i++) {
				strArr[i] = matcher.group(array[i]).trim();
			}
			return strArr;
		}
		
		return null;
	}
	
	/**
	 * 获取全部
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return
	 */
	public static List<String> getStringArray(String dealStr, String regexStr, int[] array) {
		List<String> strList = new ArrayList<String>();
		if(dealStr == null || regexStr == null || array == null) {
			return strList;
		}
		
		for(int i = 0; i < array.length; i++) {
			if(array[i] < 1) {
				return strList;
			}
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()) {
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < array.length; i++) {
				sb.append(matcher.group(array[i]).trim());
			}
			strList.add(sb.toString());
		}
		
		return strList;
	}
	
	/**
	 * 获取全部 
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return
	 */
	public static List<String[]> getListArray(String dealStr, String regexStr, int[] array) {
		List<String[]> arrayList = new ArrayList<String[]> ();
		if(dealStr == null || regexStr == null || array == null) {
			return arrayList;
		}
		
		for(int i = 0; i < array.length; i++) {
			if(array[i] < 1) {
				return arrayList;
			}
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()) {
			String[] strArray = new String[array.length];
			for(int i = 0; i < array.length; i++) {
				strArray[i] = matcher.group(array[i]).trim();
			}
			arrayList.add(strArray);
		}
		
		return arrayList;
	}

}
