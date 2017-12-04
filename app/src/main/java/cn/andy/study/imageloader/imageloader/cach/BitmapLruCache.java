package cn.andy.study.imageloader.imageloader.cach;

import android.graphics.Bitmap;

import java.util.List;

/**
 * lruCache
 *
 * @author zdxing
 */
public interface BitmapLruCache {

    void put(String key, Bitmap value);

    Bitmap get(String key);

    void remove(String key);

    List<String> getAllKey();

    void clear();

    int getMaxSize();

    int getCurrentSize();
}
