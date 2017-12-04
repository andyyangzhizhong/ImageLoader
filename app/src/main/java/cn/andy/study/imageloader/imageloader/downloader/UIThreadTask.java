package cn.andy.study.imageloader.imageloader.downloader;

import android.os.Handler;
import android.os.Looper;

/**
 * UI线程任务
 * <p>
 * <pre>
 * // 使用方法
 * UIThreadTask uiThreadTask = new UIThreadTask() {
 * 	&#064;Override
 * 	protected void runOnUIThread() {
 * 		// 在UI中处理的逻辑
 *    }
 * };
 * uiThreadTask.execute();
 * </pre>
 *
 * @author zdxing 2015年1月26日
 */
public abstract class UIThreadTask {
    private static Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUIThread();
        }
    };

    /**
     * 提交任务到UI线程中执行
     */
    public void execute() {
        handler.post(runnable);
    }

    protected abstract void runOnUIThread();
}
