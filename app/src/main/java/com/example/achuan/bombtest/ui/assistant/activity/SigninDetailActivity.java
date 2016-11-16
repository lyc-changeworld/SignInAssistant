package com.example.achuan.bombtest.ui.assistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.BaseActivity;
import com.example.achuan.bombtest.model.bean.TeacherBean;
import com.example.achuan.bombtest.presenter.SigninDetailPresenter;
import com.example.achuan.bombtest.presenter.contract.SigninDetailContract;
import com.example.achuan.bombtest.ui.assistant.adapter.SigninDetailAdapter;
import com.example.achuan.bombtest.util.DialogUtil;
import com.example.achuan.bombtest.util.SnackbarUtil;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by achuan on 16-11-7.
 * 功能：点击课程item后打开的活动界面
 * 　　　实现签到功能的具体逻辑处理
 */
public class SigninDetailActivity extends BaseActivity<SigninDetailPresenter> implements SigninDetailContract.View {
    //标题栏显示的内容\课程编号\教工号
    private String title,Cno,Tno;
    //记录选择的教师对应的item的位置
    private int index;
    //列表显示数据集合引用变量
    private List<TeacherBean> mTeacherBeanList;
    //布局设置对象
    private LinearLayoutManager linearManager;
    //列表适配器
    private SigninDetailAdapter mSigninDetailAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bt_signin)
    Button mBtSignin;
    @BindView(R.id.id_recyclerView)
    RecyclerView mIdRecyclerView;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected SigninDetailPresenter createPresenter() {
        return new SigninDetailPresenter();
    }
    @Override
    protected int getLayout() {
        return R.layout.activity_signin_detail;
    }
    @Override
    protected void initEventAndData() {
        //获取上个活动传递过来的意图对象
        Intent intent = getIntent();
        title = intent.getExtras().getString("title");
        Cno=intent.getExtras().getString("Cno");
        setToolBar(mToolbar, title);
        /***初始化***/
        //创建集合实例对象
        mTeacherBeanList = new ArrayList<>();
        //初始化适配器数据绑定
        mSigninDetailAdapter=new SigninDetailAdapter(this,mTeacherBeanList);
        //设置相关布局管理
        linearManager = new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false);//设置布局方式为线性居中布局
        mIdRecyclerView.setLayoutManager(linearManager);
        //为列表控件配置适配器
        mIdRecyclerView.setAdapter(mSigninDetailAdapter);
        //初始化加载控件启动加载动画
        mViewLoading.start();
        //初始化获取网络数据
        mPresenter.getTeacherData();
        //设置下拉刷新处理
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重置radio选中的位置
                mSigninDetailAdapter.setIndex(-1);
                mPresenter.getTeacherData();
            }
        });
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void showContent(List<TeacherBean> mList) {
        //如果在进行下拉刷新,则停止
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        //如果加载旋钮还在执行动画,则关闭
        if (mViewLoading.isStart()) {
            mViewLoading.stop();//停止刷新动画显示
        }
        mTeacherBeanList.clear();
        mTeacherBeanList.addAll(mList);
        //刷新列表显示内容
        mSigninDetailAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_signin)
    public void onClick() {
        //获取当前选择的item的位置号
        index=mSigninDetailAdapter.getIndex();
        if(index!=-1){
            DialogUtil.createOrdinaryDialog(this,"提示","确定要签到吗","确定","取消",
                    new DialogUtil.OnAlertDialogButtonClickListener() {
                        @Override
                        public void onRightButtonClick() {
                            //根据选择的radio的位置获取对应的教工号
                            Tno=mTeacherBeanList.get(index).getTno();
                            //执行签到处理,这里暂时还未对学号进行添加,后续完善用户资料设置后将添加
                            mPresenter.signinDeal("",Cno,Tno);
                        }
                        @Override
                        public void onLeftButtonClick() {
                        }
                    });
        }else {
            SnackbarUtil.showShort(mIdRecyclerView,"还未选择老师-=͟͟͞͞( °∀° )☛");
        }
    }

    @Override
    public void showSigninSuccess(String message) {
        //签到成功后屏蔽签到按钮
        mBtSignin.setEnabled(false);
        mBtSignin.setText("已签到");
        mBtSignin.setBackgroundResource(R.drawable.bt_bg_white);
        SnackbarUtil.showShort(mIdRecyclerView,message);
    }
    @Override
    public void showError(String msg) {
        //如果在进行下拉刷新,则停止
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        //如果加载旋钮还在执行动画,则关闭
        if(mViewLoading.isStart()){
            mViewLoading.stop();//停止刷新动画显示
        }
        SnackbarUtil.showShort(mIdRecyclerView,msg);
    }
}
