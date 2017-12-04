package cn.andy.study.imageloader.facebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import cn.andy.study.imageloader.R;

/**
 * Created by yangzhizhong
 */

public class ImageLoader {

    private GridView mGridView;

    private LruCache<String, Drawable> mCachesD;
    private Set<NewsAsyncTask> mTask;

    public ImageLoader(GridView gridView) {
        mGridView = gridView;
        mTask = new HashSet<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 2;

        mCachesD = new LruCache<String, Drawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, Drawable value) {
                return super.sizeOf(key, value);
            }
        };
    }

    /**
     * 添加到缓存
     *
     * @param url
     * @param drawable
     */
    private void addDrawableToCache(String url, Drawable drawable) {
        if (getDrawableFromCache(url) == null) {
            mCachesD.put(url, drawable);
        }
    }

    /**
     * 从缓存中获取数据
     *
     * @param url
     * @return
     */
    private Drawable getDrawableFromCache(String url) {
        return mCachesD.get(url);
    }

    /**
     * 从网络中获取Bitmap对象
     *
     * @param urlString
     * @return
     */
    private Drawable getDrawableFromUrl(String urlString) {
        Bitmap bitmap;
        Drawable drawable;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            drawable = new BitmapDrawable(bitmap);
            connection.disconnect();
            return drawable;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 取消所有运行的任务
     */
    public void cancelAllTasks() {
        if (mTask != null) {
            for (NewsAsyncTask task : mTask) {
                task.cancel(false);
            }
        }
    }

    /**
     * 加载当前可见的所有图片
     *
     * @param start
     * @param end
     */
    public void loadImages(int start, int end) {
        for (int i = start; i < end; i++) {
            String url = ImageAdapter.URLS[i];
            Drawable drawable = getDrawableFromCache(url);
            if (drawable == null) {
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            } else {
                ImageView imageView = (ImageView) mGridView.findViewWithTag(url);
                imageView.setImageDrawable(drawable);
            }
        }
    }

    /**
     * 使用AsyncTask的方式加载图片
     *
     * @param url
     * @param imageView
     */
    public void showImageByAsyncTask(String url, ImageView imageView) {
        Drawable drawable = getDrawableFromCache(url);
        if (drawable == null) {
            imageView.setImageResource(R.mipmap.ic_launcher);
        } else {
            imageView.setImageDrawable(drawable);
        }
    }

    /**
     * 异步加载的线程
     *
     * <String, Void, Drawable> :
     *    参数1 : 表示task启动时的输入参数的类型
     *    参数2 ：表示后台任务完成进度的类型
     *    参数3 ： 表示后台任务执行完返回的结果的类型
     *
     */
    private class NewsAsyncTask extends AsyncTask<String, Void, Drawable> {
        private String mUrl;

        public NewsAsyncTask(String url) {
            this.mUrl = url;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String url = params[0];
            Drawable drawable = getDrawableFromUrl(url);
            if (drawable != null) {
                addDrawableToCache(url, drawable);
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            ImageView imageView = mGridView.findViewWithTag(mUrl);
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
            mTask.remove(this);
        }
    }
}
