/**
 * 情景：抢票回家过年。10000张票，10个窗口往外卖。
 * 写个模拟程序，会出现哪些问题？
 * 卖重了，一张票卖给两个人，超量卖出
 * 
 * 换了个并发容器，问题解决.Queue里面不允许有null
 * 否则会抛出NullPointerException
 * synchronized是独占锁，互斥锁，其实就是一种悲观锁，
 * 悲观锁机制存在以下问题：
   在多线程竞争下，加锁、释放锁会导致比较多的上下文切换和调度延时，引起性能问题。
   一个线程持有锁会导致其它所有需要此锁的线程挂起。
   如果一个优先级高的线程等待一个优先级低的线程释放锁会导致优先级倒置，引起性能风险。
   而ConcurrentLinkedQueue.poll的底层是CAS，是乐观锁，乐观锁更加高效
   
   CAS是项乐观锁技术，当多个线程尝试使用CAS同时更新同一个变量时，只有其中一个线程
   能更新变量的值，而其它线程都失败，失败的线程并不会被挂起，而是被告知这次竞争中失败，
   并可以再次尝试。CAS 操作包含三个操作数 —— 内存位置（V）、预期原值（A）和新值(B)。
   如果内存位置的值与预期原值相匹配，那么处理器会自动将该位置值更新为新值。否则，处理器
   不做任何操作。无论哪种情况，它都会在 CAS 指令之前返回该位置的值。（在 CAS 的一些
   特殊情况下将仅返回 CAS 是否成功，而不提取当前值。）CAS 有效地说明了
   “我认为位置 V 应该包含值 A；如果包含该值，则将 B 放到这个位置；否则，不要更改该位置，
   只告诉我这个位置现在的值即可。”这其实和乐观锁的冲突检查+数据更新的原理是一样的。
   
   这里再强调一下，乐观锁是一种思想。CAS是这种思想的一种实现方式。
   在JDK1.5 中新增java.util.concurrent(J.U.C)就是建立在CAS之上的。相对于对于
   synchronized这种阻塞算法，CAS是非阻塞算法的一种常见实现。所以J.U.C在性能上有了很大的提升。
   我们以java.util.concurrent中的AtomicInteger为例，看一下在不使用锁的情况下是如何保证线
   程安全的。主要理解getAndIncrement方法，该方法的作用相当于 ++i 操作
 */

package com.lisz.concur25.ticketseller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TicketSeller5 {
	private static Queue<String> tickets = new ConcurrentLinkedQueue<>();
	
	static {
		for (int i = 0; i < 10000; i++) {
			tickets.offer("Ticketnumber：" + i);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() ->{
				while (true) {
					String s = tickets.poll();//底层是CAS（Compare and Set）不是加锁的实现
					if (s == null) break;
					System.out.println("卖出了：" + s);
				}
			}).start();
		}
	}

}
