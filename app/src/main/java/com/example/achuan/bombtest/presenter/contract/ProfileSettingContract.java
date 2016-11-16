package com.example.achuan.bombtest.presenter.contract;

import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;
import com.example.achuan.bombtest.model.bean.MyUser;

/**
 * Created by achuan on 16-11-13.
 */
public interface ProfileSettingContract {
    //view层接口方法
    interface View extends BaseView {
        //显示当前用户的信息
        void showNetUserContent(MyUser myUser);//有网时显示
        void showLocalUserContent();//无网时显示
        //显示和隐藏进度条
        void showLoading();
        void hideLoading();
    }
    //presenter层接口方法
    interface  Presenter extends BasePresenter<View> {
        //根据用户名获取用户对象
        void getUserObject(String userName);
        //根据键值更新用户信息
        void updateUserInfoByKey(String id,String key,Object value);

    }

}
