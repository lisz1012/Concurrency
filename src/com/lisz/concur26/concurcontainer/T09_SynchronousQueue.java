/**
 * SynchronousQueue同步队列，是一种特殊的TransferQueue
 * 容量为0的TransferQueue。add的时候必须有消费者消费。
 * 其实也就表面上是个容器。put阻塞，add报错
 */

package com.lisz.concur26.concurcontainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class T09_SynchronousQueue {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> queue = new SynchronousQueue<>();
		new Thread(()->{
			try {
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		queue.put("aaa");//put不报错，阻塞，等着消费者来消费，内部调用的就是transfer方法
		//queue.add("aaa");//此时add就不行，报错，容量为0 不能往里加，Queue full
		System.out.println(queue.size());
	}

}
