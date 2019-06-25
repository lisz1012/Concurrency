/**
 * synchronized 锁定静态方法
 * 其实是锁定了Test.class
 * synchronized代码块是一个原子操作，不可分
 * 
 */

package com.lisz.concur06.sychronized;

public class Test implements Runnable {
	private int count = 5;
	
	public /*synchronized*/ void run() { //相当于锁定了 Test.class 类对象
		count --;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		for (int i = 0; i < 5; i++) {
			new Thread(test, "THREAD_" + i).start();
		}
	}
	
	/*
	 *  THREAD_0 count = 0
		THREAD_2 count = 0
		THREAD_3 count = 0
		THREAD_1 count = 0
		THREAD_4 count = 0
	 *  sleep的时候，其他线程来了，减为0之后才打印
	 */
}
