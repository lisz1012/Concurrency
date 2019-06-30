/**
 * 情景：抢票回家过年。10000张票，10个窗口往外卖。
 * 写个模拟程序，会出现哪些问题？
 * 不用任何锁或者同步容器，错误！
 */

package com.lisz.concur25.ticketseller;

import java.util.ArrayList;
import java.util.List;

public class TicketSeller1 {
	private static List<String> tickets = new ArrayList<>();
	
	static {
		for (int i = 0; i < 10000; i++) {
			tickets.add("Ticketnumber：" + i);
		}
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			new Thread(() ->{
				while (tickets.size() > 0) {
					System.out.println("卖出了：" + tickets.remove(0));
				}
			}).start();
		}
	}

}
