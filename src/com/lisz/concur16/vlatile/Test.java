/**
 * AtomicXXX原子类，内部不是用synchronized实现的，而是用系统相当底层的原理实现的，
 * 所以它比synchronized效率要高好多，但是某网上课程的吕老师说不建议用，效率太低。
 * 
 * AtomicXXX原子类底层用到了CAS compare-and-swap
 * 比较并设置，这里利用Unsafe类的JNI方法实现，使用CAS指令，可以保证读-改-写是一个原子操作。
 * 简单的来说，CAS有3个操作数，内存值V，旧的预期值A，要修改的新值B。当且仅当预期值A和内存值V相同时，
 * 将内存值V修改为B，否则返回V。这是一种乐观锁的思路，它相信在它修改之前，没有其它线程去修改它；
 * 而Synchronized是一种悲观锁，它认为在它修改之前，一定会有其它线程去修改它，悲观锁效率很低。
 * 抢购的时候甚至可以用AtomicInteger/AtomicLong
 * 
 * AtomicInteger的两个incrementAndGet之间如果不加锁的话，还是会被别的线程打断
 * 
 * https://blog.csdn.net/qfycc92/article/details/46489553
 */
package com.lisz.concur16.vlatile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
	AtomicInteger count = new AtomicInteger(0);
	
	void m() {
		for (int i = 0; i < 10000; i++) {
			// if(count.get() < 1000) //有这一句的话，并不会与下一句构成原子性
			count.incrementAndGet(); // 替代++count
		}
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			threads.add(new Thread(t::m, "thread-" + i));
		}
		threads.forEach(o->o.start());
		threads.forEach(o->{
			try {
				o.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		System.out.println(t.count);// 输出100000
	}

}
