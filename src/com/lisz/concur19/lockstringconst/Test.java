/**
 * 不要以字符串常量作为被锁定对象
 * 由于字符串常量在常量池中其实是同一个对象（享元模式），
 * 所以m1和m2锁定的其实是同一个对象
 * 正中情况会发生比较诡异的现象，比如你用到了一个类库，在该类库中代码锁定了字符串“Hello”，
 * 但是你读不到源码，所以你在你的代码中也锁定了“Hello”，这时候就可能发生诡异的死锁阻塞
 * 因为你的程序和你用的类库不经意使用了同意把锁。Jetty （类似于tomcat的web server）
 * 曾经出现过类似的bug
 */

package com.lisz.concur19.lockstringconst;

public class Test {
	String s1 = "Hello";
	String s2 = "Hello";
	
	void m1 () {
		synchronized (s1) {
			
		}
	}
	
	void m2 () {
		synchronized (s2) {
			
		}
	}
}
