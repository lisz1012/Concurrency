/**
 * Queue在高并发的时候可以使用两种队列：1.ConcurrentQueue 2. BlockingQueue
 * BlockingQueue包括：LinkedBlockingQueue,ArrayBlockingQueue阻塞式的队列
 * 在阻塞式容器里面添加了put：如果满了，就会等待；take如果空了就会等待
 * 有了BlockingQueue就不用自己去实现同步阻塞式容器了。BlockingQueue用的很多
 */

package com.lisz.concur26.concurcontainer;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class T05_LinkedBlockingQueue {
	private static BlockingDeque<String> queue = new LinkedBlockingDeque<>();
	private static Random r = new Random();
	
	public static void main(String[] args) {
		new Thread(()->{
			for (int i = 0; i < 100; i++) {
				try {
					queue.put("a" + r.nextInt(1000));
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "p1").start();
		
		for (int i = 0; i < 5; i++) {
			new Thread(() ->{
				for (;;) {
					try {
						System.out.println(Thread.currentThread().getName() + " take: " + queue.take());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, "p" + i).start();
		}
	}

}
