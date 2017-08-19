package com.express.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.express.R;
import com.express.bean.User;
import com.express.other.CacheManager;
import com.express.util.ConversationUtil;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMUserInfo;
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

    public static final int REQUEST_CODE_IMAGE = 101;
    @Bind(R.id.btn_person_chat)
    Button btnPersonChat;
    @Bind(R.id.btn_person_flower)
    Button btnPersonFlower;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initData();
        setToolBar(R.id.tb_personal);
        initWhiteHome();
        loadBackground();
    }

    public void loadBackground() {
        String background = BmobUser.getCurrentUser(User.class).getBackground();
        if (!TextUtils.isEmpty(background)) {
            Glide.with(this)
                    .load(background)
                    .into(ivPersonalBg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.update_background) {
            SImagePicker
                    .from(this)
                    .maxCount(1)
                    .rowCount(3)
                    .showCamera(true)
                    .pickMode(SImagePicker.MODE_IMAGE)
                    .forResult(REQUEST_CODE_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateBackground(String path) {
        showProgressDialog();
        Tiny.getInstance().source(path).asFile().withOptions(new Tiny.FileCompressOptions()).compress(new FileCallback() {
            @Override
            public void callback(boolean isSuccess, String outfile) {
                if (isSuccess) {
                    final BmobFile bmobFile = new BmobFile(new File(outfile));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                synchroUser(bmobFile.getUrl());
                            } else {
                                dissmiss();
                                if (e.getErrorCode() == 9016) {
                                    Toast.makeText(mActivity, "网络不给力", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                } else {
                    dissmiss();
                    Toast.makeText(mActivity, "图片压缩失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void synchroUser(String path) {
        User newUser = new User();
        newUser.setBackground(path);
        User bmobUser = BmobUser.getCurrentUser(User.class);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dissmiss();
                if (e == null) {
                    loadBackground();
                    Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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


    @OnClick({R.id.cm_person, R.id.ll_introduction,R.id.btn_person_chat,R.id.btn_person_flower})
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
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.btn_person_chat:
                BmobIMUserInfo info = new BmobIMUserInfo();
                info.setName(user.getNickname());
                info.setUserId(user.getObjectId());
                info.setAvatar(user.getHeadPicThumb());
                ConversationUtil.getInstance().OpenWindow(info,this);
                break;
            case R.id.btn_person_flower:
                final EditText etName;
                final AlertView mAlertViewExt;
                ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setHint("请输入你的评价内容");
                final User user =  BmobUser.getCurrentUser(User.class);
                mAlertViewExt = new AlertView("提示", "评价该用户！", "取消", null, new String[]{"确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            if (TextUtils.isEmpty(etName.getText().toString())){
                                Toast.makeText(PersonalActivity.this, "请输入评价", Toast.LENGTH_SHORT).show();
                            }else {
                                String comment = user.getName()+"#"+System.currentTimeMillis()+"#"+etName.getText().toString();
                            }

                        }
                    }
                });
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean focus) {
                        boolean isOpen = imm.isActive();
                        mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
                    }
                });
                mAlertViewExt.addExtView(extView);
                mAlertViewExt.show();
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
                                    updateHead(bmobFile.getUrl());
                                    Toast.makeText(PersonalActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    dissmiss();
                                    Toast.makeText(PersonalActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        dissmiss();
                        Toast.makeText(PersonalActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            updateBackground(pathList.get(0));
        }
    }


    public void updateHead(final String path) {
        User newUser = new User();
        newUser.setHeadPicThumb(path);
        User bmobUser = User.getCurrentUser(User.class);// 获得当前登陆的用户
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    dissmiss();
                    Toast.makeText(PersonalActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                    Glide.with(PersonalActivity.this)
                            .load(path)
                            .into(cmPerson);
                } else {
                    dissmiss();
                    Toast.makeText(PersonalActivity.this, "更改失败", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

}
