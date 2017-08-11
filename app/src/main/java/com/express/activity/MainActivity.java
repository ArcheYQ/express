package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;

public class MainActivity extends AppCompatActivity {

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
    @Bind(R.id.ll_side)
    LinearLayout llSide;
    @Bind(R.id.srl_main)
    SmartRefreshLayout srlMain;
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
        srlMain.autoRefresh();
        adapter = new ExpressAdapter(list,this);
        rvMain.setAdapter(adapter);
        initView();
        initData();
    }

    private void initView() {
        srlMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                ExpressUtil.getInstance().queryExpressHelp(0, new ExpressUtil.QueryListener() {
                    @Override
                    public void complete(List<ExpressHelp> expressHelps) {
                        adapter.setList(expressHelps);
                        page = 1;
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void fail(String error) {
                        refreshlayout.finishRefresh();
                        Toast.makeText(MainActivity.this,error, Toast.LENGTH_SHORT).show();
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
                        adapter.setList(expressHelps);
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
        srlMain.setPrimaryColors(getColor(R.color.blue_balloon));
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
        if (TextUtils.isEmpty(user.getNickname())) {
            tvSlideNickname.setText("编辑昵称");
        } else {
            tvSlideNickname.setText(user.getNickname());
        }
        Glide.with(this)
                .load(user.getHeadPicThumb())
                .into(cvSlideTou);

    }

    @OnClick({R.id.cv_slide_tou, R.id.tv_slide_nickname, R.id.ll_address, R.id.fb_publish, R.id.ll_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_slide_tou:
                startActivity(new Intent(this, PersonalActivity.class));
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
        }
    }


}
