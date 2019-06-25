/**
 * synchronized
 * 对某个对象加锁，真正申请锁的时候
 * 锁实在堆内存中的那个new出来的对象上
 * 互斥锁，只有一个线程可以拿到这把锁
 * 分布式锁有五六种算法。。。
 */

package com.lisz.concur01.sychronized;

public class Test {
	private int count;
	private Object o = new Object();
	
	public void m() {
		synchronized(o) { //任何线程想执行下面的代码，必须先得到o的锁
			count --;
			System.out.println(Thread.currentThread().getName() + " count = " + count);
		}
	}
}
