/**
 * 曾经的面试题
 * 实现一个容器，提供两个方法add和size，写两个线程：
 * 线程1添加10个元素到容器中，线程2实现监控元素的个数，
 * 当个数到5时，线程2给出提示并结束
 * 
 * 分析下面这个程序，能完成这个功能吗？
 * 
 * 加了volatile功能上是正确了，但是仍然有两个问题：
 * 1. 没有同步，t2中size==5的时候可能又有别的线程已经又网list中加了一个，size已经等于6，不精确
 * 2. t2线程的死循环很浪费CPU
 */

package com.lisz.concur20.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyContainer2 {
	// 加volatile使得t2可以得到通知即可
	volatile List<Integer> list = new ArrayList<>();
	
	public void add(Integer i) {
		list.add(i);
	}
	
	public int size() {
		return list.size();
	}
	
	public static void main(String[] args) {
		MyContainer2 container2 = new MyContainer2();
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				container2.add(i);
				System.out.println("Add " + i);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t1").start();
		
		new Thread(()->{
			while (true) {
				//System.out.println(container1.size()); 有这一句就能打印出来"t2结束"
				if (container2.size() == 5) {
					break;
				}
			}
			System.out.println("t2结束");
		}, "t2").start();
	}

}
