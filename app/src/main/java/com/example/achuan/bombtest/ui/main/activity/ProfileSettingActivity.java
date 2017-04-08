package com.example.achuan.bombtest.ui.main.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.base.BaseActivity;
import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.presenter.ProfileSettingPresenter;
import com.example.achuan.bombtest.presenter.contract.ProfileSettingContract;
import com.example.achuan.bombtest.model.http.BmobHelper;
import com.example.achuan.bombtest.util.DialogUtil;
import com.example.achuan.bombtest.util.ImageUtil;
import com.example.achuan.bombtest.util.SnackbarUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by achuan on 16-11-13.
 */
public class ProfileSettingActivity extends BaseActivity<ProfileSettingContract.Presenter> implements ProfileSettingContract.View {

    @BindView(R.id.iv_headIcon)
    ImageView mIvHeadIcon;
    @BindView(R.id.rt_headIcon)
    RelativeLayout mRtHeadIcon;
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

    public static final int TAKE_PHOTO = 1;//打开相机拍照
    public static final int CHOOSE_FROMALUBM = 2;//从相册
    public static final int CROP_PHOTO = 3;

    private Context mContext;
    //缓存根目录\裁剪后的图片\拍照后的
    private File mOutputImageTakePhoto;
    //裁剪后的\拍照后的
    private Uri mImageUri,mImageUriTakePhoto;
    //引用变量,指向当前登录的缓存用户
    private MyUser mMyUser;

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
        mContext=this;
        //初始化设置标题栏
        setToolBar(mToolbar, "基本信息");
        //获取本地用户的用户名(这个是一旦用户注册了,就不会变的)
        mMyUser=App.getInstance().getMyUser();
        /*初始化创建File对象,用于存储拍照后的图片和裁剪后的图片*/
        mOutputImageTakePhoto = new File(App.getInstance().getDiskCacheDir(),
                "headTakePhoto.jpg");
        if(mOutputImageTakePhoto.exists()){
            mOutputImageTakePhoto.delete();
        }
        //将File对象转换成Uri对象,标示着.jpg图片的唯一地址
        mImageUri=Uri.fromFile(App.getInstance().getmOutputImage());
        /***2-初始化显示用户信息(先去网络端加载,如果不成功再去本地加载缓存的用户信息)***/
        mPresenter.getUserObject(mMyUser.getUsername());

    }

    /***进行头像显示的判断***/
    private void showHeadIcon(){
        if(mMyUser.getHeadUri()!=null&&mMyUser.getHeadUri()!=""){//说明用户有头像
            mIvHeadIcon.setImageBitmap(ImageUtil.decodeSampledBitmapFromFile(
                    App.getInstance().getmOutputImage().getPath(),60,60));
        }else {//说明用户本身没有头像,只好显示"无头像"
            mIvHeadIcon.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(
                    getResources(),R.drawable.nohead,60,60));
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mOutputImageTakePhoto.exists()){
            mOutputImageTakePhoto.delete();
        }
    }
    @OnClick({R.id.rt_headIcon, R.id.rt_nickName, R.id.rt_sex, R.id.rt_age, R.id.tv_student_info, R.id.rt_email, R.id.rt_signature})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rt_headIcon:
                final Dialog dialog1 = DialogUtil.createMyselfDialog(this,
                        R.layout.view_dialog_headselect, Gravity.CENTER);
                TextView takePhoto = (TextView) dialog1.findViewById(R.id.tv_takePhoto);
                TextView chooseFromAlbum = (TextView) dialog1.findViewById(R.id.tv_chooseFromAlbum);
                //执行拍照监听事件
                takePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mImageUriTakePhoto = Uri.fromFile(mOutputImageTakePhoto);
                        //隐式的Intent,系统会找出能够响应这个Intent的活动去启动
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//添加意图为拍照
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUriTakePhoto);//指定图片的输出地址
                        startActivityForResult(intent, TAKE_PHOTO);//启动活动
                        dialog1.dismiss();
                    }
                });
                //执行去相册
                chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT,null);
                        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(albumIntent,CHOOSE_FROMALUBM);
                        dialog1.dismiss();
                    }
                });
                break;
            case R.id.rt_nickName:
                final String nickName = mTvNickName.getText().toString();
                DialogUtil.createInputDialog(this, nickName,
                        "修改昵称", "确定", "取消", new DialogUtil.OnInputDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(String input) {
                                if (!input.equals(nickName)) {
                                    mTvNickName.setText(input);
                                    mPresenter.updateUserInfoByKey(
                                            mMyUser.getObjectId(), "nickName", input);
                                    mMyUser.setNickName(input);
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
            case R.id.rt_sex:
                final Dialog dialog2 = DialogUtil.createMyselfDialog(this,
                        R.layout.view_dialog_sexselect, Gravity.BOTTOM);
                //初始化布局控件
                TextView chooseMan = (TextView) dialog2.findViewById(R.id.tv_choose_man);
                TextView chooseWoman = (TextView) dialog2.findViewById(R.id.tv_choose_woman);
                TextView chooseCancel = (TextView) dialog2.findViewById(R.id.tv_choose_cancel);
                chooseMan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mTvSex.getText().equals("男")) {
                            mTvSex.setText("男");
                            mPresenter.updateUserInfoByKey(mMyUser.getObjectId(),
                                    "sex", "男");
                            mMyUser.setSex("男");
                        }
                        dialog2.dismiss();
                    }
                });
                chooseWoman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mTvSex.getText().equals("女")) {
                            mTvSex.setText("女");
                            mPresenter.updateUserInfoByKey(mMyUser.getObjectId(),
                                    "sex", "女");
                            mMyUser.setSex("女");
                        }
                        dialog2.dismiss();
                    }
                });
                chooseCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                    }
                });
                break;
            case R.id.rt_age:
                DialogUtil.createDatePickerDialog(this, "请选择出生日期", "确定", "取消",
                        new DialogUtil.OnDatePickerDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(Boolean isBorn, int age, String StarSeat) {
                                //根据是否出生的标志进行判断
                                if (isBorn) {
                                    if (!mTvAge.getText().equals(String.valueOf(age))) {
                                        mTvAge.setText(String.valueOf(age));
                                        SnackbarUtil.showShort(mRtAge, "你今年" + age + "岁,星座:" + StarSeat);
                                        mPresenter.updateUserInfoByKey(
                                                mMyUser.getObjectId(), "age", age);
                                        mMyUser.setAge(age);
                                    }
                                } else {
                                    SnackbarUtil.showShort(mRtSignature, "你还未出生,请重新选择-=͟͟͞͞( °∀° )☛");
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
                final String email = mTvEmail.getText().toString();
                DialogUtil.createInputDialog(this, email,
                        "修改邮箱", "确定", "取消", new DialogUtil.OnInputDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(String input) {
                                //equals()比较的是对象的内容（区分字母的大小写格式），但是
                                // 如果使用“==”比较两个对象时，比较的是两个对象的内存地址，所以不相等
                                if (!input.equals(email)) {
                                    mTvEmail.setText(input);
                                    mPresenter.updateUserInfoByKey(
                                            mMyUser.getObjectId(), "email", input);
                                    mMyUser.setEmail(input);
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
            case R.id.rt_signature:
                final String signature = mTvSignature.getText().toString();
                DialogUtil.createInputDialog(this, signature,
                        "修改个性签名", "确定", "取消", new DialogUtil.OnInputDialogButtonClickListener() {
                            @Override
                            public void onRightButtonClick(String input) {
                                //equals()比较的是对象的内容（区分字母的大小写格式），但是
                                // 如果使用“==”比较两个对象时，比较的是两个对象的内存地址，所以不相等
                                if (!input.equals(signature)) {
                                    mTvSignature.setText(input);
                                    mPresenter.updateUserInfoByKey(
                                            mMyUser.getObjectId(), "signature", input);
                                    mMyUser.setSignature(input);
                                }
                            }
                            @Override
                            public void onLeftButtonClick() {
                            }
                        });
                break;
        }
    }

    /*子活动结束后的回调方法,用来获取子活动中传递过来的信息*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    ImageUtil.cropImageFromUri(mContext,mImageUriTakePhoto,mImageUri,1,1,666,666,CROP_PHOTO);
                }else {
                    if(mOutputImageTakePhoto.exists()){
                        mOutputImageTakePhoto.delete();
                    }
                }
                break;
            case CHOOSE_FROMALUBM:
                if (resultCode == RESULT_OK){
                    Uri uri=data.getData();//获取相册中选中图片的资源链接
                    ImageUtil.cropImageFromUri(mContext,uri,mImageUri,1,1,666,666,CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if(mOutputImageTakePhoto.exists()){
                    mOutputImageTakePhoto.delete();
                }
                if (resultCode == RESULT_OK) {
                    /*裁剪图片成功后要做的事：
                    １-界面上显示裁剪后的图片
                    2-上传新的头像文件到后端服务器
                    3-删除后端的历史的头像文件
                    4-更新用户对应的头像的链接地址
                    (步骤2和3属于一个事务,步骤4在2完成后才进行)
                    * */
                    /************************1*************************/
                    mIvHeadIcon.setImageBitmap(ImageUtil.decodeSampledBitmapFromFile(
                                    mImageUri.getPath(),60,60));
                    /************************2*************************/
                    final BmobFile bmobFile= BmobHelper.getInstance().fileBmobUpload(mImageUri.getPath());
                    mPresenter.uploadFile(bmobFile);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showUploadFileSuccess(String headUri) {
        /************************3*************************/
        //接着删除后台服务器端历史的头像
        if(mMyUser.getHeadUri()!=null&&mMyUser.getHeadUri()!=""){
            mPresenter.deleteFile(mMyUser.getHeadUri());
        }
        mMyUser.setHeadUri(headUri);//更新历史链接
        /************************4*************************/
        //并更新用户的头像链接地址
        mPresenter.updateUserInfoByKey(mMyUser.getObjectId(),"headUri",headUri);
        App.getInstance().getMyUser().setHeadUri(headUri);
    }

    //显示网络后台端用户的信息
    @Override
    public void showNetUserContent(MyUser myUser) {
        //让本地端缓存的用户信息和网络端同步
        mMyUser.setObjectId(myUser.getObjectId());
        mMyUser.setHeadUri(myUser.getHeadUri());
        mMyUser.setNickName(myUser.getNickName());
        mMyUser.setSex(myUser.getSex());
        mMyUser.setAge(myUser.getAge());
        mMyUser.setSignature(myUser.getSignature());
        mMyUser.setEmail(myUser.getEmail());
        //显示当前用户的信息
        mTvNickName.setText(myUser.getNickName());
        mTvSex.setText(myUser.getSex());
        mTvAge.setText(myUser.getAge().toString());
        mTvEmail.setText(myUser.getEmail());
        mTvSignature.setText(myUser.getSignature());
        showHeadIcon();
    }
    //显示本地用户缓存的信息
    @Override
    public void showLocalUserContent() {
        MyUser myUser = App.getInstance().getMyUser();
        mTvNickName.setText(myUser.getNickName());
        mTvSex.setText(myUser.getSex());
        mTvAge.setText(myUser.getAge().toString());
        mTvEmail.setText(myUser.getEmail());
        mTvSignature.setText(myUser.getSignature());
        showHeadIcon();
    }
    @Override
    public void showLoading(String message) {
        DialogUtil.createProgressDialog(this,"", message);
    }
    @Override
    public void hideLoading() {
        DialogUtil.closeProgressDialog();
    }
    @Override
    public void showError(String msg) {
        SnackbarUtil.showShort(mRtSignature, msg);
    }
}
