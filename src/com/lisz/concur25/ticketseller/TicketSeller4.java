/**
 * 情景：抢票回家过年。10000张票，10个窗口往外卖。
 * 写个模拟程序，会出现哪些问题？
 * 加synchronized锁，不会出问题, 判断和执行都在一个原子性操作里
 */

package com.lisz.concur25.ticketseller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TicketSeller4 {
	private static List<String> tickets = new ArrayList<>();
	
	static {
		for (int i = 0; i < 10000; i++) {
			tickets.add("Ticketnumber：" + i);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() ->{
				while (true) {
					synchronized (tickets) {
						if (tickets.size() <= 0) break;
						try {
							TimeUnit.MICROSECONDS.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("卖出了：" + tickets.remove(0));
					}
				}
			}).start();
		}
	}

}
