package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.express.R;
import com.express.adapter.ExpressAdapter;
import com.express.bean.ExpressHelp;
import com.express.bean.User;
import com.express.util.ConversationUtil;
import com.express.util.ExpressUtil;
import com.express.view.SlidingMenu;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends BaseActivity {

    @Bind(R.id.rv_main)
    RecyclerView rvMain;
    @Bind(R.id.cv_slide_tou)
    CircleImageView cvSlideTou;
    @Bind(R.id.tv_slide_nickname)
    TextView tvSlideNickname;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.fb_publish)
    FloatingActionButton fbPublish;
    @Bind(R.id.iv_left_background)
    ImageView ivLeftBackground;
    @Bind(R.id.id_menu)
    SlidingMenu idMenu;
    @Bind(R.id.srl_main)
    SmartRefreshLayout srlMain;
    @Bind(R.id.iv_PersonInformation)
    ImageView ivPersonInformation;
    @Bind(R.id.iv_sms)
    ImageView ivSms;
    @Bind(R.id.ll_going)
    LinearLayout llGoing;
    @Bind(R.id.ll_reputation)
    LinearLayout llReputation;
    private int page = 0;
    ExpressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this);//去掉状态栏
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setItemAnimator(new DefaultItemAnimator());
        List<ExpressHelp> list = new ArrayList<>();
        adapter = new ExpressAdapter(list, this);
        rvMain.setAdapter(adapter);
        initView();
        srlMain.autoRefresh();
        initData();
        ConversationUtil.getInstance().connect();
        addActivity(this);

    }

    private void initView() {
        srlMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ExpressUtil.getInstance().queryExpressHelp(0, new ExpressUtil.QueryListener() {
                    @Override
                    public void complete(List<ExpressHelp> expressHelps) {
                        adapter.setList(expressHelps,true);
                        page = 1;
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void fail(String error) {
                        refreshlayout.finishRefresh();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        srlMain.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                ExpressUtil.getInstance().queryExpressHelp(page, new ExpressUtil.QueryListener() {
                    @Override
                    public void complete(List<ExpressHelp> expressHelps) {
                        refreshlayout.finishLoadmore();
                        adapter.addData(expressHelps);
                        page++;
                    }

                    @Override
                    public void fail(String error) {
                        refreshlayout.finishLoadmore();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setRefreshHeader(new DeliveryHeader(this));
        srlMain.setHeaderHeight(150);
//        srlMain.setPrimaryColors(getColor(R.color.blue_balloon));
        srlMain.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
    }


    public void loadBackground() {
        String background = BmobUser.getCurrentUser(User.class).getBackground();
        if (!TextUtils.isEmpty(background)) {
            Glide.with(this)
                    .load(background)
                    .into(ivLeftBackground);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        loadBackground();
    }

    private void initData() {
        User user = BmobUser.getCurrentUser(User.class);
//        fetchUserInfo();
        if (TextUtils.isEmpty(user.getNickname())) {
            tvSlideNickname.setText("编辑昵称");
        } else {
            tvSlideNickname.setText(user.getNickname());
        }
        if (TextUtils.isEmpty(user.getHeadPicThumb())){
            Glide.with(this)
                    .load("http://bmob-cdn-13164.b0.upaiyun.com/2017/09/04/b1b8899cc0934c899bc86f88bafdf302.jpg")
                    .into(cvSlideTou);
        }else{
            Glide.with(this)
                    .load(user.getHeadPicThumb())
                    .into(cvSlideTou);
        }

    }
//    private void fetchUserInfo() {
//        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//
//                } else {
//
//                }
//            }
//        });
//    }
    @OnClick({R.id.cv_slide_tou, R.id.tv_slide_nickname, R.id.ll_address, R.id.fb_publish, R.id.ll_feedback, R.id.iv_PersonInformation, R.id.iv_sms, R.id.ll_going, R.id.ll_my_record, R.id.iv_setting, R.id.ll_reputation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_slide_tou:
                Intent intent = new Intent(this, PersonalActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", BmobUser.getCurrentUser(User.class));
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_slide_nickname:
                startActivity(new Intent(this, NicknameActivity.class));
                break;
            case R.id.ll_address:
                startActivity(new Intent(this, AddressActivity.class));
                break;
            case R.id.fb_publish:
                startActivity(new Intent(this, PublishActivity.class));
                break;
            case R.id.ll_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.iv_PersonInformation:
                idMenu.toggle();
                break;
            case R.id.iv_sms:
                startActivity(new Intent(this, ConversationActivity.class));
                break;
            case R.id.ll_going:
                startActivity(new Intent(this, GoingActivity.class));
                break;
            case R.id.ll_my_record:
                startActivity(new Intent(this, AllExpressActivity.class));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.ll_reputation:
                startActivity(new Intent(this, ReputationActivity.class));
                break;
        }
    }
}


