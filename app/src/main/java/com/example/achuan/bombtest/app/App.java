package com.example.achuan.bombtest.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.smssdk.SMSSDK;
/*
*功能：定制一个自己的Application类,以便于管理程序内一些全局的状态信息
 */

public class App extends Application
{
    /***在服务器端获取的Bmob应用ID***/
    public static String BmobAppid ="f85e90d6599e09343fde6cf8410ff5a4";
    /***在服务器端获取的Mob应用key和secret*/
    private static String MobAppkey="18699bb95cfb8";
    private static String MobAppsecret="79ac6771720ff687bb1c5cea3bcfbb0e";

    //单例模式定义变量,保证只会实例化一次
    private static App instance;
    private static Context sContext;//全局变量
    //声明一个数组来存储活动
    private static List<Activity> sActivities=new ArrayList<Activity>();
    //声明一个全局变量来记录登录状态
    private static boolean isLogin;
    //声明一个全局变量来引用当前登录的缓存用户对象
    private static BmobUser sBmobUser;


    //单例模式,避免内存造成浪费,需要实例化该类时才将其实例化
    public static synchronized App getInstance() {
        //网上说Application的instance不能用new
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bmob后台服务
        Bmob.initialize(this,BmobAppid);
        //初始化Mob的后台服务
        SMSSDK.initSDK(this,MobAppkey,MobAppsecret);
        instance = this;
        sContext=getApplicationContext();//获得一个应用程序级别的Context
        /***进行判断,标记登录的状态***/
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser!=null){
            setIsLogin(true);//设置状态为:登录
            setBmobUser(bmobUser);
        }else {
            setIsLogin(false);//设置状态为:未登录
            setBmobUser(null);
        }
    }

    //登录状态的set和get方法
    public static boolean getIsLogin() {
        return isLogin;
    }
    public static void setIsLogin(boolean isLogin) {
        App.isLogin = isLogin;
    }
    //本地缓存的用户对象的set和get方法
    public static BmobUser getBmobUser() {
        return sBmobUser;
    }
    public static void setBmobUser(BmobUser bmobUser) {
        sBmobUser = bmobUser;
    }

    //1-全局Context的获取
    public static Context getContext() {
        return sContext;//返回这个全局的Context
    }
    /**********2-管理活动***********/
    //1添加活动到数组中
    public static void addActivity(Activity activity)
    {
        sActivities.add(activity);
    }
    //2从数组中移除活动
    public static void removeActivity(Activity activity)
    {
        sActivities.remove(activity);
    }
    //3退出APP的操作,杀光所有的进程
    public static void exitApp()
    {
        if(sActivities!=null)
        {
            //同步执行活动销毁
            synchronized (sActivities) {
                for (Activity activity:sActivities)
                {
                    if(!activity.isFinishing())
                    {
                        activity.finish();//该操作只会将活动出栈,并没有执行onDestory()方法
                        // onDestory()方法是活动生命的最后一步,将资源空间等回收
                        // 当重新进入此Activity的时候,必须重新创建,执行onCreate()方法.
                    }
                }
            }
        }
        //杀光所有的进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);//将整个应用程序的进程KO掉
    }

}