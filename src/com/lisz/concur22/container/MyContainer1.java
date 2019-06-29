/**
 * 面试题：写一个固定容量的同步容器，拥有put，get和getCount方法
 * 能够支持2个生产者线程和10个消费者线程的阻塞调用
 * 面的很多，陷阱多
 * 本例中使用wait，notify/notifyAll来实现
 */

package com.lisz.concur22.container;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class MyContainer1 <T>{
	private final LinkedList<T> list = new LinkedList<>();
	private static final int MAX = 10;
	private int count = 0;
	
	public synchronized void put(T t) {
		while (list.size() == MAX) {//while会回头再判断一次，是防止其他线程抢先拿到锁add，造成当前线程拿到锁add之后size超过MAX的情况
			try {
				wait();//wait在99.9%情况下都跟while一起用 -- 《Effective Java》
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		list.add(t);
		++count;
		notifyAll();//notify的话有可能刚刚加满，又叫醒了一个生产者而不是消费者，
					//由于满了它立刻wait，而当前生产者得到锁之后又一看满了，也wait，整个程序执行不下去了
					//永远使用notifyAll，而不要使用notify -- 《Effective Java》
	}
	
	public synchronized T get() {
		while (list.size() == 0) {//while会回头再判断一次，是防止其他线程抢先remove到0个，造成异常
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		T t = list.removeFirst();
		--count;
		notifyAll();
		return t;
	}
	
	public synchronized int getCount() {
		return count;
	}
	
	public static void main(String[] args) {
		MyContainer1<String> c = new MyContainer1<>();
		for (int i = 0; i < 10; i++) {
			new Thread(()->{
				for (int j = 0; j < 5; j++) {
					System.out.println(c.get());
				}
			}, "c" + i).start();
		}
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < 2; i++) {
			new Thread(()->{
				for (int j = 0; j < 25; j++) {
					c.put(Thread.currentThread().getName() + " " + j);
				}
			}, "p" + i).start();
		}
	}

}
