/**
 * 情景：抢票回家过年。10000张票，10个窗口往外卖。
 * 写个模拟程序，会出现哪些问题？
 * 卖重了，一张票卖给两个人，超量卖出
 * 换了个容器，问题依旧，while判断和remove是分离的，
 * 他俩中间的地方如果有其他线程的代码执行就出错了
 */

package com.lisz.concur25.ticketseller;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class TicketSeller1 {
	// Vector本身是个同步容器，所有方法都是加了锁的
	private static Vector<String> tickets = new Vector<>();
	
	static {
		for (int i = 0; i < 10000; i++) {
			tickets.add("Ticketnumber：" + i);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() ->{
				while (tickets.size() > 0) {
					try {
						TimeUnit.MICROSECONDS.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("卖出了：" + tickets.remove(0));
				}
			}).start();
		}
	}

}
