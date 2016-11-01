package com.example.achuan.bombtest.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by achuan on 16-10-22.
 * 功能：创建BmobObject对象
 *     在Bmob中,BmobObject就相当于数据库中的一张表,每个属性就相当于表的字段
 *    每一个BmobObject对象就相当于表里的一行数据
 */
public class Person extends BmobObject {
    private String  name;
    private String  address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getaddress() {
        return address;
    }

    public void setaddress(String address) {
        this.address = address;
    }
}
