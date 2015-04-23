package com.jerry.crawler.chap4;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class Regex {
	@Test
	public void regexTest1() {
		Pattern pattern = Pattern.compile("^Java.*");
		Matcher matcher = pattern.matcher("Java是一门编程语言");
		boolean result = matcher.matches();
		System.out.println(result);
	}
	
	@Test
	public void regexTest2() {
		Pattern pattern = Pattern.compile("[, |]+");
		String[] strs = pattern.split("Java Hello World Java, Hello , World|Sun");
		for(int i = 0; i < strs.length; i++) {
			System.out.println(strs[i]);
		}
	}
	
	public void regexTest3() {
		Pattern pattern = Pattern.compile("regex");
		Matcher matcher = pattern.matcher("regex Hello World, regex Hello World");
		System.out.println(matcher.replaceAll("Java"));
	}
	
	@Test
	public void regexTest4() {
		Pattern pattern = Pattern.compile("regex");
		Matcher matcher = pattern.matcher("regex Hello World, regex Hello World");
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			matcher.appendReplacement(sb, "Java");
		}
		matcher.appendTail(sb);
		System.out.println(sb.toString());
	}

	/**
	 * 验证是否为邮箱地址
	 */
	@Test
	public void regexTest5() {
		String str = "regextest@126.com";
		Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(str);
		System.out.println(matcher.matches());
	}
	
	/**
	 * 去除html标记
	 */
	@Test
	public void regexTest6() {
		Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
		Matcher matcher = pattern.matcher("<a href=\"index.html\">主页</a>");
		String result = matcher.replaceAll("");
		System.out.println(result);
	}
	
	/**
	 * 查找html中对应条件字符串
	 */
	@Test
	public void regexTest7() {
		Pattern pattern = Pattern.compile("href=\"(.+?)\"");
		Matcher matcher = pattern.matcher("<a href=\"index.html\">主页</a>");
		if(matcher.find()) {
			System.out.println(matcher.group(1));
		}
	}
	
	/**
	 * 截取http://地址
	 */
	@Test
	public void regexTest8() {
		Pattern pattern = Pattern.compile("(http://|https://){1}[\\w\\.\\=/:]+");
		Matcher matcher = pattern.matcher("awdawd<http://awd//awdawd>awdawdad");
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
			sb.append(matcher.group());
			sb.append("\r\n");
			System.out.println(sb.toString());
		}
	}
	
	/**
	 * 替换{}中的文字
	 */
	@Test
	public void regexTest9() {
		String str = "Java目前是发展史是由{0}年-{1}年";
		String[][] object = {new String[]{"\\{0\\}","1995"}, new String[] {"\\{1\\}","2007"}};
		str = replace(str, object);
		System.out.println(str);
	}
	
	public static String replace(final String sourceString, Object[] object) {
		String temp = sourceString;
		for(int i = 0; i < object.length; i++) {
			String[] result = (String[]) object[i];
			Pattern pattern = Pattern.compile(result[0]);
			Matcher matcher = pattern.matcher(temp);
			temp = matcher.replaceAll(result[1]);
		}
		return temp;
	}
	
	
}
