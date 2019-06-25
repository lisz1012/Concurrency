/**
 * synchronized 锁定当前对象
 * 锁定的是一个对象，而不是代码块
 */
package com.lisz.concur02.sychronized;

public class Test {
	private int count = 10;
	
	public void m() {//任何线程想执行下面的代码，必须先得到this的锁
		synchronized (this) {
			count --;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
		}
	}
}
