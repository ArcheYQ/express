package com.express.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.express.ExpressApplication;
import com.express.R;
import com.express.bean.ExpressHelp;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
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
    }

    public void load(ExpressHelp expressHelp) {
        Glide.with(ExpressApplication.getContext())
                .load(expressHelp.getUser().getHeadPicThumb())
                .into(cvItemTou);
        tvPointName.setText(expressHelp.getPointName());
        tvItemNickname.setText(expressHelp.getUser().getNickname());
        tvItemGetAddress.setText(expressHelp.getAddressAccuracy());
        SimpleDateFormat format = new SimpleDateFormat("MM-dd-hh:mm");
        tvItemTime.setText(format.format(new Date(expressHelp.getPublishTime())));

    }

}
