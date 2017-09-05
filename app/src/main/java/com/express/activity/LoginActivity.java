package com.express.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.express.R;
import com.express.api.Api;
import com.express.bean.User;
import com.express.bean.UserBean;
import com.express.http.ApiCallback;
import com.express.http.ApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import qiu.niorgai.StatusBarCompat;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends BaseActivity {
    protected Api apiStores;
    @Bind(R.id.et_login_account)
    EditText etLoginAccount;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.iv_login_background)
    ImageView ivLoginBackground;
    @Bind(R.id.tv_forget)
    TextView tvForget;
    @Bind(R.id.tv_loginRule)
    TextView tvLoginRule;
    @Bind(R.id.iv_into)
    ImageView ivInto;
    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        apiStores = ApiClient.retrofit().create(Api.class);
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

    public void login(final String username, final String password) {
        addSubscription(apiStores.login(username, password), new ApiCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean model) {
                if (model.getMsg().equals("ok")) {
                    User user = new User(model.getData());
                    user.setUsername(username);
                    user.setPassword(password);
                    synchroBmob(user);
                } else {
                    Toast.makeText(LoginActivity.this, "请确认账号密码是否正确", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String msg) {
                Log.i("测试", msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void synchroBmob(final User user) {
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                if (e == null || e.getErrorCode() == 202) {
                    e = null;
                    loginBmob(user);
                } else {
                    Log.i("测试", e.getErrorCode() + " " + e.getMessage());
                }
            }
        });
    }

    public void loginBmob(User user) {
        user.login(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                dissmiss();
                if (e == null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(mActivity, "请确认账号密码是否正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onUnSubscribe();
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }


    @OnClick({R.id.tv_register, R.id.tv_forget, R.id.tv_loginRule,R.id.iv_into})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                break;
            case R.id.tv_loginRule:
                showNormalDialog();
                break;
            case R.id.tv_register:
                showProgressDialog();
                login(etLoginAccount.getText().toString(), etLoginPassword.getText().toString());
                break;
            case R.id.iv_into:
                showProgressDialog();
                User user = new User();
                user.setUsername(etLoginAccount.getText().toString());
                user.setPassword(etLoginPassword.getText().toString());
                loginBmob(user);
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
        normalDialog.setMessage("密码说明：密码默认为身份证后6位。若在工大助手上改过密码，请使用更改后的密码登录。\n首次登录的同学请点击第一次登录，否则无法登录。");
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
