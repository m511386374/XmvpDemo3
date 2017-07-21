package com.huitouwuyou.huitou.xmvpdemo.present;

import com.apkfuns.logutils.LogUtils;
import com.huitouwuyou.huitou.xmvpdemo.ui.activity.MainActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import cn.droidlover.xdroidmvp.mvp.XPresent;

/**
 * Created by nick.ming on 2017/2/10.
 */

    public class PMainActivityData extends XPresent<MainActivity> {
    private static String baseUrl1 = "http://123.57.28.18:8080/seastation";
    private static String checkUrl1 = "/queryVersionMess.action";

    public void update (final UpDateCallback callback) {
        OkGo.<String>get(baseUrl1 + checkUrl1)//
                .cacheKey("MainActivity")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.NO_CACHE)    // 缓存模式，详细请看缓存介绍
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LogUtils.d(response.body());
                        if (callback != null) callback.onUpDateReady(response);
                    }
                        @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if(!response.isFromCache()){
                            LogUtils.d("缓存失败");
                        }
                    }
                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);
//                        LogUtils.d("缓存成功");
                        if (callback != null) callback.onUpDateReady(response);
                    }
                });
     }



    public interface UpDateCallback{
        void onUpDateReady(Response<String> listLzyResponse);
    }
}
