/**
 * 写时复制的List，写的效率非常低但是读的效率非常高
 * 添加新元素的时候，先把内部的容器deep copy复制一遍，加一个新的，
 * 然后再把引用指到新的容器上面 O(N)。读数据的线程再也不用加锁了
 * 特定的情形之下可能用得上，写得很少读的很多的情况，比如事件监听器
 * 队列，读的多，但是基本不加新的监听器
 */

package com.lisz.concur26.concurcontainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public class T02_CopyOnWriteList {
	public static void main(String[] args) {
		List<String> list = new CopyOnWriteArrayList<>();
		runAndComputeTime(list);
		System.out.println("Size: " + list.size());
		
		list = new Vector<>();
		runAndComputeTime(list);
		System.out.println("Size: " + list.size());
		
		list = new ArrayList<>();
		runAndComputeTime(list);
		System.out.println("Size: " + list.size());
	}
	/*
	 *  java.util.concurrent.CopyOnWriteArrayList: 2477 非常慢
		Size: 100000
		java.util.Vector: 27
		Size: 100000
		java.util.ArrayList: 19	 size被其他线程覆盖不支持并发，出问题了
		Size: 95729
	 */

	private static void runAndComputeTime(List<String> list) {
		Thread ths[] = new Thread[100];
		Random r = new Random();
		for (int i = 0; i < ths.length; i++) {
			ths[i] = new Thread(()->{
				for (int j = 0; j < 1000; j++) {
					list.add("a" + r.nextInt(10000));
				}
			});
		}
		
		long s1 = System.currentTimeMillis();
		Arrays.asList(ths).forEach(t->t.start());
		Arrays.asList(ths).forEach(t->{
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		long s2 = System.currentTimeMillis();
		System.out.println(list.getClass().getName() + ": " + (s2 - s1));
	}
}
