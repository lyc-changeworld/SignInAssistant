package com.example.achuan.bombtest.ui.assistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.SimpleActivity;

import butterknife.BindView;

/**
 * Created by achuan on 16-11-7.
 * 功能：点击item后打开的活动界面
 */
public class SigninDetailActivity extends SimpleActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    String title;

    @Override
    protected int getLayout() {
        return R.layout.activity_assistant_detail;
    }

    @Override
    protected void initEventAndData() {
        //获取上个活动传递过来的意图对象
        Intent intent = getIntent();
        title=intent.getExtras().getString("title");
        setToolBar(mToolbar,title);


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


}
