package com.example.achuan.bombtest.presenter.contract;


import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;

/**
 * Created by codeest on 16/8/15.
 */

public interface WelcomeContract {

    interface View extends BaseView {

        //void showContent(WelcomeBean welcomeBean);
        void jumpToMain();

    }

    interface  Presenter extends BasePresenter<View> {

        void getWelcomeData();

    }
}
