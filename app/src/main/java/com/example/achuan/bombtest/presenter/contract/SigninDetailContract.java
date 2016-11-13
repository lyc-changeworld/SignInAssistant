package com.example.achuan.bombtest.presenter.contract;

import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;
import com.example.achuan.bombtest.model.bean.TeacherBean;

import java.util.List;

/**
 * Created by achuan on 16-11-11.
 */
public interface SigninDetailContract {
    //view层接口方法
    interface View extends BaseView {
        //显示列表内容的方法
        void showContent(List<TeacherBean> mList);
        //显示签到成功
        void showSigninSuccess(String message);
    }
    //presenter层接口方法
    interface  Presenter extends BasePresenter<View> {
        //获取课程对应的任课老师的数据
        void getTeacherData();
        //签到处理
        void signinDeal(String Sno,String Cno,String Tno);
    }
}
