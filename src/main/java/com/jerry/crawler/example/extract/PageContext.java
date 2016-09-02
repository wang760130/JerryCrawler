package com.jerry.crawler.example.extract;

import org.htmlparser.Node;

/**
 * @author Jerry Wang
 * @Email  jerry002@126.com
 * @date   2016年9月2日
 */
public class PageContext {
	
	private StringBuffer textBuffer;
	
	private int number;
	
	private Node node;

	public StringBuffer getTextBuffer() {
		return textBuffer;
	}

	public void setTextBuffer(StringBuffer textBuffer) {
		this.textBuffer = textBuffer;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
}
