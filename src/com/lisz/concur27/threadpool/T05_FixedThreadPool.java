/**
 * 线程池就是一堆线程装在某个容器里等着运行任务
 * 本例中：启动了5个线程但是扔过来了6个任务，多余的任务会被放在一个
 * 任务队列中，任务队列一般是个BlockingQueue。同样的，线程池还维
 * 护着一个已完成的任务队列，什么时候任务结束了，扔到里面。线程池的
 * 好处是：
 * 任务执行完了之后，线程不消失，新的任务再来了，它可以接受，不需要
 * 再新启动一个线程，效率会比较高，并发性会比较好。启动和关闭线程都
 * 是要消耗系统资源的，资源的消耗还比较大，新启动线程需要操作系统由
 * 用户态转变为内核态，所以线程启动之后，能重用就重用。池里的任务执
 * 行完成之后，各个线程的状态变为idle，发呆中...
 * c3p0和DBCP都是数据库的连接池，但是他们的底层都是线程池
 */

package com.lisz.concur27.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T05_FixedThreadPool {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(5); //个数位5的线程池，需要的时候才会启动线程，execute和submit方法
		for (int i = 0; i < 6; i++) { //5个线程，6个任务，所以有个线程要做两个任务，多余的那个任务会被暂时放在任务队列里
			service.execute(()->{
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			});
		}
		
		System.out.println(service);
		service.shutdown(); // 正常的关闭，等着所有任务都执行完才会关闭掉，还有个方法叫shutdownNow，二话不说直接关掉
		System.out.println(service.isTerminated()); // isTerminated代表的是所有的任务是否都执行完了
		System.out.println(service.isShutdown());   //状态变成Shutting Down，因为已经调用过shutdown方法了
		System.out.println(service);
		
		TimeUnit.SECONDS.sleep(7);
		System.out.println(service.isTerminated()); //true，工人都已经干完活了
		System.out.println(service.isShutdown());
		System.out.println(service);				//状态变成Terminated
		
		// 已经terminate之后就不能再execute了，会报java.util.concurrent.RejectedExecutionException
	}
/*
 * java.util.concurrent.ThreadPoolExecutor@31b7dea0[Running, pool size = 5, active threads = 5, queued tasks = 1, completed tasks = 0] //6个任务，所以5个线程全起来了，还有一个等着的
false
true
java.util.concurrent.ThreadPoolExecutor@31b7dea0[Shutting down, pool size = 5, active threads = 5, queued tasks = 1, completed tasks = 0]
pool-1-thread-4
pool-1-thread-3
pool-1-thread-1
pool-1-thread-5
pool-1-thread-2
pool-1-thread-4
true
true
java.util.concurrent.ThreadPoolExecutor@31b7dea0[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 6]
 */
}
