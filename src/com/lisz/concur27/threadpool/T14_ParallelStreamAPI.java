/**
 * 本例跟ThreadPool没有关系，讲的是Stream API，是Java 8
 * 新增加的一种比较方便的编程接口。能往多线程上扯就往上面扯！
 * 扯扯说不定就多挣几千块
 */

package com.lisz.concur27.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class T14_ParallelStreamAPI {
	private static int count = 0;
	
	public static void main(String[] args) {
		List<Integer> nums = new ArrayList<>();
		Random r = new Random();
		for (int i = 0; i < 10000; i++) {
			nums.add(1000000 + r.nextInt(1000000));
		}
		
		long start = System.currentTimeMillis();
		nums.forEach(v->isPrime(v));
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println("count: " + count);
		
		start = System.currentTimeMillis();
		nums.forEach(T14_ParallelStreamAPI::isPrime);
		end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println("count: " + count);
		
		start = System.currentTimeMillis();
		nums.parallelStream().forEach(T14_ParallelStreamAPI::isPrime);
		end = System.currentTimeMillis();
		System.out.println(end - start);
		System.out.println("count: " + count);
	}

	private static Object isPrime(Integer v) {
		for (int i = 2; i * i <= v; i++) {
			if (v % i == 0) {
				count ++;
				return true;
			}
		}
		return false;
	}

	/* 打印：
	   30
	   5 快这么多!马老师讲的不准确，nums.forEach(T14_ParallelStreamAPI::isPrime);最快
	   22
	   运行程序验证第二种写法的正确性：
       34
       count: 9251
       5
       count: 18502
       20
       count: 27279 这里由于没加锁所以count不如预期的大
	 */
}
