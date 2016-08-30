package com.jerry.crawler.example.chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpUtil {
	public static String sendURL(String spec) throws IOException{		
		URL url = new URL(spec);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
		String str = null;
		StringBuffer result = new StringBuffer();
		while((str = bufferedReader.readLine()) != null){
			result.append(str);
		}
		return result.toString();
	 }
	public static void main(String[] args) {
		try {
			String result = HttpUtil.sendURL("http://www.baidu.com");
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 