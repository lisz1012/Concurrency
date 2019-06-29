/**
 * 面试题：写一个固定容量的同步容器，拥有put，get和getCount方法
 * 能够支持2个生产者线程和10个消费者线程的阻塞调用
 * 面的很多，陷阱多
 * 用Condition来实现，可以精确地指定哪些线程被叫醒，哪些线程进入等待
 * Condition要跟Lock一起使用，之前要锁定: lock.lock()，用完了要
 * 释放锁lock.unlock();
 * 
 * 总结：lock和await和signal一起使用；synchronized和wait和notify
 * 一起使用
 */

package com.lisz.concur22.container;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyContainer2 <T>{
	private final LinkedList<T> list = new LinkedList<>();
	private static final int MAX = 10;
	private int count = 0;
	
	private Lock lock = new ReentrantLock();
	private Condition producer = lock.newCondition();
	private Condition consumer = lock.newCondition();
	
	
	public void put(T t) {
		try {
			lock.lock(); // Condition要跟Lock一起使用
			while (list.size() == MAX) {
				producer.await(); // 执行到这一句话的线程都给我等着
			}
			list.add(t);
			++count;
			consumer.signalAll(); //所有在consumer.await();那里等着的线程，启动吧！
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public T get() {
		T t = null;
		try {
			lock.lock();
			while (list.size() == 0) {
				consumer.await(); // 执行到这一句话的线程都给我等着
			}
			t = list.removeFirst();
			--count;
			producer.signalAll(); //所有在producer.await();那里等着的线程，启动吧！
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return t;
	}
	
	public synchronized int getCount() {
		return count;
	}
	
	public static void main(String[] args) {
		MyContainer2<String> c = new MyContainer2<>();
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
