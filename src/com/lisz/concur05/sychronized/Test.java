/**
 * synchronized 锁定静态方法
 * 其实是锁定了Test.class
 */

package com.lisz.concur05.sychronized;

public class Test implements Runnable {
	private int count = 5;
	
	public /*synchronized*/ void run() { //相当于锁定了 Test.class 类对象
		count --;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		for (int i = 0; i < 5; i++) {
			new Thread(test, "THREAD_" + i).start();
		}
	}
	
	/*
	 *  THREAD_0 count = 4
		THREAD_1 count = 3
		THREAD_4 count = 1
		THREAD_2 count = 2
		THREAD_3 count = 0
	 *  THREAD_2 把count减程2，但还没有打印的时候，THREAD_4把count改为1并立即打印
	 */
}
