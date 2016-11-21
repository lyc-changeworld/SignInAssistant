package com.example.achuan.bombtest.ui.assistant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.SimpleFragment;

import butterknife.ButterKnife;

/**
 * Created by achuan on 16-11-10.
 */
public class VoteFragment extends SimpleFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vote;
    }

    @Override
    protected void initEventAndData() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}
