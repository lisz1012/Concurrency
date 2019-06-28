/**
 * ReentrantLock可以完全完成synchronized同样的功能
 * 需要注意的是必须必须必须要手动释放锁（重要的事情说三遍）
 * synchronized如果遇到异常，JVM会自动释放锁，但是ReentrantLock
 * 必须手动释放，因此经常在finally中释放。
 * synchronized手动上锁，自动释放
 */

package com.lisz.concur20.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLock2 {
	Lock lock = new ReentrantLock();
	
	void m1() {
		lock.lock();
		try {
			for (int i = 0; i < 10; i++) {
				TimeUnit.SECONDS.sleep(1);
				System.out.println(i);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	synchronized void m2() {
		lock.lock();
		System.out.println("m2 ...");
		lock.unlock();
	}
	
	public static void main(String[] args) {
		ReentrantLock2 r1 = new ReentrantLock2();
		new Thread(r1::m1).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(r1::m2).start();
	}

}
