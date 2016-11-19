package com.example.achuan.bombtest.presenter.contract;

import com.example.achuan.bombtest.base.BasePresenter;
import com.example.achuan.bombtest.base.BaseView;

import java.io.File;

/**
 * Created by achuan on 16-10-29.
 * 功能：
 */
public interface MainContract {
    //view层接口方法
    interface View extends BaseView {
        //下载网络图片后显示在控件上
        void showDownloadFileSuccess(String path);
    }
    //presenter层接口方法
    interface  Presenter extends BasePresenter<View> {
        //下载图片
        void downloadFile(String fileName, String group,
                          String headUrl,File saveFile);
    }
}
