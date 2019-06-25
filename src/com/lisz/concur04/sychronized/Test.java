/**
 * synchronized 锁定静态方法
 * 其实是锁定了Test.class
 */

package com.lisz.concur04.sychronized;

public class Test {
	private static int count = 10;
	
	public synchronized static void m() { //相当于锁定了 Test.class 类对象
		count --;
		System.out.println(Thread.currentThread().getName() + " count = " + count);
	}
	
	public static void mm() {
		synchronized (Test.class) { // 换成this编译报错，静态，没有this可以上锁
			count --;
		}
	}
}
