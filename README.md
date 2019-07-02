多线程内容总结：
1. synchronized互斥锁，锁的获得与释放，阻塞的概念，死锁，锁的重入，粒度和异常处理
2. volatile关键字，Java内存模型（JMM）
3. AtomicXXX类保证原子性且高效，CAS的概念
4. CountDownLatch门闩
5. 手动锁：ReentrantLock，注意手动释放
6. wait,notify/notifyAll线程间通信
7. Condition，叫醒或者中止指定的线程（生产者消费者）
8. ThreadLocal，线程间变量的隔离
9. 单例的多线程下的lazy实现方式
10. 并发容器 ConcurrentHashMap，ConcurrentQueue，CopyOnWriteList，各种BlockingQueue的使用等
11. 线程池：FixedThreadPool，CachedThreadPool，SingleThreadExecutor，ScheduledThreadPool，
           WorkStealingPool，ForkJoinPool以及底层的ThreadPoolExecutor。Executors工厂/工具类，
           Runnable和Callable接口，Future和FutureTask。Parallel的编程思想
12. 常见多线程相关面试题

新版笔记和详细解释见各个例子程序及其注释，以下是2018的老版笔记



========================================================================================

public synchronized void m() {
...
执行方法内的代码时锁定当前这个对象
..
}

synchronized 加在静态方法上意思是锁定当前类的.class
一个synchronized代码块相当于一个原子操作

非synchronized方法可以在synchronized方法执行期间执行，不用拿到锁,直接就可以执行
两个不同的synchronized方法先拿到锁的先执行

共享资源读写的时候才需要线程同步，在两个或多个线程读写同一个内存资源时才需要同步。

线程之间的通讯有两种模型1.共享内存2.互相发消息。java用的是第一种


程序出了异常，默认情况下锁会被释放
 * 所以在并发处理的过程中，有异常要多加小心，不然可能会出现不一致的情况。
 * 比如，在一个web app处理过程中，多个servlet线程共同访问同一个资源，这时如果异常处理不合适，
 * 在第一个线程中抛出异常，其他线程就会进入同步代码区，有可能访问到异常产生时的数据
 * 因此要非常小心的处理同步业务逻辑中的异常,加try catch做出正确的处理
 
 volatile原理 参看Java Memory Model （JMM）
Java有个主线程，所用的内存叫主内存，我们平常所说的堆栈都在主内存中。每个线程可能用一个不同的CPU。每个分线程都有它自己的一个工作区，包括内存、cpu缓存或寄存器。。。当分线程启动之后，主内存中的变量会被copy到CPU缓存中一个副本。

当CPU没有空闲的时候：分线程只访问这个副本以加快效率，比如+1+1+1.。。再写回主内存去。而加了volatile之后，其他某个线程若改变主内存中此变量的值的话，主线程会通知各个分线程，告诉他们改变量已经过期，请访问主内存同步一下。不是每次读都必须到主内存拿那个值，读内存相对于读CPU缓存或者寄存器要慢很多.

当CPU有空闲的时候，它有可能去主内存中刷新一下变量值

尽量让synchronized包裹尽量少的代码，细粒度的锁效率较高

synchronized锁的是堆内存中的实际对象，而不是栈内存中的引用。锁的信息记录在堆内存中,java锁只能锁堆，锁就是在对象上面的一个值1,2,3.。。

不要用synchronized锁定字符串常量
String s1 = "Hello";
String s2 = "Hello";
由于拼写相同，s1和s2实际上指向的是同一个对象，所以不要用字符串常量作为锁定对象




===== CAS =====
CAS:Compare and Swap, 翻译成比较并交换。 
java.util.concurrent包中借助CAS实现了区别于synchronouse同步锁的一种乐观锁。
 
CAS有3个操作数，内存值V，旧的预期值A，要修改的新值B。当且仅当预期值A和内存值V相同时，将内存值V修改为B，否则什么都不做。
从Java1.5开始JDK提供了AtomicReference类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行CAS操作

public static class MyLock {
    private AtomicBoolean locked = new AtomicBoolean(false);

    public boolean lock() {
        return locked.compareAndSet(false, true);
    }

}
locked变量不再是boolean类型而是AtomicBoolean。这个类中有一个compareAndSet()方法，它使用一个期望值和AtomicBoolean实例的值比较，和两者相等，则使用一个新值替换原来的值。在这个例子中，它比较locked的值和false，如果locked的值为false，则把修改为true。

===== CAS Ends =====




只有wait会释放锁

* CountDownLatch用await和countDown方法代替wait和notify，
         * 不涉及锁定（synchronized），每次countDown count值减1
         * 当count值为0时await的线程继续运行
         * 当不涉及同步，只是涉及线程通信的时候，用synchronized + wait/notify
         * 就显得太重了
         * 这时应该考虑CountDownLatch/cyclicbarrier/semaphore
         * CountDownLatch打开之后，两个线程会同时运行


java高并发有三大快知识点
1.synchronizer 2.同步容器 3.Thread Pool、executor、codigo、future

/* ReentrantLock必须手动释放！！
             * synchronized中有异常抛出的话会自动释放锁
             * ReentrantLock是手动锁，有异常抛出并不会自动释放锁
             * 所以finally中必须手动释放锁。写try catch finally 或
             * try finally，在finally中释放锁。
             * synchronized是手动上锁自动释放；ReentrantLock是
             * 手动上锁手动释放
             */

性能上ReentrantLock和synchronized基本没区别，只是前者锁定更灵活

