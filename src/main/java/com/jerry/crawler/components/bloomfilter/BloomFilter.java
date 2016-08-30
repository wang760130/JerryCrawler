package com.jerry.crawler.components.bloomfilter;

import java.util.BitSet;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年8月30日
 */
public class BloomFilter {

	private static final int DEFAULT_SIZE = 2 << 24;
	private static final int[] seeds = new int[] {7,11,13,31,37,61};
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	private Hash[] hash = new Hash[seeds.length];

	
	public BloomFilter() {
		for(int i = 0; i < seeds.length; i++) {
			hash[i] = new Hash(DEFAULT_SIZE, seeds[i]);
		}
	}
	
	public void add(String value) {
 		for(Hash h : hash) {
 			bits.set(h.hash(value), true);
 		}
 	}
	
	public boolean contains(String value) {
 		if(value == null) {
 			return false;
 		}
 		
 		boolean ret = true;
 		for(Hash h : hash) {
 			ret = ret && bits.get(h.hash(value));
 		}
 		return ret;
 	}
}
