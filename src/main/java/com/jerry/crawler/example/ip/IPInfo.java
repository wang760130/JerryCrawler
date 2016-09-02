package com.jerry.crawler.example.ip;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;

import com.jerry.crawler.components.crawler.HTTPProxy;
import com.jerry.crawler.utils.RegexUtil;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月31日
 */
public class IPInfo {

	private String ip ;
	
	private String location ;
	
	//第三方接口地址  
    private static String IP_URL = "http://opendata.baidu.com/api.php?query=%ip%&co=&resource_id=6006&t=%t1%&ie=utf8&oe=gbk&format=json&tn=baidu&_=%t2%"; 
    private static long TIME_DIFFERENCE = 1000L;  
    private static String LOCATION_REGEX = "\"location\":\"(.*?)\"";

	public void crawling() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("Referer", "http://www.baidu.com");  
		    params.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36");  
			
		    long t1 = new Date().getTime();
		    long t2 = t1 + TIME_DIFFERENCE;
		    
		    String url = IP_URL.replaceAll("%ip%", ip).replaceAll("%t1%", t1 + "").replaceAll("%t2%", t2 + "");  
		    
		    HTTPProxy proxy = new HTTPProxy();
	   
			proxy.get(url, params);
			
			String sourceCode = proxy.getSourceCode();
			
			location = RegexUtil.getFirstString(sourceCode, LOCATION_REGEX, 1); 
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}

	public String getLocation() {
		return location;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public static void main(String[] args) {
		IPInfo ip = new IPInfo();
		ip.setIp("122.49.34.58");
		ip.crawling();
		System.out.println(ip.getLocation());
	}
}
