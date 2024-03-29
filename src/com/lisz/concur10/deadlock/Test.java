package com.lisz.concur10.deadlock;

import java.util.concurrent.TimeUnit;

public class Test {
	private Object a = new Object();
	private Object b = new Object();
	
	public void m1() {
		synchronized (a) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			m2();
			System.out.println("a complete");
		}
		
	}
	
	public void m2() {
		synchronized (b) {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			m1();
			System.out.println("b complete");
		}
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		new Thread(()->{
			t.m1();
		}).start();
		new Thread(()->{
			t.m2();
		}).start();
	}
	// 无任何输出，两个线程都在等待对方释放锁
}
