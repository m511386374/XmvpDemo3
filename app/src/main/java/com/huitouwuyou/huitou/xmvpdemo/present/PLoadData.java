package com.huitouwuyou.huitou.xmvpdemo.present;

import com.apkfuns.logutils.LogUtils;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonCallback;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.JsonConvert;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.LzyResponse;
import com.huitouwuyou.huitou.xmvpdemo.adapter.QuickAdapter;
import com.huitouwuyou.huitou.xmvpdemo.m.MHomeFragment;
import com.huitouwuyou.huitou.xmvpdemo.model.LoginModel;
import com.huitouwuyou.huitou.xmvpdemo.ui.fragment.HomeFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.util.List;

import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nick.ming on 2017/2/10.
 * HomeFragment数据获取
 */

    public class PLoadData extends XPresent<HomeFragment> {
      public static final String API_BASE_URL = "http://gank.io/api/data/";
      MHomeFragment model=new MHomeFragment();
      private QuickAdapter quickadapter;

        public void loadData(String code , int count, int page, final Boolean pullToRefresh) {
              model.loadData(code,count,page)
                      .subscribeOn(Schedulers.io())
                      .doOnSubscribe(new Consumer<Disposable>() {
                          @Override
                          public void accept(@NonNull Disposable disposable) throws Exception {
                              LogUtils.d("请求中");
                          }
                      })
                      .map(new Function<Response<LzyResponse<List<LoginModel>>>, List<LoginModel>>() {
                          @Override
                          public List<LoginModel> apply(@NonNull Response<LzyResponse<List<LoginModel>>> lzyResponseResponse) throws Exception {
                              LogUtils.d(lzyResponseResponse.isFromCache());
                              return lzyResponseResponse.body().results;
                          }
                      })
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(new Observer<List<LoginModel>>() {
                          @Override
                          public void onSubscribe(@NonNull Disposable d) {
                              getV().addDisposable(d);
                          }
                          @Override
                          public void onNext(@NonNull List<LoginModel> loginModels) {

                              if (pullToRefresh){
                                  if (!Kits.Empty.check(loginModels)) {
                                      quickadapter.addData(loginModels);
                                      quickadapter.loadMoreComplete();
                                  } else {
                                      quickadapter.loadMoreEnd(false);
                                  }
                              }else {
                                  quickadapter = new QuickAdapter(loginModels);
                                  getV().setAdapter(quickadapter);
                                  LogUtils.d(loginModels);
                              }

                          }
                          @Override
                          public void onError(@NonNull Throwable e) {
                          }
                          @Override
                          public void onComplete() {
                              LogUtils.d("请求结束");
                          }
                      });



        }



//    public  Observable<Response<LzyResponse<List<LoginModel>>>> loadData(String code , int count, final int page) {
//          return OkGo.<LzyResponse<List<LoginModel>>>get(API_BASE_URL+code+"/"+count+""+"/"+page+"")//
//                  .cacheKey("Home")
//                  .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
//                  .converter(new JsonConvert<LzyResponse<List<LoginModel>>>(){})//
//                  .adapt(new ObservableResponse<LzyResponse<List<LoginModel>>>());
//    }



//    public interface DataLoadCallback{
//
//        void onDataReady(Response<LzyResponse<List<LoginModel>>> response, int page);
//
//    }

}
