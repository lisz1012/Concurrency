/**
 * 曾经的面试题
 * 实现一个容器，提供两个方法add和size，写两个线程：
 * 线程1添加10个元素到容器中，线程2实现监控元素的个数，
 * 当个数到5时，线程2给出提示并结束
 * 
 * 分析下面这个程序，能完成这个功能吗？
 * 
 * 对照MyContainer3
 * t1 size到了5之后notify，notify之后自己先wait释放锁，让t2
 * 执行，t2执行到末了，再notify，让t1再继续执行
 * 
 */

package com.lisz.concur20.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyContainer4 {
	volatile List<Integer> list = new ArrayList<>();
	
	public void add(Integer i) {
		list.add(i);
	}
	
	public int size() {
		return list.size();
	}
	
	public static void main(String[] args) {
		MyContainer4 container4 = new MyContainer4();
		
		final Object lock = new Object();
		
		new Thread(()->{
			synchronized (lock) {
				System.out.println("t2启动");
				if (container4.size() != 5) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("t2结束");
				lock.notify();
			}
		}, "t2").start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		new Thread(()->{
			synchronized (lock) {
				for (int i = 0; i < 10; i++) {
					container4.add(i);
					System.out.println("Add " + i);
					if (container4.size() == 5) {
						lock.notify();//即使notify也不释放锁，sleep也不释放锁
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, "t1").start();
		
		
	}

}
