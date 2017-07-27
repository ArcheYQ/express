package com.express.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.express.R;

import java.util.List;

/**
 * Created by hyc on 2017/7/27 10:10
 */

public class ExpressAdapter extends RecyclerView.Adapter{

    List<String> list;

    public ExpressAdapter(List<String> list){
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AdvertiseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertising,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdvertiseViewHolder advertiseViewHolder = (AdvertiseViewHolder) holder;
        advertiseViewHolder.load();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
