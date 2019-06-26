/**
 * 曾经的面试题
 * 实现一个容器，提供两个方法add和size，写两个线程：
 * 线程1添加10个元素到容器中，线程2实现监控元素的个数，
 * 当个数到5时，线程2给出提示并结束
 * 
 * 分析下面这个程序，能完成这个功能吗？
 * 
 * t1结束时t2才退出，因为使用wait和notify，wait会释放锁，notify不会释放锁
 * 需要注意的是，运用这种方法，必须保证t2先执行，也就是让t2先监听上才行
 * t2先执行，锁定，然后再进入wait状态，释放锁等着，当再调用notify的时候，
 * 这个线程再开始执行。notify方法会启动正在这个对象上等待的某一个线程;
 * notufyAll 方法会叫醒在这个对象上等待的所有线程
 * 使用wait和notify必须先锁定对象。
 * 
 */

package com.lisz.concur20.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyContainer3 {
	volatile List<Integer> list = new ArrayList<>();
	
	public void add(Integer i) {
		list.add(i);
	}
	
	public int size() {
		return list.size();
	}
	
	public static void main(String[] args) {
		MyContainer3 container3 = new MyContainer3();
		
		final Object lock = new Object();
		
		new Thread(()->{
			synchronized (lock) {
				System.out.println("t2启动");
				if (container3.size() != 5) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("t2结束");
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
					container3.add(i);
					System.out.println("Add " + i);
					if (container3.size() == 5) {
						lock.notify();//即使notify也不释放锁，sleep也不释放锁
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
