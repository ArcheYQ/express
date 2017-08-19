package com.express.util;

import com.express.bean.ExpressHelp;
import com.express.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 雅倩宝宝 on 2017/8/17.
 */

public class AllExpressUtil {

    public static void MyAllHelp(int page, final ExpressUtil.QueryListener listener){
        BmobQuery<ExpressHelp> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10*page);
        query.include("HelpUser");//包括其他表的信息
        query.order("-createdAt");//排序，按时间降序
        query.addWhereEqualTo("user",BmobUser.getCurrentUser(User.class));
        query.findObjects(new FindListener<ExpressHelp>() {
            @Override
            public void done(List<ExpressHelp> list, BmobException e) {
                if (e==null){
                    User user = BmobUser.getCurrentUser(User.class);
                    for (ExpressHelp expressHelp : list) {
                        expressHelp.setUser(user);
                    }
                    listener.complete(list);
                }else {
                    if(e.getErrorCode()==9016){
                        listener.fail("没有网络");
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }
    public static void otherAllHelp (int page, final ExpressUtil.QueryListener listener){
        BmobQuery<ExpressHelp> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10*page);
        query.include("user");
        query.order("-createdAt");
        query.addWhereEqualTo("HelpUser",BmobUser.getCurrentUser(User.class));
        query.findObjects(new FindListener<ExpressHelp>() {
            @Override
            public void done(List<ExpressHelp> list, BmobException e) {
                if (e==null){
                    listener.complete(list);
                }else{
                    if (e.getErrorCode()==9016){
                        listener.fail("没有网络");
                    }else{
                        listener.fail(e.getMessage());
                    }
                }
            }
        });

    }
}
