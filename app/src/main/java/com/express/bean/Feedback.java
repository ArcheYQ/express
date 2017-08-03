package com.express.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/7/31 15:44
 */

public class Feedback extends BmobObject{
    /**
     * 用户
     */
    private User user;
    /**
     * 判断是产品建议还是程序错误
     */
    private boolean isSuggestion;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 联系方式
     */
    private String way;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSuggestion() {
        return isSuggestion;
    }

    public void setSuggestion(boolean suggestion) {
        isSuggestion = suggestion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
