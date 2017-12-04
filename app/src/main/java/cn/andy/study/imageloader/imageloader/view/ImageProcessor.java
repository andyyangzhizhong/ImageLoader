package cn.andy.study.imageloader.imageloader.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import cn.andy.study.imageloader.imageloader.ImageLoadListener;

/**
 * 图片处理
 *
 * @author zdxing 2015年1月30日
 */
public interface ImageProcessor {
    /**
     * 图片加载成功后，处理图片
     *
     * @param bitmapSource 图片来源
     * @param bitmap       加载成功后的图片
     * @return 处理过后的Drawable
     */
    Drawable onProcessImage(ImageLoadListener.BitmapSource bitmapSource, Bitmap bitmap);
}
