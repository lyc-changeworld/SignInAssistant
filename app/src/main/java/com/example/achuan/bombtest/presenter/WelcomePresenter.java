package com.example.achuan.bombtest.presenter;

import com.example.achuan.bombtest.base.RxPresenter;
import com.example.achuan.bombtest.presenter.contract.WelcomeContract;

/**
 * Created by achuan on 16-10-29.
 */
public class WelcomePresenter extends RxPresenter<WelcomeContract.View>
        implements WelcomeContract.Presenter {


    @Override
    public void getWelcomeData() {
        mView.jumpToMain();
    }
}
