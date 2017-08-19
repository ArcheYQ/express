package com.express.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.express.ExpressApplication;
import com.express.R;
import com.express.bean.User;

import java.util.List;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 雅倩宝宝 on 2017/8/14.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    List<BmobIMMessage> list;

    public List<BmobIMMessage> getList() {
        return list;
    }

    User user;

    public ChatAdapter(List<BmobIMMessage> list) {
        this.list = list;
        user = BmobUser.getCurrentUser(User.class);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RIGHT){
            return new TextRightViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right,parent,false));
        }else{
            return new TextLeftViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
         if (holder instanceof TextLeftViewHolder){
             TextLeftViewHolder holder1 = (TextLeftViewHolder) holder;
             holder1.load(position);
         }else {
             TextRightViewHolder holder1 = (TextRightViewHolder) holder;
             holder1.load(position);
         }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getFromId().equals(user.getObjectId())) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    public class TextLeftViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ccChatHead;
        TextView tvChatName;
        TextView tvChatContent;

        public TextLeftViewHolder(View itemView) {
            super(itemView);
            tvChatContent = (TextView) itemView.findViewById(R.id.tv_chat_content);
            tvChatName = (TextView) itemView.findViewById(R.id.tv_chat_name);
            ccChatHead = (CircleImageView) itemView.findViewById(R.id.cc_chat_head);
        }
        public void load(int position){
            if (list.get(position).getBmobIMUserInfo() != null){
                Glide.with(ExpressApplication.getContext())
                        .load(list.get(position).getBmobIMUserInfo().getAvatar())
                        .into(ccChatHead);
                tvChatName.setText(list.get(position).getBmobIMUserInfo().getName());
            }

            tvChatContent.setText(list.get(position).getContent());
        }
    }

    public class TextRightViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ccChatHead;
        TextView tvChatName;
        TextView tvChatContent;

        public TextRightViewHolder(View itemView) {
            super(itemView);
            tvChatContent = (TextView) itemView.findViewById(R.id.tv_chat_content);
            tvChatName = (TextView) itemView.findViewById(R.id.tv_chat_name);
            ccChatHead = (CircleImageView) itemView.findViewById(R.id.cc_chat_head);
        }
        public void load(int position){
            Glide.with(ExpressApplication.getContext())
                    .load(user.getHeadPicThumb())
                    .into(ccChatHead);
            tvChatName.setText(user.getNickname());
            tvChatContent.setText(list.get(position).getContent());
        }
    }

    public void add(BmobIMMessage bmobIMMessage){
        list.add(bmobIMMessage);
        notifyItemInserted(list.size()-1);
    }

    public void insert(List<BmobIMMessage> data){
        if (data != null &&data.size()>0){
            for (BmobIMMessage bmobIMMessage : data) {
                int position = data.indexOf(bmobIMMessage);
                list.add(position,bmobIMMessage);
                notifyItemInserted(position);
            }
        }
    }



}


