/**
 * Queue是并发容器里用得比较多的，要求大致掌握
 */
package com.lisz.concur26.concurcontainer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class T04_ConcurrentQueue {

	public static void main(String[] args) {
		Queue<String> strs = new ConcurrentLinkedQueue<>();
		
		for (int i = 0; i < 10; i++) {
			strs.offer("a" + i);
		}
		
		System.out.println(strs);
		System.out.println(strs.size());
		System.out.println(strs.poll()); // poll会从头部拿走元素
		System.out.println(strs.size());
		System.out.println(strs.peek()); // peek只是读一下
		System.out.println(strs.size()); 
	}

}