package cn.andy.study.imageloader.imageloader.downloader;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadProcess implements Executor {
    private LinkedBlockingQueue<Runnable> m_doInBackgrounds;
    private ProcessThread m_processThread;

    /**
     * 新建一个线程池
     *
     * @param name 线程池名字，不可以传空，可以为任意字符串，用于打印日志
     */
    public ThreadProcess(String name) {
        m_doInBackgrounds = new LinkedBlockingQueue<Runnable>();
        m_processThread = new ProcessThread(name);
        m_processThread.start();
    }

    /**
     * 提交任务到线程池中执行</br>
     * 等同于:{@link ThreadProcess#execute(Runnable)}
     *
     * @param runnable
     */
    public void submit(Runnable runnable) {
        long time = System.currentTimeMillis();
        m_doInBackgrounds.offer(runnable);
    }

    /**
     * 阻塞获取任务，如果取不到任务，线程进入休眠
     */
    private Runnable getTask() {
        Runnable runnable = null;
        try {
            runnable = m_doInBackgrounds.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return runnable;
    }

    @Override
    public void execute(Runnable command) {
        submit(command);
    }

    /**
     * 群消息处理线程
     *
     * @author Administrator
     */
    private class ProcessThread extends Thread {
        public ProcessThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            Runnable runnable = null;
            while ((runnable = getTask()) != null) {
                long time = System.currentTimeMillis();
                runnable.run();
                long spendTime = System.currentTimeMillis() - time;
                runnable = null;
            }
        }
    }
}