lock.lock(); //一般锁定，无法打断，拿不到锁就一直等
lock.lockInterruptibly();//可被打断的锁定，如果线程对象在其他线程中被调用线程对象的interrupt则跳至catch finally,等不着锁就别等了

synchronized 使非公平锁，一个线程t持有锁，其他线程t1,t2,t3 ...都必须等待，t释放锁之后其他线程随机持有锁，有可能t1等了1秒，t3等了1天，而最终锁却给了t1
ReentrantLock有公平和非公平之分：Lock lock = new ReentrantLock();是非公平锁Lock lock = new ReentrantLock(true);则是公平锁，先到先得，按等待时间来

加锁本质上是将并发转变为串行

activeMQ
redis，memchached mongodb
springcloud， dubbo
springboot springcloud netty rocketmq
mybatis nginx 
activity flow engine
搜索引擎：Elasticsearch，Logstash，Kibana，Beats
企业级开发环境搭建：maven，nexus，jekins，git


Java并发容器
对于set和map的使用
非并发：hashmap,treemap,linkedhashmap
想加锁，且并发不高：hashtable，Collections.sychronizedMap/Set
并发性较高：concurrenthashmap
并发性比较高且要求排序：concurrentskiplistmap
concurrenthashmap,concurrentskiplistmap容器分为16段，每次插入仅仅锁定一个数据段。如果两个线程插入的不是一个数据段，则可以并发往里插306. Java 1.8用的是node和CAS:http://blog.csdn.net/not_in_mountain/article/details/77864444

对于使用队列的情况：
无并发：ArrayList
linkedList
低并发：
Vector
Collections.synchronizedXXX
高并发:
ConcurrentLinkedQueue
BlockingQueue
LinkedBlockingQueue
ArrayBlockingQueue
TransferQueue
SynchronousQueue
DelayQueue执行定时任务
CopyOnWriteList：写得特别少而读得特别多时使用


高并发的时候可以使用两种队列：
ConcurrentLinkedQueue
BlockingQueue
LinkedBlockingQueue(并发的，加锁的)
ArrayBlockingQueue（阻塞式的）
有put方法如果容器满了就会等待；take方法(拿出并删除)，如果阻塞式容器空了就会等待。BlockingQueue用的特别多
LinkedTransferQueue（TransferQueue）	
如果消费者先启动，生产者才产出产品，TransferQueue可以在高并发状态下不往queue
里面放而是直接给消费者
transfer方法若找不到消费者则会阻塞,其后的代码无法运行。TransferQueue如果不用transfer而用add，put，offer就没有这个问题，实时消息处理用transferQueue用得比较多
SynchronousQueue
DelayQueue（implements BlockingQueue 无界队列，默认是排好顺序的）
DelayQueue所加进去的元素必须实现Delayed接口，DelayQueue可以用来做定时执行任务

可以先用Hashtable,Vector等低并发的容器，然后撑不住的时候再上高并发的。

脏读：读到了正在修改过程中的数据


Callable和Runnable的区别，当一个线程运行完了之后需要返回值的时候用Callable，不需要时用Runnable。Callable可以抛异常.
execute方法接受runnable接口对象，没有返回值。ExecutorService的submit方法接受Callable接口对象
5个线程6个任务，则有一个任务会被放到线程池的一个BlockingQueue里;除此之外还有一个completedTasks队列，存放结束了的任务队列。
线程池就是一堆线程维护着两个队列
Future就是Callable的返回值，Callable线程要执行一段时间所以是Future

ExecotorService的isTerminated是指是不是所有的任务都执行完了
ExecotorService的isShutDown是指是不是关闭了，关了不到表执行完了，只是代表正在关闭的过程之中，写了一个boolean值，说正在管，但是还有没执行完的任务在跑
任务完成之后线程池里的现成是Idle状态 

所有ThreadPool都是ExecutorService类型
FixedThreadPool 有固定的容量
CachedThreadPool会随着任务的到来逐步启动新线程，有空闲的线程时来新任务则用空闲的线程，没有空闲的线程时来新任务则new一个新线程执行任务。默认一个线程空闲60秒就自动销毁。此空闲时间限制可以自己指定（最大不能超过int类型的最大数）
SingleThreadExecutor只有一个线程，保证任务顺序，先扔进去的先执行
ScheduledPool和DelayQueue相对应.ScheduledExecutorService.scheduleAtFixedRate每隔一段时间就执行相同的任务

ScheduledExecutorService service = Executors.newScheduledThreadPool(4);
        service.scheduleAtFixedRate(()->{
            System.out.println(Thread.currentThread().getName());
        }, 0, 1000, TimeUnit.MILLISECONDS);

WorkStealingPool每个线程维护自己的一个队列。当某个线程把自己的任务列表里的任务全都完成之后，它会把其他线程任务列表里的任务“偷”过来继续执行。线程们会主动的找活干
ForkJoinPool把大任务切分成小任务，如果还是过大则继续切分，然后分别执行之后汇总

除了WorkStealingPool和ForkJoinPool（继承于AbstractExecutorService->ExecutorService），其实都用了ThreadPoolExecutor（也继承于AbstractExecutorService->ExecutorService），是它的子类。ThreadPoolExecutor是线程池通用的类，感觉6种都不好，则继承他，改。
具体什么样的TreadPool就看ThreadPoolExecutor构造方法中参数值和最后BlockingQueue参数的类型，到底是什么样的队列

List.parallelStream().forEach(ParallelStreamAPITest::isPrime);多线程共同做计算，默认使用多线程
