package com.huitouwuyou.huitou.xmvpdemo.base;

import cn.droidlover.xdroidmvp.mvp.IPresent;
import cn.droidlover.xdroidmvp.mvp.XLazyFragment;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * Created by YoKeyword on 16/2/3.
 */
public abstract class BaseFragment<P extends IPresent> extends XLazyFragment<P> {

    public void startActivity(Class activity){
        Router.newIntent(getActivity())
                .to(activity)
                .launch();
    }

}
