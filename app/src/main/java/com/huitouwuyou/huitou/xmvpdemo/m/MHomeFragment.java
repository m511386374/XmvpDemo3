package com.huitouwuyou.huitou.xmvpdemo.m;

import com.apkfuns.logutils.LogUtils;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonCallback;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonConvert;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.LzyResponse;
import com.huitouwuyou.huitou.xmvpdemo.adapter.QuickAdapter;
import com.huitouwuyou.huitou.xmvpdemo.model.LoginModel;
import com.huitouwuyou.huitou.xmvpdemo.present.PLoadData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Nick.Ming on 2017/8/9.
 */

public class MHomeFragment {

    public static final String API_BASE_URL = "http://gank.io/api/data/";
    public Observable<Response<LzyResponse<List<LoginModel>>>> loadData(String code , int count, final int page) {
        return OkGo.<LzyResponse<List<LoginModel>>>get(API_BASE_URL+code+"/"+count+""+"/"+page+"")//
                .cacheKey("Home")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .converter(new JsonConvert<LzyResponse<List<LoginModel>>>(){})//
                .adapt(new ObservableResponse<LzyResponse<List<LoginModel>>>());
    }
}
