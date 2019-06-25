/**
 * volatile 关键字，使一个一个变量在多个线程间可见
 * AB线程都用到一个变量，java默认A线程保留一份copy，这样如果B线程修改了该变量，则A线程未必知道
 * volatile关键字，会让所有线程都读到变量的修改值
 * 
 * 下面代码中running存在于内存的t对象中
 * 当线程t1开始运行的时候，会把running从内存中读取到t1线程的工作区(CPU缓存)，在运行中直接使用这个copy，并不会每次都读取堆内存
 * 这样，主线程修改running之后，t1线程感知不到，所以不会停止
 * 
 * 使用volatile关键字将会在修改了之后，强制所有线程去堆内存中读取running的值。其实是向其他线程发送了内存过期通知
 * 
 * volatile并不能保证多个线程共同修改running变量时所带来的不一致问题，也就是说volatile不能代替synchronized
 * 保证可见性，但不保证原子性。Java线程间的通讯就是指多个线程共享同一块内存。能用volatile就不要加锁
 * synchronized 既保证了可见性，又保证了原子性
 * Java 1.8之后Java对 synchronized 做出了优化，所以它也不是那么重了，但肯定比volatile重，volatile实现了无锁同步
 * volatile面试经常问
 */
package com.lisz.concur13.vlatile;

import java.util.concurrent.TimeUnit;

public class Test {
	private /*volatile*/ boolean running = true;
	
	void m() {
		System.out.println("m starts...");
		while (running) {
			/*try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*///sleep就可能让线程去刷内存。执行语句的时候cpu可能空闲出来一下，这时就有可能去主内存刷一下
		} 
		System.out.println("m ends!");
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		new Thread(t::m).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t.running = false;
	}

}
