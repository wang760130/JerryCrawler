package com.jerry.crawler.chap1;

import java.util.Set;

public class MyCrawler {
	
	/**
	 * 使用种子初始化 URL 队列
	 * @param seeds
	 */
	private void initCrawlerWithSeeds(String[] seeds) {
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addUnvisitedUrl(seeds[i]);
		}
	}
	
	public void crawling(String[] seeds) {
		// 定义过滤器， 提取已URL开头的连接
		LinkFilter filter = new LinkFilter() {

			@Override
			public boolean accept(String url) {
				if(url.startsWith("http://www.leitu.com")) {
					return true;
				} else {
					return false;
				}
			}
		};
		
		// 初始化 URL 队列
		this.initCrawlerWithSeeds(seeds);
		
		// 循环条件 ： 待抓取的链接布控且抓取的网页不多于1000
		while(!LinkQueue.unVisitedUrlsEmpty() && LinkQueue.getVisitedUrlNum() <= 1000) {
			// 对头 URL 出队列
			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();
			
			if(visitUrl == null) 
				continue;
			
			DownloadFile downloader = new DownloadFile();
			// 下载网页
			downloader.downloadFile(visitUrl);
			// 该 URL 放入已访问的 URL中
			LinkQueue.addVisitedUrl(visitUrl);
			// 提取出下载网页的URL
			Set<String> links = HtmlParserTool.extracLinks(visitUrl, filter);
			// 新的未访问的URL入队
			for(String link : links) {
				LinkQueue.addUnvisitedUrl(link);
				
			}
		}
	}
	
	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler();
		crawler.crawling(new String[] {"http://www.lietu.com"});
	}
}
