package com.huitouwuyou.huitou.xmvpdemo.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemChildLongClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.huitouwuyou.huitou.xmvpdemo.R;
import com.huitouwuyou.huitou.xmvpdemo.adapter.QuickAdapter;
import com.huitouwuyou.huitou.xmvpdemo.base.BaseFragment;
import com.huitouwuyou.huitou.xmvpdemo.model.LoginModel;
import com.huitouwuyou.huitou.xmvpdemo.model.LzyResponse;
import com.huitouwuyou.huitou.xmvpdemo.present.PLoadData;
import com.huitouwuyou.huitou.xmvpdemo.ui.activity.Photoview;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.Kits;


public class HomeFragment extends BaseFragment<PLoadData> implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
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
            public void onDataReady(LzyResponse<List<LoginModel>> model, int page) {
                showData(model, page);
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
                    public void onDataReady(LzyResponse<List<LoginModel>> model, int page) {
                        if (!Kits.Empty.check(model.results)) {
                            quickadapter.addData(model.results);
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
                    public void onDataReady(LzyResponse<List<LoginModel>> model, int page) {
                        showData(model, page);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


            }
        }, 1000);


    }

    private void initAdapter() {
        quickadapter = new QuickAdapter(new LzyResponse<List<LoginModel>>());
        quickadapter.setOnLoadMoreListener(this);
//        pullToRefreshAdapter.setAutoLoadMoreSize(3);
        mRecyclerView.setAdapter(quickadapter);

    }


}