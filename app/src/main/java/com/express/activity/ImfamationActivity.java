package com.express.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ImfamationActivity extends BaseActivity {

    @Bind(R.id.iv_personal_bg)
    ImageView ivPersonalBg;
    @Bind(R.id.cm_person)
    CircleImageView cmPerson;
    @Bind(R.id.et_true_name)
    EditText etTrueName;
    @Bind(R.id.et_student_id)
    EditText etStudentId;
    @Bind(R.id.et_sex)
    EditText etSex;
    @Bind(R.id.et_college)
    EditText etCollege;
    @Bind(R.id.et_class)
    EditText etClass;
    @Bind(R.id.et_introduction)
    EditText etIntroduction;
    @Bind(R.id.ll_introduction)
    LinearLayout llIntroduction;
    @Bind(R.id.tb_personal)
    Toolbar tbPersonal;

    public static final int REQUEST_CODE_AVATAR = 100;
    private String cmUrl;
    public static final String AVATAR_FILE_NAME = "avatar.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imfamation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_personal);
        Glide.with(this).load("http://bmob-cdn-13164.b0.upaiyun.com/2017/09/04/b1b8899cc0934c899bc86f88bafdf302.jpg").into(cmPerson);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AVATAR) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            options.width = 200;
            options.height = 200;
            showProgressDialog();
            Tiny.getInstance().source(pathList.get(0)).asFile().withOptions(options).compress(new FileCallback() {
                @Override
                public void callback(boolean isSuccess, String outfile) {
                    if (isSuccess) {
                        final BmobFile bmobFile = new BmobFile(new File(outfile));
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    cmUrl = bmobFile.getUrl();
                                    updateHead();

                                    Toast.makeText(ImfamationActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    dissmiss();
                                    Toast.makeText(ImfamationActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        dissmiss();
                        Toast.makeText(ImfamationActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    public void updateHead() {
        User bmobUser = User.getCurrentUser(User.class);
        User user1 = new User();
        user1.setHeadPicThumb(cmUrl);
        user1.setHeadPic(cmUrl);
        user1.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    dissmiss();
                    Toast.makeText(ImfamationActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    Glide.with(ImfamationActivity.this)
                            .load(cmUrl)
                            .into(cmPerson);
                } else {
                    dissmiss();
                    Toast.makeText(ImfamationActivity.this, "更改失败", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.finish) {
            if (!etClass.getText().toString().isEmpty() && !etSex.getText().toString().isEmpty() && !etIntroduction.getText().toString().isEmpty() && !etTrueName.getText().toString().isEmpty() && !etStudentId.getText().toString().isEmpty() && !etCollege.getText().toString().isEmpty()) {
            User bmobUser = User.getCurrentUser(User.class);
            User user= new User();
            user.setClassName(etClass.getText().toString());
            user.setGender(etSex.getText().toString());
            user.setProfile(etIntroduction.getText().toString());
            user.setName(etTrueName.getText().toString());
            user.setNickname(etStudentId.getText().toString());
            user.setDepName(etCollege.getText().toString());
                user.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mActivity, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "更新用户信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                startActivity(new Intent(ImfamationActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(mActivity, "您有信息没有填完", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.cm_person)
    public void onViewClicked() {
        if(getCcamra()&&getStorage()){
            SImagePicker
                    .from(this)
                    .pickMode(SImagePicker.MODE_AVATAR)
                    .showCamera(true)
                    .cropFilePath(
                            CacheManager.getInstance().getImageInnerCache()
                                    .getAbsolutePath(AVATAR_FILE_NAME))
                    .forResult(REQUEST_CODE_AVATAR);
        }else{
            Toast.makeText(mActivity, "请给予权限", Toast.LENGTH_SHORT).show();
        }

    }

}

