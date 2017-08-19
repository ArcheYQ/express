package com.express.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.express.R;
import com.express.adapter.HelpAdapter;
import com.express.bean.ExpressHelp;
import com.express.util.ExpressUtil;
import com.express.util.HelpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 雅倩宝宝 on 2017/8/16.
 */

public class MyHelpFragment extends Fragment {

    @Bind(R.id.rv_fragment_help)
    RecyclerView rvFragmentHelp;
    HelpAdapter adapter;
    @Bind(R.id.sr_fragment_help)
    SmartRefreshLayout srFragmentHelp;
    private int page;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(this, view);
        adapter = new HelpAdapter(new ArrayList<ExpressHelp>(),getActivity());
        rvFragmentHelp.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFragmentHelp.setItemAnimator(new DefaultItemAnimator());
        rvFragmentHelp.setAdapter(adapter);
        srFragmentHelp.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                HelpUtil.myHelp(0, new ExpressUtil.QueryListener() {
                    @Override
                    public void complete(List<ExpressHelp> expressHelps) {
                        page=1;
                        adapter.setList(expressHelps);
                        refreshlayout.finishRefresh();
                    }

                    @Override
                    public void fail(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        refreshlayout.finishRefresh();
                    }
                });
            }
        });
        srFragmentHelp.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                HelpUtil.myHelp(page, new ExpressUtil.QueryListener() {
                    @Override
                    public void complete(List<ExpressHelp> expressHelps) {
                        page++;
                        adapter.add(expressHelps);
                        refreshlayout.finishLoadmore();
                    }

                    @Override
                    public void fail(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        refreshlayout.finishLoadmore();
                    }
                });
            }
        });
        srFragmentHelp.autoRefresh();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
