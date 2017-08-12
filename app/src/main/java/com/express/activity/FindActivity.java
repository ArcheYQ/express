package com.express.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.express.R;
import com.express.bean.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class FindActivity extends BaseActivity {

    @Bind(R.id.tb_find)
    Toolbar tbfind;
    @Bind(R.id.et_find)
    EditText etFind;
    @Bind(R.id.iv_find)
    ImageView ivFind;
    @Bind(R.id.cc_find_tou)
    CircleImageView ccFindTou;
    @Bind(R.id.tv_find_nickname)
    TextView tvFindNickname;
    @Bind(R.id.tv_find_student)
    TextView tvFindStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_find);
        initHome();
    }

    @OnClick(R.id.iv_find)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etFind.getText().toString())) {
            Toast.makeText(mActivity, "学号都不输，是想让我和你尬聊吗？，O(∩_∩)O~~", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            final BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("username", etFind.getText().toString());
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    dissmiss();
                    if (e == null) {
                        if (list.size() == 0) {
                            Toast.makeText(mActivity, "嘤嘤嘤，连人家学号都不记得了吗？⁄(⁄ ⁄•⁄ω⁄•⁄ ⁄)⁄", Toast.LENGTH_SHORT).show();
                        } else {
                            Glide.with(FindActivity.this)
                                    .load(list.get(0).getHeadPicThumb())
                                    .into(ccFindTou);
                            tvFindNickname.setText(list.get(0).getNickname());
                            tvFindStudent.setText("学号"+list.get(0).getUsername());

                        }
                    } else {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
