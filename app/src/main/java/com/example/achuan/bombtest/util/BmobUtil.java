package com.example.achuan.bombtest.util;

import android.widget.Toast;

import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.model.bean.MyUser;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by achuan on 16-10-22.
 * 功能：Bmob后台数据操作封装类
 */
public class BmobUtil {
    private static String TAG="lyc-bmob";

    /************************** 用户管理************************/
    /***.1 用户注册***/
    //设置用户名==手机号码
    public static BmobUser userSignUp(String phone, String password){
        BmobUser bmobUser=new BmobUser();
        bmobUser.setUsername(phone);//设置用户名==手机号码
        //这里直接将用户注册时输入的密码上传到服务器,后续将实现加密处理后提交
        bmobUser.setPassword(password);//设置密码
        bmobUser.setMobilePhoneNumber(phone);//设置手机号码
        //bmobUser.setEmail(email);//设置邮箱地址
        /*//注意：不能用save方法进行注册
        bmobUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e==null){
                }else{
                }
            }
        });*/
        return bmobUser;
    }
    /***.2 用户登录***/
    //通过：用户名+密码
    public static BmobUser userLogin(String userName, String password){
        BmobUser  bmobUser= new BmobUser();
        bmobUser.setUsername(userName);
        bmobUser.setPassword(password);
        /*bmobUser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                    //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                }else{
                }
            }
        });*/
        return bmobUser;
    }
    /***.3 查询用户***/
    //根据用户名来查询
    public static BmobQuery<BmobUser> userQuery(String userName){
        final BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", userName);//一个用户名对应一个用户
        /*query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object,BmobException e) {
                if(e==null){
                }else{
                }
            }
        });*/
        return query;
    }
    /***.4 退出登录***/
    public static void userLogOut(){
        BmobUser.logOut();   //清除缓存用户对象
    }
    /***.5 当前用户***/
    //如果用户在每次打开你的应用程序时都要登录，这将会直接影响到你应用的用户体验。
    // 为了避免这种情况，你可以使用缓存的CurrentUser对象。缓存的用户有效期为1年。
    //每当你应用的用户注册成功或是第一次登录成功，都会在本地磁盘中有一个缓存的用户对象，
    // 这样，你可以通过获取这个缓存的用户对象来进行登录：
        /*BmobUser bmobUser = BmobUser.getCurrentUser();
        if(bmobUser != null){
            // 允许用户使用应用
        }else{
            //缓存用户对象为空时， 可打开用户注册界面…
        }*/
    //在扩展了用户类的情况下获取当前登录用户，可以使用如下的示例代码：
    //MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
    //自V3.4.5版本开始，SDK新增了getObjectByKey(context,key)方法从本地缓存中+
    // 获取当前登陆用户某一列的值。其中key为用户表的指定列名。
    //BmobUser中的特定属性
        /*String username = (String) BmobUser.getObjectByKey("username");
        //MyUser中的扩展属性
        Integer age = (Integer) BmobUser.getObjectByKey("age");
        Boolean sex = (Boolean) BmobUser.getObjectByKey("sex");*/
    /***.6 更新用户***/
    //很多情况下你可能需要修改用户信息，比如你的应用具备修改个人资料的功能
    //新建一个用户对象，并调用update(objectId,updateListener)方法来更新（推荐使用），示例：
    /*BmobUser newUser = new BmobUser();
    newUser.setEmail("xxx@163.com");
    BmobUser bmobUser = BmobUser.getCurrentUser(App.getContext());
    newUser.update(bmobUser.getObjectId(),new UpdateListener() {
        @Override
        public void done(BmobException e) {
            if(e==null){
            }else{
            }
        }
    });*/





    /***.5 邮箱重置密码***/
    //邮箱重置密码的流程如下：
    //1-用户输入他们的电子邮件，请求重置自己的密码。
    //2-Bmob向他们的邮箱发送一封包含特殊的密码重置链接的电子邮件。
    //3-用户根据向导点击重置密码连接，打开一个特殊的Bmob页面，根据提示他们可以输入一个新的密码。
    //4-用户的密码已被重置为新输入的密码。
    public static void resetPasswordByEmail(final String email){
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(App.getInstance().getContext(),
                            "重置密码请求成功，请到" + email + "邮箱进行密码重置操作",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(App.getInstance().getContext(),
                            "失败:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /***.7 密码修改***/
    //自V3.4.3版本开始，SDK为开发者提供了直接修改当前用户登录密码的方法，只需要传入旧密码和新密码，
    // 然后调用BmobUser提供的静态方法updateCurrentUserPassword即可，以下是示例：
    public static void changePassword(String oldPassword,String newPassword){
        BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(App.getInstance().getContext(),"密码修改成功，可以用新密码进行登录啦",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(App.getInstance().getContext(),"失败:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*********************6.8 邮箱相关*******************/
    /***6.8.1 邮箱登录***/
    //新增邮箱+密码登录方式,可以通过loginByAccount方法来操作：
    public static void userLogInByEmail(String email,String password){
        BmobUser.loginByAccount(email, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if(user!=null){
                    LogUtil.d(TAG,"用户登陆成功");
                }
            }
        });
    }
    /***6.8.2 请求验证Email***/
    //当一个用户的邮件被新添加或者修改过的话，emailVerified会被默认设为false，
    // 如果应用设置中开启了邮箱认证功能，Bmob会对用户填写的邮箱发送一个链接,
    // 这个链接可以把emailVerified设置为 true.
    public static void requestEmailVerify(final String email){
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(App.getInstance().getContext(),
                            "请求验证邮件成功，请到" + email + "邮箱中进行激活。",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(App.getInstance().getContext(),
                            "失败:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
