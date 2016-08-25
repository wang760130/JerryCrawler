package com.jerry.crawler.example.chap4;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesAnalyze {
	// 用于缓存文件列表
	private List<File> files = new ArrayList<File>();
	
	// 表示文件路径
	private String path = "";
	
	// 表示未合并的正则表达式
	private String regex = "";
	
	class MyFileFilter implements FileFilter{
		
		/**
		 * 匹配文件名称
		 */
		@Override
		public boolean accept(File file) {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(file.getName());
			return matcher.matches();
		}
	}
	
	FilesAnalyze(String path, String regex) {
		this.getFileName(path, regex);
	}
	
	/**
	 * 分析文件名并加入files
	 */
	private void getFileName(String path, String regex) {
		this.path = path;
		this.regex = regex;
		File directory = new File(path);
		File[] filesFile = directory.listFiles(new MyFileFilter());
		if(filesFile == null)
			return ;
		for(int j = 0; j < filesFile.length; j++) {
			files.add(filesFile[j]);
		}
		return ;
	}
	
	/**
	 * 显示输出信息
	 */
	public void print(PrintStream out) {
		Iterator<File> elements = files.iterator();
		while(elements.hasNext()) {
			File file = (File) elements.next();
			out.println(file.getPath());
		}
	}
	
	public static void output(String path, String regex) {
		FilesAnalyze fileGroup = new FilesAnalyze(path, regex);
		fileGroup.print(System.out);
	}
	
	public static void main(String[] args) {
		output("C:\\","[A-z|.]*");
	}
}
