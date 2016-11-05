package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by achuan on 16-10-22.
 * 功能：创建BmobObject对象
 *     在Bmob中,BmobObject就相当于数据库中的一张表,每个属性就相当于表的字段
 *    每一个BmobObject对象就相当于表里的一行数据
 */
public class CourseBean extends BmobObject {
    //Course课程表数据类型
    private String Cno;//课程号
    private String Cname;//课程名
    private Double Credit;//学分:0~6
    private Integer Semester;//学期：1~8

    public String getCno() {
        return Cno;
    }

    public void setCno(String cno) {
        Cno = cno;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public Double getCredit() {
        return Credit;
    }

    public void setCredit(Double credit) {
        Credit = credit;
    }

    public Integer getSemester() {
        return Semester;
    }

    public void setSemester(Integer semester) {
        Semester = semester;
    }
}
