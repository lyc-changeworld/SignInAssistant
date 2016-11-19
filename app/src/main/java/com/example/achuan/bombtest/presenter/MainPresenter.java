package com.example.achuan.bombtest.presenter;

import com.example.achuan.bombtest.base.RxPresenter;
import com.example.achuan.bombtest.presenter.contract.MainContract;
import com.example.achuan.bombtest.util.BmobUtil;

import java.io.File;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by achuan on 16-10-29.
 */
public class MainPresenter extends RxPresenter<MainContract.View>
        implements MainContract.Presenter{


    @Override
    public void downloadFile(String fileName, String group, String headUrl, File saveFile) {
        BmobUtil.fileBmobDownload(fileName,group,headUrl).
                download(saveFile, new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            mView.showDownloadFileSuccess(s);
                        }else {
                            mView.showError(e.getMessage());
                            //LogUtil.d("lyc-changeworld",);
                        }
                    }
                    @Override
                    public void onProgress(Integer integer, long l) {
                    }
                });
    }
}
