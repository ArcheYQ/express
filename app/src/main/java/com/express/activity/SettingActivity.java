package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.express.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


public class SettingActivity extends BaseActivity {


    @Bind(R.id.tb_setting)
    Toolbar tbSetting;
    @Bind(R.id.rl_password)
    RelativeLayout rlPassword;
    @Bind(R.id.tv_user_number)
    TextView tvUserNumber;
    @Bind(R.id.rl_phone_number)
    RelativeLayout rlPhoneNumber;
    @Bind(R.id.rl_notice)
    RelativeLayout rlNotice;
    @Bind(R.id.rl_current_version)
    RelativeLayout rlCurrentVersion;
    @Bind(R.id.rl_version_update)
    RelativeLayout rlVersionUpdate;
    @Bind(R.id.tv_exit_login)
    TextView tvExitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_setting);
        initHome();
    }

    @OnClick({R.id.rl_password, R.id.rl_notice, R.id.tv_exit_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_password:
                startActivity(new Intent(this,PasswordActivity.class));
                break;
            case R.id.rl_notice:
                startActivity(new Intent(this,NoticeActivity.class));
                break;
            case R.id.tv_exit_login:
                BmobUser.logOut();
                finishAll();
                startActivity(new Intent(this,SecondLoginActivity.class));
                finish();
                break;
        }
    }
}
