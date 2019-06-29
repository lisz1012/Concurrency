/**
 * ReentrantLock可以完全完成synchronized同样的功能
 * 需要注意的是必须必须必须要手动释放锁（重要的事情说三遍）
 * synchronized如果遇到异常，JVM会自动释放锁，但是ReentrantLock
 * 必须手动释放，因此经常在finally中释放。
 * synchronized手动上锁，自动释放
 */

package com.lisz.concur21.reentrantlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLock3 {
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
	
	/**
	 * 使用tryLock尝试锁定不管锁定与否，方法都会继续执行
	 * 可以根据tryLock的返回值来判定是否锁定也可以指定tryLock
	 * 的时间由于tryLock抛异常，所以要注意unlock的处理，必须放到finally中
	 */
	void m2() {
		boolean locked = false;
		/*locked = lock.tryLock();
		if (locked) {
			System.out.println("m2");
		}*/
		try {
			locked = lock.tryLock(5, TimeUnit.SECONDS);//等锁等5秒，一般应该根据返回值判断要执行哪个分支
			if (locked) {
				System.out.println("Get lock");
			} else {
				System.out.println("Did not get the lock");
			}
			System.out.println("m2 ... " + locked);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(locked) lock.unlock();//这里得不到锁就不解锁
		}
	}
	
	public static void main(String[] args) {
		ReentrantLock3 r1 = new ReentrantLock3();
		new Thread(r1::m1).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(r1::m2).start();
	}

}
