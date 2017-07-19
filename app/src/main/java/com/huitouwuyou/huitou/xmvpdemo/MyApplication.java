package com.huitouwuyou.huitou.xmvpdemo;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.utils.Utils;
import com.huitouwuyou.huitou.xmvpdemo.net.okGo;
import com.huitouwuyou.huitou.xmvpdemo.update.UpdateConfig;

import cn.droidlover.xdroidmvp.imageloader.ILFactory;

/**
 * Created by Nick.Ming on 2017/2/16.
 */
public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        UpdateConfig.initGet(this);
        okGo.initOkgo(this);
        ILFactory.getLoader().init(context);
        Utils.init(context);
        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this); // 尽可

        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("MyAppName")
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")
                .configLevel(LogLevel.TYPE_VERBOSE);
    }

    public static Context getContext() {
        return context;
    }
}
