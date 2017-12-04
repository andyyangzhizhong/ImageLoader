package cn.andy.study.imageloader.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.andy.study.imageloader.ImageUrls;
import cn.andy.study.imageloader.R;

/**
 * Created by yangzhizhong
 */

public class FBActivity extends Activity {

    private List<String> uriList;
    private Button picasso;
    private Button fb;
    private Button myself;
    private Button glide;

    private boolean isPicasso = true;
    private boolean isFb = true;
    private boolean isMySelf = true;
    private boolean isGlide = true;
    private ImageAdapterFB imageAdapterFB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb);
        initData();
        initView();
        initOnClickListener();
    }

    private void initOnClickListener() {
        picasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPicasso) {
                    isPicasso = false;
                } else {
                    isPicasso = true;
                }
                imageAdapterFB.setPicasso(isPicasso);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFb) {
                    isFb = false;
                } else {
                    isFb = true;
                }
                imageAdapterFB.setFb(isFb);
            }
        });

        myself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMySelf) {
                    isMySelf = false;
                } else {
                    isMySelf = true;
                }
                imageAdapterFB.setMySelf(isMySelf);
            }
        });

        glide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGlide) {
                    isGlide = false;
                } else {
                    isGlide = true;
                }
                imageAdapterFB.setGlide(isGlide);
            }
        });

    }

    private void initData() {
        uriList = new ArrayList<>();
        uriList.addAll(Arrays.asList(ImageUrls.imageUrls));
    }

    private void initView() {
        GridView gvFb = findViewById(R.id.gv_fb);
        picasso = findViewById(R.id.picasso);
        fb = findViewById(R.id.fb);
        myself = findViewById(R.id.myself);
        glide = findViewById(R.id.glide);
        imageAdapterFB = new ImageAdapterFB(getApplication(), uriList);
        gvFb.setAdapter(imageAdapterFB);
    }
}
