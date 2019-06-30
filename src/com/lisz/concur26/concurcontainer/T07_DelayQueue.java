/**
 * 每一个元素自己记录着还有多长时间才可以被消费者拿走，类似于ActiveMQ的延迟消费模式
 * 默认是有先后顺序的，等待时间最长的先拿出来
 * 可以用来做定时执行的任务
 */

package com.lisz.concur26.concurcontainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class T07_DelayQueue {
	private static BlockingQueue<MyTask> queue = new DelayQueue<>();//无界队列.泛型必须是实现了Delayed接口的
	
	private static class MyTask implements Delayed {
		private long runningTime;
		public MyTask(long runningTime) {
			this.runningTime = runningTime;
		}

		@Override
		public int compareTo(Delayed o) {
			if (getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)) {
				return -1;
			} else if (getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)) {
				return 1;
			}
			return 0;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(runningTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		}
		
		@Override
		public String toString() {
			return "" + runningTime;
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		long now = System.currentTimeMillis();
		MyTask t1 = new MyTask(now + 1000);
		MyTask t2 = new MyTask(now + 2000);
		MyTask t3 = new MyTask(now + 1500);
		MyTask t4 = new MyTask(now + 2500);
		MyTask t5 = new MyTask(now + 500);
		
		queue.add(t1);
		queue.add(t2);
		queue.add(t3);
		queue.add(t4);
		queue.add(t5);
		
		System.out.println(queue); //按照放入的顺序
		
		for (int i = 0; i < 5; i++) {
			System.out.println(queue.take()); // 按照设定的Delay时间拿出来
		}
	}
	/*
	 * [1561866027715, 1561866028215, 1561866028715, 1561866029715, 1561866029215]
	1561866027715
	1561866028215
	1561866028715
	1561866029215
	1561866029715
	 */
}
