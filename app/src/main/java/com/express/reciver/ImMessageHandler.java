package com.express.reciver;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/**
 * Created by 雅倩宝宝 on 2017/8/14.
 */

public class ImMessageHandler extends BmobIMMessageHandler {
    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        EventBus.getDefault().post(messageEvent);

    }

    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
        //离线消息
    }
}
