package com.huitouwuyou.huitou.xmvpdemo.ui.activity;


import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.apkfuns.logutils.LogUtils;
import com.dou361.update.UpdateHelper;
import com.dou361.update.listener.UpdateListener;
import com.huitouwuyou.huitou.xmvpdemo.R;
import com.huitouwuyou.huitou.xmvpdemo.present.PMainActivityData;
import com.huitouwuyou.huitou.xmvpdemo.ui.fragment.BaseFragment;
import com.huitouwuyou.huitou.xmvpdemo.ui.fragment.HomeFragment;
import com.huitouwuyou.huitou.xmvpdemo.ui.fragment.OneFrament;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import qiu.niorgai.StatusBarCompat;
import rx.functions.Action1;

@Route(path = "/mvp/MainActivity")
public  class MainActivity extends XActivity<PMainActivityData> implements HomeFragment.CallBack {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"首页", "干货", "妹子"};
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    XFragmentAdapter adapter;
    private Context mContext;
    private boolean isAutoUpdate;//更新标识


    @Override
    public void initData(Bundle savedInstanceState) {

        setSupportActionBar(toolbar);
        mContext = this;
        getPersimmions();
        StatusBarCompat.setStatusBarColor(MainActivity.this, getResources().getColor(R.color.colorPrimary));
        fragmentList.clear();

        HomeFragment h1=HomeFragment.newInstance();

        fragmentList.add(OneFrament.newInstance());
        fragmentList.add(h1);
//        fragmentList.add(h1);
        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public PMainActivityData newP() {
        return new PMainActivityData();
    }

    private void checkUpdate() {
        getP().update( new PMainActivityData.UpDateCallback() {
            @Override
            public void onUpDateReady(Response<String> listLzyResponse) {
                LogUtils.e(listLzyResponse.body());
                isAutoUpdate = false;
                UpdateHelper.getInstance()
                        .setRequestResultData(listLzyResponse.body())
                        .setUpdateListener(new UpdateListener() {
                            @Override
                            public void noUpdate() {
                                if (!isAutoUpdate) {
                                    Toast.makeText(mContext, "已经是最新版本了", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCheckError(int code, String errorMsg) {
                                if (!isAutoUpdate) {
                                    Toast.makeText(mContext, "检测更新失败：" + errorMsg, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onUserCancel() {
                                if (!isAutoUpdate) {
                                    Toast.makeText(mContext, "用户取消", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .checkNoUrl(MainActivity.this);

            }
        });

    }


    private void getPersimmions() {
        getRxPermissions().request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) { // 在android 6.0之前会默认返回true
                    // 已经获取权限
                    checkUpdate();
                } else {
                    // 未获取权限
                    Toast.makeText(getApplicationContext(),"获取权限失败！",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private static boolean mBackKeyPressed = false;//记录是否有首次按键
    @Override
    public void onBackPressed() {
        if(!mBackKeyPressed){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                    }
                }, 2000);
            }
        else{//退出程序
            this.finish();
            System.exit(0);
            }
    }


    @Override
    public void showMessage(String message) {
   Toast.makeText(getApplication(),message,Toast.LENGTH_SHORT).show();
    }
}
