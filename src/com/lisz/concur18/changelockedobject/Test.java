/**
 * 锁是锁在new出来的真正的那个对象上的，而不是栈空间的引用上
 * 下面o所指向的对象变了，结果两个线程都获得了锁，同时执行。
 * 锁的信息记录在堆内存
 */
package com.lisz.concur18.changelockedobject;

import java.util.concurrent.TimeUnit;

public class Test {
	Object o = new Object();
	
	void m() {
		synchronized (o) {
			while (true) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			}
		}
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		new Thread(t::m, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread t2 = new Thread(t::m, "t2");
		t.o = new Object(); //改变锁定对象，线程2得以和线程1一起执行
		t2.start();
	}

}
