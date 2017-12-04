package cn.andy.study.imageloader.imageloader.downloader;

import android.os.Bundle;

/**
 * 异步加载结果
 *
 * @param <D>
 * @author zdxing 2015年2月7日
 */
public class AsyncResult<D> {
    private int result = 0;
    private D data;
    private Bundle bundle = new Bundle();

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getString(String key, String defaultValue) {
        String bundleString = bundle.getString(key);
        return (bundleString == null) ? defaultValue : bundleString;
    }

    public void putString(String key, String value) {
        bundle.putString(key, value);
    }

    public int getInt(String key, int defaultValue) {
        return bundle.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        bundle.putInt(key, value);
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
