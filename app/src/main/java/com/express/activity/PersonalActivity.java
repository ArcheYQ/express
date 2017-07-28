package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.express.R;
import com.express.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;


public class PersonalActivity extends AppCompatActivity {

    @Bind(R.id.tb_personal)
    Toolbar tbPersonal;
    @Bind(R.id.iv_personal_bg)
    ImageView ivPersonalBg;
    @Bind(R.id.cm_person)
    CircleImageView cmPerson;
    @Bind(R.id.tv_true_name)
    TextView tvTrueName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_college)
    TextView tvCollege;
    @Bind(R.id.tv_class)
    TextView tvClass;
    @Bind(R.id.tv_student_id)
    TextView tvStudentId;
    @Bind(R.id.tv_help_sum)
    TextView tvHelpSum;
    @Bind(R.id.tv_introduction)
    TextView tvIntroduction;
    @Bind(R.id.ll_introduction)
    LinearLayout llIntroduction;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
    private void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        tvTrueName.setText(user.getName());
        tvSex.setText(user.getGender());
        tvClass.setText(user.getClassName());
        tvHelpSum.setText("" + 0);
        tvNickname.setText(user.getNickname());
        tvStudentId.setText(user.getUsername());
        tvCollege.setText(user.getDepName());
        Glide.with(this)
                .load(user.getHeadPicThumb())
                .into(cmPerson);
        if (TextUtils.isEmpty(user.getProfile())) {
            tvIntroduction.setText("编辑个人简介");
        } else {
            tvIntroduction.setText(user.getProfile());
        }
    }


    @OnClick({R.id.cm_person, R.id.ll_introduction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cm_person:
                break;
            case R.id.ll_introduction:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
        }

    }
}
