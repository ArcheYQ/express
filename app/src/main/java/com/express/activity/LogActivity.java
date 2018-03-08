package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.express.R;
import com.express.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LogActivity extends BaseActivity {

    @Bind(R.id.et_register_username)
    EditText etRegisterUsername;
    @Bind(R.id.et_register_password)
    EditText etRegisterPassword;
    @Bind(R.id.et_register_passwordTrued)
    EditText etRegisterPasswordTrued;
    @Bind(R.id.button_register)
    Button buttonRegister;
    @Bind(R.id.tv_register_login)
    TextView tvRegisterLogin;
    @Bind(R.id.fl_login)
    FrameLayout flLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);
        initData();
        Glide.with(LogActivity.this)
                .load(R.drawable.bg)
                .dontAnimate()
                // 设置高斯模糊
//                .bitmapTransform(new BlurTransformation(this, 14,3))
                .into(new ViewTarget<View, GlideDrawable>(flLogin) {
                    //括号里为需要加载的控件
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }
    private void initData() {
        flLogin = (FrameLayout) findViewById(R.id.fl_login);
    }
    public void registerUser(){
        showProgressDialog();
        User student = new User();
        student.setUsername(etRegisterUsername.getText().toString());
        student.setPassword(etRegisterPasswordTrued.getText().toString());
        student.signUp(new SaveListener<User>() {
            @Override
            public void done(User s, BmobException e) {
                dissmiss();
                if(e==null){
                    Toast.makeText(LogActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LogActivity.this,IDcardActivity.class));
                    finish();
                }else if(e.getErrorCode() == 9016){
                    Toast.makeText(LogActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LogActivity.this, "该学号已经被注册", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @OnClick({R.id.button_register, R.id.tv_register_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_register:

                if (etRegisterUsername.getText().toString().length()==11){
                    if(!etRegisterPassword.getText().toString().equals("")&&
                            !etRegisterPasswordTrued.getText().toString().equals("")){
                        if (etRegisterPassword.getText().toString()
                                .equals(etRegisterPasswordTrued.getText().toString())) {
                            registerUser();


                        }else {
                            Toast.makeText(LogActivity.this, "密码两次输入不一致", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LogActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LogActivity.this, "请填写正确的学号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_register_login:
                startActivity(new Intent(LogActivity.this,LogininActivity.class));
                finish();
                break;
        }
    }
}
