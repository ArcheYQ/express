package com.express.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.express.R;
import com.express.bean.ExpressHelp;

import java.util.List;

/**
 * Created by 雅倩宝宝 on 2017/8/16.
 */

public class HelpAdapter extends RecyclerView.Adapter<ExpressViewHolder>{
    /**
     * 刷新时
     * @param list
     */
    public void setList(List<ExpressHelp> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    List<ExpressHelp> list;
    Context context ;
    public HelpAdapter (List<ExpressHelp> list, Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public ExpressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_express,parent,false));
    }

    @Override
    public void onBindViewHolder(ExpressViewHolder holder, int position) {
        holder.load(context,list.get(position));

    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }
    public void add(List<ExpressHelp> data){
        if (data != null && data.size() > 0){
            for (ExpressHelp expressHelp : data) {
                list.add(expressHelp);
            }
            notifyItemInserted(list.size()-1);
        }

    }
}
