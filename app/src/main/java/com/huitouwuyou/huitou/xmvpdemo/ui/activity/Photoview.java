package com.huitouwuyou.huitou.xmvpdemo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.huitouwuyou.huitou.xmvpdemo.R;
import com.huitouwuyou.huitou.xmvpdemo.adapter.VpAdapter;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by chenshuai on 2016/11/14.
 */
public class Photoview extends Activity {
    private Intent intent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(Photoview.this);
        setContentView(R.layout.photoview);
        initView();

    }

    private void initView() {
        intent = getIntent();
        String datas = intent.getStringExtra("cs");
        bundle = intent.getExtras();
//        String[] datas = (String[]) bundle.get("cs");
            PhotoView photoView = (PhotoView)findViewById(R.id.pv_photo);
//            final ProgressBar mProgressBar = (ProgressBar)findViewById(R.id.progress);

            Glide.with(Photoview.this)
                    .load(datas)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                            mProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                           // 这里可以设置进度条是否可见
//                            mProgressBar.setVisibility(View.GONE);
                           // Toast.makeText(Photoview.this, "我加载成功了", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(photoView);
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }

                @Override
                public void onOutsidePhotoTap() {

                }
            });





       /* Intent intent = getIntent();
        if (intent!=null) {
            String url = intent.getStringExtra("url");
            Log.d("TAG",url);
            Glide.with(this)
                    .load(url)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                           mProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_photo);
        }*/

    }


}
