package com.express.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.express.activity.ChatActivity;
import com.express.bean.User;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by 雅倩宝宝 on 2017/8/12.
 */

public class ConversationUtil {

    private static ConversationUtil conversationUtil;

    private boolean isConnect = false;

    public static synchronized ConversationUtil getInstance(){
        if (conversationUtil ==  null){
            conversationUtil = new ConversationUtil();
        }
        return conversationUtil;
    }

    /**
     * 连接服务器 ，使用bmob sdk提供的连接服务器的方法需要注意
     * ，若是第一次连接服务器因没有网络出错，之后重启网络不会
     * 自动连接服务器，没有错误则会自动连接服务器。所以用hasError
     * 判断需不需要再次调用连接方法。
     */
    public void connect(){
        if (!isConnect){
            BmobIM.connect(BmobUser.getCurrentUser(User.class).getObjectId(),new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null){
                        isConnect = true;
                    }else {
                        isConnect = false;
                    }
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
                    Log.i("ganma","连接状态"+isConnect+"  "+connectionStatus.getCode());
                }
            });
        }



    }
    public void OpenWindow(BmobIMUserInfo info, final Context context){

        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if(e==null){
                    Intent intent = new Intent(context, ChatActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("start",c);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "开启会话出错", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public boolean isConnect() {
        return isConnect;
    }
}
