package com.example.achuan.bombtest.presenter.contract;

import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;

/**
 * Created by achuan on 16-11-11.
 */
public interface SigninDetailContract {
    //view层接口方法
    interface View extends BaseView {
        //显示签到成功
        void showSigninSuccess();
    }
    //presenter层接口方法
    interface  Presenter extends BasePresenter<View> {
        //签到处理
        void signinDeal();
    }
}
