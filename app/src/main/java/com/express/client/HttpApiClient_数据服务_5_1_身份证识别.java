//
//  Created by  fred on 2017/1/12.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.express.client;


import com.alibaba.cloudapi.sdk.client.HttpApiClient;
import com.alibaba.cloudapi.sdk.enums.HttpMethod;
import com.alibaba.cloudapi.sdk.enums.Scheme;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;


public class HttpApiClient_数据服务_5_1_身份证识别 extends HttpApiClient {
    public final static String HOST = "dm-51.data.aliyun.com";
    static HttpApiClient_数据服务_5_1_身份证识别 instance = new HttpApiClient_数据服务_5_1_身份证识别();
    public static HttpApiClient_数据服务_5_1_身份证识别 getInstance(){return instance;}

    public void init(HttpClientBuilderParams httpClientBuilderParams){
        httpClientBuilderParams.setScheme(Scheme.HTTP);
        httpClientBuilderParams.setHost(HOST);
        super.init(httpClientBuilderParams);
    }




    public void 印刷文字识别_身份证识别(byte[] body , ApiCallback callback) {
        String path = "/rest/160601/ocr/ocr_idcard.json";
        ApiRequest request = new ApiRequest(HttpMethod.POST_BODY , path, body);
        

        sendAsyncRequest(request , callback);
    }
}