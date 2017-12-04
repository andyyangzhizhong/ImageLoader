package cn.andy.study.imageloader.imageloader;

import android.content.Context;

/**
 * 图片加载器。
 * <p>
 * 使用方法：
 * <p>
 * <p>
 * <pre>
 * 一、初始化
 * 在程序Application的onCreate()方法中加入如下代码即可完成初始化,该方法只需要执行一次即可：
 *
 * ImageLoader.getInstance().init(this);
 *
 * 二、使用方法,需要进行图片显示的地方：
 * ImageLoader imageLoader = ImageLoader.getInstance();
 *
 * // 构造图片加载参数
 * ImageLoadOptions.Builder builder = new Builder(item.filePath);
 * // 设置图片网页地址，如果加载本地图片，可不用设置该项
 * builder.setUrl(item.url);
 * // 设置是否使用缩略图缓存，如果不想使用缩略图缓存，可不设置该值。
 * // 如果图片是png，该值请设置为false，默认值为false
 * builder.setDiskCacheEnable(true);
 * // 设置是否使用内存缓存，默认为true
 * builder.setMemeorCacheEnable(true);
 * // 设置图片加载大小和缩放方式，如果不设置该值，将会加载全图
 * // 建议根据显示大小设置该值，加快图片加载速度
 * builder.setImageLoadSize(new ImageLoadSize(width, width, ImageScaleType.CENTER_CROP));
 *
 * //开始加载图片
 * imageLoadTask = imageLoader.loadImage(builder.build(), new ImageLoadListener() {
 * 		public void onLoadFailed(String reason) {
 * 			// 图片加载失败了
 *        }
 *
 * 		public void onLoadProgressChange(int totalSize, int currentSize) {
 * 			// 图片加载进度回调，当图片下载时才会产生该回调
 *        }
 *
 * 		public void onLoadFinish(BitmapSource bitmapSource, Bitmap bitmap) {
 * 			// 图片加载成功了
 * 			imageView.setImageBitmap(bitmap);
 *        }
 * });
 * </pre>
 *
 * @author zdxing
 */
public abstract class ImageLoader {
    /** 图片加载实例 */
    public static ImageLoader imageLoader;

    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            imageLoader = new ImageLoaderImp();
        } else {
            if (!imageLoader.isInit()) {
                throw new IllegalStateException("ImageLoader未初始化，请在Application.onCreate()中调用init方法初始化");
            }
        }
        return imageLoader;
    }

    /**
     * 初始化 ImageLoader
     *
     * @param memoryCacheSize    内存缓存的大小
     * @param diskCacheSize      硬盘缓存的大小
     * @param diskCacheDir       硬盘缓存的路径
     * @param downloadThreadSize 下载线程的大小
     */
    public abstract void init(int memoryCacheSize, int diskCacheSize, String diskCacheDir, int downloadThreadSize);

    /**
     * 使用默认值初始化。
     * <p>
     * 内存：总量的1/8<br />
     * 缩略图：30MB<br />
     * 缩略图缓存路径：cache/zdxing/imgCache_14314314<br />
     * 同时的下载线程 默认2个<br />
     */
    public abstract void init(Context context);

    /**
     * 加载图片
     *
     * @param imageLoadOptions  图片加载参数
     * @param imageLoadListener 图片加载结果监听器
     */
    public abstract ImageLoadTask loadImage(ImageLoadOptions imageLoadOptions, ImageLoadListener imageLoadListener);

    /**
     * imageLoader是否初始化
     *
     * @return true：已经初始化， false：未初始化
     */
    public abstract boolean isInit();

    /**
     * 获取内存缓存已使用的大小，单位字节
     */
    public abstract int getMemoryCacheSize();

    /**
     * 获取硬盘（缩略图）缓存已使用的大小，单位字节
     */
    public abstract int getDiskCacheSize();

    /** 清除缓存 */
    public abstract void clear(DiskCacheImageLoader.OnClearCacheListener onClearCacheListener);

    /**
     * 移除 文件缓存
     *
     * @param filePath 文件路径， 包括缩略图缓存和内存缓存
     */
    public abstract void removeCache(String filePath);
}
