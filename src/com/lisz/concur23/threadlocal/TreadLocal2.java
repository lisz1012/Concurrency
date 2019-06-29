/**
 * TheadLocal指的是线程局部变量，隔离作用。
 * ThreadLocal里面的变量，当前线程set的，仅仅给自己用
 * 不共享，别的线程想用的话自己set去。相当于每个线程有自己的一个变量，
 * 互相之间没有任何冲突，每个线程内部有一个copy，用空间换时间；
 * 上锁是时间换空间。ThreadLocal时间效率更高。自己维护一个状态不用
 * 通知其他线程的时候可以考虑使用ThreadLocal。Spring和Hibernate中
 * 大量的使用了ThreadLocal，它提高了效率。比如Hibernate的SessionFactory
 * 所产生的Session，就是存在于ThreadLocal中。多线程下，每一个线程有自己的
 * Session这就不需要加锁了，高效。但是，ThreadLocal会导致内存泄漏：
 * 
 * 弱引用：
 * WeakReference<Widget> weakWidget = new WeakReference<Widget>(widget);
 * 使用weakWidget.get()就可以得到真实的Widget对象，因为弱引用不能阻挡垃圾回收器对
 * 其回收，你会发现（当没有任何强引用到widget对象时）使用get时突然返回null。
 * https://droidyue.com/blog/2014/10/12/understanding-weakreference-in-java/
 * 
 * ThreadLocal的实现是这样的：每个Thread 维护一个 ThreadLocalMap 
 * 映射表，这个映射表的 key 是 ThreadLocal实例本身，value 是真正需要
 * 存储的 Object。也就是说 ThreadLocal 本身并不存储值，它只是作为一个 
 * key 来让线程从 ThreadLocalMap 获取 value。值得注意的是图中的虚线，
 * 表示 ThreadLocalMap 是使用 ThreadLocal 的弱引用作为 Key 的，弱引
 * 用的对象在 GC 时会被回收。ThreadLocalMap使用ThreadLocal的弱引用作
 * 为key，如果一个ThreadLocal没有外部强引用来引用它，那么系统 GC 的时候，
 * 这个ThreadLocal势必会被回收，这样一来，ThreadLocalMap中就会出现key
 * 为null的Entry，就没有办法访问这些key为null的Entry的value，如果当前
 * 线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强
 * 引用链：Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value
 * 永远无法回收，造成内存泄漏。
 * https://blog.csdn.net/xlgen157387/article/details/78298840
 * 
 * ThreadLocal实例通常来说都是private static类型。 
 * 总结：ThreadLocal不是为了解决多线程访问共享变量，而是为每个线程创建
 * 一个单独的变量副本，提供了保持对象的方法和避免参数传递的复杂性。
 * 
 * 思考：如何设计一个游戏服务器？
 * TCP能撑得住优先选择TCP连接，实在不行可以用UDP模拟TCP保证消息的重传
 * 棋牌类游戏用的多的还是TCP.网站的高并发处理主要是用缓存和负载均衡来做。
 * 淘宝的双十一秒杀靠消息队列支撑那么大的并发量。
 * 面试谈高并发一定要结合应用场景，关系数据库的高并发处理的几种方式：加索引，
 * 分库，分表，读写分离，主从结构。
 */

package com.lisz.concur23.threadlocal;

import java.util.concurrent.TimeUnit;

public class TreadLocal2 {
	private static ThreadLocal<Person> tl = new ThreadLocal<>();
	
	public static void main(String[] args) {
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(tl.get());//打印null就对了，一秒钟之前设置的那个new Person是仅仅属于另一个线程的，ThreadLocal并不会与当前线程共享
		}).start();
		
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tl.set(new Person());//仅仅属于本线程，别的线程查看tl.get();仍然拿到null值
		}).start();
		
	}
	
	static class Person {
		String name = "zhangsan";
	}
}

