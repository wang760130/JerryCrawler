package com.jerry.crawler.example.chap1;

import java.util.Map.Entry;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;

public class BDBFrontier extends AbstractFrontier implements Frontier{

	private StoredMap pendingUrisDB = null;
	
	public BDBFrontier(String homeDirectory) {
		super(homeDirectory);
		EntryBinding keyBinding = new SerialBinding(storedClassCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(storedClassCatalog,CrawlUrl.class);
		pendingUrisDB = new StoredMap(database,keyBinding,valueBinding,true);
	}
	
	@Override
	public CrawlUrl getNext() throws Exception {
		CrawlUrl result = null;
		if(!pendingUrisDB.isEmpty()) {
//			Set  entrys = pendingUrisDB.entrySet();
			Entry<String,CrawlUrl> entry = (Entry<String, CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
			result = entry.getValue();
			delete(entry.getKey());
		}
		return result;
	}

	@Override
	public boolean putUrl(CrawlUrl url) throws Exception {
		put(url.getOriUrl(),url);
		return true;
	}

	@Override
	public boolean visited(CrawlUrl url) throws Exception {
		return false;
	}

	@Override
	protected void put(Object key, Object value) {
		pendingUrisDB.put(key, value);
	}

	@Override
	protected Object get(Object key) {
		return pendingUrisDB.get(key);
	}

	@Override
	protected Object delete(Object key) {
		return pendingUrisDB.remove(key);
	}
	
	// 根据URL计算键值，可以使用各种压缩算法，包括ＭＤ５等压缩算法
	private String caculateUrl(String url) {
		return url;
	}
	
	public static void main(String[] args) {
		BDBFrontier dbBFrontier = null;
		try {
			dbBFrontier = new BDBFrontier("F://12");
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			dbBFrontier.putUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbBFrontier.close();
		}
	}
}
