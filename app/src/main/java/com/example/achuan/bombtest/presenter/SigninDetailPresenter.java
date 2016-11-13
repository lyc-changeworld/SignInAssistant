package com.example.achuan.bombtest.presenter;

import com.example.achuan.bombtest.base.RxPresenter;
import com.example.achuan.bombtest.model.bean.TeacherBean;
import com.example.achuan.bombtest.presenter.contract.SigninDetailContract;
import com.example.achuan.bombtest.util.BmobUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by achuan on 16-11-12.
 */
public class SigninDetailPresenter extends RxPresenter<SigninDetailContract.View> implements SigninDetailContract.Presenter {


    //获取该课程对应的老师的数据
    @Override
    public void getTeacherData() {
        BmobUtil.teacherBmobQueryAll().findObjects(new FindListener<TeacherBean>() {
            @Override
            public void done(List<TeacherBean> list, BmobException e) {
                if(e==null){
                    mView.showContent(list);
                }else{
                    mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    //LogUtil.d("lyc-bmob",e.getMessage());
                }
            }
        });
    }

    //签到处理
    @Override
    public void signinDeal() {

    }
}
