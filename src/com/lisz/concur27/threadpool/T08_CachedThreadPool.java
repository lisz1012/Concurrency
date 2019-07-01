/**
 * CachedThreadPool刚开始没有线程，来一个任务就启动一个线程，有空闲的就拿空闲的来执行
 * 没有则新启动一个线程，启动到系统不能支撑为止。默认里面的线程空闲超过60秒之后，就自动
 * 消失。这个60s的时间可以自己指定。最大还是有界限的，至少不能超过int类型的最大值，一般
 * 系统可以支持几万个线程。CachedThreadPool底层用的是SynchronousQueue，任务队列size
 * 为0，来一个任务就必须来（起）一个线程（消费者）去执行
 * 
 * PS：当一个技术网上资料比较少的时候说明用的不多，生产环境下做技术选型的时候要谨慎使用。
 * 简单说一下RPC：远程过程调用，从本地调用另外一台机器上的某一个对象里的方法，类似于在
 * 本地调用一样，中间经历了一系列的封装。做一个简单的爬虫，最好用python
 */

package com.lisz.concur27.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T08_CachedThreadPool {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService service = Executors.newCachedThreadPool();
		System.out.println(service);
		
		for (int i = 0; i < 2; i++) {
			service.execute(()->{
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			});
		}
		
		System.out.println(service);
		
		TimeUnit.SECONDS.sleep(2);
		
		System.out.println(service);
		
		TimeUnit.SECONDS.sleep(80);
		
		System.out.println(service); 
		//执行完了之后service也结束，不像fixedThreadPool那样一直还在运行等新的任务
		for (int i = 0; i < 2; i++) {
			service.execute(()->{
				try {
					TimeUnit.MILLISECONDS.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			});
		}
		// 上面这段还可以执行，不会reject
	}
	/*
	 * java.util.concurrent.ThreadPoolExecutor@555590[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
java.util.concurrent.ThreadPoolExecutor@555590[Running, pool size = 2, active threads = 2, queued tasks = 0, completed tasks = 0]
pool-1-thread-1
pool-1-thread-2
java.util.concurrent.ThreadPoolExecutor@555590[Running, pool size = 2, active threads = 0, queued tasks = 0, completed tasks = 2]
java.util.concurrent.ThreadPoolExecutor@555590[Running, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 2] //超过60秒，全部销毁
	 */
}
