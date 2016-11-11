package com.example.achuan.bombtest.presenter;

import android.widget.Toast;

import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.base.RxPresenter;
import com.example.achuan.bombtest.model.bean.CourseBean;
import com.example.achuan.bombtest.presenter.contract.SigninContract;
import com.example.achuan.bombtest.util.BmobUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by achuan on 16-11-3.
 */
public class SigninPresenter extends RxPresenter<SigninContract.View>
        implements SigninContract.Presenter {


    //接口实现网络端全部课程数据的加载
    @Override
    public void getCourseData() {
        BmobUtil.courseBmobQueryAll().findObjects(new FindListener<CourseBean>() {
            @Override
            public void done(List<CourseBean> list, BmobException e) {
                if(e==null){
                    mView.showContent(list);
                }else{
                    mView.showError("数据加载失败ヽ(≧Д≦)ノ");
                    //LogUtil.d("lyc-bmob",e.getMessage());
                }
            }
        });
    }
    //接口实现根据关键字进行模糊查询数据的方法
    @Override
    public void getSearchCourseData(String keyword) {
        BmobUtil.courseBmobQueryFromKeyword(keyword).findObjects(new FindListener<CourseBean>() {
            @Override
            public void done(List<CourseBean> list, BmobException e) {
                if(e==null){
                    mView.showContent(list);
                }else{
                    //LogUtil.d("lyc-bmob",e.getMessage());
                    Toast.makeText(App.getInstance().getContext(),
                            "课程加载失败"+e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
