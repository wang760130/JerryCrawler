package com.jerry.crawler.example.chap1;

public interface Frontier {
	
	public CrawlUrl getNext() throws Exception;
	
	public boolean putUrl(CrawlUrl url) throws Exception;
	
	public boolean visited(CrawlUrl url) throws Exception;
	
}
