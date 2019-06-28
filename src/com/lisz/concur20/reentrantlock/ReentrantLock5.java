/**
 * ReentrantLock可以完全完成synchronized同样的功能
 * 
 * 公平锁，两个线程相间执行
 */

package com.lisz.concur20.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLock5 extends Thread {
	
	private static ReentrantLock lock = new ReentrantLock(true);//是否是公平锁，看等待时间，先到先得，效率比较低
	
	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {
			lock.lock();
			try {
				System.out.println(Thread.currentThread().getName() + "获得锁");
			} finally {
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args) {
		ReentrantLock5 r = new ReentrantLock5();
		Thread t1 = new Thread(r);
		Thread t2 = new Thread(r);
		t1.start();
		t2.start();
	}

}
