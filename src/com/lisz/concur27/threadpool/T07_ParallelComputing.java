/**
 * 线程池应用的例子：做并行计算，计算1-200000的质数
 */

package com.lisz.concur27.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class T07_ParallelComputing {

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		List<Integer> results = getPrime(1, 200000);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		
		final int cpuCoreNum = Runtime.getRuntime().availableProcessors();//CPU有几个核心，就至少启动几个线程
		ExecutorService service = Executors.newFixedThreadPool(cpuCoreNum);
		
		MyTask t1 = new MyTask(1, 80000); // 为什么不平均分？跟素数计算有关系，越大的数字素数计算需要的时间越长
		MyTask t2 = new MyTask(80001, 130000);
		MyTask t3 = new MyTask(130001, 170000);
		MyTask t4 = new MyTask(170001, 200000);
		
		Future<List<Integer>> f1 = service.submit(t1);
		Future<List<Integer>> f2 = service.submit(t2);
		Future<List<Integer>> f3 = service.submit(t3);
		Future<List<Integer>> f4 = service.submit(t4);
		
		start = System.currentTimeMillis();
		f1.get();
		f2.get();
		f3.get();
		f4.get();
		end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	private static List<Integer> getPrime(int start, int end) {
		List<Integer> list = new ArrayList<>();
		for (int i = start; i <= end; i++) {
			for (int j = 2; j * j <= i; j++) {
				if (i % j == 0) {
					list.add(i);
				}
			}
		}
		return list;
	}

	private static final class MyTask implements Callable<List<Integer>>{
		private int start;
		private int end;
		public MyTask(int start, int end) {
			this.start = start;
			this.end = end;
		}
		@Override
		public List<Integer> call() throws Exception {
			return getPrime(start, end);
		}
		
	}
	/* 打印：
	 * 344
	   145 //最慢的线程用了145毫秒，也比单线程快很多
	 */
}
