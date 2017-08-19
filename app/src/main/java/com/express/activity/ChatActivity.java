package com.express.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.express.R;
import com.express.adapter.ChatAdapter;
import com.express.bean.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;


public class ChatActivity extends BaseActivity {

    @Bind(R.id.tv_chat_title)
    TextView tvChatTitle;
    @Bind(R.id.tb_conservation)
    Toolbar tbConservation;
    @Bind(R.id.rv_conversation)
    RecyclerView rvConversation;
    @Bind(R.id.et_chat)
    EditText etChat;
    @Bind(R.id.iv_send)
    ImageView ivSend;
    ChatAdapter adapter;
    BmobIMConversation bmobIMConversation;
    @Bind(R.id.sl_chat)
    SwipeRefreshLayout slChat;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_conservation);
        initHome();
        userId = BmobUser.getCurrentUser(User.class).getObjectId();
        bmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getIntent().getSerializableExtra("start"));
        tvChatTitle.setText(bmobIMConversation.getConversationTitle());
        rvConversation.setLayoutManager(new LinearLayoutManager(this));
        rvConversation.setItemAnimator(new DefaultItemAnimator());
        oldMessage();
        slChat.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                oldMessage();
                slChat.setRefreshing(false);
            }
        });

    }
    public void oldMessage (){
        bmobIMConversation.queryMessages(adapter ==null  ? null : adapter.getList().get(0),10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                if (e == null) {
                    if (adapter == null){
                        adapter = new ChatAdapter(list);
                        rvConversation.setAdapter(adapter);
                        rvConversation.scrollToPosition(adapter.getItemCount()-1);
                    }else{
                        adapter.insert(list);
                    }
                }else {
                    Toast.makeText(mActivity, "查询消息记录出错"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_send)
    public void onViewClicked() {
        if (TextUtils.isEmpty(etChat.getText().toString())) {
            Toast.makeText(this, "没有什么想说的吗n(*≧▽≦*)n", Toast.LENGTH_SHORT).show();
        } else {
            BmobIMTextMessage msg = new BmobIMTextMessage();
            msg.setContent(etChat.getText().toString());
            etChat.setText("");
            msg.setFromId(userId);
            adapter.add(msg);
            rvConversation.scrollToPosition(adapter.getItemCount() - 1);
            bmobIMConversation.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    if (e != null) {
                        Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        adapter.add(event.getMessage());
        rvConversation.scrollToPosition(adapter.getItemCount() - 1);
    }

}
