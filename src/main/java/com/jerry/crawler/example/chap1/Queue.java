package com.jerry.crawler.example.chap1;

import java.util.LinkedList;

public class Queue {
	// 使用链表实现队列
	private LinkedList<Object> queue = new LinkedList<Object>();
	public void enQueue(Object t) {
		queue.addLast(t);
	}
	
	// 入队列
	public Object deQueue() {
		return queue.removeFirst();
	}
	
	// 出队列
	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}
	
	// 判断队列是否包含t
	public boolean contians(Object t) {
		return queue.contains(t);
	}
	
	// 判断队列是否为空
	public boolean empty() {
		return queue.isEmpty();
	}

}
