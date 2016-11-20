package com.example.achuan.bombtest.ui.assistant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.SimpleFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobPushManager;

/**
 * Created by achuan on 16-11-10.
 */
public class VoteFragment extends SimpleFragment {

    @BindView(R.id.bt_push)
    Button mBtPush;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vote;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick(R.id.bt_push)
    public void onClick() {
        BmobPushManager pushManager=new BmobPushManager();
        pushManager.pushMessageAll("Test");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}
