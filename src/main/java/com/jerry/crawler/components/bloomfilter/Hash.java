package com.jerry.crawler.components.bloomfilter;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月30日
 */
public class Hash {

	private int cap;
	
	private int seed;
	
	public Hash(int cap, int seed) {
		this.cap = cap;
		this.seed = seed;
	}
	
	public int hash(String value) {
		int result = 0;
		int len = value.length();
		for(int i = 0; i < len; i++) {
			result = seed * result + value.charAt(i);
		}
		return (cap - 1) & result;
	}
}
