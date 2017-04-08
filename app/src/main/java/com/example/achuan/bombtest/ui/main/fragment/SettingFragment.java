package com.example.achuan.bombtest.ui.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.base.SimpleFragment;
import com.example.achuan.bombtest.model.http.BmobHelper;
import com.example.achuan.bombtest.util.DialogUtil;
import com.example.achuan.bombtest.util.FileUtil;
import com.example.achuan.bombtest.util.ImageUtil;
import com.example.achuan.bombtest.util.SharedPreferenceUtil;
import com.example.achuan.bombtest.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by achuan on 16-10-30.
 * 功能：设置界面(fragment)view层
 */
public class SettingFragment extends SimpleFragment implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.cb_setting_open_bluetooth)
    AppCompatCheckBox mCbSettingOpenBluetooth;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout mLlSettingFeedback;
    @BindView(R.id.bt_logout)
    Button mBtLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        //初始化check按钮框的状态
        mCbSettingOpenBluetooth.setChecked(SharedPreferenceUtil.getAutoOpenBlueToothState());
        mCbSettingOpenBluetooth.setOnCheckedChangeListener(this);

    }

    //在活动准备好和用户进行交互时调用
    @Override
    public void onResume() {
        super.onResume();
        //交互的同时更新显示的内容
        if(App.getInstance().getIsLogin()){
            mBtLogout.setVisibility(View.VISIBLE);
        }else {
            mBtLogout.setVisibility(View.INVISIBLE);
        }
    }

    //复选框点击触发事件的实现
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_setting_open_bluetooth:
                SharedPreferenceUtil.setAutoOpenBlueToothState(b);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.ll_setting_feedback, R.id.bt_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_feedback:
                Toast.makeText(getActivity(),
                        "意见反馈功能后续将实现",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_logout:
                showLogout();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    //弹出对话框,确认是否退出账号
    private void showLogout() {
        DialogUtil.createOrdinaryDialog(getContext(), "提示", "确定要退出当前账号吗?", "退出登录", "取消",
                new DialogUtil.OnAlertDialogButtonClickListener() {
                    @Override
                    public void onRightButtonClick() {
                        mBtLogout.setVisibility(View.INVISIBLE);
                        //退出账号,清除本地缓存用户对象
                        BmobHelper.getInstance().userLogOut();
                        /*****更新登录的全局变量*****/
                        App.getInstance().setIsLogin(false);
                        App.getInstance().setMyUser(null);
                        //删除当前用户的缓存文件夹(名称＝＝手机号),并更新重置全局变量
                        if(App.getInstance().getDiskCacheDir().exists()){
                            //删除用户目录下的所有文件
                            FileUtil.deleteAllFiles(App.getInstance().getDiskCacheDir());
                            //将缓存用户信息的文件目录删除
                            App.getInstance().getDiskCacheDir().delete();
                        }
                        App.getInstance().setDiskCacheDir(null);
                        App.getInstance().setmOutputImage(null);
                        /*****重置用户信息显示布局*****/
                        TextView mTV_nickName= (TextView) getActivity().findViewById(R.id.tv_nickName);
                        TextView mTV_userSignature=(TextView) getActivity().findViewById(R.id.tv_userSignature);
                        CircleImageView mCircleImageView= (CircleImageView) getActivity().findViewById(R.id.iv_headIcon);
                        mTV_nickName.setText(R.string.clickLogin);
                        mTV_userSignature.setText("");
                        mCircleImageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(
                                getResources(),R.drawable.nohead,50,50
                        ));
                    }
                    @Override
                    public void onLeftButtonClick() {
                    }
                });
    }
}
