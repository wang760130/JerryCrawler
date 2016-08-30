package com.jerry.crawler.components.crawler;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月30日
 */
public class VisiteQueue {

	/**
	 * 已访问 url 集合
	 */
	private static Set<String> visitedUrl = new HashSet<String>();
	
	/**
	 * 待访问 url 集合
	 */
	private static Queue<String> unVisitedUrl = new PriorityQueue<String>();
	
	/**
	 * 获取 url 队列
	 * @return
	 */
	public static Queue<String> getUnVisitedUrl() {
		return unVisitedUrl;
	}
	
	/**
	 * 添加到访问过的 url 队列中
	 * @param url
	 */
	public static void addVisitedUrl(String url) {
		visitedUrl.add(url);
	}
	
	/**
	 * 移除访问过的 url
	 * @param url
	 */
	public static void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}
	
	/**
	 * 未访问的 url 出队列
	 * @return
	 */
	public static Object unVisitedUrlDeQueue() {
		return unVisitedUrl.poll();
	}
	
	/**
	 * 保证每个 url 只被访问过一次
	 * @param url
	 */
	public static void addUnvisitedUrl(String url) {
		if (url != null && !url.trim().equals("") && !visitedUrl.contains(url)
				&& !unVisitedUrl.contains(url))
			unVisitedUrl.add(url);
	}
	
	/**
	 * 获得已经访问过的 url 数目
	 * @return
	 */
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}
	
	/**
	 * 判断未访问的 url 队列是否为空
	 * @return
	 */
	public static boolean unVisitedUrlsEmpty() {
		return unVisitedUrl.isEmpty();
	}
		
}
