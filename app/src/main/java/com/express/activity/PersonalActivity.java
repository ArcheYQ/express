package com.express.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.express.R;
import com.express.bean.User;
import com.express.other.CacheManager;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;


public class PersonalActivity extends BaseActivity {

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

    public static final int REQUEST_CODE_AVATAR = 100;

    public static final String AVATAR_FILE_NAME = "avatar.png";

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
        Log.i("照片", user.getHeadPicThumb());
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
                SImagePicker
                        .from(PersonalActivity.this)
                        .pickMode(SImagePicker.MODE_AVATAR)
                        .showCamera(true)
                        .cropFilePath(
                                CacheManager.getInstance().getImageInnerCache()
                                        .getAbsolutePath(AVATAR_FILE_NAME))
                        .forResult(REQUEST_CODE_AVATAR);
                break;
            case R.id.ll_introduction:
                startActivity(new Intent(this,ProfileActivity.class));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AVATAR) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            options.width=200;
            options.height=200;
            showProgressDialog();
            Tiny.getInstance().source(pathList.get(0)).asFile().withOptions(options).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (isSuccess){
                        final BmobFile bmobFile = new BmobFile(new File(outfile));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    updateHead(bmobFile.getUrl());
                                    Toast.makeText(PersonalActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    dissmiss();
                                    Toast.makeText(PersonalActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        dissmiss();
                        Toast.makeText(PersonalActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public void updateHead(final String path){
        User newUser = new User();
        newUser.setHeadPicThumb(path);
        User bmobUser = User.getCurrentUser(User.class);// 获得当前登陆的用户
        newUser.update(bmobUser.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    dissmiss();
                    Toast.makeText(PersonalActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    Glide.with(PersonalActivity.this)
                            .load(path)
                            .into(cmPerson);
                }else{
                    dissmiss();
                    Toast.makeText(PersonalActivity.this, "更改失败", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
}
