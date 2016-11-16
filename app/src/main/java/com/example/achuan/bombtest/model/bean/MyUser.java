package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by achuan on 16-10-23.
 * 功能：扩展用户类
 *     对BmobUser类进行扩展，添加一些新的属性
 */
public class MyUser extends BmobUser {
    //扩展的信息
    private String nickName;//昵称
    private String sex;//性别
    private Integer age;//年龄
    private String signature;//个性签名

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

