package com.express.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.express.R;
import com.express.bean.User;

import java.nio.channels.ScatteringByteChannel;

import qiu.niorgai.StatusBarCompat;


public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_first);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (User.getCurrentUser(User.class) !=null){
                    startActivity(new Intent(FirstActivity.this, MainActivity.class));
                }else {
                    startActivity(new Intent(FirstActivity.this, SecondLoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}
