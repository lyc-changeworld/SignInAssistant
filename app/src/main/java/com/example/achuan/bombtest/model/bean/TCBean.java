package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by achuan on 16-11-12.
 * 功能：教工和课程信息相关的表的数据模型类
 */
public class TCBean extends BmobObject {

    private String Tno;//教工号
    private String Cno;//课程号

    public String getTno() {
        return Tno;
    }

    public void setTno(String tno) {
        Tno = tno;
    }

    public String getCno() {
        return Cno;
    }

    public void setCno(String cno) {
        Cno = cno;
    }
}
