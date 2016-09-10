package com.jerry.crawler.components.db;

import java.util.HashSet;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月11日
 */
public class Link {

	public String fromURL;	
    public HashSet<String> toURL = new HashSet<String>();
}
