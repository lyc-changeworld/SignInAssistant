package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by achuan on 16-11-12.
 * 功能：签到记录的单行的数据的模型类
 *      签到记录中包括：
 *      　　　签到的学生号\签到的课程\带课老师\签到时间(数据创建的时间)
 */
public class SigninRecordBean extends BmobObject {

    /*签到记录中包括：
 *      　　　签到的学生号\签到的课程\带课老师\签到时间(数据创建的时间)*/
    private String Sno;//学生号
    private String Cno;//课程号
    private String Tno;//教工号
    //签到时间即数据创建添加的时间(createdAt)

    public String getCno() {
        return Cno;
    }

    public void setCno(String cno) {
        Cno = cno;
    }

    public String getTno() {
        return Tno;
    }

    public void setTno(String tno) {
        Tno = tno;
    }

    public String getSno() {
        return Sno;
    }

    public void setSno(String sno) {
        Sno = sno;
    }


}
