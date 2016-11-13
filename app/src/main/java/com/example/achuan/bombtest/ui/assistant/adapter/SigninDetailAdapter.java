package com.example.achuan.bombtest.ui.assistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.model.bean.TeacherBean;
import com.example.achuan.bombtest.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 16-11-12.
 */
public class SigninDetailAdapter extends RecyclerView.Adapter<SigninDetailAdapter.ViewHolder> {

    //创建布局装载对象来获取相关控件（类似于findViewById()）
    private LayoutInflater mInflater;
    private Context mContext;//显示框面
    private List<TeacherBean> mList;
    // 标记用户当前选择的那一个教师
    private int index = -1;

    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    //构造方法
    public SigninDetailAdapter(Context context, List<TeacherBean> list) {
        this.mContext = context;
        this.mList = list;
        //通过获取context来初始化mInflater对象
        mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_signin_detail, parent, false);//载入item布局
        ViewHolder viewHolder = new ViewHolder(view);//创建一个item的viewHoler实例
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //再通过viewHolder中缓冲的控件添加相关数据
        final TeacherBean teacherBean = mList.get(position);//从数据源集合中获得对象
        //绑定数据
        holder.mTvTeacherName.setText(teacherBean.getTname());
        final RadioButton radioButton=holder.mRbSelect;
        //对radio按钮的点击状态进行监听
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    index=position;//记录当前选中的radio的位置
                    LogUtil.d("lyc-changeworld","选中了第"+index+"个radio");
                    //刷新列表显示,会重新执行onBindViewHolder的方法
                    notifyDataSetChanged();
                }
            }
        });
        //列表刷新显示时每个item中的radio都进行了下面的判断,即保证只有一个radio被选中
        if(position==index){
            radioButton.setChecked(true);
        }else {
            radioButton.setChecked(false);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_TeacherName)
        TextView mTvTeacherName;
        @BindView(R.id.rb_select)
        RadioButton mRbSelect;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
