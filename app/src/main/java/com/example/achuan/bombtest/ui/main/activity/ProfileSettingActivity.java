package com.example.achuan.bombtest.ui.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.base.BaseActivity;
import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.presenter.ProfileSettingPresenter;
import com.example.achuan.bombtest.presenter.contract.ProfileSettingContract;
import com.example.achuan.bombtest.util.DialogUtil;
import com.example.achuan.bombtest.util.SnackbarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by achuan on 16-11-13.
 */
public class ProfileSettingActivity extends BaseActivity<ProfileSettingContract.Presenter> implements ProfileSettingContract.View {

    //记录当前登录的用户的id号
    private String id;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_nickName)
    TextView mTvNickName;
    @BindView(R.id.rt_nickName)
    RelativeLayout mRtNickName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.rt_sex)
    RelativeLayout mRtSex;
    @BindView(R.id.tv_age)
    TextView mTvAge;
    @BindView(R.id.rt_age)
    RelativeLayout mRtAge;
    @BindView(R.id.tv_student_info)
    TextView mTvStudentInfo;
    @BindView(R.id.tv_email)
    TextView mTvEmail;
    @BindView(R.id.rt_email)
    RelativeLayout mRtEmail;
    @BindView(R.id.tv_signature)
    TextView mTvSignature;
    @BindView(R.id.rt_signature)
    RelativeLayout mRtSignature;


    @Override
    protected ProfileSettingContract.Presenter createPresenter() {
        return new ProfileSettingPresenter();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_profile_setting;
    }
    @Override
    protected void initEventAndData() {
        //初始化设置标题栏
        setToolBar(mToolbar, "基本信息");
        //初始化显示用户信息
        mPresenter.getUserObject(App.getInstance().
                getMyUser().getUsername());

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.rt_nickName, R.id.rt_sex, R.id.rt_age, R.id.tv_student_info, R.id.rt_email, R.id.rt_signature})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rt_nickName:
                final String nickName=mTvNickName.getText().toString();
                DialogUtil.createInputDialog(this, nickName,
                        "修改昵称", "确定", "取消", new DialogUtil.OnInputDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(String input) {
                                if(!input.equals(nickName)){
                                    mTvNickName.setText(input);
                                    mPresenter.updateUserInfoByKey(id,"nickName",input);
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
            case R.id.rt_sex:
                final Dialog dialog=DialogUtil.createMyselfDialog(this,R.layout.view_bottom_dialog);
                //初始化布局控件
                TextView chooseMan= (TextView) dialog.findViewById(R.id.tv_choose_man);
                TextView chooseWoman= (TextView) dialog.findViewById(R.id.tv_choose_woman);
                TextView chooseCancel= (TextView) dialog.findViewById(R.id.tv_choose_cancel);
                chooseMan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mTvSex.getText().equals("男")){
                            mTvSex.setText("男");
                            mPresenter.updateUserInfoByKey(id,"sex","男");
                        }
                        dialog.dismiss();
                    }
                });
                chooseWoman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!mTvSex.getText().equals("女")){
                            mTvSex.setText("女");
                            mPresenter.updateUserInfoByKey(id,"sex","女");
                        }
                        dialog.dismiss();
                    }
                });
                chooseCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.rt_age:
                DialogUtil.createDatePickerDialog(this,"请选择出生日期","确定","取消",
                        new DialogUtil.OnDatePickerDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(Boolean isBorn,int age,String StarSeat) {
                                //根据是否出生的标志进行判断
                                if(isBorn){
                                    if(!mTvAge.getText().equals(String.valueOf(age))){
                                        mTvAge.setText(String.valueOf(age));
                                        SnackbarUtil.showShort(mRtAge,"你今年"+age + "岁,星座:"+StarSeat);
                                        mPresenter.updateUserInfoByKey(id,"age",age);
                                    }
                                }else {
                                    SnackbarUtil.showShort(mRtSignature,"你还未出生,请重新选择-=͟͟͞͞( °∀° )☛");
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
            case R.id.tv_student_info:

                break;
            case R.id.rt_email:
                final String email=mTvEmail.getText().toString();
                DialogUtil.createInputDialog(this,email ,
                        "修改邮箱", "确定", "取消", new DialogUtil.OnInputDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(String input) {
                                //equals()比较的是对象的内容（区分字母的大小写格式），但是
                                // 如果使用“==”比较两个对象时，比较的是两个对象的内存地址，所以不相等
                                if(!input.equals(email)){
                                    mTvEmail.setText(input);
                                    mPresenter.updateUserInfoByKey(id,"email",input);
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
            case R.id.rt_signature:
                final String signature=mTvSignature.getText().toString();
                DialogUtil.createInputDialog(this,signature ,
                        "修改个性签名", "确定", "取消", new DialogUtil.OnInputDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(String input) {
                                //equals()比较的是对象的内容（区分字母的大小写格式），但是
                                // 如果使用“==”比较两个对象时，比较的是两个对象的内存地址，所以不相等
                                if(!input.equals(signature)){
                                    mTvSignature.setText(input);
                                    mPresenter.updateUserInfoByKey(id,"signature",input);
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
        }
    }

    //显示网络后台端用户的信息
    @Override
    public void showNetUserContent(MyUser myUser) {
        //记录当前用户的id号
        id=myUser.getObjectId();
        //显示当前用户的信息
        mTvNickName.setText(myUser.getNickName());
        mTvSex.setText(myUser.getSex());
        mTvAge.setText(myUser.getAge().toString());
        mTvEmail.setText(myUser.getEmail());
        mTvSignature.setText(myUser.getSignature());
    }
    //显示本地用户缓存的信息
    @Override
    public void showLocalUserContent() {
        MyUser myUser=App.getInstance().getMyUser();
        id=myUser.getObjectId();
        mTvNickName.setText(myUser.getNickName());
        mTvSex.setText(myUser.getSex());
        mTvAge.setText(myUser.getAge().toString());
        mTvEmail.setText(myUser.getEmail());
        mTvSignature.setText(myUser.getSignature());
    }
    @Override
    public void showLoading() {
        DialogUtil.createProgressDialog(this,"","正在加载,请稍候");
    }
    @Override
    public void hideLoading() {
        DialogUtil.closeProgressDialog();
    }
    @Override
    public void showError(String msg) {
        SnackbarUtil.showShort(mRtSignature,msg);
    }
}
