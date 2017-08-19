package com.express.util;

import com.express.bean.ExpressHelp;
import com.express.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by 雅倩宝宝 on 2017/8/16.
 */

public class HelpUtil {

    /**
     * 加载正在进行的我的求助
     * @param page 页码
     * @param listener 监听
     */
    public static void myHelp(int page,final ExpressUtil.QueryListener listener){
        BmobQuery<ExpressHelp> query = new BmobQuery<>();
        query.setLimit(10);//每次查询十个数据
        query.setSkip(10*page);//跳过前面多少数据
        query.include("HelpUser");//包括其他表的信息
        query.order("-createdAt");//排序，按时间降序
        List<BmobQuery<ExpressHelp>> queryList = new ArrayList<>();
        BmobQuery<ExpressHelp> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));//筛选出自己发布的求助
        BmobQuery<ExpressHelp> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("state",false);//筛选出还未完成（即正在进行嘛 喵呜）
        queryList.add(query2);
        queryList.add(query1);//添加查询条件1 2
        query.and(queryList);//and或者or 最后我们选择了and
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

    /**
     * 加载正在进行的他人求助
     * @param page 页码
     * @param listener 监听
     */
    public static void otherHelp(int page, final ExpressUtil.QueryListener listener){
        BmobQuery<ExpressHelp> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10*page);
        query.include("user");
        query.order("-createdAt");
        List<BmobQuery<ExpressHelp>> queryList = new ArrayList<>();
        BmobQuery<ExpressHelp> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("HelpUser",BmobUser.getCurrentUser(User.class));
        BmobQuery<ExpressHelp>  query2 = new BmobQuery<>();
        query2.addWhereEqualTo("state",false);
        queryList.add(query1);
        queryList.add(query2);
        query.and(queryList);
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
