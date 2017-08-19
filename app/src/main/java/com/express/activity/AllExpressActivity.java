package com.express.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.express.R;
import com.express.activity.fragment.MyAllFragment;
import com.express.activity.fragment.OtherAllFragment;
import com.express.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AllExpressActivity extends BaseActivity {

    @Bind(R.id.tb_all_express)
    Toolbar tbAllExpress;
    @Bind(R.id.tv_all_express_myHelp)
    TextView tvAllExpressMyHelp;
    @Bind(R.id.tv_all_express_otherHelp)
    TextView tvAllExpressOtherHelp;
    @Bind(R.id.ll_all_express)
    LinearLayout llAllExpress;
    @Bind(R.id.vp_all_express)
    ViewPager vpAllExpress;
    private FragmentAdapter adapter;
    private int currentIndex;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_express);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_all_express);
        initHome();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth() - dip2px(this, 20);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llAllExpress.getLayoutParams();
        lp.width = screenWidth / 2;
        llAllExpress.setLayoutParams(lp);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyAllFragment());
        fragments.add(new OtherAllFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        vpAllExpress.setAdapter(adapter);
        vpAllExpress.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llAllExpress.getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset * screenWidth / 2);
                } else if (currentIndex == 1 && position == 0) {
                    lp.leftMargin = (int) ((1 + positionOffset) * screenWidth / 4);
                }
                llAllExpress.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tvAllExpressMyHelp.setTextColor(getColor(R.color.color_accent));
                    tvAllExpressOtherHelp.setTextColor(getColor(R.color.black));
                    currentIndex = 0;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llAllExpress.getLayoutParams();
                    lp.leftMargin = 0;
                    llAllExpress.setLayoutParams(lp);

                } else if (position == 1) {
                    tvAllExpressMyHelp.setTextColor(getColor(R.color.black));
                    tvAllExpressOtherHelp.setTextColor(getColor(R.color.color_accent));
                    currentIndex = 1;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llAllExpress.getLayoutParams();
                    lp.leftMargin = screenWidth / 4;
                    llAllExpress.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpAllExpress.setCurrentItem(0);
        tvAllExpressMyHelp.setTextColor(getColor(R.color.color_accent));
        tvAllExpressOtherHelp.setTextColor(getColor(R.color.black));
        currentIndex = 0;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @OnClick({R.id.tv_all_express_myHelp, R.id.tv_all_express_otherHelp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all_express_myHelp:
                vpAllExpress.setCurrentItem(0);
                break;
            case R.id.tv_all_express_otherHelp:
                vpAllExpress.setCurrentItem(1);
                break;
        }
    }
}
