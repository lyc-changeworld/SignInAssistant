package com.example.achuan.bombtest.ui.assistant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.model.bean.CourseBean;
import com.example.achuan.bombtest.util.StringUtil;
import com.example.achuan.bombtest.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by achuan on 16-10-5.
 * 功能：实现列表中数据的适配
 * 技巧：提高列表的显示效果,滑动时,停止加载数据;停止时,只加载当前显示范围类的数据
 * 注意事项：第一次显示列表时并没有发生滑动事件,此时并不会触发滑动状态改变的监听事件,所以需要
 * 　　　　　在scroll方法中添加一个初始化第一屏的图片加载事件
 */
public class SigninAdapter extends RecyclerView.Adapter<SigninAdapter.ViewHolder> {

    private LayoutInflater mInflater;//创建布局装载对象来获取相关控件（类似于findViewById()）
    private Context mContext;//显示框面
    private List<CourseBean> mList;
    //private int mStart,mEnd;//定义item加载的起始和结束的位置序号
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , CourseBean courseBean);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /*构造方法*/
    public SigninAdapter(Context mContext, List<CourseBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //通过获取context来初始化mInflater对象
        mInflater = LayoutInflater.from(mContext);
    }
    //适配器中数据集中的个数
    public int getItemCount() {
        return mList.size();
    }
    /****
     * item第一次显示时,才创建其对应的viewholder进行控件存储,之后直接使用即可
     ****/
    //先创建ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = mInflater.inflate(R.layout.item_signin_course, parent, false);//载入item布局
        ViewHolder viewHolder = new ViewHolder(view);//创建一个item的viewHoler实例
        return viewHolder;
    }
    /****
     * 当前界面中出现了item的显示更新时执行该方法（即有item加入或者移除界面）
     * <p/>
     * 该方法的执行顺序　　早于　　onScrolled（）方法
     ****/
    //绑定ViewHolder
    public void onBindViewHolder(final ViewHolder holder, final int postion) {
        //再通过viewHolder中缓冲的控件添加相关数据
        final CourseBean courseBean = mList.get(postion);//从数据源集合中获得对象
        /*//获取对应item的图片链接
        String url=bean.getPicUrl();
        *//*通过设置tag来保证图片和url的对应显示,防止网络加载时的时序错乱*//*
        holder.mIvWechatItemImage.setTag(url);
        */
        //绑定数据
        holder.mIvWechatItemImage.setImageResource(R.drawable.course);
        holder.mTvAssistantItemCname.setText(courseBean.getCname());
        holder.mTvAssistantItemCredit.setText(courseBean.getCredit().toString()+"学分");
        String mSemester = null;
        switch (courseBean.getSemester()){
            case 1:mSemester="大一上学期";break;
            case 2:mSemester="大一下学期";break;
            case 3:mSemester="大二上学期";break;
            case 4:mSemester="大二下学期";break;
            case 5:mSemester="大三上学期";break;
            case 6:mSemester="大三下学期";break;
            case 7:mSemester="大四上学期";break;
            case 8:mSemester="大四下学期";break;
        }
        holder.mTvAssistantItemSemester.setText(mSemester);
        /***为item设置点击监听事件***/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener!=null){
                    //设置回调监听
                    mOnItemClickListener.onItemClick(view, courseBean);
                }
            }
        });
    }

    /***根据右边栏点击的字母来实现列表滚动到对应的位置***/
    public int getFirstPositionByChar(char sign) {
        if (sign == '#') {
            //统计非字母数据的个数
            int count=0;
            for (int i = 0; i <mList.size() ; i++) {
                if(StringUtil.getHeadChar(mList.get(i).getCname()) == ' '){
                    count++;
                }
            }
            if(count>0){
                return mList.size()-count;
            }
        }else {
            for (int i = 0; i < mList.size(); i++) {
                if (StringUtil.getHeadChar(mList.get(i).getCname()) == sign) {
                    return i;
                }
            }
        }
        return -1;
    }

    /*创建自定义的ViewHolder类*/
    public  static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_wechat_item_image)
        SquareImageView mIvWechatItemImage;
        @BindView(R.id.tv_assistant_item_Cname)
        TextView mTvAssistantItemCname;
        @BindView(R.id.tv_assistant_item_Credit)
        TextView mTvAssistantItemCredit;
        @BindView(R.id.tv_assistant_item_Semester)
        TextView mTvAssistantItemSemester;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
