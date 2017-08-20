package com.express.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.express.R;
import com.express.bean.ExpressHelp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyc on 2017/7/27 10:10
 */

public class ExpressAdapter extends RecyclerView.Adapter{

    List<ExpressHelp> oldList;

    public void setList(List<ExpressHelp> list,boolean isNeedChange) {
        if (list != null){
            this.list = list;
            if (isNeedChange){
                oldList = new ArrayList<>();
                for (ExpressHelp expressHelp : list) {
                    oldList.add(expressHelp);
                }
            }
            list.add(0,new ExpressHelp());
            notifyDataSetChanged();
        }

    }

    public void addData(List<ExpressHelp> list){
        for (ExpressHelp expressHelp : list) {
            this.list.add(expressHelp);
            oldList.add(expressHelp);
            notifyItemInserted(getItemCount()-1);
        }
    }

    List<ExpressHelp> list;

    private Context context;

    public ExpressAdapter(List<ExpressHelp> list , Context context){
        this.list = list;
        this.context = context;


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new AdvertiseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertising,parent,false));
        }else {
            return new ExpressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_express,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdvertiseViewHolder){
            AdvertiseViewHolder advertiseViewHolder = (AdvertiseViewHolder) holder;
            advertiseViewHolder.load(this,oldList);
        }else {
            ExpressViewHolder expressViewHolder = (ExpressViewHolder) holder;
            expressViewHolder.load(context , list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
