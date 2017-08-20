package com.express.api;

import com.express.bean.UserBean;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by hyc on 2017/5/10 15:40
 */

public interface Api {
    String baseUri = "http://218.75.197.121:8888/api/v1/";

    @GET("get/login/{username}/{password_login}")
    Observable<UserBean> login(@Path("username") String username, @Path("password_login") String password);
}



