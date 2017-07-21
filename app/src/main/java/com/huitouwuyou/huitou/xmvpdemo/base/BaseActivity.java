package com.huitouwuyou.huitou.xmvpdemo.base;

import android.os.Bundle;
import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import qiu.niorgai.StatusBarCompat;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/10/1
 * 描    述：统一管理所有的订阅生命周期
 * 修订历史：
 * ================================================
 */
public abstract class BaseActivity<P extends IPresent> extends XActivity<P> {

    @Override
    public void initData(Bundle savedInstanceState) {

    }


}
