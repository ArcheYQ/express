package com.express.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.express.R;
import com.express.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class PasswordActivity extends BaseActivity {

    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.btn_update_password)
    Button btnUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_password);
        initHome();
    }

    @OnClick(R.id.btn_update_password)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etOldPassword.getText().toString())){
            Toast.makeText(mActivity, "请输入原密码", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(etNewPassword.getText().toString())){
            Toast.makeText(mActivity, "请输入新密码", Toast.LENGTH_SHORT).show();
        }else {
            showProgressDialog();
            BmobUser.getCurrentUser(User.class).updateCurrentUserPassword(etOldPassword.getText().toString(), etNewPassword.getText().toString(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    dissmiss();
                    if (e==null){
                        Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        if (e.getErrorCode() == 9016){
                            Toast.makeText(mActivity, "网络不给力", Toast.LENGTH_SHORT).show();
                        }else if (e.getErrorCode() == 210){
                            Toast.makeText(mActivity, "原密码不正确", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

    }
}
