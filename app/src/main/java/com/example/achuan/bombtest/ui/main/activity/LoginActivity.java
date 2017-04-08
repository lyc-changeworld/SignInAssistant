package com.example.achuan.bombtest.ui.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.base.SimpleActivity;
import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.model.http.BmobHelper;
import com.example.achuan.bombtest.util.FileUtil;
import com.example.achuan.bombtest.util.MobUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by achuan on 16-10-24.
 * 功能：用户登录界面实现
 */
public class LoginActivity extends SimpleActivity {

    private static LoginActivity instance;

    @BindView(R.id.id_et_AccName)
    EditText mIdEtAccName;
    @BindView(R.id.id_et_AccPassword)
    EditText mIdEtAccPassword;
    @BindView(R.id.id_bt_login)
    Button mIdBtLogin;
    @BindView(R.id.id_tv_forgetPassword)
    TextView mIdTvForgetPassword;
    @BindView(R.id.id_tv_newUser)
    TextView mIdTvNewUser;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    public static synchronized LoginActivity getInstance() {
        return instance;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        //初始化设置标题栏
        setToolBar(mToolbar,"登录");
        instance = this;
        //对只有在用户名和密码的输入都不为空的情况下，button按钮才显示有效，
        // 可以自己构造一个TextChange的类，实现一个TextWatcher接口，
        // 里面有三个函数可以实现对所有text的监听。
        TextChange textChange = new TextChange();
        mIdEtAccName.addTextChangedListener(textChange);
        mIdEtAccPassword.addTextChangedListener(textChange);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.id_bt_login, R.id.id_tv_forgetPassword, R.id.id_tv_newUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_bt_login:
                loginDeal();//登录处理
                break;
            case R.id.id_tv_forgetPassword:


                break;
            case R.id.id_tv_newUser:
                //跳转到mob的短信验证界面
                MobUtil.registerBySms(LoginActivity.this);
                //等到短信验证成功并跳转到注册界面才将当前创建的loginActivity销毁
                /*Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);*/
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
            if (mIdEtAccName.length() > 0 &&
                    mIdEtAccPassword.length() > 0) {
                mIdBtLogin.setBackgroundResource(R.drawable.bt_bg_blue);
                mIdBtLogin.setEnabled(true);//设置按钮可以点击使用
            } else {
                mIdBtLogin.setBackgroundResource(R.drawable.bt_bg_white);
                mIdBtLogin.setEnabled(false);
            }
        }
    }
    //登录处理的方法实现
    private void loginDeal() {
        final String userName = mIdEtAccName.getText().toString().trim();
        final String passWord = mIdEtAccPassword.getText().toString().trim();
        //最先执行查询操作
        BmobHelper.getInstance().userQuery(userName).findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e == null) {
                    if (list.size() <= 0) {
                        //不存在,提示该用户还没注册
                        Toast.makeText(
                                LoginActivity.this,//在该activity显示
                                "该账号未注册...",//显示的内容
                                Toast.LENGTH_SHORT).show();//显示的格式
                    } else {
                        //存在,直接进行登录操作
                        // 执行登录操作,并会缓存用户信息到本地
                        BmobHelper.getInstance().userLogin(userName, passWord)
                                .login(new SaveListener<BmobUser>() {
                                    @Override
                                    public void done(BmobUser bmobUser, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(LoginActivity.this,
                                                    "登录成功",
                                                    Toast.LENGTH_SHORT).show();
                                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                                            //开始跳转
                                            /*********************更新登录的全局变量****************/
                                            App.getInstance().setIsLogin(true);
                                            App.getInstance().setMyUser(BmobUser.getCurrentUser(MyUser.class));
                                            /*********全局设定缓存路径**********/
                                            App.getInstance().setDiskCacheDir(FileUtil.getDiskCacheDir
                                                    (App.getInstance().getMyUser().getUsername()));
                                            //建立一个新的子目录
                                            if (!App.getInstance().getDiskCacheDir().exists()) {
                                                App.getInstance().getDiskCacheDir().mkdir();
                                            }
                                            /*设置全局使用的头像file对象*/
                                            App.getInstance().setmOutputImage(new File(App.getInstance().getDiskCacheDir(),
                                                    "head_"+App.getInstance().getMyUser().getUsername()+".jpg"));

                                            LoginActivity.this.finish();//结束当前activity
                                        } else {
                                            Toast.makeText(LoginActivity.this,
                                                    "登录失败" + e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(
                            LoginActivity.this,//在该activity显示
                            "查询用户信息失败:" + e.getMessage(),//显示的内容
                            Toast.LENGTH_SHORT).show();//显示的格式
                }
            }
        });
    }
}
