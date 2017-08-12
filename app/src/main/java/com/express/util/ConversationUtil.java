package com.express.util;

import com.express.bean.User;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 雅倩宝宝 on 2017/8/12.
 */

public class ConversationUtil {

    private static ConversationUtil conversationUtil;

    private boolean isConnect = false;
    /**
     * 判断第一次连接服务器时是否出现错误
     */
    private boolean hasError = false;

    public static synchronized ConversationUtil getInstance(){
        return conversationUtil == null ? new ConversationUtil() : conversationUtil;
    }

    /**
     * 连接服务器 ，使用bmob sdk提供的连接服务器的方法需要注意
     * ，若是第一次连接服务器因没有网络出错，之后重启网络不会
     * 自动连接服务器，没有错误则会自动连接服务器。所以用hasError
     * 判断需不需要再次调用连接方法。
     */
    public void connect(){
        if (!hasError){
            BmobIM.connect(BmobUser.getCurrentUser(User.class).getObjectId(),new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    hasError = (e != null && e.getErrorCode() == 9016);
                }
            });

            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus connectionStatus) {
                    switch (connectionStatus.getCode()){
                        //断开连接
                        case 0:
                            isConnect = false;
                            break;
                        //正在连接
                        case 1:
                            break;
                        //连接成功
                        case 2:
                            isConnect = true;
                            break;
                        //网络不可用
                        case -1:
                            break;
                        //另一台设备登录
                        case -2:
                            isConnect = false;
                            break;
                    }
                }
            });
        }
    }

}
