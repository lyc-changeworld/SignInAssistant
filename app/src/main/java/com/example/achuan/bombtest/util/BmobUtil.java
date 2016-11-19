package com.example.achuan.bombtest.util;

import android.widget.Toast;

import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.model.bean.CourseBean;
import com.example.achuan.bombtest.model.bean.MyUser;
import com.example.achuan.bombtest.model.bean.SigninRecordBean;
import com.example.achuan.bombtest.model.bean.StudentBean;
import com.example.achuan.bombtest.model.bean.TeacherBean;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by achuan on 16-10-22.
 * 功能：Bmob后台数据操作封装类
 */
public class BmobUtil {
    private static String TAG="lyc-bmob";

    /************************** 1-用户管理************************/
    /***.1 用户注册***/
    //设置用户名==手机号码
    public static MyUser userSignUp(String phone, String password){
        //BmobUser bmobUser=new BmobUser();
        MyUser bmobUser=new MyUser();
        bmobUser.setUsername(phone);//设置用户名==手机号码
        //这里直接将用户注册时输入的密码上传到服务器,后续将实现加密处理后提交
        bmobUser.setPassword(password);//设置密码
        bmobUser.setMobilePhoneNumber(phone);//设置手机号码
        bmobUser.setNickName(phone);//初始化昵称＝＝手机号
        bmobUser.setSex("男");
        bmobUser.setAge(0);
        bmobUser.setEmail("xxx@xxx.com");
        bmobUser.setSignature("编辑个性签名");
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
    public static MyUser userLogin(String userName, String password){
        MyUser bmobUser= new MyUser();
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
    public static BmobQuery<MyUser> userQuery(String userName){
        final BmobQuery<MyUser> query = new BmobQuery<MyUser>();
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
    /***.6 根据键值更新用户对应的信息***/
    public static MyUser userBmobUpdate(String key,Object value){
        MyUser myUser=new MyUser();
        myUser.setValue(key,value);
        /*bmobUser.update(, new UpdateListener() {
            @Override
            public void done(BmobException e) {
            }
        });*/
        //修改数据只能通过objectId来修改，目前不提供查询条件方式的修改方法。
        return myUser;
    }


     /**************************2-课程查询相关************************/
    /***. 1查询全部的课程***/
    public static BmobQuery<CourseBean> courseBmobQueryAll(){
        final BmobQuery<CourseBean> query = new BmobQuery<CourseBean>();
        // 根据Semester字段升序显示数据（由小到大）
        //query.order("Semester");
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
    /***. 2查询包含输入关键字的课程***/
    public static BmobQuery<CourseBean> courseBmobQueryFromKeyword(String keyword){
        final BmobQuery<CourseBean> query = new BmobQuery<CourseBean>();
        /*目前模糊查询已经改成收费用户才能使用了*/
        //查询Cname字段的值含有keyword关键字的数据
        query.addWhereContains("Cname", keyword);
        //模糊查询
        //String bql ="select * from CourseBean where Cname like '%"+keyword+"%'";
        //select * from GameScore where name like 'smile%'
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

    /**************************3-签到相关************************/
    /***.　1查询全部的教师数据***/
    public static BmobQuery<TeacherBean> teacherBmobQueryAll(){
        final BmobQuery<TeacherBean> query = new BmobQuery<TeacherBean>();
        // 根据Semester字段升序显示数据（由小到大）
        //query.order("Semester");
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
    /***. 2添加数据到签到记录表中***/
    public static SigninRecordBean signinDetailBmobSave(String Sno,String Cno,String Tno){
        SigninRecordBean signinRecordBean=new SigninRecordBean();
        signinRecordBean.setSno(Sno);
        signinRecordBean.setCno(Cno);
        signinRecordBean.setTno(Tno);
        /*signinRecordBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                }else{
                }
            }
        });*/
        return signinRecordBean;
    }

    /**************************4-学生信息相关************************/
    /***. 1通过手机号来查询对应的学生数据是否存在***/
    public static BmobQuery<StudentBean> studentBmobQuery(String mobilePhoneNumber){
        final BmobQuery<StudentBean> query = new BmobQuery<StudentBean>();
        query.addWhereEqualTo("mobilePhoneNumber", mobilePhoneNumber);//一个用户名对应一个用户
        /*query.findObjects(new FindListener<StudentBean>() {
            @Override
            public void done(List<StudentBean> list, BmobException e) {
                if(e==null){
                    if (list.size()>0){
                        //提示学生信息已经存在,无需构造新的一行
                    }else {
                        //不存在,接着构造新的一行学生数据

                    }
                }else{
                }
            }
        });*/
        return query;
    }
    /***.2 添加一条只带手机号的数据到学生信息表中***/
    public static StudentBean StudentBmobSave(String mobilePhoneNumber){
        StudentBean studentBean=new StudentBean();
        studentBean.setMobilePhoneNumber(mobilePhoneNumber);
        /*studentBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                }else{
                }
            }
        });*/
        return studentBean;
    }
    /***.3 根据键值更新学生成员对应的信息***//*
    public static StudentBean StudentBmobUpdate(String key,String value){
        StudentBean studentBean=new StudentBean();
        studentBean.setValue(key,value);
        *//*studentBean.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                }else{
                }
            }
        });*//*
        return studentBean;
    }*/

    /**************************5-文件管理相关************************/
    /***.　1上传单一文件***/
    public static BmobFile fileBmobUpload(String picPath){
        //String picPath = "sdcard/temp.jpg";
        BmobFile bmobFile = new BmobFile(new File(picPath));
        /*bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整后台访问地址
                }else{
                }
            }
            @Override
            public void onProgress(Integer value) {
                //super.onProgress(value);
                // 返回的上传进度（百分比）
            }
        });*/
        return  bmobFile;
    }
    /***.　2删除单一文件***/
    public static BmobFile fileBmobDelete(String headUrl){
        BmobFile file = new BmobFile();
        file.setUrl(headUrl);//此url是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        /*file.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                }else{
                }
            }
        });*/
        return file;
    }
    /***.　3下载单一文件***/
    public static BmobFile fileBmobDownload(String fileName,String group,String headUrl){
        BmobFile bmobfile =new BmobFile(fileName,group,headUrl);
        /*bmobfile.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
            }
            @Override
            public void onProgress(Integer integer, long l) {
            }
        });*/
        return bmobfile;
    }

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
}
