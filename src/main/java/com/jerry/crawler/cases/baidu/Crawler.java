package com.jerry.crawler.cases.baidu;

import java.io.File;
import java.util.Set;

import com.jerry.crawler.components.crawler.DownloadFile;
import com.jerry.crawler.components.crawler.HtmlParserHepler;
import com.jerry.crawler.components.crawler.LinkFilter;
import com.jerry.crawler.components.crawler.VisiteQueue;
import com.jerry.crawler.utils.PathUtil;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月31日
 */
public class Crawler {
	
	/**
	 * 使用种子初始化 URL 队列
	 * @param seeds
	 */
	private void initCrawlerWithSeeds(String[] seeds) {
		for(int i = 0; i < seeds.length; i++) {
			VisiteQueue.addUnvisitedUrl(seeds[i]);
		}
	}
	
	/**
	 * 抓取过程
	 * @param seeds
	 */
	public void crawling(String[] seeds) {
		// 定义过滤器，提取以http://www.baidu.com开头的链接
		LinkFilter filter = new LinkFilter() {

			@Override
			public boolean accept(String url) {
				if(url.startsWith("http://www.baidu.com")) {
					return true;
				} else {
					return false;
				}
			}
			
		};
		
		// 初始化 URL 队列
		initCrawlerWithSeeds(seeds);
		
		// 循环条件：待抓取的链接不空且抓取的网页不多于1000
		while(!VisiteQueue.unVisitedUrlsEmpty()
//				&& VisiteQueue.getVisitedUrlNum() <= 1000
				) {
			String visitUrl = (String) VisiteQueue.unVisitedUrlDeQueue();
			if(visitUrl == null) {
				continue;
			}

			File downloadPath = new File(System.getProperty("user.dir") + File.separator + "baidu" + File.separator);
			if(!downloadPath.exists()) {
				downloadPath.mkdirs();
			}
			DownloadFile downloader = new DownloadFile();
			downloader.downloadFile(visitUrl, downloadPath.getAbsolutePath());
			
			// 该 url 放入到已访问的 URL 中
			VisiteQueue.addVisitedUrl(visitUrl);
			// 提取出下载网页中的 URL
			Set<String> links = HtmlParserHepler.extractLinks(visitUrl, filter);
			for(String link : links) {
				VisiteQueue.addUnvisitedUrl(link);
				System.out.println(link);
			}
		}
	}
	
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		crawler.crawling(new String[] { "http://www.baidu.com" });
	}
}
