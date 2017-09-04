package com.express.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.express.R;
import com.express.adapter.FlowerAdapter;
import com.express.bean.Comment;
import com.express.bean.User;
import com.express.util.FlowerUtil;
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


public class FlowerActivity extends BaseActivity {

    @Bind(R.id.tb_flower)
    Toolbar tbFlower;
    @Bind(R.id.rv_flowers)
    RecyclerView rvFlowers;
    @Bind(R.id.sr_flower)
    SmartRefreshLayout srFlower;
    FlowerAdapter adapter;
    private int page = 0;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_flower);
        initHome();
        user = (User) getIntent().getSerializableExtra("user");
        rvFlowers.setLayoutManager(new LinearLayoutManager(this));
        rvFlowers.setItemAnimator(new DefaultItemAnimator());
        List<Comment> list = new ArrayList<>();
        adapter= new FlowerAdapter(list);
        rvFlowers.setAdapter(adapter);
        initView();
        srFlower.autoRefresh();

    }

    private void initView() {
        srFlower.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                FlowerUtil.getInstance().queryFlower(0, user, new FlowerUtil.OnQueryCommentListener() {
                    @Override
                    public void onSuccess(List<Comment> comments) {
                        adapter.setList(comments);
                        page = 1;
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void onError(String error) {
                        refreshlayout.finishRefresh();
                        Toast.makeText(FlowerActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        srFlower.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                FlowerUtil.getInstance().queryFlower(page, user, new FlowerUtil.OnQueryCommentListener() {
                    @Override
                    public void onSuccess(List<Comment> comments) {
                        refreshlayout.finishLoadmore();
                        adapter.setList(comments);
                        page++;
                    }

                    @Override
                    public void onError(String error) {
                        refreshlayout.finishLoadmore();
                        Toast.makeText(FlowerActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setRefreshHeader(new DeliveryHeader(this));
        srFlower.setHeaderHeight(150);
        srFlower.setPrimaryColors(Color.parseColor("#8dc3e6"));
        srFlower.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
    }
}
