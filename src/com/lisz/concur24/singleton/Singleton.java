/**
 * 私有静态内部类的私有静态对象作为单例返回。
 * 不用加锁也能实现懒加载的Singleton方法, 线程安全。
 * 因为只有执行到Inner.s;的时候Inner这个class才会
 * load进内存，才会初始化静态的s变量，new出单例对象
 */


package com.lisz.concur24.singleton;

import java.util.Arrays;

public class Singleton {
	
	private Singleton() {
		System.out.println("single");
	}
	
	private static class Inner {
		private static Singleton s = new Singleton(); 
	}
	
	public static Singleton getInstance() {
		return Inner.s;
	}
	
	public static void main(String[] args) {
		Thread ths[] = new Thread[200];
		for (int i = 0; i < ths.length; i++) {
			ths[i] = new Thread(()->{
				Singleton.getInstance();
			});
		}
		
		Arrays.asList(ths).forEach(o->o.start());
	}

}
