/**
 * 这种单一线程的线程池有什么用？保证任务是按照前后顺序执行的，先来的任务先执行完
 */
package com.lisz.concur27.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class T09_SingleThreadExecutor {

	public static void main(String[] args) {
		ExecutorService service = Executors.newSingleThreadExecutor(); // 注意名称，独木不成林，单个thread不成pool
		for (int i = 0; i < 5; i++) {
			final int j = i;
			service.execute(()->{
				System.out.println(j + " " + Thread.currentThread().getName());
			});
		}
	}
/*
 * 0 pool-1-thread-1
1 pool-1-thread-1
2 pool-1-thread-1
3 pool-1-thread-1
4 pool-1-thread-1
且打印完成之后程序并不结束
 */
}
