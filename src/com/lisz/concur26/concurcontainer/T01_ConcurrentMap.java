/**
 * HashTable进行操作的时候会锁定整个对象，但是Java 1.8之前的ConcurrentHashMap会把容器分为16段，
 * 每次操作只是锁定其中对应的一段，其他的段如果被其他线程操作的话则不会被阻塞。锁被细化了。很多线程共
 * 同插数据的时候，效率高，大锁变小锁。1.8用了CAS替代Segment
 */

package com.lisz.concur26.concurcontainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

public class T01_ConcurrentMap {
	public static void main(String[] args) {
		Map<String, String> map = new Hashtable<>();
		performanceTest(map);
		map = new ConcurrentHashMap<>();
		performanceTest(map);
		map = new ConcurrentSkipListMap<>();
		performanceTest(map);
		map = new HashMap<>();
		performanceTest(map);
		//map = new TreeMap<>();
		//performanceTest(map);
	}

	private static void performanceTest(Map<String, String> map) {
		Random r = new Random();
		Thread ths[] = new Thread[100];
		CountDownLatch latch = new CountDownLatch(ths.length);
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < ths.length; i++) {
			ths[i] = new Thread(()->{
				for (int j = 0; j < 10000; j++) {
					map.put("a" + r.nextInt(10000), "a" + r.nextInt(10000));
				}
				latch.countDown();
			});
		}
		Arrays.asList(ths).forEach(o->o.start());
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println(map.getClass().getName() + ": " + (end - start));
	}
	/*
	 *java.util.Hashtable: 433							//默认所有操作都加锁，低效，用得少。并发不高也可以用Collections.synchronizedMap(map)代替,给加个锁
	  java.util.concurrent.ConcurrentHashMap: 393		//安全且比较高效，并发性比较高的时候用。读的时候应该也有锁(？)
	  java.util.concurrent.ConcurrentSkipListMap: 1241  //高并发并且支持排序，插入的时候效率低一些，查找的时候快一些。并发性比较高的时候用
	  java.util.HashMap: 209
	 */
}
