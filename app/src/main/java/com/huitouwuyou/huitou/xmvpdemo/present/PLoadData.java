package com.huitouwuyou.huitou.xmvpdemo.present;

import com.apkfuns.logutils.LogUtils;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonCallback;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonConvert;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.LzyResponse;
import com.huitouwuyou.huitou.xmvpdemo.model.LoginModel;
import com.huitouwuyou.huitou.xmvpdemo.ui.fragment.HomeFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import io.reactivex.Observable;

/**
 * Created by nick.ming on 2017/2/10.
 * HomeFragment数据获取
 */

    public class PLoadData extends XPresent<HomeFragment> {
      public static final String API_BASE_URL = "http://gank.io/api/data/";

        public void loadData(String code , int count, final int page, final DataLoadCallback callback) {
            OkGo.<LzyResponse<List<LoginModel>>>get(API_BASE_URL+code+"/"+count+""+"/"+page+"")//
                    .cacheKey("HomeFragment")
                    .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                    .execute(new JsonCallback<LzyResponse<List<LoginModel>>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<List<LoginModel>>> response) {
                            if (callback!=null) callback.onDataReady(response,page);
                        }
                        @Override
                        public void onCacheSuccess(Response<LzyResponse<List<LoginModel>>> response) {
                            super.onCacheSuccess(response);
                            LogUtils.d("缓存成功");
                            if (callback!=null) callback.onDataReady(response,page);
                        }
                        @Override
                        public void onError(Response<LzyResponse<List<LoginModel>>> response) {
                            super.onError(response);

                            if (!response.isFromCache()) {
                                LogUtils.d("缓存失败");
                            }
                        }

                    });

        }

    public void loadHomeData(String code , int count, final int page) {
        OkGo.<LzyResponse<List<LoginModel>>>get(API_BASE_URL+code+"/"+count+""+"/"+page+"")//
                .cacheKey("HomeFragment")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new JsonCallback<LzyResponse<List<LoginModel>>>() {
                    @Override
                    public void onSuccess(Response<LzyResponse<List<LoginModel>>> response) {
                       getV().showDate(response);
                    }
                    @Override
                    public void onCacheSuccess(Response<LzyResponse<List<LoginModel>>> response) {
                        super.onCacheSuccess(response);
                        LogUtils.d("缓存成功");
                    }
                    @Override
                    public void onError(Response<LzyResponse<List<LoginModel>>> response) {
                        super.onError(response);
                        if (!response.isFromCache()) {
                            LogUtils.d("缓存失败");
                        }
                    }

                });

    }


    public  Observable<Response<LzyResponse<List<LoginModel>>>> loadData(String code , int count, final int page) {
          return OkGo.<LzyResponse<List<LoginModel>>>get(API_BASE_URL+code+"/"+count+""+"/"+page+"")//
                  .cacheKey("Home")
                  .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                  .converter(new JsonConvert<LzyResponse<List<LoginModel>>>(){})//
                  .adapt(new ObservableResponse<LzyResponse<List<LoginModel>>>());
    }



    public interface DataLoadCallback{
        void onDataReady(Response<LzyResponse<List<LoginModel>>> listLzyResponse, int page);
    }

}
