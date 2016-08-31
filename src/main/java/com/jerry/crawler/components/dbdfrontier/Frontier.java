package com.jerry.crawler.components.dbdfrontier;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月31日
 */
public interface Frontier {

	public Object getNext() throws Exception;
	
	public boolean put(Object object) throws Exception;
	
	public boolean visited(Object object) throws Exception;

}
