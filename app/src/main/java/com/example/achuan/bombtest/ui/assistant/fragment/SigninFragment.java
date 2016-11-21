package com.example.achuan.bombtest.ui.assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.BaseFragment;
import com.example.achuan.bombtest.model.bean.CourseBean;
import com.example.achuan.bombtest.presenter.SigninPresenter;
import com.example.achuan.bombtest.presenter.contract.SigninContract;
import com.example.achuan.bombtest.ui.assistant.activity.SigninDetailActivity;
import com.example.achuan.bombtest.ui.assistant.adapter.SigninCourseAdapter;
import com.example.achuan.bombtest.util.SnackbarUtil;
import com.example.achuan.bombtest.widget.SideBar;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 16-11-3.
 */
public class SigninFragment extends BaseFragment<SigninPresenter> implements SigninContract.View {

    @BindView(R.id.id_recyclerView)
    RecyclerView mIdRecyclerView;
    @BindView(R.id.view_loading)
    RotateLoading mViewLoading;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.sideBar)
    SideBar mSideBar;
    @BindView(R.id.tv_hint)
    TextView mTvHint;

    private SigninCourseAdapter mAssistantAdapter;//列表适配器
    private List<CourseBean> mCourseBeanList;//数据集合引用变量
    private LinearLayoutManager linearManager;


    @Override
    protected SigninPresenter createPresenter() {
        return new SigninPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signin;
    }

    @Override
    protected void initEventAndData() {
        //创建集合实例对象
        mCourseBeanList = new ArrayList<>();
        //初始化适配器数据绑定
        mAssistantAdapter = new SigninCourseAdapter(getActivity(), mCourseBeanList);
        //设置相关布局管理
        linearManager = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);//设置布局方式为线性居中布局
        mIdRecyclerView.setLayoutManager(linearManager);
        //为列表控件配置适配器
        mIdRecyclerView.setAdapter(mAssistantAdapter);
        //初始化加载控件启动加载动画
        mViewLoading.start();
        //初始化获取网络数据
        mPresenter.getCourseData();
        //设置下拉刷新处理
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getCourseData();//重新获取列表显示数据
            }
        });
        /***设置滑动监听事件,后续再具体实现相关功能***/
        mIdRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE)//滑动停止的状态,加载数据
                {
                    //将指定范围中的图片加载显示出来
                    //mImageLoader.loadImages(mStart,mEnd);
                } else {//其他状态停止加载数据
                    //mImageLoader.cancelAllTasks();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // dy :正  列表向上划动
                // dy :负  列表向下划动 上下滑动时dx一直为正（水平方向）
                //当前列表界面最上面的item的序号（小号） 序号从0开始
                /*mStart=((LinearLayoutManager) recyclerView
                .getLayoutManager()).findFirstVisibleItemPosition();
                //当前列表界面最下面的item的序号（大号）　
                mEnd = ((LinearLayoutManager) recyclerView
                        .getLayoutManager()).findLastVisibleItemPosition();
                //int mCurrent=recyclerView.getLayoutManager().
                /*//***第一次显示列表时先加载第一屏的图片***//**//**//**//*
                if(mFirstIn){
                    mFirstIn=false;//已经不是第一次加载列表
                    mImageLoader.loadImages(mStart,mEnd);
                }*/
            }
        });

        /***设置索引栏点击监听事件,实现点击字母实现列表栏定位移动***/
        mSideBar.setTextView(mTvHint);//添加中间部分显示控件
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnChooseLetterChangedListener() {
            @Override
            public void onChooseLetter(String s) {
                int i = mAssistantAdapter.getFirstPositionByChar(s.charAt(0));
                //i==-1代表头字母为s的数据不存在,不执行任何操作
                if (i == -1) {
                    return;
                }
                //列表滚动跳转到指定的位置
                linearManager.scrollToPositionWithOffset(i, 0);
            }
            @Override
            public void onNoChooseLetter() {

            }
        });

        /***设置item的点击监听事件***/
        mAssistantAdapter.setOnItemClickListener(new SigninCourseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, CourseBean courseBean) {
                //跳转到签到界面
                Intent intent=new Intent(getContext(), SigninDetailActivity.class);
                intent.putExtra("title",courseBean.getCname());
                intent.putExtra("Cno",courseBean.getCno());
                getContext().startActivity(intent);
            }
        });

    }

    //将后台加载好的数据进行适配显示
    @Override
    public void showContent(List<CourseBean> mList) {
        //如果在进行下拉刷新,则停止
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
        //如果加载旋钮还在执行动画,则关闭
        if(mViewLoading.isStart()){
            mViewLoading.stop();//停止刷新动画显示
        }
        mCourseBeanList.clear();//清空数据
        mCourseBeanList.addAll(mList);//更新集合数据
        /*让数组中的数据按照compareTo方法中的规则返回的结果进行排序*/
        Collections.sort(mCourseBeanList);
        mAssistantAdapter.notifyDataSetChanged();//刷新列表显示内容
    }

    //根据关键字进行数据查询
    public void doSearch(String query) {
        mPresenter.getSearchCourseData(query);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
