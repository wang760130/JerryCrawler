package com.jerry.crawler.chap4;

public class ExtractLinkConsole {
	public static ExtractLinkConsole context = null;
	private SplitManager splitManager = null;
	public SplitManager getBean(String bean) {
		return splitManager;
	}
}
