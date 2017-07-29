package com.express;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.express.other.GlideImageLoader;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.zxy.tiny.Tiny;

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
        Tiny.getInstance().init(this);
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new GlideImageLoader())
                .setToolbaseColor(Color.parseColor("#108de8"))
                .build());
    }


}
