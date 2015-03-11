package com.jerry.crawler.lietu.chap1;

import java.util.BitSet;

public class SimpleBloomFilter /*implements VisitedFrontier*/{
	private static final int DEFAULT_SIZE = 2 << 24;
	private static final int[] seeds = new int[] {7,11,13,31,37,61};
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	private SimpleHash[] func = new SimpleHash[seeds.length];
	
 	public SimpleBloomFilter() {
		for(int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
	}
 	
 	public void add(CrawlUrl value) {
 		if(value != null) {
 			add(value.getOriUrl());
 		}
 	}
 	
 	public void add(String value) {
 		for(SimpleHash f : func) {
 			bits.set(f.hash(value), true);
 		}
 	}
 	
 	public boolean contains(CrawlUrl value) {
 		return contains(value.getOriUrl());
 	}
 	
 	public boolean contains(String value) {
 		if(value == null) {
 			return false;
 		}
 		
 		boolean ret = true;
 		for(SimpleHash f : func) {
 			ret = ret && bits.get(f.hash(value));
 		}
 		return ret;
 	}
 	
 	public static void main(String[] args) {
		String value = "123@124.com";
		SimpleBloomFilter filter = new SimpleBloomFilter();
		System.out.println(filter.contains(value));
		filter.add(value);
		System.out.println(filter.contains(value));
	}
}
