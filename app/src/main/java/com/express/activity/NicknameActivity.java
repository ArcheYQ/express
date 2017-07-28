package com.express.activity;

import android.os.Bundle;
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


public class NicknameActivity extends BaseActivity {

    @Bind(R.id.iv_nickname_back)
    ImageView ivNicknameBack;
    @Bind(R.id.tv_nickname_complete)
    TextView tvNicknameComplete;
    @Bind(R.id.et_new_nickname)
    EditText etNewNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        ButterKnife.bind(this);
    }




    @OnClick({R.id.iv_nickname_back, R.id.tv_nickname_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_nickname_back:
                finish();
                break;
            case R.id.tv_nickname_complete:
                showProgressDialog();
                User newUser = new User();
                newUser.setNickname(etNewNickname.getText().toString());
                User bmobUser = User.getCurrentUser(User.class);// 获得当前登陆的用户
                newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            dissmiss();
                            Toast.makeText(NicknameActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            dissmiss();
                            Toast.makeText(NicknameActivity.this, "更改失败", Toast.LENGTH_SHORT).show();
                        }
                    }


                });

                break;
        }
    }


}
