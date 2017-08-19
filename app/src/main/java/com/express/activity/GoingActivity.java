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
import com.express.activity.fragment.MyHelpFragment;
import com.express.activity.fragment.OtherHelpFragment;
import com.express.adapter.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class GoingActivity extends BaseActivity {

    @Bind(R.id.vp_going)
    ViewPager vpGoing;
    @Bind(R.id.tb_going)
    Toolbar tbGoing;
    @Bind(R.id.tv_going_myHelp)
    TextView tvGoingMyHelp;
    @Bind(R.id.tv_going_otherHelp)
    TextView tvGoingOtherHelp;
    @Bind(R.id.vv_going)
    LinearLayout vvGoing;
    private FragmentAdapter adapter;
    private int currentIndex;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_going);
        initHome();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth() - dip2px(this,20);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvGoing.getLayoutParams();
        lp.width = screenWidth/2;
        vvGoing.setLayoutParams(lp);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyHelpFragment());
        fragments.add(new OtherHelpFragment());
        adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        vpGoing.setAdapter(adapter);
        vpGoing.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvGoing.getLayoutParams();
                if (currentIndex == 0 && position == 0)// 0->1
                {
                    lp.leftMargin = (int) (positionOffset*screenWidth/2);
                } else if (currentIndex == 1 && position == 0){
                    lp.leftMargin = (int) ((1+positionOffset)*screenWidth/4);
                }
                vvGoing.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    tvGoingMyHelp.setTextColor(getColor(R.color.color_accent));
                    tvGoingOtherHelp.setTextColor(getColor(R.color.black));
                    currentIndex = 0;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvGoing.getLayoutParams();
                    lp.leftMargin = 0;
                    vvGoing.setLayoutParams(lp);

                }else if (position == 1){
                    tvGoingMyHelp.setTextColor(getColor(R.color.black));
                    tvGoingOtherHelp.setTextColor(getColor(R.color.color_accent));
                    currentIndex = 1;
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vvGoing.getLayoutParams();
                    lp.leftMargin = screenWidth/4;
                    vvGoing.setLayoutParams(lp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpGoing.setCurrentItem(0);
        tvGoingMyHelp.setTextColor(getColor(R.color.color_accent));
        tvGoingOtherHelp.setTextColor(getColor(R.color.black));
        currentIndex = 0;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @OnClick({R.id.tv_going_myHelp, R.id.tv_going_otherHelp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_going_myHelp:
                vpGoing.setCurrentItem(0);
                break;
            case R.id.tv_going_otherHelp:
                vpGoing.setCurrentItem(1);
                break;
        }
    }
}
