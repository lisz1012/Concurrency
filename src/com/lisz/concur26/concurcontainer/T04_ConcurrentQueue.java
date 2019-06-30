/**
 * Queue是并发容器里用得比较多的，要求大致掌握
 * 项目开发中，队列 + 线程池并较常用。异步方案
 * 经常用队列解耦合。相关设计模式：mediator
 */
package com.lisz.concur26.concurcontainer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class T04_ConcurrentQueue {

	public static void main(String[] args) {
		Queue<String> strs = new ConcurrentLinkedQueue<>();
		
		for (int i = 0; i < 10; i++) {
			strs.offer("a" + i); // 返回boolean表示有没有加进去。相当于add，但是add在ArrayQueue有容量限制的时候会报异常
		}
		
		System.out.println(strs);
		System.out.println(strs.size());
		System.out.println(strs.poll()); // poll会从头部拿走元素，返回元素并删除。remove的话拿空了会报异常
		System.out.println(strs.size());
		System.out.println(strs.peek()); // peek只是读一下，不改变队列结构
		System.out.println(strs.size()); 
		// 双端队列叫Deque，ConcurrentLinkedDeque...两边都可以操作，见API
	}

}