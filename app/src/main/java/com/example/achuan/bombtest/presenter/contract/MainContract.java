package com.example.achuan.bombtest.presenter.contract;

import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;

/**
 * Created by achuan on 16-10-29.
 * 功能：
 */
public interface MainContract {
    //view层接口方法
    interface View extends BaseView {

    }
    //presenter层接口方法
    interface  Presenter extends BasePresenter<View> {

    }
}
