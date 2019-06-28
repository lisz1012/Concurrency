/**
 * 曾经的面试题
 * 实现一个容器，提供两个方法add和size，写两个线程：
 * 线程1添加10个元素到容器中，线程2实现监控元素的个数，
 * 当个数到5时，线程2给出提示并结束
 * 
 * 分析下面这个程序，能完成这个功能吗？
 * 
 * 使用Latch（门闩）替代wait和notify进行通知
 * 好处是通信方式简单，同时可以指定等待时间
 * 使用await和countdown方法替代wait和notify
 * CountDownLatch不涉及锁定，当count值为0的时候
 * 当前线程继续运行。当不涉及同步，只涉及线程通信的时候，
 * 用synchronized + wait/notify就显得太重了，
 * 这是应该考虑CountDownLatch/semaphore/cyclicbarrier
 * latch的等待不需要锁定任何对象，两个线程都继续执行
 * 
 * 下面这个版本对MyContainer5优化了一下
 * 
 */

package com.lisz.concur20.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MyContainer6 {
	volatile List<Integer> list = new ArrayList<>();//volatile只缓存数据，不缓存整个对象
	
	public void add(Integer i) {
		list.add(i);
	}
	
	public int size() {
		return list.size();
	}
	
	public static void main(String[] args) {
		MyContainer6 container5 = new MyContainer6();
		
		CountDownLatch latch = new CountDownLatch(5);
		
		new Thread(()->{
			System.out.println("t2启动");
			try {
				latch.await();//也可以指定时间latch.await(5000， TimeUnit.MILLSECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("t2结束");
		}, "t2").start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				container5.add(i);
				System.out.println("Add " + i);
				latch.countDown();
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t1").start();
		
		
	}

}
