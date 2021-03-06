package com.express.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.express.R;
import com.express.bean.User;
import com.github.lzyzsd.circleprogress.ArcProgress;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class ReputationActivity extends BaseActivity {

    @Bind(R.id.arc_progress)
    ArcProgress arcProgress;
    @Bind(R.id.iv_level_0)
    ImageView ivLevel0;
    @Bind(R.id.iv_level_1)
    ImageView ivLevel1;
    @Bind(R.id.iv_level_2)
    ImageView ivLevel2;
    @Bind(R.id.iv_level_3)
    ImageView ivLevel3;
    @Bind(R.id.iv_level_4)
    ImageView ivLevel4;
    @Bind(R.id.iv_level_5)
    ImageView ivLevel5;
    @Bind(R.id.iv_level_6)
    ImageView ivLevel6;
    @Bind(R.id.tv_rule)
    TextView tvRule;
    @Bind(R.id.tb_reputation)
    Toolbar tbReputation;
    @Bind(R.id.tv_residue_help)
    TextView tvResidueHelp;
    @Bind(R.id.tv_reputation_values)
    TextView tvReputationValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_reputation);
        initHome();
        User user = BmobUser.getCurrentUser(User.class);
        if (user.getSum()==0) {
            tvReputationValues.setText("当前荣誉值：0");
        } else {
            tvReputationValues.setText("当前荣誉值：" + user.getSum());
        }

        if (user.getHelpSum()==0) {
            tvResidueHelp.setText("剩余可请求帮助次数：0");
        } else {
            tvResidueHelp.setText("剩余可请求帮助次数：" + user.getHelpSum());
        }
        int progress = 0;
        if (user.getSum() <5){
            progress = (5-user.getSum())*100/5;
            ivLevel0.setVisibility(View.VISIBLE);
        }else if (user.getSum() <20){
            progress = (20-user.getSum())*100/20;
            ivLevel1.setVisibility(View.VISIBLE);
        }else if (user.getSum() <40){
            progress = (40-user.getSum())*100/40;
            ivLevel2.setVisibility(View.VISIBLE);
        }else if (user.getSum() <65){
            progress = (65-user.getSum())*100/65;
            ivLevel3.setVisibility(View.VISIBLE);
        }else if (user.getSum() <95){
            progress = (95-user.getSum())*100/95;
            ivLevel4.setVisibility(View.VISIBLE);
        }else if (user.getSum() <130) {
            progress = (130-user.getSum())*100/130;
            ivLevel5.setVisibility(View.VISIBLE);
        }else{
            progress = 0;
            ivLevel6.setVisibility(View.VISIBLE);
        }
        arcProgress.setProgress(progress);
        arcProgress.setBottomText("升级还差"+progress+"%");
        arcProgress.setBottomTextSize(28);
    }






    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.rule);
        normalDialog.setTitle("规则说明n(*≧▽≦*)n");
        normalDialog.setMessage("荣誉值：按相应快递的重量计分。1-3瓶水增加一分，4-6瓶水增加两分，6瓶水以上增加三分。达到一定的荣誉值后可获得对应的等级。\n剩余帮助次数：帮助他人获得一次，请求帮助减少一次。");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });

        // 显示
        normalDialog.show();
    }

    @OnClick(R.id.tv_rule)
    public void onViewClicked() {
        showNormalDialog();
    }
}
