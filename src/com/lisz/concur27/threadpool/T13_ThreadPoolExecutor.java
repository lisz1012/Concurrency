/**
 * SingleThreadExecutor, FixedThreadPool, CachedThreadPool, ScheduledThreadPool
 * 底层都是ThreadPoolExecutor，只不过Executors的各个工厂方法传进去的参数不同而已。
 * ThreadPoolExecutor是线程池通用的类，可以通过它自定义返回什么样的线程池，比如调整存活时间，
 * 线程数超过了corePoolSize超过多长时间的话，多余的空闲的线程就会被terminate，其最后一个参数
 * 是个BlockingQueue，真正装任务的容器.
 * CachedThreadPool底层用的是SynchronousQueue，任务队列size为0，来一个任务就必须来（起）
 * 一个线程去执行。
 * 
 * ThreadPoolExecutor其实是策略模式的思想
 */

package com.lisz.concur27.threadpool;

public class T13_ThreadPoolExecutor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
