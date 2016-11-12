package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by achuan on 16-11-12.
 * 功能：教工信息的数据模型类
 */
public class TeacherBean extends BmobObject {
    //Teacher课程表数据类型
    private String Tno;//教工号
    private String Tname;//教工名
    private String Tsex;//性别
    private Integer Tage;//年龄
    private String Tdept;//院系

    public String getTno() {
        return Tno;
    }

    public void setTno(String tno) {
        Tno = tno;
    }

    public String getTname() {
        return Tname;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public String getTsex() {
        return Tsex;
    }

    public void setTsex(String tsex) {
        Tsex = tsex;
    }

    public Integer getTage() {
        return Tage;
    }

    public void setTage(Integer tage) {
        Tage = tage;
    }

    public String getTdept() {
        return Tdept;
    }

    public void setTdept(String tdept) {
        Tdept = tdept;
    }
}
