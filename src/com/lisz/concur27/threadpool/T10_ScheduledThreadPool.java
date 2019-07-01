/**
 * ScheduledThreadPool跟DelayQueue用法差不多.scheduleAtFixedRate方法有4个参数
 * Runnnable，一开始的延迟时间，周期，时间单位TimieUnit
 */

package com.lisz.concur27.threadpool;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class T10_ScheduledThreadPool {

	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
		service.scheduleAtFixedRate(()->{
			try {
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName());
		}, 0, 500, TimeUnit.MILLISECONDS); // 任务马上执行，每隔500毫秒重复执行
	}

}
