/**
 * TheadLocal指的是线程局部变量。
 */

package com.lisz.concur23.threadlocal;

import java.util.concurrent.TimeUnit;

public class TreadLocal1 {
	volatile static Person p = new Person(); //lisi是正常结果，不写volatile有可能会发生问题，写上没问题。
											 //正确的就该写volatile。但是如果本线程内的改变就是不想让其他线程知道
											 //则使用ThreadLocal，见TreadLocal2
	
	public static void main(String[] args) {
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(p.name);
		}).start();
		
		new Thread(() -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			p.name = "lisi";
		}).start();
		
	}

}

class Person {
	String name = "zhangsan";
}