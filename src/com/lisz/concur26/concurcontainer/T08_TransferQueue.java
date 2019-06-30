/**
 * TransferQueue提供了一个transfer方法，
 * 用这种queue的时候往往是这个情形：
 * 各个消费者先启动，生产者调用transfer的时候，
 * 首先去找有没有消费者等着呢，有的话直接把元素
 * 给消费者，而不是扔到队列里；如果消费者都忙着呢，
 * 则transfer方法会阻塞
 * 经常用在：
 * 更高的并发的情况下，比如网络游戏，一个坦克的
 * 位置改变，发给其他的客户端的时候，就会用到一个
 * 队列，这时把消息直接给消费者就会更高效，服务器
 * 能支撑的玩家数量就更多。
 * 这个例子中，如果生产者不幸先行动，则就会卡在transfer
 * 那里无法继续执行了，用put/add/offer都没这问题，
 * 实时消息处理用这个的比较多，比如netty内部
 */

package com.lisz.concur26.concurcontainer;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;

public class T08_TransferQueue {
	public static void main(String[] args) throws InterruptedException {
		LinkedTransferQueue<String> queue = new LinkedTransferQueue<>();
		/*new Thread(()->{
			try {
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/
		new Thread(()->{
			try {
				TimeUnit.SECONDS.sleep(1);  // 等一秒钟之后消费就可以解救transfer了
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		queue.transfer("aaa"); // 这里用put/add/offer都没这问题。生产出来的消息必须有人给处理掉，否则不往下走了
		/*new Thread(()->{
			try {
				System.out.println(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();*/
	}

}
