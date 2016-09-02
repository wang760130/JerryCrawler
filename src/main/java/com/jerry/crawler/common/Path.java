package com.jerry.crawler.common;

import java.io.File;


/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月1日
 */
public class Path {
	
	public static final String CURRENT_PATH = System.getProperty("user.dir") + File.separator;
	
	public static final String IP_FILE = CURRENT_PATH + "output" + File.separator + "qqwry" + File.separator + "QQWry.Dat";
	
	public static final String BAIDU_OUTPUT_PATH = CURRENT_PATH + "output" + File.separator + "baidu" + File.separator;
	
	public static final String DB_PATH = CURRENT_PATH + "output" + File.separator + "db" + File.separator;
	
	public static final String EXTRACT_SRC_FILE = CURRENT_PATH + "output" + File.separator + "extract" + File.separator + "extract.vm";
	
	public static final String EXTRACT_DEST_FILE = CURRENT_PATH + "output" + File.separator + "extract" + File.separator + "extract.html";
}
