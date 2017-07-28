package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
                    showProgressDialog();
                    login(etLoginAccount.getText().toString(),etLoginPassword.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void login(final String username, final String password){
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
                Log.i("测试",msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void synchroBmob(final User user){
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                if (e == null || e.getErrorCode() == 202){
                    e = null ;
                    loginBmob(user);
                }else {
                    Log.i("测试",e.getMessage());
                }
            }
        });
    }

    public void loginBmob(User user){
        user.login(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                if (e == null){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    dissmiss();
                }else {
                    Log.i("测试",e.getMessage());
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
}
