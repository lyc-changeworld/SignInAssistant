package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by achuan on 16-10-23.
 * 功能：扩展用户类
 *     对BmobUser类进行扩展，添加一些新的属性
 */
public class MyUser extends BmobUser {

    private String nickName;//昵称
    private Boolean sex;//true代表男,false代表女
    private Integer age;//年龄

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
