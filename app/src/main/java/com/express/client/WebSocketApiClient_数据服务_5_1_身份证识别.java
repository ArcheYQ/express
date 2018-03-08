//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.express.client;

import com.alibaba.cloudapi.sdk.client.WebSocketApiClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.WebSocketClientBuilderParams;


public class WebSocketApiClient_数据服务_5_1_身份证识别 extends WebSocketApiClient {
    public final static String HOST = "dm-51.data.aliyun.com";
    static WebSocketApiClient_数据服务_5_1_身份证识别 instance = new WebSocketApiClient_数据服务_5_1_身份证识别();
    public static WebSocketApiClient_数据服务_5_1_身份证识别 getInstance(){return instance;}


    public void init(WebSocketClientBuilderParams websocketClientBuilderParams){
        websocketClientBuilderParams.setScheme(Scheme.WEBSOCKET);
        websocketClientBuilderParams.setHost(HOST);
        super.init(websocketClientBuilderParams);
    }



    public void 印刷文字识别_身份证识别(byte[] body , ApiCallback callback) {
        String path = "/rest/160601/ocr/ocr_idcard.json";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        
        sendAsyncRequest(request , callback);
    }
}