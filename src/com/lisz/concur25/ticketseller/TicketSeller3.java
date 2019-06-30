/**
 * 情景：抢票回家过年。10000张票，10个窗口往外卖。
 * 写个模拟程序，会出现哪些问题？
 * 卖重了，一张票卖给两个人，超量卖出
 * 加锁同步，可以解决问题, 判断和执行都在一个原子性操作里
 */

package com.lisz.concur25.ticketseller;

import java.util.ArrayList;
import java.util.List;

public class TicketSeller3 {
	private static List<String> tickets = new ArrayList<>();
	
	static {
		for (int i = 0; i < 10000; i++) {
			tickets.add("Ticketnumber：" + i);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() ->{
				synchronized (TicketSeller3.class) {
					while (tickets.size() > 0) {
						System.out.println("卖出了：" + tickets.remove(0));
					}
				}
			}).start();
		}
	}

}
