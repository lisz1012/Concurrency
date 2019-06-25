/**
 * 程序执行中，若出现异常，默认情况锁（synchronized）会被释放
 * 所以在并发处理中，有异常要多加小心，否则回发生不一致的情况。
 * 比如，在web app处理过程中，多个servlet线程共同访问同一个资源，这时如果异常处理不合适，
 * 在第一个线程中抛出异常，其他线程就会进入同步代码区，用可能会访问到异常产生时的数据，
 * 因此要非常小心地处理同步业务逻辑中的异常
 * 不想释放锁就得加try catch抓住异常。运行时异常要进行事务回滚
 */
package com.lisz.concur12.sychronized;

import java.util.concurrent.TimeUnit;

public class Test {
	private int count = 0;
	
	synchronized void m() {
		System.out.println(Thread.currentThread().getName() + " start");
		while (true) {
			count ++;
			System.out.println(Thread.currentThread().getName() + " count " + count);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (count == 5) {
				int i = 1/0; //抛异常
			}
		}
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		new Thread(()->{
			t.m();
		}, "t1").start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(()->{
			t.m();
		}, "t2").start();
	}

}
