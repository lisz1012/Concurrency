/**
 * Future, 未来的结果.ExecutorService执行任务会花费一定的时间，在未来的某个时间点，
 * 会产生一个结果返回，而这个Future，代表的就是Callable里面的那个返回值。
 * 关于Future，会有两个类FureTask和Future。Future实现了RunnableFuture，
 * RunnableFuture又继承了Runnable接口。小程序里套了些泛型之类的东西，不要把它想得
 * 太麻烦。FutureTask是跟Runnable的任务作区分的，Runnable不产生任何返回值。而
 * FutureTask接受一个Callable参数泛型指定的就是返回值类型，通过FutureTask的泛型
 * 来指定，Callable的call方法必须return int类型。未来可以产生Integer返回值的任务。
 * FutureTask的get方法是阻塞的，直到拿到返回值才继续往下执行。
 * 
 * Future用来放ExecutorService的submit方法的返回值，未来的返回值
 * 
 * FutureTask跟 new Thread...一起使用；Future跟线程池一起使用
 * 
 * 不把线程池ExecutorService shutdown它就一直跑，所以打印完了程序并不结束
 * 
 * 线程池里面可以是数据库连接，也可以是网络连接，也可以是worker的那种消息的处理池，
 * 也可以是消息的生产者的池，也可以是发邮件的池...可以灵活指定.每一个线程维护一个
 * 数据库连接就是一个数据库连接池
 */



package com.lisz.concur27.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class T06_Future {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		FutureTask<Integer> task = new FutureTask<>(()->{ //Integer 代表未来我这个任务执行的结果是一个int类型，任务可以是Callable类型
			TimeUnit.MILLISECONDS.sleep(5000);
			return 1000;
		});
		new Thread(task).start();
		System.out.println(task.get()); // 阻塞, 5秒钟之后才会打印，拿到的返回值1000
		
		// ***************************
		ExecutorService service  = Executors.newFixedThreadPool(5); // 5个线程的线程池，扔进来1个任务
		Future<Integer> f = service.submit(()->{
			TimeUnit.SECONDS.sleep(1);
			return 1;
		});
		System.out.println(f.isDone());
		System.out.println(f.get());  //也是阻塞，拿不到返回值就傻等在这里
		TimeUnit.SECONDS.sleep(2);
		System.out.println(f.isDone());
		System.out.println(f.get());
	}

}
