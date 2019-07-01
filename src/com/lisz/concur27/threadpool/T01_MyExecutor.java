/**
 * 线程池初步：Executor接口，java线程池中最顶层的借口，里面就一个execute方法，
 * 自己去写实现怎么执行。当然我们一般不会直接用顶层接口的
 */

package com.lisz.concur27.threadpool;

import java.util.concurrent.Executor;

public class T01_MyExecutor implements Executor {

	public static void main(String[] args) {
		new T01_MyExecutor().execute(()->System.out.println("Hello executor!"));
	}

	@Override
	public void execute(Runnable command) {
		new Thread(command).start(); //新起一个线程
		//command.run();			//直接run，相当于方法调用
	}

}
