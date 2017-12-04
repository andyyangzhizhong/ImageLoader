/*
 * Copyright (c) 2014 zengdexing
 */
package cn.andy.study.imageloader.imageloader.cach;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单任务的线程池，按先进先出方式执行提交的任务，区别于：{@link Executors#newSingleThreadExecutor()}
 *
 * @author longmaster
 */
public class SingleThreadPool implements Executor {
    private ExecutorService mExecutor;

    /**
     * 单任务线程池，当线程空闲超过设定时间时会回收线程，有新的任务时再起
     *
     * @param keepAliveTime  允许线程最大空闲时间
     * @param timeUnit       线程空闲时间单位
     * @param threadName     线程池使用线程的名字，不可为空，可以为任意值，方便DDMS查看线程状态
     * @param threadPriority 线程优先级
     */
    public SingleThreadPool(int keepAliveTime, TimeUnit timeUnit, final String threadName, final int threadPriority) {
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, threadName);
                thread.setPriority(threadPriority);
                return thread;
            }
        };
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<Runnable>();
        mExecutor = new ThreadPoolExecutor(0, 1, keepAliveTime, timeUnit, linkedBlockingQueue, threadFactory);
    }

    /**
     * 单任务线程池，默认优先级为（Thread.NORM_PRIORITY - 1）。当线程空闲超过设定时间时会回收线程，有新的任务时再起
     *
     * @param keepAliveTime 允许线程最大空闲时间
     * @param timeUnit      线程空闲时间单位
     */
    public SingleThreadPool(int keepAliveTime, TimeUnit timeUnit) {
        this(keepAliveTime, timeUnit, "SingleThreadPool", Thread.NORM_PRIORITY - 1);
    }

    public SingleThreadPool() {
        this(60, TimeUnit.SECONDS);
    }

    /**
     * 填加任务到队列，如果线程池中没有线程，将创建新的线程来执行任务，先进先出执行任务
     *
     * @param runnable
     * @return
     */
    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public <V> Future<V> submit(Callable<V> task) {
        return mExecutor.submit(task);
    }
}
