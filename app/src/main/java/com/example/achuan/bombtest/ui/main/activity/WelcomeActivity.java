package com.example.achuan.bombtest.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.BaseActivity;
import com.example.achuan.bombtest.presenter.WelcomePresenter;
import com.example.achuan.bombtest.presenter.contract.WelcomeContract;

public class WelcomeActivity extends BaseActivity<WelcomePresenter>
        implements WelcomeContract.View {

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_welcome;
    }
    @Override
    protected void initEventAndData() {
        mPresenter.getWelcomeData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    //跳转到主界面的方法
    @Override
    public void jumpToMain() {
        /*****延时后跳转到登陆界面*****/
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /*//进行判断,如果之前已经登录过了,打开应用时将直接登录
                BmobUser bmobUser = BmobUser.getCurrentUser();
                if(bmobUser != null){
                    // 允许用户使用应用
                    Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    //缓存用户对象为空时， 可打开用户注册界面…
                    Intent mIntent=new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(mIntent);
                }*/
                Intent intent=new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                WelcomeActivity.this.finish();//结束当前activity
            }
        },3000);//3秒
    }
}
