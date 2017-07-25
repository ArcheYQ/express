package com.express;

import android.app.Application;
import android.content.Context;
import cn.bmob.v3.Bmob;

/**
 * Created by hyc on 2017/7/25 16:53
 */

public class ExpressApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"1258d8b2a3aad308ba4b2214dc870d7b");
        context=getApplicationContext();

    }


}
