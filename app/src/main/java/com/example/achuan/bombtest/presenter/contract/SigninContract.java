package com.example.achuan.bombtest.presenter.contract;

import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;
import com.example.achuan.bombtest.model.bean.CourseBean;

import java.util.List;

/**
 * Created by achuan on 16-11-3.
 */
public interface SigninContract {
    //view层接口方法
    interface View extends BaseView {
        //显示列表内容的方法
        void showContent(List<CourseBean> mList);
    }
    //presenter层接口方法
    interface  Presenter extends BasePresenter<View> {
        //获取网络端全部的课程数据
        void getCourseData();
        //根据关键字获取数据集合
        void getSearchCourseData(String keyword);
    }
}
