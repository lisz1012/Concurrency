/**
 * 一个同步方法可以调用另外一个同步方法。同一个线程已经拥有某个对象的锁，再次申请的时候仍然会得到该对象的锁。
 * 也就是说synchronized获得的锁是可重入的，实际上是锁定了两次（细节不用管了）。获得了锁之后还可以获得一遍
 */

package com.lisz.concur09.sychronized;

import java.util.concurrent.TimeUnit;

public class Test {
	synchronized void m1() {
		System.out.println("m1 start");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		m2();
	}
	
	synchronized void m2() {
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("m2");
	}
}
