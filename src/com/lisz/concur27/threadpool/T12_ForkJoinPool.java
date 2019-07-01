/**
 * 概念上有点像MapReduce。把大任务分成小任务（fork），分别开启线程计算具体怎么分，在RecursiveAction
 * （无返回值）或者RecursiveTask（有返回值）的compute中定义。执行ForkJoinPool的execute方法，
 * 然后传入RecursiveAction或者ForkJoinTask对象（RecursiveAction或者RecursiveTask）。
 * 对于要求返回值的情况，还要调用RecursiveTask对象的get或者join方法得到返回结果。
 * Java 1.7 之后新加的ForkJoinPool
 */

package com.lisz.concur27.threadpool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class T12_ForkJoinPool {
	private static int[] nums = new int[1000000];
	private static final int MAX_NUM = 50000;	//任务最大不能超过50000个数字
	private static final Random r = new Random();
	
	static {
		for (int i = 0; i < nums.length; i++) {
			nums[i] = r.nextInt(100);
		}
		System.out.println(Arrays.stream(nums).sum());// Stream API
	}	
	
	private static final class AddAction extends RecursiveAction {
		private int start;
		private int end;
		
		public AddAction(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected void compute() { //compute里面指明什么时候任务足够小可以计算了，或者继续往下拆分任务
			if (end - start <= MAX_NUM) {
				long sum = 0L;
				for (int i = start; i < end; i++) {
					sum += nums[i];
				}
				System.out.println("From: " + start + " to: " + end + " = " + sum);
			} else {
				int middle = start + ((end - start) >> 1);
				AddAction subTask1 = new AddAction(start, middle);
				AddAction subTask2 = new AddAction(middle, end);
				subTask1.fork();
				subTask2.fork();
			}
		}
	}
	
	private static final class AddTask extends RecursiveTask<Long> {//这里要指定返回值类型
		private int start;
		private int end;
		
		public AddTask(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Long compute() { //compute里面指明什么时候任务足够小可以计算了，或者继续往下拆分任务
			if (end - start <= MAX_NUM) {
				long sum = 0L;
				for (int i = start; i < end; i++) {
					sum += nums[i];
				}
				System.out.println("From: " + start + " to: " + end + " = " + sum);
				return sum;
			} else {
				int middle = start + ((end - start) >> 1);
				AddTask subTask1 = new AddTask(start, middle);
				AddTask subTask2 = new AddTask(middle, end);
				subTask1.fork();
				subTask2.fork();
				return subTask1.join() + subTask2.join();
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		ForkJoinPool fjp = new ForkJoinPool();
		AddAction action = new AddAction(0, nums.length);
		fjp.execute(action);
		//task.compute();
		AddTask task = new AddTask(0, nums.length);
		fjp.execute(task);
		System.out.println(task.get()); //get方法可以捕捉ExecutionException和InterruptedException异常
		System.out.println(task.join());//　效果和get一样，只是不可以捕捉ExecutionException和InterruptedException异常
		System.in.read();
	}

}
