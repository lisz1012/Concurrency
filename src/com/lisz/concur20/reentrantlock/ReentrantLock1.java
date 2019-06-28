/**
 * reentrantlock用于替代synchronized
 * 本例子中，由于m1锁定this，只有m1执行完的时候
 * m2才执行这里是复习synchronized最原始的意义
 */

package com.lisz.concur20.reentrantlock;

import java.util.concurrent.TimeUnit;

public class ReentrantLock1 {
	synchronized void m1() {
		for (int i = 0; i < 10; i++) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(i);
		}
	}
	
	synchronized void m2() {
		System.out.println("m2 ...");
	}
	
	public static void main(String[] args) {
		ReentrantLock1 r1 = new ReentrantLock1();
		new Thread(r1::m1).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(r1::m2).start();
	}

}
