package com.jerry.crawler.components.crawler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jerry.crawler.utils.RegexUtil;

/**
 * 正则处理工具
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月29日
 */
public class RegexHelper {
	
//	private static final String URL_REGEX = "(http://.*/)";
	
	private static String ROOT_URL_REGEX = "(http://.*?/)";
	
	private static String CURRENT_URL_REGEX = "(http://.*/)";
	
	// 中文正则
	private static final String CHINA_REGEX = "([\u4e00-\u9fa5]+)";
	
	// 电子邮件正则
	private static final String EMAIL_REGEX = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";
	
	// 去除html标记
//	private static final String HTML_REGEX = "<.+?>";
	
	/**
	 * 获取和正则匹配的绝对链接地址 
	 * @param dealStr
	 * @param regexStr
	 * @param currentUrl
	 * @param n
	 * @return
	 */
	public static List<String> getArrayList(String dealStr, String regexStr, String currentUrl, int n) {
		List<String> arrayList = new ArrayList<String>();
		if(dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return arrayList;
		}
		
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while(matcher.find()) {
			arrayList.add(getHttpUrl(matcher.group(n).trim(), currentUrl));
		}
		
		return arrayList;
	}
	
	
	/**
	 * 组装网址，网页的url
	 * @param url
	 * @param currentUrl
	 * @return
	 */
	public static String getHttpUrl(String url, String currentUrl) {
		try {
			url = encodeUrlChina(url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		if (url.indexOf("http") == 0){
			return url;
		}
		
		if  (url.indexOf("/") == 0){
			return RegexUtil.getFirstString(currentUrl, ROOT_URL_REGEX, 1) + url.substring(1);
		}
		return RegexUtil.getFirstString(currentUrl, CURRENT_URL_REGEX, 1) + url;
	}
	
	
	/**
	 * 将连接地址中的中文进行编码处理 
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeUrlChina(String url) throws UnsupportedEncodingException {
		while(true) {
			String str = RegexUtil.getFirstString(url, CHINA_REGEX, 1);
			if("".equals(str)) {
				return url;
			}
			url = url.replaceAll(str, URLEncoder.encode(str,"utf-8"));
		}
	}
	
	/**
	 * 判断是否为电子邮箱
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	/**
	 * 去除html标签
	 * @param html
	 * @return
	 */
	public static String removeHtml(String html) {
		Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(html);
		return matcher.replaceAll("");
	}
	
	/**
	 * 查找html中对应条件字符串
	 * @param html
	 * @return
	 */
	public static String findHtml(String html) {
		Pattern pattern = Pattern.compile("href=\"(.+?)\"");
		Matcher matcher = pattern.matcher(html);
		if(matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}
	
	
}
