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

import com.express.R;
import com.express.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

public class LogininActivity extends BaseActivity {

    @Bind(R.id.et_login_username)
    EditText etLoginUsername;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.button_login)
    Button buttonLogin;
    @Bind(R.id.tv_login_register)
    TextView tvLoginRegister;
    @Bind(R.id.fl_login)
    FrameLayout flLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_loginin);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.button_login, R.id.tv_login_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                loginUser();
                break;
            case R.id.tv_login_register:
                startActivity(new Intent(LogininActivity.this,LogActivity.class));
                finish();
                break;
        }
    }

    private void loginUser() {
        User student = new User();
        student.setUsername(etLoginUsername.getText().toString());
        student.setPassword(etLoginPassword.getText().toString());
        showProgressDialog();
        student.login(new SaveListener<User>() {
            @Override
            public void done(User bmobUser, BmobException e) {
                dissmiss();
                if (e == null) {
                    //success;
                } else if (e.getErrorCode() == 9016) {
                    Toast.makeText(LogininActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogininActivity.this, "请检查你的账号密码是否正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
