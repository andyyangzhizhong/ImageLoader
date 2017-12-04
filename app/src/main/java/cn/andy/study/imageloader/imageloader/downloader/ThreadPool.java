package cn.andy.study.imageloader.imageloader.downloader;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单任务线程池，在该线程池中，当线程空闲一段时间后会回收线程
 *
 * @author zengdexing
 */
public class ThreadPool implements Executor {
    private Executor executor;

    /**
     * 线程池构造方法
     *
     * @param keepAliveTime 线程最大空闲时间，当线程空闲时间超过该时间后，会回收线程，下次提交再新建线程
     * @param unit          空闲时间单位
     * @param priorit       线程优先级，最高优先级：{@link Thread#MAX_PRIORITY} 最低优先级：{@link Thread#MIN_PRIORITY} 普通的：
     *                      {@link Thread#NORM_PRIORITY}</br>
     */
    public ThreadPool(long keepAliveTime, TimeUnit unit, final int priorit) {
        executor = new ThreadPoolExecutor(0, 1, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setPriority(priorit);
                return thread;
            }
        });
    }

    /**
     * 默认构造方法
     */
    public ThreadPool() {
        this(60, TimeUnit.SECONDS, Thread.NORM_PRIORITY);
    }

    /**
     * 填加任务
     *
     * @param runnable
     * @return
     */
    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public abstract static class ThreadPoolTask implements Runnable {
        private boolean isCancle = false;

        public void cancle() {
            isCancle = true;
            onCancle();
        }

        @Override
        public void run() {
            if (isCancle) {
                return;
            }
            onRun();
        }

        public void onCancle() {

        }

        public abstract void onRun();
    }
}
