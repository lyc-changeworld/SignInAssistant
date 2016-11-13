package com.example.achuan.bombtest.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by achuan on 16-11-13.
 * 功能：对话框的封装功能类
 */
public class AlertDialogUtil {

    //define interface
    public interface OnAlertDialogButtonClickListener {
        //右边按钮点击事件
        void onRightButtonClick();
        //左边按钮点击事件
        void onLeftButtonClick();
    }

    //创建一个对话框
    public static void createDialog(Context context, String title, String message,
                                    String rightString, String leftString,
                                    final OnAlertDialogButtonClickListener onAlertDialogButtonClickListener){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);//先创建一个构造实例
        dialog.setTitle(title);//设置标题
        dialog.setMessage(message);//设置内容部分
        dialog.setCancelable(true);//设置是否可以通过Back键取消：false为不可以取消,true为可以取消
        //设置右边按钮的信息
        dialog.setPositiveButton(rightString, new DialogInterface.OnClickListener() {
            @Override//点击触发事件
            public void onClick(DialogInterface dialogInterface, int i) {
                if(onAlertDialogButtonClickListener!=null){
                    onAlertDialogButtonClickListener.onRightButtonClick();
                }
            }
        });
        //设置左边按钮的信息
        dialog.setNegativeButton(leftString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(onAlertDialogButtonClickListener!=null){
                    onAlertDialogButtonClickListener.onLeftButtonClick();
                }
            }
        });
        dialog.show();//将对话框显示出来
    }


}
