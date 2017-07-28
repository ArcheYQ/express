package com.express.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.express.R;
import com.express.bean.User;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ProfileActivity extends BaseActivity {

    @Bind(R.id.iv_profile_back)
    ImageView ivProfileBack;
    @Bind(R.id.tv_profile_complete)
    TextView tvProfileComplete;
    @Bind(R.id.et_new_profile)
    EditText etNewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        final int maxNum = 75;
        final TextView leftNum = (TextView) findViewById(R.id.tv_new_profile);
        EditText et = (EditText) findViewById(R.id.et_new_profile);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                leftNum.setText("还可以输入："+ (maxNum-s.length())+"个字");
            }
        });

    }

    @OnClick({R.id.iv_profile_back, R.id.tv_profile_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_profile_back:
                finish();
                break;
            case R.id.tv_profile_complete:
                showProgressDialog();
                User newUser = new User();
                newUser.setProfile(etNewProfile.getText().toString());
                User bmobUser = User.getCurrentUser(User.class);// 获得当前登陆的用户
                newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            dissmiss();
                            Toast.makeText(ProfileActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            dissmiss();
                            Toast.makeText(ProfileActivity.this, "更改失败", Toast.LENGTH_SHORT).show();
                        }
                    }


                });
                break;
        }
    }

}
