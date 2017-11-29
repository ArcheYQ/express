package com.express.bean;

import android.text.TextUtils;

import cn.bmob.v3.BmobUser;

/**
 * Created by hyc on 2017/7/25 15:59
 */


public class User extends BmobUser {

    private String number;
    private String highSchool;
    private String name;
    private String nickname;
    private String depName;
    private String className;
    private String address;
    private String headPic;
    private String gender;
    private String userId;
    private String headPicThumb;
    private String profile;
    private String background;
    private int sum;

    public String[] getComment() {
        return comment;
    }

    public void setComment(String[] comment) {
        this.comment = comment;
    }

    private String[] comment;
    public int getHelpSum() {
        return helpSum;
    }

    public void setHelpSum(int helpSum) {
        this.helpSum = helpSum;
    }

    private int helpSum;
    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public User(int sum, int helpSum){
        this.sum =sum;
        this.helpSum = helpSum;
    }

    public User(){
        sum = 0;
        helpSum= 5;
        headPicThumb ="";
    }

//    public User(UserBean.DataBean dataBean){
//        number = dataBean.getStudentKH();
//        highSchool = dataBean.getSchool();
//        name = dataBean.getTrueName();
//        nickname = dataBean.getUsername();
//        depName = dataBean.getDep_name();
//        className = dataBean.getClass_name();
//        address = dataBean.getAddress();
//        gender  = dataBean.getSex();
//        if(TextUtils.isEmpty(dataBean.getHead_pic())){
//            if (dataBean.getSex().equals("男")){
//                headPic = "http://bmob-cdn-13164.b0.upaiyun.com/2017/09/04/b1b8899cc0934c899bc86f88bafdf302.jpg";
//                headPicThumb= "http://bmob-cdn-13164.b0.upaiyun.com/2017/09/04/b1b8899cc0934c899bc86f88bafdf302.jpg";}
//            else{
//                headPic = "http://bmob-cdn-13164.b0.upaiyun.com/2017/09/04/ebc8bb9661b14708a94e644fe0037901.jpg";
//                headPicThumb = "http://bmob-cdn-13164.b0.upaiyun.com/2017/09/04/ebc8bb9661b14708a94e644fe0037901.jpg";
//            }
//        }else{
//            headPic = "http://218.75.197.121:8888"+dataBean.getHead_pic();
//            headPicThumb = "http://218.75.197.121:8888"+dataBean.getHead_pic_thumb();
//        }
//
//
//        userId = dataBean.getUser_id();
//        headPicThumb = "http://218.75.197.121:8888"+dataBean.getHead_pic_thumb();
//        sum = 0;
//        helpSum= 5;
//    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHighSchool() {
        return highSchool;
    }

    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHeadPicThumb() {
        return headPicThumb;
    }

    public void setHeadPicThumb(String headPicThumb) {
        this.headPicThumb = headPicThumb;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
