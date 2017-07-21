package com.huitouwuyou.huitou.xmvpdemo.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.huitouwuyou.huitou.xmvpdemo.JsonCallback.LzyResponse;
import com.huitouwuyou.huitou.xmvpdemo.R;
import com.huitouwuyou.huitou.xmvpdemo.adapter.QuickAdapter;
import com.huitouwuyou.huitou.xmvpdemo.base.BaseFragment;
import com.huitouwuyou.huitou.xmvpdemo.base.BaseRxFragment;
import com.huitouwuyou.huitou.xmvpdemo.model.LoginModel;
import com.huitouwuyou.huitou.xmvpdemo.present.PLoadData;
import com.huitouwuyou.huitou.xmvpdemo.ui.activity.Photoview;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends BaseRxFragment<PLoadData> implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    QuickAdapter quickadapter;
    public int page = 0;
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        getP().loadData("福利", 10, page, new PLoadData.DataLoadCallback() {
            @Override
            public void onDataReady(Response<LzyResponse<List<LoginModel>>> listLzyResponse, int page) {
               LogUtils.d(listLzyResponse.body());
            }
        });
        initAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent=new Intent(getActivity(),Photoview.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pos",position);
                bundle.putString("cs",quickadapter.getData().get(position).getUrl().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        getP().loadData("福利", 10, 18)
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
                        addDisposable(d);
                    }
                    @Override
                    public void onNext(@NonNull List<LoginModel> loginModels) {
                        LogUtils.d(loginModels);
                            quickadapter.addData(loginModels);

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

    public void showDate(Response<LzyResponse<List<LoginModel>>> response){
        LogUtils.d(response.body());
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragmentitem;
    }

    @Override
    public PLoadData newP() {
        return new PLoadData();
    }

    public void showData(final LzyResponse<List<LoginModel>> model, int page) {
        LogUtils.d("MODE1" + model.toString());
        if (page == 0) {
            quickadapter.addData(model.results);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getP().loadData("福利", 10, ++page, new PLoadData.DataLoadCallback() {
                    @Override
                    public void onDataReady(Response<LzyResponse<List<LoginModel>>> listLzyResponse, int page) {

                        if (!Kits.Empty.check(listLzyResponse.body().results)) {
                            quickadapter.addData(listLzyResponse.body().results);
                            mSwipeRefreshLayout.setEnabled(true);
                            quickadapter.loadMoreComplete();
                        } else {
                            quickadapter.loadMoreEnd(false);
                        }
                    }
                });

            }
        }, 1000);


    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getP().loadData("福利", 10, page, new PLoadData.DataLoadCallback() {
                    @Override
                    public void onDataReady(Response<LzyResponse<List<LoginModel>>> listLzyResponse, int page) {
                        showData(listLzyResponse.body(), page);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


            }
        }, 1000);


    }

    private void initAdapter() {
        quickadapter = new QuickAdapter(new LzyResponse<List<LoginModel>>());
        quickadapter.setOnLoadMoreListener(this);
//      pullToRefreshAdapter.setAutoLoadMoreSize(3);
        mRecyclerView.setAdapter(quickadapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

}