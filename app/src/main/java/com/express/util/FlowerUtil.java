package com.express.util;


import com.express.ExpressApplication;
import com.express.R;
import com.express.bean.Comment;
import com.express.bean.ExpressHelp;
import com.express.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hyc on 2017/8/20 09:07
 */

public class FlowerUtil {
    private List<Comment> list;

    private static FlowerUtil flowerUtil;

    public synchronized static FlowerUtil getInstance(){
        return flowerUtil == null ? new FlowerUtil() : flowerUtil;
    }
    public interface OnQueryCommentListener{
        void onSuccess(List<Comment> comments);
        void onError(String error);
    }
    public void queryFlower(int page, User user , final OnQueryCommentListener listener) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.setLimit(10);//每次查询十个数据
        query.setSkip(10 * page);//跳过前面多少数据
        query.order("-createdAt");//排序，按时间降序
        query.addWhereEqualTo("user",user);
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null){
                    listener.onSuccess(list);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.onError(ExpressApplication
                                .getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        listener.onError(e.getMessage());
                    }
                }
            }
        });
    }

}