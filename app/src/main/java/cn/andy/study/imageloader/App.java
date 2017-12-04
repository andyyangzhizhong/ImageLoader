package cn.andy.study.imageloader;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jianyuyouhun.permission.library.EZPermission;

import cn.andy.study.imageloader.imageloader.ImageLoader;

/**
 * Created by yangzhizhong
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        // 初始化图片加载
        ImageLoader.getInstance().init(this);
        EZPermission.Companion.init(this);
        Utils.init(this);
    }
}
