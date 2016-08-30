package com.jerry.crawler.example.chap1;

import java.util.Set;

import com.jerry.crawler.components.crawler.DownloadFile;
import com.jerry.crawler.components.crawler.HtmlParserHepler;
import com.jerry.crawler.components.crawler.LinkFilter;
import com.jerry.crawler.components.crawler.VisiteQueue;

public class MyCrawler {
	
	/**
	 * 使用种子初始化 URL 队列
	 * @param seeds
	 */
	private void initCrawlerWithSeeds(String[] seeds) {
		for (int i = 0; i < seeds.length; i++) {
			VisiteQueue.addUnvisitedUrl(seeds[i]);
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
		while(!VisiteQueue.unVisitedUrlsEmpty() && VisiteQueue.getVisitedUrlNum() <= 1000) {
			// 对头 URL 出队列
			String visitUrl = (String) VisiteQueue.unVisitedUrlDeQueue();
			
			if(visitUrl == null) 
				continue;
			
			DownloadFile downloader = new DownloadFile();
			// 下载网页
			downloader.downloadFile(visitUrl);
			// 该 URL 放入已访问的 URL中
			VisiteQueue.addVisitedUrl(visitUrl);
			// 提取出下载网页的URL
			Set<String> links = HtmlParserHepler.extractLinks(visitUrl, filter);
			// 新的未访问的URL入队
			for(String link : links) {
				VisiteQueue.addUnvisitedUrl(link);
				
			}
		}
	}
	
	public static void main(String[] args) {
		MyCrawler crawler = new MyCrawler();
		crawler.crawling(new String[] {"http://www.lietu.com"});
	}
}
