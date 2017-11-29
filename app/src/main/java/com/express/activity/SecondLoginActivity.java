package com.express.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.express.R;
import com.express.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;

public class SecondLoginActivity extends BaseActivity {

    @Bind(R.id.et_login_account)
    EditText etLoginAccount;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    @Bind(R.id.iv_into)
    ImageView ivInto;
    @Bind(R.id.tv_register)
    TextView tvRegister;
    @Bind(R.id.tv_forget)
    TextView tvForget;
    @Bind(R.id.tv_loginRule)
    TextView tvLoginRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_second_login);
        ButterKnife.bind(this);
        etLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                        && KeyEvent.ACTION_DOWN == event.getAction())) {

                    return true;
                }
                return false;
            }
        });
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(etLoginPassword.getText().toString())){
                    ivInto.setVisibility(View.VISIBLE);

                }else{
                    ivInto.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.iv_into, R.id.tv_register, R.id.tv_forget, R.id.tv_loginRule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_into:
                BmobUser bu2 = new BmobUser();
                bu2.setUsername(etLoginAccount.getText().toString());
                bu2.setPassword(etLoginPassword.getText().toString());
                showProgressDialog();
                bu2.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            Toast.makeText(SecondLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SecondLoginActivity.this, MainActivity.class));
                            finish();
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                        }else{
                            dissmiss();
                            Toast.makeText(SecondLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.tv_register:
                if (!etLoginAccount.getText().toString().isEmpty()&&!etLoginPassword.getText().toString().isEmpty()){
                    BmobUser bu = new BmobUser();
                    bu.setUsername(etLoginAccount.getText().toString());
                    bu.setPassword(etLoginPassword.getText().toString());
                    showProgressDialog();
                    bu.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                Toast.makeText(SecondLoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SecondLoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    startActivity(new Intent(SecondLoginActivity.this, ImfamationActivity.class));
                    finish();
                }else{
                    dissmiss();
                    Toast.makeText(SecondLoginActivity.this, "请输入您的学号或者密码进行注册", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_forget:
                break;
            case R.id.tv_loginRule:
                showNormalDialog();
                break;
        }
    }
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.rule);
        normalDialog.setTitle("登录说明n(*≧▽≦*)n");
        normalDialog.setMessage("账号说明：账号为学号，注册后无法修改。");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });

        // 显示
        normalDialog.show();
    }
}
