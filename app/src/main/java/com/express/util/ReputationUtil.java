package com.express.util;

import com.express.bean.ExpressHelp;
import com.express.bean.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hyc on 2017/8/19 19:04
 */

public class ReputationUtil {


    public static void reputationSum (final ExpressUtil.QueryListener listener){
        BmobQuery<ExpressHelp> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("HelpUser", BmobUser.getCurrentUser(User.class));
        bmobQuery.addQueryKeys("weight");
        bmobQuery.findObjects(new FindListener<ExpressHelp>() {
            @Override
            public void done(List<ExpressHelp> object, BmobException e) {
                if(e==null){
                    listener.complete(object);
                }else{
                    if (e.getErrorCode()==9016){
                        listener.fail("网络不给力/(ㄒoㄒ)/~~");
                    }else{
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }
}
