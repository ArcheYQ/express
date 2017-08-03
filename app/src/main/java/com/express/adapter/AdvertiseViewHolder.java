package com.express.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.express.ExpressApplication;
import com.express.R;

/**
 * Created by hyc on 2017/7/27 10:23
 */

public class AdvertiseViewHolder extends RecyclerView.ViewHolder {
    SliderLayout slider;
    boolean isLoad = false;
    public AdvertiseViewHolder(View itemView) {
        super(itemView);
        slider = (SliderLayout) itemView.findViewById(R.id.slider);
    }
    public void load() {
        if (!isLoad){
            TextSliderView mTextSlideView1 = new TextSliderView(ExpressApplication.getContext());
            mTextSlideView1.description("壮观=一座山+一座山+...")
                    .image(R.drawable.advertising_1)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);//裁剪图片
            TextSliderView mTextSlideView2 = new TextSliderView((ExpressApplication.getContext()));
            mTextSlideView2.description("帮助别人后 天都变成了棉花糖")
                    .image(R.drawable.advertising_2)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            TextSliderView mTextSlideView3 = new TextSliderView((ExpressApplication.getContext()));
            mTextSlideView3.description("一只鸭子登不上山顶")
                    .image(R.drawable.advertising_3)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop);
            slider.addSlider(mTextSlideView1);
            slider.addSlider(mTextSlideView2);
            slider.addSlider(mTextSlideView3);
            isLoad = true;

        }


    }
}
