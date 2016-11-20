package com.example.achuan.bombtest.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;
import cn.smssdk.SMSSDK;
/*
*功能：定制一个自己的Application类,以便于管理程序内一些全局的状态信息
 */

public class App extends Application
{
    //单例模式定义变量,保证只会实例化一次
    private static App instance;
    private static Context sContext;//全局变量
    //声明一个数组来存储活动
    private static List<Activity> sActivities=new ArrayList<Activity>();
    /******************全局单用户变量(属于某个用户的)********************/
    //声明一个全局变量来记录登录状态
    private static boolean isLogin;
    //声明一个全局变量来引用当前登录的缓存用户对象
    private static MyUser sMyUser;
    //全局缓存地址
    private static File diskCacheDir,mOutputImage;

    //单例模式,避免内存造成浪费,需要实例化该类时才将其实例化
    public static synchronized App getInstance() {
        //网上说Application的instance不能用new
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sContext=getApplicationContext();//获得一个应用程序级别的Context
        /******初始化Bmob后台服务*****/
        //设置BmobConfig
        BmobConfig config =new BmobConfig.Builder(this).
                setConnectTimeout(30).//请求超时时间（单位为秒）：默认15s
                //文件分片上传时每片的大小（单位字节），默认512*1024
                setUploadBlockSize(500*1024).
                setApplicationId(Constants.BmobAppid).//设置appkey
                setFileExpiration(2500)//文件的过期时间(单位为秒)：默认1800s
                .build();
        Bmob.initialize(config);
        //保存当前安装该应用的设备的信息,用该信息来进行设备定向消息推送
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);

        /*****初始化Mob的后台服务*****/
        SMSSDK.initSDK(this,Constants.MobAppkey,Constants.MobAppsecret);
        /***进行判断,标记登录的状态,并装载全局变量***/
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        if(myUser!=null){
            setIsLogin(true);//设置状态为:登录
            setMyUser(myUser);
            App.getInstance().setDiskCacheDir(FileUtil.getDiskCacheDir
                    (App.getInstance().getMyUser().getUsername()));
            //建立一个新的子目录
            if (!App.getInstance().getDiskCacheDir().exists()) {
                App.getInstance().getDiskCacheDir().mkdir();
            }
            /*设置全局使用的头像file对象*/
            App.getInstance().setmOutputImage(new File(App.getInstance().getDiskCacheDir(),
                    "head_"+App.getInstance().getMyUser().getUsername()+".jpg"));
        }else {
            setIsLogin(false);//设置状态为:未登录
            setMyUser(null);
            setDiskCacheDir(null);
            setmOutputImage(null);
        }
    }

    //头像缓存地址
    public static File getmOutputImage() {
        return mOutputImage;
    }
    public static void setmOutputImage(File mOutputImage) {
        App.mOutputImage = mOutputImage;
    }
    //缓存路径
    public static File getDiskCacheDir() {
        return diskCacheDir;
    }
    public static void setDiskCacheDir(File diskCacheDir) {
        App.diskCacheDir = diskCacheDir;
    }
    //登录状态的set和get方法
    public static boolean getIsLogin() {
        return isLogin;
    }
    public static void setIsLogin(boolean isLogin) {
        App.isLogin = isLogin;
    }
    //本地缓存的用户对象的set和get方法
    public static MyUser getMyUser() {
        return sMyUser;
    }
    public static void setMyUser(MyUser myUser) {
        sMyUser = myUser;
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