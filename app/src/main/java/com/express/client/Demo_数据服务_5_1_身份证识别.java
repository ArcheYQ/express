//
//  Created by  fred on 2016/10/26.
//  Copyright © 2016年 Alibaba. All rights reserved.
//

package com.express.client;

import android.util.Log;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.alibaba.cloudapi.sdk.model.HttpClientBuilderParams;
import com.express.util.IDUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Demo_数据服务_5_1_身份证识别 {

    public static final String TAG = "数据服务";

    static{
        //HTTP Client init
        HttpClientBuilderParams httpParam = new HttpClientBuilderParams();
        httpParam.setAppKey("24801358");
        httpParam.setAppSecret("8f229380104af06b4ef80be7c8579b42");
        HttpApiClient_数据服务_5_1_身份证识别.getInstance().init(httpParam);


        //HTTPS Client init
        HttpClientBuilderParams httpsParam = new HttpClientBuilderParams();
        httpsParam.setAppKey("24801358");
        httpsParam.setAppSecret("8f229380104af06b4ef80be7c8579b42");

        /**
        * 以HTTPS方式提交请求
        * 本DEMO采取忽略证书的模式,目的是方便大家的调试
        * 为了安全起见,建议采取证书校验方式
        */
        X509TrustManager xtm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        httpsParam.setSslSocketFactory(sslContext.getSocketFactory());
        httpsParam.setX509TrustManager(xtm);
        httpsParam.setHostnameVerifier(DO_NOT_VERIFY);

        HttpsApiClient_数据服务_5_1_身份证识别.getInstance().init(httpsParam);


    }

    public static void 印刷文字识别_身份证识别HttpTest(String base){

        String real = "{\n" +
                "\t\"image\":\""+base+"\",\n" +
                "\t\t\t\t\"configure\":{\n" +
                "\t\t\t\t\t\"side\":\"face\"\n" +
                "\t\t\t\t}\n" +
                "}";
        real.replace("123",base);
        IDUtil.saveFile(real);
        Log.d(TAG, "印刷文字识别_身份证识别HttpTest: -->:"+real);
        HttpApiClient_数据服务_5_1_身份证识别.getInstance().印刷文字识别_身份证识别(real.getBytes(SdkConstant.CLOUDAPI_ENCODING) , new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {
                    String sb = getResultString(response);
                    Log.d(TAG, "onResponse: *******************: "+sb);
                    System.out.println(sb);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

    public static void 印刷文字识别_身份证识别HttpsTest(){
        HttpsApiClient_数据服务_5_1_身份证识别.getInstance().印刷文字识别_身份证识别("test".getBytes(SdkConstant.CLOUDAPI_ENCODING) , new ApiCallback() {
            @Override
            public void onFailure(ApiRequest request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ApiRequest request, ApiResponse response) {
                try {

                    System.out.println(getResultString(response));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }



    public static String getResultString(ApiResponse response) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append("【服务器返回结果为】").append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        result.append("ResultCode:").append(SdkConstant.CLOUDAPI_LF).append(response.getCode()).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        if(response.getCode() != 200){
            result.append("错误原因：").append(response.getHeaders().get("X-Ca-Error-Message")).append(SdkConstant.CLOUDAPI_LF).append(SdkConstant.CLOUDAPI_LF);
        }

        result.append("ResultBody:").append(SdkConstant.CLOUDAPI_LF).append(new String(response.getBody() , SdkConstant.CLOUDAPI_ENCODING));

        return result.toString();
    }
}
