package com.example.achuan.bombtest.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.app.Constants;


/**
 * Created by achuan on 16-9-10.
 * 功能：存储设置及一些全局的信息到SharedPreferences文件中
 */
public class SharedPreferenceUtil {
    //创建的SharedPreferences文件的文件名
    private static final String SHAREDPREFERENCES_NAME = "my_sp";
    /***设置默认模式***/
    //蓝牙
    private static final boolean DEFAULT_AUTO_OPEN_BlueTooth = false;//默认蓝牙不自动打开
    //默认显示的item布局
    private static final int DEFAULT_CURRENT_ITEM = Constants.TYPE_SETTING;


    //1-创建一个SharedPreferences文件
    public static  SharedPreferences getAppSp() {
        return App.getInstance().getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    /****2-定义get和set方法,实现对SharedPreferences文件中属性值的读取和修改****/
    //蓝牙开启模式
    public static boolean getAutoOpenBlueToothState() {
        return getAppSp().getBoolean(Constants.SP_AUTO_OPEN_BLUETOOTH, DEFAULT_AUTO_OPEN_BlueTooth);
    }
    public static void setAutoOpenBlueToothState(boolean state) {
        getAppSp().edit().putBoolean(Constants.SP_AUTO_OPEN_BLUETOOTH, state).commit();
    }
    //当前显示对应的item的布局
    public static int getCurrentItem() {
        return getAppSp().getInt(Constants.SP_CURRENT_ITEM, DEFAULT_CURRENT_ITEM);
    }
    public static void setCurrentItem(int item) {
        getAppSp().edit().putInt(Constants.SP_CURRENT_ITEM, item).commit();
    }
    //





}
