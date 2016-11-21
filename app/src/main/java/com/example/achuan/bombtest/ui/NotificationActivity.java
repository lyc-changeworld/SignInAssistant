package com.example.achuan.bombtest.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.Constants;
import com.example.achuan.bombtest.base.SimpleActivity;

import butterknife.BindView;

/**
 * Created by achuan on 16-11-21.
 */
public class NotificationActivity extends SimpleActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_notifi_message)
    TextView mTvNotifiMessage;

    @Override
    protected int getLayout() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initEventAndData() {
        //初始化设置标题栏
        setToolBar(mToolbar, "消息通知");
        //关闭通知弹窗
        NotificationManager manager= (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(Constants.NotificationID);//通过id号取消通知弹窗
        //获取传递过来的消息
        Intent intent=getIntent();
        String msg=intent.getStringExtra("msg");
        mTvNotifiMessage.setText(msg);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
