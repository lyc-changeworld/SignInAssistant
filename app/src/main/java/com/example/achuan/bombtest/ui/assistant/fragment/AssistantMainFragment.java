package com.example.achuan.bombtest.ui.assistant.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.SimpleFragment;
import com.example.achuan.bombtest.ui.assistant.adapter.AssistantMainAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 16-11-10.
 * 功能：签到助手的主界面
 * 实现ViewPager和Fragment实现顶部导航界面滑动效果
 */
public class AssistantMainFragment extends SimpleFragment {

    //顶部导航标签文字
    String[] tabTitle = new String[]{"课程签到","投票","客服"};
    //定义一个fragment集合实例来存储对应要滑动显示的多个fragment实例
    List<Fragment> fragments = new ArrayList<Fragment>();
    AssistantMainAdapter mAssistantMainAdapter;

    @BindView(R.id.tab_assistant_main)
    TabLayout mTabAssistantMain;
    @BindView(R.id.vp_assistant_main)
    ViewPager mVpAssistantMain;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_assistant_main;
    }

    @Override
    protected void initEventAndData() {
        //装载fragment实例到集合体中
        fragments.add(new SigninFragment());
        fragments.add(new VoteFragment());
        fragments.add(new CustomerServiceFragment());
        //实例化适配器
        mAssistantMainAdapter=new AssistantMainAdapter(getChildFragmentManager(),fragments);
        //为viewpager添加适配器
        mVpAssistantMain.setAdapter(mAssistantMainAdapter);
        /***TabLayout配合ViewPager有时会出现不显示Tab文字的Bug,需要按如下顺序***/
        //添加tab选项卡
        mTabAssistantMain.addTab(mTabAssistantMain.newTab().setText(tabTitle[0]));
        mTabAssistantMain.addTab(mTabAssistantMain.newTab().setText(tabTitle[1]));
        mTabAssistantMain.addTab(mTabAssistantMain.newTab().setText(tabTitle[2]));
        //将TabLayout和ViewPager关联起来
        mTabAssistantMain.setupWithViewPager(mVpAssistantMain);
        /*一定得添加下面的代码,否则将无法显示Tab文字*/
        mTabAssistantMain.getTabAt(0).setText(tabTitle[0]);
        mTabAssistantMain.getTabAt(1).setText(tabTitle[1]);
        mTabAssistantMain.getTabAt(2).setText(tabTitle[2]);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
