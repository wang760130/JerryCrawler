package com.jerry.crawler.example.joke;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpException;

import com.jerry.crawler.components.crawler.HTTPProxy;
import com.jerry.crawler.components.crawler.RegexHelper;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月7日
 */
public class Crawler {

	// 笑话集更新列表页url格式
	private static String LIST_PAGE_URL = "http://www.jokeji.cn/list_%page%.htm";
	
//	private static String DETAIL_URL_REGEX = "<li><b><a href=\"(.*?)\" target=\"_blank\">(.*?)</a>";
	
	private static String DETAIL_URL_REGEX = "<a href=\"(.*?)\" target=\"_blank\">(.*?)</a>";
	
	private static void crawlering() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Host", "www.jokeji.cn");
		params.put("Pragma", "no-cache");
		params.put("User-Agent", "Mozilla/5.0 (Windows NT 5.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36");
		params.put("Referer", "http://www.jokeji.cn/list.htm");
		
		int start = 1;
		int end = 1;
		
		for(int i = start; i <= end; i++) {
			try {
				String listUrl = LIST_PAGE_URL.replace("%page%", start + "");
				HTTPProxy proxy = new HTTPProxy();
				proxy.get(listUrl, params);
				String sourceCode = proxy.getSourceCode();
				
//				List<String> detailUrlList = RegexHelper.getArrayList(sourceCode, DETAIL_URL_REGEX, listUrl, 1);
				
				System.out.println(sourceCode);
				Pattern pattern = Pattern.compile(DETAIL_URL_REGEX, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
				Matcher matcher = pattern.matcher(sourceCode);
				while(matcher.find()) {
					System.out.println("true");
//					System.out.println(matcher.group(1).trim());
					System.out.println(matcher.group(2).trim());
				}
				
				
			} catch (HttpException e) {
				e.printStackTrace();	
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	public static void main(String[] args) {
		Crawler.crawlering();
	}
}
