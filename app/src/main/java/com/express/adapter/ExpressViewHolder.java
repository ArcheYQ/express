package com.express.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.express.ExpressApplication;
import com.express.R;
import com.express.activity.DetailsActivity;
import com.express.bean.ExpressHelp;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/8/1 19:10
 */

public class ExpressViewHolder extends RecyclerView.ViewHolder {

    CircleImageView cvItemTou;
    TextView tvItemNickname;
    TextView tvItemTime;
    ImageView ivHuangguan;
    ImageView ivLevel;
    TextView tvItemGetAddress;
    TextView tvPointName;
    ImageView ivWater6;
    ImageView ivWater5;
    ImageView ivWater4;
    ImageView ivWater3;
    ImageView ivWater2;
    ImageView ivWater1;
    View itemView;

    public ExpressViewHolder(View itemView) {
        super(itemView);
        cvItemTou = (CircleImageView) itemView.findViewById(R.id.cv_item_tou);
        tvItemNickname = (TextView) itemView.findViewById(R.id.tv_item_nickname);
        tvItemTime = (TextView) itemView.findViewById(R.id.tv_item_time);
        ivHuangguan = (ImageView) itemView.findViewById(R.id.iv_huangguan);
        ivLevel = (ImageView) itemView.findViewById(R.id.iv_level);
        tvItemGetAddress = (TextView) itemView.findViewById(R.id.tv_item_get_address);
        tvPointName = (TextView) itemView.findViewById(R.id.tv_point_name);
        ivWater1 = (ImageView) itemView.findViewById(R.id.iv_water1);
        ivWater2 = (ImageView) itemView.findViewById(R.id.iv_water2);
        ivWater3 = (ImageView) itemView.findViewById(R.id.iv_water3);
        ivWater4 = (ImageView) itemView.findViewById(R.id.iv_water4);
        ivWater5 = (ImageView) itemView.findViewById(R.id.iv_water5);
        ivWater6 = (ImageView) itemView.findViewById(R.id.iv_water6);
        this.itemView=itemView;
    }

    public void load(final Context context, final ExpressHelp expressHelp) {
        Glide.with(ExpressApplication.getContext())
                .load(expressHelp.getUser().getHeadPicThumb())
                .into(cvItemTou);
        tvPointName.setText(expressHelp.getPointName());
        tvItemNickname.setText(expressHelp.getUser().getNickname());
        tvItemGetAddress.setText(expressHelp.getAddressAccuracy());
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-hh:mm");
        tvItemTime.setText(format.format(new Date(expressHelp.getPublishTime())));
        if(expressHelp.getUser().getSum()<5){
            ivHuangguan.setImageResource(R.drawable.nolevel);
            ivLevel.setImageResource(R.drawable.ic_userlevel_0);
        }else if (expressHelp.getUser().getSum()<20){
            ivHuangguan.setImageResource(R.drawable.brass);
            ivLevel.setImageResource(R.drawable.ic_userlevel_1);
        }else if(expressHelp.getUser().getSum()<40){
            ivLevel.setImageResource(R.drawable.ic_userlevel_2);
            ivHuangguan.setImageResource(R.drawable.brass);
        }else if (expressHelp.getUser().getSum()<65){
            ivLevel.setImageResource(R.drawable.ic_userlevel_3);
            ivHuangguan.setImageResource(R.drawable.silver);
        }else if (expressHelp.getUser().getSum()<95){
            ivHuangguan.setImageResource(R.drawable.silver);
            ivLevel.setImageResource(R.drawable.ic_userlevel_4);
        }else if (expressHelp.getUser().getSum()<130){
            ivHuangguan.setImageResource(R.drawable.yellow);
            ivLevel.setImageResource(R.drawable.ic_userlevel_5);
        }else {
            ivHuangguan.setImageResource(R.drawable.yellow);
            ivLevel.setImageResource(R.drawable.ic_userlevel_6);
        }
        switch (expressHelp.getWeight().toString()){
            case "1-3瓶水" :
                ivWater1.setVisibility(View.VISIBLE);
                ivWater2.setVisibility(View.VISIBLE);
                break;
            case "4-6瓶水" :
                ivWater1.setVisibility(View.VISIBLE);
                ivWater2.setVisibility(View.VISIBLE);
                ivWater3.setVisibility(View.VISIBLE);
                ivWater4.setVisibility(View.VISIBLE);
                break;
            case "6瓶以上" :
                ivWater1.setVisibility(View.VISIBLE);
                ivWater2.setVisibility(View.VISIBLE);
                ivWater3.setVisibility(View.VISIBLE);
                ivWater4.setVisibility(View.VISIBLE);
                ivWater5.setVisibility(View.VISIBLE);
                ivWater6.setVisibility(View.VISIBLE);
                break;
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("express",expressHelp);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

}
