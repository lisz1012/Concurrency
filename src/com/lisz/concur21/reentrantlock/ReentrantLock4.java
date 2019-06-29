/**
 * ReentrantLock可以完全完成synchronized同样的功能
 * ReentrantLock还可以调用lockInterruptibly方法，可以让当前线程对
 * interrupt做出响应，可以被外面其他的线程打断，不至于一直等待锁，然后
 * 会进入catch代码块
 */

package com.lisz.concur21.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLock4 {
	
	public static void main(String[] args) {
		Lock lock = new ReentrantLock();
		
		Thread t1 = new Thread(()-> {
			lock.lock();
			try {
				for (int i = 0; i < 10; i++) {
					System.out.println("t1 start");
					TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
					System.out.println("t1 end");
				} 
			} catch (InterruptedException e) {
				System.out.println("t1 interrupted");
			} finally {
				lock.unlock();
			}
		});
		t1.start();
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Thread t2 = new Thread(()-> {
			try {
				lock.lockInterruptibly();//尝试获取锁，但是可以被外界打断，不至于一直傻等。这个线程被interrupt之后，跳到catch代码块，不执行try里面的语句了
				System.out.println("t2 start");
				TimeUnit.SECONDS.sleep(5);
				System.out.println("t2 end");
			} catch (InterruptedException e) {
				System.out.println("t2 interrupted");
			} finally {
				lock.unlock();//这里得不到锁就不解锁
			}
		});
		t2.start();
		
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t2.interrupt();
	}

}
