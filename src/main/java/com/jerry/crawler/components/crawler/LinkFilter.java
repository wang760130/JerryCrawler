package com.jerry.crawler.components.crawler;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月30日
 */
public interface LinkFilter {
	
	public boolean accept(String url);
	
}

