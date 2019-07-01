/**
 * Queue在高并发的时候可以使用两种队列：1.ConcurrentQueue 2. BlockingQueue
 * BlockingQueue包括：LinkedBlockingQueue,ArrayBlockingQueue阻塞式的队列
 * 在阻塞式容器里面添加了put：如果满了，就会等待；take如果空了就会等待
 * 有了BlockingQueue就不用自己去实现同步阻塞式容器了。BlockingQueue用的很多
 * 把下面的Queue换成Deque也一样效果
 */

package com.lisz.concur26.concurcontainer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class T06_ArrayBlockingQueue {
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);//有界队列
	
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			queue.put("a" + i);
		}
		//queue.offer("aaa"); // 加不进去，不阻塞，也不抛异常
		queue.offer("aaa", 1, TimeUnit.SECONDS);//指定的时间之后还是加不进去就不往里加了
		//queue.put("aaa"); // 满了的话程序停在这里阻塞
		//queue.add("aaa"); // 满了就抛异常
		System.out.println(queue);
	}

}
