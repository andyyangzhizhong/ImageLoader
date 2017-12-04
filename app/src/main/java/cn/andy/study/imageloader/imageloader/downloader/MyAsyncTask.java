/*
 * Copyright (c) 2014 zengdexing
 */
package cn.andy.study.imageloader.imageloader.downloader;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.andy.study.imageloader.imageloader.cach.SingleThreadPool;

/**
 * 简化版本AsyncTask
 *
 * @param <D>
 * @author zdxing 2015年1月23日
 */
public class MyAsyncTask<D> implements Callable<AsyncResult<D>> {
    private static SingleThreadPool singleThreadPool = new SingleThreadPool(60, TimeUnit.SECONDS, "SingleThreadPool",
            Thread.NORM_PRIORITY - 1);
    private static Handler handler = new Handler(Looper.getMainLooper());

    private AsyncResult<D> mResult = new AsyncResult<D>();

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<D>> execute() {
        return execute(singleThreadPool);
    }

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<D>> execute(SingleThreadPool executor) {
        return executor.submit(this);
    }

    /**
     * 开始执行任务
     */
    public Future<AsyncResult<D>> execute(ExecutorService executor) {
        return executor.submit(this);
    }

    public AsyncResult<D> call() throws Exception {
        runOnBackground(mResult);
        post();
        return mResult;
    }

    private void post() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUIThread(mResult);
            }
        });
    }

    /**
     * 该函数在线程中执行
     */
    protected void runOnBackground(AsyncResult<D> asyncResult) {

    }

    /**
     * 该函数在UI线程中执行
     *
     * @param asyncResult {@link #runOnBackground(AsyncResult<D>)}执行后返回的结果
     */
    protected void runOnUIThread(AsyncResult<D> asyncResult) {
    }

}
