package com.example.achuan.bombtest.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.achuan.bombtest.app.App;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by achuan on 16-10-29.
 * 功能：无MVP的activity基类
 */
public abstract class SimpleActivity extends SupportActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());//设置布局文件
        ButterKnife.bind(this);//初始化加载控件
        App.getInstance().addActivity(this);
        initEventAndData();//初始化事件和数据
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.removeActivity(this);
    }

    //设置toolbar的方法,子类可以直接调用该方法
    protected void setToolBar(Toolbar toolbar, String title) {
        //设置标题名称
        toolbar.setTitle(title);
        //用自定义的toolbar取代原始的toolbar
        setSupportActionBar(toolbar);
        //给左上角图标的左边加上一个返回的图标,默认设置为false(不显示)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //对NavigationIcon添加点击
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    //设置接口方法,具体功能让子类来实现
    protected abstract int getLayout();
    protected abstract void initEventAndData();
}