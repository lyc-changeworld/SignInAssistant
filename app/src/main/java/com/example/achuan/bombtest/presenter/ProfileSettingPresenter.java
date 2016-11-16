package com.example.achuan.bombtest.presenter;

import com.example.achuan.bombtest.base.RxPresenter;
import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.presenter.contract.ProfileSettingContract;
import com.example.achuan.bombtest.util.BmobUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by achuan on 16-11-13.
 */
public class ProfileSettingPresenter extends RxPresenter<ProfileSettingContract
        .View> implements ProfileSettingContract.Presenter{


    @Override
    public void getUserObject(String userName) {
        mView.showLoading();
        BmobUtil.userQuery(userName).findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                mView.hideLoading();
                if(e==null){
                    if(list.size()>0){
                        mView.showNetUserContent(list.get(0));
                    }else {
                        mView.showError("当前用户不存在ヽ(≧Д≦)ノ");
                    }
                }else {
                    //网络加载失败时,显示本地用户的缓存信息
                    mView.showLocalUserContent();
                    mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                }
            }
        });
    }

    @Override
    public void updateUserInfoByKey(String id,String key, Object value) {
        /*开始更新,显示进度对话框*/
        mView.showLoading();
        //执行更新操作
        BmobUtil.userBmobUpdate(key,value).update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                //关闭进度对话框
                mView.hideLoading();
                if(e==null){
                }else {
                    mView.showError("数据更新失败ヽ(≧Д≦)ノ");
                    //mView.showError(e.getMessage());
                }
            }
        });
    }
}
