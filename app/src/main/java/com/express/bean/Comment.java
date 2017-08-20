package com.express.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by hyc on 2017/8/20 16:13
 */

public class Comment extends BmobObject {
    public User getValuer() {
        return valuer;
    }

    public void setValuer(User valuer) {
        this.valuer = valuer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 评价者
     */
    private User valuer;
    /**
     * 被评价者（评论拥有者）
     */
    private User user;

    /**
     * 评论
     */
    private String comment;
}
