package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by achuan on 16-10-23.
 * 功能：扩展用户类
 *     对BmobUser类进行扩展，添加一些新的属性
 */
public class MyUser extends BmobUser {
    //Student表数据元素
    private String Sno;//学号
    private String Sname;//真实姓名
    private String Ssex;//性别
    private Integer Sage;//年龄
    private String Sdept;//院系
    //private String SphoneNum;//手机号码,user表中默认存在了

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }

    public String getSname() {
        return Sname;
    }

    public void setSname(String sname) {
        Sname = sname;
    }


    public Integer getSage() {
        return Sage;
    }

    public void setSage(Integer sage) {
        Sage = sage;
    }

    public String getSdept() {
        return Sdept;
    }

    public void setSdept(String sdept) {
        Sdept = sdept;
    }

    public String getSsex() {
        return Ssex;
    }

    public void setSsex(String ssex) {
        Ssex = ssex;
    }
}
