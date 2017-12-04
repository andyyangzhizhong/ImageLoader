package cn.andy.study.imageloader.imageloader;

import android.graphics.Bitmap;

/**
 * 图片加载结果监听
 *
 * @author zdxing
 */
public interface ImageLoadListener extends OnLoadProgressChangeListener {

    /**
     * 图片开始下载了，只有本地图片不存在时，本方法才会被执行
     */
    void onStartDownload();

    /**
     * 加载失败的回调
     *
     * @param reason 失败原因
     */
    void onLoadFailed(String reason);

    /**
     * 图片加载成功的回调
     *
     * @param bitmapSource 图片来源
     * @param bitmap       加载成功后的图片
     */
    void onLoadSuccessful(BitmapSource bitmapSource, Bitmap bitmap);

    /** 加载的图片来源 */
    enum BitmapSource {
        /** 内存缓存 */
        MEMORY_CACHE,

        /** 磁盘缓存 */
        DISK_CACHE,

        /** 文件缓存 */
        FILE,

        /** 网络 */
        NETWORK
    }

    class SimpleImageLoadListener implements ImageLoadListener {
        @Override
        public void onLoadProgressChange(int totalSize, int currentSize) {

        }

        @Override
        public void onLoadFailed(String reason) {

        }

        @Override
        public void onLoadSuccessful(BitmapSource bitmapSource, Bitmap bitmap) {

        }

        @Override
        public void onStartDownload() {

        }
    }
}
