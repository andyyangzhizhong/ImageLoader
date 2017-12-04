package cn.andy.study.imageloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;

import java.io.File;

import cn.andy.study.imageloader.facebook.FBActivity;
import cn.andy.study.imageloader.imageloader.DiskCacheImageLoader;
import cn.andy.study.imageloader.imageloader.ImageLoader;
import cn.andy.study.imageloader.imageloader.downloader.AsyncResult;
import cn.andy.study.imageloader.imageloader.downloader.MyAsyncTask;

public class MainActivity extends AppCompatActivity {

    private Button fb;
    private Button ok;
    private Button google;
    private Button clear;
    private StorageManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (StorageManager) MainActivity.this.getSystemService(Context.STORAGE_SERVICE);

        initView();
        initListener();

    }

    private void initListener() {
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用FaceBook提供的图片加载工具加载图片
                startActivity(new Intent(MainActivity.this, FBActivity.class));
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用OkHttp加载图片

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用google提供的图片加载工具

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清理缓存
                clearAllCache(getApplication());
                clearCache();
                Toast.makeText(getApplication(), "清理缓存成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        fb = findViewById(R.id.image_loader_fb);
        ok = findViewById(R.id.image_loader_ok);
        google = findViewById(R.id.image_loader_google);
        clear = findViewById(R.id.clear_cache);
    }


    public static void clearAllCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    //清除缓存
    private void clearCache() {
        new MyAsyncTask<Void>() {
            @Override
            protected void runOnBackground(AsyncResult<Void> asyncResult) {
                clear();
                ImageLoader.getInstance().clear(new DiskCacheImageLoader.OnClearCacheListener() {
                    @Override
                    public void onClearCacheFinish() {
                    }
                });
            }

            @Override
            protected void runOnUIThread(AsyncResult<Void> asyncResult) {
                /*Toast.makeText(getApplication(), "清理成功", Toast.LENGTH_SHORT).show();
                finish();*/
            }
        }.execute();
    }

    /**
     * 清缓存
     */
    public void clear() {
        File directory = new File(Environment.getExternalStorageDirectory().getPath());
        File file = new File(ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR);
        if (directory.exists()) {
            deleteFile(directory);
        }
        if (file.exists()) {
            deleteFile(file);
        }
    }

    private void deleteFile(File file) {
        if (file.isFile()) {
            deleteFileSafely(file);
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                deleteFileSafely(file);
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            deleteFileSafely(file);
        }
    }

    /**
     * 安全删除文件（解决：open failed: EBUSY (Device or resource busy)）
     *
     * @param file
     * @return
     */
    private boolean deleteFileSafely(File file) {
        if (file != null) {
            String tmpPath = file.getParent() + File.separator + System.currentTimeMillis();
            File tmp = new File(tmpPath);
            file.renameTo(tmp);
            return tmp.delete();
        }
        return false;
    }
}
