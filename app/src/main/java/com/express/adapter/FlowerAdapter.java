package com.express.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.express.R;
import com.express.bean.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hyc on 2017/8/20 08:37
 */

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.FlowerAdapterViewHolder> {
    List<Comment> list;

    public void setList(List<Comment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public FlowerAdapter(List<Comment> list){
        this.list=list;
    }
    @Override
    public FlowerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlowerAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flower,parent,false));
    }

    @Override
    public void onBindViewHolder(FlowerAdapterViewHolder holder, int position) {
       holder.load(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FlowerAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tvFlowerName;
        TextView tvFlowerTime;
        TextView tvFlowerComment;
        public FlowerAdapterViewHolder(View itemView) {
            super(itemView);
            tvFlowerComment = (TextView) itemView.findViewById(R.id.tv_flower_comment);
            tvFlowerName = (TextView) itemView.findViewById(R.id.tv_flower_name);
            tvFlowerTime = (TextView) itemView.findViewById(R.id.tv_flower_time);
        }
        public void load(int position){
            String[] str = list.get(position).getComment().split("#",3);
            tvFlowerName.setText(str[0]);
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            tvFlowerTime.setText(format.format(new Date(Long.parseLong(str[1]))));
            tvFlowerComment.setText(str[2]);
        }
    }
}
