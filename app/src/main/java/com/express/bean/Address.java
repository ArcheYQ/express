package com.express.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 地址信息 实现Serializable这个接口可以通过intent在两个activity中进行传递整个类的信息 Created by hyc on 2017/7/30 09:38
 */

public class Address extends DataSupport implements Serializable{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    /**
     * 地址关联用户的id
     */
    private String userId;
    /**
     * 收件人
     */
    private String receiver;
    /**
     * 收件人的电话号码
     */
    private String phoneNumber;
    /**
     * 详细地址
     */
    private String address;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    /**
     * 是否为默认地址

     */
    @Column()
    private boolean isDefault = false;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
