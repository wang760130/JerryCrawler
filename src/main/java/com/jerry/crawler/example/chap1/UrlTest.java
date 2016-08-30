package com.jerry.crawler.example.chap1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlTest {
	private static String path = "http://www.baidu.com";
	public static void main(String[] args) {
		URL pageURL;
		try {
			pageURL = new URL(path);
			InputStream is = pageURL.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String line = null;
			while((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
