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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.express.R;
import com.express.bean.Comment;
import com.express.bean.User;
import com.express.other.CacheManager;
import com.express.util.ConversationUtil;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
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
    @Bind(R.id.ll_flower)
    LinearLayout llFlower;
    @Bind(R.id.tv_flower_name1)
    TextView tvFlowerName1;
    @Bind(R.id.tv_flower_time1)
    TextView tvFlowerTime1;
    @Bind(R.id.tv_flower_comment1)
    TextView tvFlowerComment1;
    @Bind(R.id.tv_flower_name2)
    TextView tvFlowerName2;
    @Bind(R.id.tv_flower_time2)
    TextView tvFlowerTime2;
    @Bind(R.id.tv_flower_comment2)
    TextView tvFlowerComment2;
    @Bind(R.id.tv_flower_name3)
    TextView tvFlowerName3;
    @Bind(R.id.tv_flower_time3)
    TextView tvFlowerTime3;
    @Bind(R.id.tv_flower_comment3)
    TextView tvFlowerComment3;
    @Bind(R.id.vv_flower2)
    View vvFlower2;
    @Bind(R.id.vv_flower3)
    View vvFlower3;
    @Bind(R.id.rl_comment2)
    RelativeLayout rlComment2;
    @Bind(R.id.rl_comment3)
    RelativeLayout rlComment3;
    private User user;
    private boolean isSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra("user");
        isSelf = BmobUser.getCurrentUser(User.class).getObjectId().equals(user.getObjectId());
        initData();
        setToolBar(R.id.tb_personal);
        initWhiteHome();
        loadBackground();
        if (isSelf) {
            btnPersonChat.setVisibility(View.GONE);
            btnPersonFlower.setVisibility(View.GONE);
        }
    }

    public void loadBackground() {
        String background = user.getBackground();
        if (!TextUtils.isEmpty(background)) {
            Glide.with(this)
                    .load(background)
                    .into(ivPersonalBg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isSelf) {
            getMenuInflater().inflate(R.menu.menu_personal, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.update_background) {
            if (getCcamra() && getStorage()){
                SImagePicker
                        .from(this)
                        .maxCount(1)
                        .rowCount(3)
                        .showCamera(true)
                        .pickMode(SImagePicker.MODE_IMAGE)
                        .forResult(REQUEST_CODE_IMAGE);
            }else {
                Toast.makeText(this, "请给予权限", Toast.LENGTH_SHORT).show();
            }
           
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

        User bmobUser = BmobUser.getCurrentUser(User.class);
        User newUser = new User(bmobUser.getSum(),bmobUser.getHelpSum());
        newUser.setBackground(path);
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
        tvTrueName.setText(user.getName());
        tvSex.setText(user.getGender());
        tvClass.setText(user.getClassName());
        tvHelpSum.setText("" + 0);
        tvNickname.setText(user.getNickname());
        tvStudentId.setText(user.getUsername());
        tvCollege.setText(user.getDepName());
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.setLimit(3);
        query.addWhereEqualTo("user",user);
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e==null){
                    if (list != null){
                        if (list.size() > 0 && list.get(0)!= null) {
                            String[] str1 = list.get(0).getComment().split("#", 3);
                            tvFlowerName1.setText(str1[0]);
                            SimpleDateFormat format1 = new SimpleDateFormat("MM-dd");
                            tvFlowerTime1.setText(format1.format(new Date(Long.parseLong(str1[1]))));
                            tvFlowerComment1.setText(str1[2]);

                            if (list.size() > 1 && list.get(1)!= null) {
                                rlComment2.setVisibility(View.VISIBLE);
                                vvFlower2.setVisibility(View.VISIBLE);
                                String[] str2 = list.get(1).getComment().split("#", 3);
                                tvFlowerName2.setText(str2[0]);
                                SimpleDateFormat format2 = new SimpleDateFormat("MM-dd");
                                tvFlowerTime2.setText(format2.format(new Date(Long.parseLong(str2[1]))));
                                tvFlowerComment2.setText(str2[2]);
                            }
                            if (list.size() > 2 && list.get(2)!= null) {
                                vvFlower3.setVisibility(View.VISIBLE);
                                rlComment3.setVisibility(View.VISIBLE);
                                String[] str3 = list.get(2).getComment().split("#", 3);
                                tvFlowerName3.setText(str3[0]);
                                SimpleDateFormat format3 = new SimpleDateFormat("MM-dd");
                                tvFlowerTime3.setText(format3.format(new Date(Long.parseLong(str3[1]))));
                                tvFlowerComment3.setText(str3[2]);
                            }
                        } else {
                            tvFlowerName1.setText("当前评价为空~快来评价吧");
                        }
                    }else {
                        Log.i("Test","评论为空");
                        tvFlowerName1.setText("当前评价为空~快来评价吧");
                    }
                }else {
                    Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        Glide.with(this)
                .load(user.getHeadPicThumb())
                .into(cmPerson);
        if (TextUtils.isEmpty(user.getProfile())) {
           if (isSelf){
               tvIntroduction.setText("编辑个人简介");
           }else{
               tvIntroduction.setText("对方没有写个人简介");
           }
        } else {
            tvIntroduction.setText(user.getProfile());
        }
    }


    @OnClick({R.id.cm_person, R.id.ll_introduction, R.id.btn_person_chat, R.id.btn_person_flower, R.id.ll_flower})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_flower:
                Intent intent = new Intent(this, FlowerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                intent.putExtras(bundle);
                this.startActivity(intent);
                break;
            case R.id.cm_person:
                if (isSelf) {
                    if(getCcamra()&&getStorage()){
                        SImagePicker
                                .from(PersonalActivity.this)
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
                break;
            case R.id.ll_introduction:
                if (isSelf) {
                    startActivity(new Intent(this, ProfileActivity.class));
                }
                break;
            case R.id.btn_person_chat:
                BmobIMUserInfo info = new BmobIMUserInfo();
                info.setName(user.getNickname());
                info.setUserId(user.getObjectId());
                info.setAvatar(user.getHeadPicThumb());
                ConversationUtil.getInstance().OpenWindow(info, this);
                break;
            case R.id.btn_person_flower:
                final EditText etName;
                final AlertView mAlertViewExt;
                ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
                etName = (EditText) extView.findViewById(R.id.etName);
                etName.setHint("请输入你的评价内容");
                final Comment comment = new Comment();
                mAlertViewExt = new AlertView("提示", "评价该用户！", "取消", null, new String[]{"确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            if (TextUtils.isEmpty(etName.getText().toString())) {
                                Toast.makeText(PersonalActivity.this, "请输入评价", Toast.LENGTH_SHORT).show();
                            } else {
                                String com = BmobUser.getCurrentUser(User.class).getName() + "#" + System.currentTimeMillis() + "#" + etName.getText().toString();
                                comment.setUser(user);
                                comment.setValuer(BmobUser.getCurrentUser(User.class));
                                comment.setComment(com);
                                comment.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(PersonalActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
                                            initData();
                                        }else{
                                            if(e.getErrorCode()==9016){
                                                Toast.makeText(PersonalActivity.this, "网络不给力/(ㄒoㄒ)/~~", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(PersonalActivity.this, e.getErrorCode(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
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

        User bmobUser = User.getCurrentUser(User.class);// 获得当前登陆的用户
        User newUser = new User(bmobUser.getSum(),bmobUser.getHelpSum());
        newUser.setHeadPicThumb(path);
        newUser.setHeadPic(path);
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
