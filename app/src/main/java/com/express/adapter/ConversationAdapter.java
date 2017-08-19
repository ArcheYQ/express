package com.express.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.express.R;
import com.express.activity.ChatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import cn.bmob.newim.bean.BmobIMConversation;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 雅倩宝宝 on 2017/8/11.
 */

public class ConversationAdapter extends BaseQuickAdapter<BmobIMConversation> {

    public ConversationAdapter( List<BmobIMConversation> data) {
        super(R.layout.item_conversation, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final BmobIMConversation bmobIMConversation) {
        baseViewHolder.setText(R.id.tv_con_name,bmobIMConversation.getConversationTitle());
        if (bmobIMConversation.getMessages().size()>0){
            baseViewHolder.setText(R.id.tv_con_sms,bmobIMConversation.getMessages().get(0).getContent());
        }else {
            baseViewHolder.setText(R.id.tv_con_sms,"");
        }
        SimpleDateFormat formatter =new SimpleDateFormat("MM-dd");
        baseViewHolder.setText(R.id.tv_con_time,formatter.format(new Date(bmobIMConversation.getUpdateTime())));
        Glide.with(mContext)
                .load(bmobIMConversation.getConversationIcon())
                .into((CircleImageView) baseViewHolder.getView(R.id.cc_con_tou));
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("start",bmobIMConversation);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
