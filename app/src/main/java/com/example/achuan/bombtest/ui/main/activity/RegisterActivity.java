package com.example.achuan.bombtest.ui.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.base.SimpleActivity;
import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.util.BmobUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by achuan on 16-10-24.
 * 功能：用户注册
 * 注册后将直接进行登录
 */
public class RegisterActivity extends SimpleActivity {

    @BindView(R.id.id_et_firstPassword)
    EditText mIdEtFirstPassword;
    @BindView(R.id.id_et_confirmPassword)
    EditText mIdEtConfirmPassword;
    @BindView(R.id.id_bt_register)
    Button mIdBtRegister;
    @BindView(R.id.id_cb_agree)
    CheckBox mIdCbAgree;
    @BindView(R.id.id_tv_ruleinfo)
    TextView mIdTvRuleinfo;
    @BindView(R.id.id_tv_exitUser_login)
    TextView mIdTvExitUserLogin;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEventAndData() {
        //初始化设置标题栏
        setToolBar(mToolbar,"注册");
        //对只有在用户名和密码的输入都不为空的情况下，button按钮才显示有效，
        // 可以自己构造一个TextChange的类，实现一个TextWatcher接口，
        // 里面有三个函数可以实现对所有text的监听。
        TextChange textChange = new TextChange();
        mIdEtFirstPassword.addTextChangedListener(textChange);
        mIdEtConfirmPassword.addTextChangedListener(textChange);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @OnClick({R.id.id_bt_register, R.id.id_cb_agree, R.id.id_tv_ruleinfo, R.id.id_tv_exitUser_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_bt_register:
                //获取上个activity传递过来的电话号码
                String phone = getIntent().getStringExtra("phone");
                registerDeal(phone);//注册处理
                break;
            case R.id.id_cb_agree:
                //同意协议后,才让注册按钮变亮
                if (mIdEtFirstPassword.length() > 0 &&
                        mIdEtConfirmPassword.length() > 0 &&
                        mIdCbAgree.isChecked()) {
                    mIdBtRegister.setBackgroundResource(R.drawable.bt_bg_blue);
                    mIdBtRegister.setEnabled(true);//设置按钮可以点击使用
                } else {
                    mIdBtRegister.setBackgroundResource(R.drawable.bt_bg_white);
                    mIdBtRegister.setEnabled(false);
                }
                break;
            case R.id.id_tv_exitUser_login:
                //跳转到登录界面
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                //RegisterActivity.this.finish();//销毁当前activity
                break;
            case R.id.id_tv_ruleinfo:
                //介绍使用条款和隐私政策
                break;
        }
    }

    //创建一个多editext的输入监听类
    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mIdEtFirstPassword.length() > 0 &&
                    mIdEtConfirmPassword.length() > 0 &&
                    mIdCbAgree.isChecked()) {
                mIdBtRegister.setBackgroundResource(R.drawable.bt_bg_blue);
                mIdBtRegister.setEnabled(true);//设置按钮可以点击使用
            } else {
                mIdBtRegister.setBackgroundResource(R.drawable.bt_bg_white);
                mIdBtRegister.setEnabled(false);
            }
        }
    }

    //注册处理的方法实现
    private void registerDeal(final String registerName) {
        final String firstPassword = mIdEtFirstPassword.getText().toString().trim();//登录密码
        final String confirmPassword = mIdEtConfirmPassword.getText().toString().trim();//确认密码
        //最先执行查询操作
        BmobUtil.userQuery(registerName)
                .findObjects(new FindListener<MyUser>() {
                    @Override
                    public void done(List<MyUser> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                //存在,提示该用户已经存在
                                Toast.makeText(
                                        RegisterActivity.this,//在该activity显示
                                        "该手机号已经注册...",//显示的内容
                                        Toast.LENGTH_SHORT).show();//显示的格式
                            } else {
                                //不存在,接着判断
                                //接着判断两次输入的密码是否相同
                                if (firstPassword.equals(confirmPassword)) {
                                    //开始执行注册操作
                                    BmobUtil.userSignUp(registerName, firstPassword)
                                            .signUp(new SaveListener<BmobUser>() {
                                                @Override
                                                public void done(BmobUser bmobUser, BmobException e) {
                                                    if (e == null) {
                                                        Toast.makeText(
                                                                RegisterActivity.this,//在该activity显示
                                                                "注册成功",//显示的内容
                                                                Toast.LENGTH_SHORT).show();//显示的格式
                                                        /*****登录成功后关闭登录界面*****/
                                                        //更新登录的全局变量
                                                        App.getInstance().setIsLogin(true);
                                                        App.getInstance().setBmobUser(BmobUser.getCurrentUser(MyUser.class));
                                                        RegisterActivity.this.finish();//跳转后清除内存
                                                        LoginActivity.getInstance().finish();//销毁之前保存的登录界面
                                                        /*new Handler().postDelayed(new Runnable() {
                                                            public void run() {
                                                            }
                                                        }, 1000);//延时1秒*/
                                                    } else {
                                                        Toast.makeText(
                                                                RegisterActivity.this,//在该activity显示
                                                                "注册失败:" + e.getMessage(),//显示的内容
                                                                Toast.LENGTH_SHORT).show();//显示的格式
                                                    }
                                                }
                                            });
                                } else {//不相同
                                    Toast.makeText(
                                            RegisterActivity.this,//在该activity显示
                                            "两次密码不同,请重新输入！",//显示的内容
                                            Toast.LENGTH_SHORT).show();//显示的格式
                                }
                            }
                        } else {
                            Toast.makeText(
                                    RegisterActivity.this,//在该activity显示
                                    "查询账号信息失败:" + e.getMessage(),//显示的内容
                                    Toast.LENGTH_SHORT).show();//显示的格式
                        }
                    }
                });
    }
}
