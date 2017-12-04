package cn.andy.study.imageloader.imageloader;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import java.io.File;
import java.util.List;

import cn.andy.study.imageloader.imageloader.cach.BitmapMemoryCache;
import cn.andy.study.imageloader.imageloader.cach.Encoder;
import cn.andy.study.imageloader.imageloader.cach.SingleThreadPool;
import cn.andy.study.imageloader.imageloader.downloader.AsyncResult;
import cn.andy.study.imageloader.imageloader.downloader.ImageDownloader;
import cn.andy.study.imageloader.imageloader.downloader.MyAsyncTask;

public class ImageLoaderImp extends ImageLoader {
    private boolean isInit = false;

    /** 内存缓存 */
    private BitmapMemoryCache bitmapMemoryCache;

    /** 磁盘缓存 */
    private DiskCacheImageLoader diskCacheManager;

    /** 磁盘图片加载器 */
    private DiskImageLoader diskBitmapLoader;

    /** 图片下载器 */
    private ImageDownloader imageDownloader;

    /** 图片解码线程池， 单任务 */
    private SingleThreadPool executorService = new SingleThreadPool();

    ImageLoaderImp() {
        isInit = false;
    }

    private static String getUIPName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    private static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static boolean isLargeHeap(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_LARGE_HEAP) != 0;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static int getLargeMemoryClass(ActivityManager am) {
        return am.getLargeMemoryClass();
    }

    @Override
    public void init(int memoryCacheSize, int diskCacheSize, String diskCacheDir, int downloadThreadSize) {
        // 初始化缓存
        bitmapMemoryCache = new BitmapMemoryCache(memoryCacheSize);
        diskCacheManager = new DiskCacheImageLoader(executorService, diskCacheSize, diskCacheDir);

        // 本地图片加载器
        diskBitmapLoader = new DiskImageLoader(executorService);

        /** 图片下载器 */
        imageDownloader = new ImageDownloader(downloadThreadSize);

        isInit = true;
    }

    @Override
    public void init(Context context) {
        int memoryCacheSize = getMemoryCacheSize(context);

        int diskCacheSize = 30 * 1024 * 1024;
        String sha1Key = Encoder.encodeBySHA1(getUIPName(context));
        String dirName = "zdxing/imgCache_" + sha1Key;
        String diskCacheDir = new File(context.getCacheDir(), dirName).getAbsolutePath();

        this.init(memoryCacheSize, diskCacheSize, diskCacheDir, 2);
    }

    private int getMemoryCacheSize(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        if (hasHoneycomb() && isLargeHeap(context)) {
            memoryClass = getLargeMemoryClass(am);
        }
        return 1024 * 1024 * memoryClass / 8;
    }

    @Override
    public ImageLoadTask loadImage(ImageLoadOptions imageLoadOptions, ImageLoadListener imageLoadListener) {
        ImageLoadTask imageLoadTask = new ImageLoadTask(imageLoadOptions, imageLoadListener, bitmapMemoryCache,
                diskCacheManager, diskBitmapLoader, imageDownloader);
        imageLoadTask.start();
        return imageLoadTask;
    }

    @Override
    public boolean isInit() {
        return isInit;
    }

    @Override
    public int getMemoryCacheSize() {
        return bitmapMemoryCache.getCurrentSize();
    }

    @Override
    public int getDiskCacheSize() {
        return diskCacheManager.getSize();
    }

    @Override
    public void clear(final DiskCacheImageLoader.OnClearCacheListener onClearCacheListener) {
        MyAsyncTask<Void> myAsyncTask = new MyAsyncTask<Void>() {
            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                bitmapMemoryCache.clear();
                diskCacheManager.clear(onClearCacheListener);
            }
        };
        myAsyncTask.execute(executorService);
    }

    @Override
    public void removeCache(String filePath) {
        final String encodeKey = ImageLoadOptions.getEncodeKey(filePath);
        List<String> allKey = bitmapMemoryCache.getAllKey();
        for (String key : allKey) {
            if (key.startsWith(encodeKey)) {
                bitmapMemoryCache.remove(key);
            }
        }

        MyAsyncTask<Void> myAsyncTask = new MyAsyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                List<String> allKey = diskCacheManager.getAllKey();
                for (String key : allKey) {
                    if (key.startsWith(encodeKey)) {
                        diskCacheManager.remove(key);
                    }
                }
            }
        };
        myAsyncTask.execute(executorService);
    }
}
