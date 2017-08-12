package com.express.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.express.R;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by 雅倩宝宝 on 2017/8/11.
 */

public class ConversationAdapter extends BaseQuickAdapter<BmobIMConversation> {

    public ConversationAdapter( List<BmobIMConversation> data) {
        super(R.layout.item_conversation, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BmobIMConversation bmobIMConversation) {
//        baseViewHolder.setText(R.id.tv_con_name,bmobIMConversation.getConversationTitle());
//        if (bmobIMConversation.getMessages().size()>0){
//            baseViewHolder.setText(R.id.tv_con_sms,"");
//        }else {
//            baseViewHolder.setText(R.id.tv_con_sms,bmobIMConversation.getMessages().get(bmobIMConversation.getMessages().size()-1).getContent());
//        }
//        SimpleDateFormat formatter =new SimpleDateFormat("MM-dd");
//        baseViewHolder.setText(R.id.tv_con_time,formatter.format(new Date(bmobIMConversation.getUpdateTime())));
//        Glide.with(mContext)
//                .load(bmobIMConversation.getConversationIcon())
//                .into((CircleImageView) baseViewHolder.getView(R.id.cc_con_tou));

    }
}
