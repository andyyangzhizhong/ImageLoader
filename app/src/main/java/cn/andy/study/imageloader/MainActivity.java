package cn.andy.study.imageloader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.andy.study.imageloader.facebook.FBActivity;
import cn.andy.study.imageloader.imageloader.DiskCacheImageLoader;
import cn.andy.study.imageloader.imageloader.ImageLoader;
import cn.andy.study.imageloader.imageloader.downloader.AsyncResult;
import cn.andy.study.imageloader.imageloader.downloader.MyAsyncTask;

import static com.blankj.utilcode.util.SDCardUtils.getSDCardPaths;

public class MainActivity extends AppCompatActivity {

    private Button fb;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();

    }

    private void initListener() {
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FBActivity.class));
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
        List<String> paths = getSDCardPaths();
        for (String path : paths) {
            File filePath = new File(path);
            if (filePath.exists()) {
                deleteFile(filePath);
            }
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
