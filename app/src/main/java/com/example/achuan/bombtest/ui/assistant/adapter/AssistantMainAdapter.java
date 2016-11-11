package com.example.achuan.bombtest.ui.assistant.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by achuan on 16-11-10.
 * 功能：ViewPager和Fragment顶部导航界面滑动显示的适配器类
 */
public class AssistantMainAdapter extends FragmentPagerAdapter {

    //fragment集合的引用变量,用来指向构造方法中引入的fragment集合实例
    private List<Fragment> fragments;

    public AssistantMainAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    //获取当前导航滑动到的位置的方法
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
    /***
     * FragmentPagerAdapter默认会保存的三个item也就是当前的一个，前一个和后一个
     * 滑动过程中适配器默认会把前一个之前的item destroy掉，
     * 所以当滑动回来时就依然会重新加载
     * 分析其原因就是适配器销毁了之前的item，自然解决办法就是不让他销毁
     * 具体方法就是重写FragmentPagerAdapter的destroyItem方法
     * 注释掉super.destroyItem(Container, position, object);就行了
     * ***/
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
