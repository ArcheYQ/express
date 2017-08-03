package com.express.util;

import com.express.ExpressApplication;
import com.express.R;
import com.express.bean.ExpressHelp;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by hyc on 2017/8/3 09:39
 */

public class ExpressUtil {

    private List<ExpressHelp> list;

    private static ExpressUtil expressUtil;

    public synchronized static ExpressUtil getInstance(){
        return expressUtil == null ? new ExpressUtil() : expressUtil;
    }

    public interface QueryListener{
        void complete(List<ExpressHelp> expressHelps);
        void fail(String error);
    }

    /**
     * 分页查询快递求助信息，查询正在等待帮助的快递信息。
     * @param page 页码，起始页下标为0，表示第一页数据。
     * @param listener 查询监听，查询成功后进行数据返回。
     */
    public void queryExpressHelp(int page , final QueryListener listener){
        BmobQuery<ExpressHelp> query = new BmobQuery<>();
        query.setLimit(10);
        query.setSkip(10*page);
        query.include("user");
        query.order("-createdAt");
        query.addWhereEqualTo("state",false);
        query.findObjects(new FindListener<ExpressHelp>() {
            @Override
            public void done(List<ExpressHelp> data, BmobException e) {
                if (e == null){
                    listener.complete(data);
                }else {
                    if (e.getErrorCode() == 9016){
                        listener.fail(ExpressApplication
                                .getContext()
                                .getString(R.string.err_no_net));
                    }else {
                        listener.fail(e.getMessage());
                    }
                }
            }
        });
    }

    



}
