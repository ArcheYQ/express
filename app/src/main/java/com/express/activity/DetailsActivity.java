package com.express.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.express.ExpressApplication;
import com.express.R;
import com.express.bean.ExpressHelp;
import com.express.bean.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;


public class DetailsActivity extends BaseActivity {

    @Bind(R.id.tb_detail)
    Toolbar tbDetail;
    @Bind(R.id.cv_details_person)
    CircleImageView cvDetailsPerson;
    @Bind(R.id.tv_details_name)
    TextView tvDetailsName;
    @Bind(R.id.iv_huangguan)
    ImageView ivHuangguan;
    @Bind(R.id.iv_level)
    ImageView ivLevel;
    @Bind(R.id.tv_details_HelpTime)
    TextView tvDetailsHelpTime;
    @Bind(R.id.tv_details_PublishTime)
    TextView tvDetailsPublishTime;
    @Bind(R.id.tv_details_address)
    TextView tvDetailsAddress;
    @Bind(R.id.tv_details_point)
    TextView tvDetailsPoint;
    @Bind(R.id.tv_details_weight)
    TextView tvDetailsWeight;
    @Bind(R.id.tv_details_pickup_code)
    TextView tvDetailsPickupCode;
    @Bind(R.id.tv_details_real_name)
    TextView tvDetailsRealName;
    @Bind(R.id.tv_details_telephone)
    TextView tvDetailsTelephone;
    @Bind(R.id.tv_details_remarks)
    TextView tvDetailsRemarks;
    @Bind(R.id.tv_details_message)
    TextView tvDetailsMessage;
    @Bind(R.id.btn_help)
    Button btnHelp;
    ExpressHelp express;
    private boolean isSelf ;
    private AlertView mAlertViewExt;
    private EditText etName;
    private String finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_detail);
        initHome();
        initView();

    }

    private void initView() {
        express = (ExpressHelp) getIntent().getSerializableExtra("express");
        int str= (int) (express.getPublishTime()%10000);//待处理字符串
        int finish1 = (Integer.parseInt(express.getUser().getUserId())%10000)*str;
        finish = String.valueOf(finish1);
        Glide.with(ExpressApplication.getContext())
                .load(express.getUser().getHeadPicThumb())
                .into(cvDetailsPerson);
        tvDetailsName.setText(express.getUser().getNickname());
        tvDetailsAddress.setText(express.getAddressAccuracy());
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-hh:mm");
        tvDetailsPublishTime.setText(format.format(new Date(express.getPublishTime())));
        tvDetailsHelpTime.setText(express.getUser().getSum());
        tvDetailsRemarks.setText(express.getRemarks());
        tvDetailsMessage.setText(express.getExpressSms());
        tvDetailsPickupCode.setText(express.getPickupCode());
        tvDetailsPoint.setText(express.getPointName());
        tvDetailsRealName.setText(express.getAddressName());
        tvDetailsTelephone.setText(express.getAddressTelephone());
        tvDetailsWeight.setText(express.getWeight());
        isSelf = BmobUser.getCurrentUser(User.class).getObjectId()
                .equals(express.getUser().getObjectId());

        if( express.isState()){
            btnHelp.setText("已经完成");
            btnHelp.setClickable(false);
        }else {
            if (isSelf) {
                if (express.getHelpUser() != null) {
                    btnHelp.setText("取货中");
                } else {
                    btnHelp.setText("等待中");
                }
            } else {
                if (express.getHelpUser() != null) {
                    if (BmobUser.getCurrentUser(User.class).getObjectId()
                            .equals(express.getHelpUser().getObjectId())) {
                        btnHelp.setText("输入完成码");
                    } else {
                        btnHelp.setText("别人已经拿啦");
                        btnHelp.setClickable(false);
                    }
                } else {
                    btnHelp.setText("帮助");
                }
            }
        }
    }

    @OnClick(R.id.btn_help)
    public void onClick() {
        switch (btnHelp.getText().toString()){
            case "帮助" :
                showProgressDialog();
                BmobQuery<ExpressHelp> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject(express.getObjectId(), new QueryListener<ExpressHelp>() {
                    @Override
                    public void done(ExpressHelp expressHelp, BmobException e) {
                        if (e==null){
                            if (expressHelp.getHelpUser()==null){
                                expressHelp.setHelpUser(BmobUser.getCurrentUser(User.class));
                                help(expressHelp);
                            }else {
                                Toast.makeText(DetailsActivity.this, "别人已经拿走了=￣ω￣=", Toast.LENGTH_SHORT).show();
                                dissmiss();
                            }
                        }else{
                            dissmiss();
                            if (e.getErrorCode()==9016){
                                Toast.makeText(DetailsActivity.this, "网络异常/(ㄒoㄒ)/~~", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
                break;
            case "输入完成码" :
                final EditText etFinish;
                ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_finish, null);
                etFinish= (EditText) extView.findViewById(R.id.etfinish);
                etFinish.setHint("请输入你的完成码");
                mAlertViewExt = new AlertView("提示", "请输入对方给你的完成码！", "取消", null, new String[]{"确定"}, this, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        if (position == 0) {
                            if(etFinish.getText().equals(finish)){
                                Toast.makeText(DetailsActivity.this, "交易完成，谢谢您", Toast.LENGTH_SHORT).show();
                                express.setState(true);
                            }else {
                                Toast.makeText(DetailsActivity.this, "完成码出错，请与对方确认后再输", Toast.LENGTH_SHORT).show();
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
            case "等待中" :
                new AlertView("等待中", null, "取消", null,
                        new String[]{"删除请求","查看完成码"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener(){
                    public void onItemClick(Object o,int position){
                        if(position == 0){
                            ExpressHelp expressHelp = new ExpressHelp();
                            expressHelp.setObjectId(express.getObjectId());
                            expressHelp.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(mActivity, "成功删除", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        if(e.getErrorCode()==9016){
                                            Toast.makeText(mActivity, "网络不给力/(ㄒoㄒ)/~~", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }else if (position ==1){

                            final AlertDialog.Builder normalDialog =
                                    new AlertDialog.Builder(DetailsActivity.this);
                            normalDialog.setIcon(R.drawable.rule);
                            normalDialog.setTitle("完成码");
                            normalDialog.setMessage("请当面确认快递后，再提供给对方：\n"+finish);
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
                    }
                }).show();
                break;
            case "取货中" :
                new AlertView("取货中", null, "取消", null,
                        new String[]{"查看完成码", "联系帮助者"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener(){
                    public void onItemClick(Object o,int position){
                        if(position==0){
                            final AlertDialog.Builder normalDialog =
                                    new AlertDialog.Builder(DetailsActivity.this);
                            normalDialog.setIcon(R.drawable.rule);
                            normalDialog.setTitle("完成码");
                            normalDialog.setMessage("请当面确认快递后，再提供给对方：\n"+finish);
                            normalDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //...To-do
                                        }
                                    });

                            // 显示
                            normalDialog.show();
                        }else if(position==1){
                            Intent intent = new Intent(DetailsActivity.this,PersonalActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user",express.getHelpUser());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }


                }).show();
                break;
        }

    }

    public void help(ExpressHelp express) {
        express.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                dissmiss();
                if (e == null) {
                    Toast.makeText(DetailsActivity.this, "感谢您的帮助O(∩_∩)O~~", Toast.LENGTH_SHORT).show();
                } else {
                    if (e.getErrorCode() == 9016) {
                        Toast.makeText(DetailsActivity.this, "网络异常/(ㄒoㄒ)/~~", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}