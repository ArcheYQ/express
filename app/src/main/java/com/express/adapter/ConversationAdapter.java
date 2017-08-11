package com.express.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;

/**
 * Created by 雅倩宝宝 on 2017/8/11.
 */

public class ConversationAdapter extends BaseQuickAdapter<BmobIMConversation> {

    public ConversationAdapter(int layoutResId, List<BmobIMConversation> data) {

        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BmobIMConversation bmobIMConversation) {

    }
}
