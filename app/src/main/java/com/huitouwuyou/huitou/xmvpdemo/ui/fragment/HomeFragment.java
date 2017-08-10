package com.huitouwuyou.huitou.xmvpdemo.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

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
import com.huitouwuyou.huitou.xmvpdemo.ui.activity.MainActivity;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class HomeFragment extends BaseRxFragment<PLoadData> implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;
    QuickAdapter quickadapter;
    public int page = 0;
    public  CallBack  callBack;
    public interface CallBack{
        void showMessage(String message);
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        callBack.showMessage("11111111111111111");
        getP().loadData("福利", 10, page,false);
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

    public void setAdapter(QuickAdapter quickadapter){
        this.quickadapter=quickadapter;
        quickadapter.setOnLoadMoreListener(this);
//     pullToRefreshAdapter.setAutoLoadMoreSize(3);
        mRecyclerView.setAdapter(quickadapter);
       mSwipeRefreshLayout.setOnRefreshListener(this);

    }
    @Override
    public int getLayoutId() {
        return R.layout.fragmentitem;
    }

    @Override
    public PLoadData newP() {
        return new PLoadData();
    }

    @Override
    public void onLoadMoreRequested() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getP().loadData("福利", 10, ++page,true);
                mSwipeRefreshLayout.setEnabled(true);
                quickadapter.loadMoreComplete();
            }
        }, 1000);

    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getP().loadData("福利", 10, 0,false);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
           callBack = ((MainActivity)context);
        }
    }

}