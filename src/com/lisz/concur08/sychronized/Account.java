/**
 * 对业务写方法加锁
 * 对业务读方法不加锁
 * 容易产生脏读问题（dirty read）
 * 读到了正在被改写的数据
 * 解决脏读问题可以用CopyOnWrite，牺牲写性能，
 * 增加读性能
 */
package com.lisz.concur08.sychronized;

import java.util.concurrent.TimeUnit;

public class Account {
	String name;
	double balance;
	
	public synchronized void set(String name, double balance) {
		this.name = name;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.balance = balance;
	}
	
	// 解决方法：
	public /*synchronized*/ double getBalance(String name) {
		return balance;
	}
	
	public static void main(String[] args) {
		Account account = new Account();
		new Thread(()->account.set("zhangsan", 100.00)).start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(account.getBalance("zhangsan"));
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(account.getBalance("zhangsan"));
	}

}
