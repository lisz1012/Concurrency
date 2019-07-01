/**
 * 任务窃取线程池，每个线程自己有一个任务队列，自己执行玩自己队列里的任务之后，就会从别人的
 * 队列里面偷任务来执行。本质上是用ForkJoinPool来实现的。
 * 应用场景：避免任务队列分配的长短不均匀而导致低效
 * WorkStealingPool是1.8之后加的
 */

package com.lisz.concur27.threadpool;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T11_WorkStealingPool {

	public static void main(String[] args) throws IOException {
		ExecutorService service = Executors.newWorkStealingPool(); // 生产的过程就参考了CPU内核个数
		System.out.println(Runtime.getRuntime().availableProcessors()); // 4个核
		
		service.execute(new R(1000));
		service.execute(new R(2000));
		service.execute(new R(2000));
		service.execute(new R(2000));
		service.execute(new R(2000)); //第五个任务等着把，因为只有4个核，然后第一个任务的线程(CPU核)先执行完，这个任务最后会交给它
		
		// 由于产生的事精灵线程（守护线程，后台线程，jvm不退出线程就不结束），主线程不阻塞的话就看不到输出
		System.in.read();
	}

	private static final class R implements Runnable {

		private int time;
		
		public R(int time) {
			this.time = time;
		}

		@Override
		public void run() {
			try {
				TimeUnit.MILLISECONDS.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(time + " " + Thread.currentThread().getName());
		}
		
	}
	/*
	 * 4
1000 ForkJoinPool-1-worker-1
2000 ForkJoinPool-1-worker-2
2000 ForkJoinPool-1-worker-3
2000 ForkJoinPool-1-worker-4
2000 ForkJoinPool-1-worker-1 //第一个线程一秒钟执行完了之后，抢了第五个线程的任务！
	 */
}
