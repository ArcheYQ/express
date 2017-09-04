package com.express.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.express.ExpressApplication;
import com.express.R;
import com.express.bean.ExpressHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hyc on 2017/7/27 10:23
 */

public class AdvertiseViewHolder extends RecyclerView.ViewHolder {

    boolean isLoad = false;
    @Bind(R.id.slider)
    SliderLayout slider;
    @Bind(R.id.sr_dormitory)
    Spinner srDormitory;
    @Bind(R.id.sr_point)
    Spinner srPoint;
    @Bind(R.id.sr_weight)
    Spinner srWeight;
    public String dormitory;
    public String point;
    public String weight;

    public AdvertiseViewHolder(View itemView) {
        super(itemView);
        slider = (SliderLayout) itemView.findViewById(R.id.slider);
        srDormitory = (Spinner) itemView.findViewById(R.id.sr_dormitory);
        srPoint = (Spinner) itemView.findViewById(R.id.sr_point);
        srWeight = (Spinner) itemView.findViewById(R.id.sr_weight);
    }
    public List<ExpressHelp> screenData(List<ExpressHelp> express){
        List<ExpressHelp> list = new ArrayList<>();
        for (ExpressHelp expres : express) {
            Log.i("TAG",dormitory+"   "+"  "+point+"  "+weight);
            if (!TextUtils.isEmpty(dormitory)){
                if (!expres.getDormitory().equals(dormitory)){
                    continue;
                }
            }

            if (!TextUtils.isEmpty(point)){
                if (!expres.getPointName().equals(point)){
                    continue;
                }
            }

            if (!TextUtils.isEmpty(weight)){
                if (!expres.getWeight().equals(weight)){
                    continue;
                }
            }
            list.add(expres);
        }

        return list;
    }

    public void load(final ExpressAdapter adapter , final List<ExpressHelp> oldList ) {
        if (!isLoad) {
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

        srDormitory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:dormitory = "";break;
                    case 1:dormitory = "1-5栋";break;
                    case 2:dormitory = "6-10栋";break;
                    case 3:dormitory = "11-15栋";break;
                    case 4:dormitory = "16-20栋";break;
                    case 5:dormitory = "21-23栋";break;
                    case 6:dormitory = "24-29栋";break;
                    case 7:dormitory = "30-32栋";break;
                    case 8:dormitory = "33-35栋";break;

                }
                adapter.setList(screenData(oldList),false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        srPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:point = "";break;
                    case 1:point = "东门";break;
                    case 2:point = "三食堂";break;
                    case 3:point = "五食堂";break;
                    case 4:point = "一食堂";break;
                    case 5:point = "冶金楼";break;
                    case 6:point = "唯品会";break;
                    case 7:point = "聚美优品";break;
                    case 8:point = "其他";break;

                }
                adapter.setList(screenData(oldList),false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        srWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:weight = "";break;
                    case 1:weight = "1-3瓶水";break;
                    case 2:weight = "4-6瓶水";break;
                    case 3:weight = "6瓶以上";break;

                }
                adapter.setList(screenData(oldList),false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });



    }


}
