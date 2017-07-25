package com.express.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.express.R;

import qiu.niorgai.StatusBarCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_login);
        ImageView imageView = (ImageView) findViewById(R.id.iv_login_background);
//        Glide.with(this)
//                .load(R.drawable.mainbg)
//                .bitmapTransform(new BlurTransformation(this, 23, 4))//高斯模糊
//                .into(imageView);
    }
}
