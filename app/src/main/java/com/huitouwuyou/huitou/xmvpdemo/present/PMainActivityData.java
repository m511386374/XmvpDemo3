package com.huitouwuyou.huitou.xmvpdemo.present;

import com.apkfuns.logutils.LogUtils;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonCallback;
import com.huitouwuyou.huitou.xmvpdemo.model.LoginModel;
import com.huitouwuyou.huitou.xmvpdemo.model.LzyResponse;
import com.huitouwuyou.huitou.xmvpdemo.ui.fragment.HomeFragment;

import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import cn.droidlover.xdroidmvp.net.OkGo;
import cn.droidlover.xdroidmvp.net.cache.CacheMode;
import cn.droidlover.xdroidmvp.net.callback.StringCallback;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by nick.ming on 2017/2/10.
 */

    public class PMainActivityData extends XPresent<HomeFragment> {
    private static String baseUrl = "http://123.57.28.18:8080/seastation";
    private static String checkUrl = "/queryVersionMess.action";
        public void update (final UpDateCallback callback) {
            OkGo.get(baseUrl+checkUrl)     // 请求方式和请求url
                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                    .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            // s 即为所需要的结果
                            if (callback!=null) callback.onUpDateReady(s);
                        }
                    });

        }


    public interface UpDateCallback{
        void onUpDateReady(String listLzyResponse);
    }

}
