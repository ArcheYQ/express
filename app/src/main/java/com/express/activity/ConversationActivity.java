package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.express.R;
import com.express.adapter.ConversationAdapter;
import com.express.util.ConversationUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.event.MessageEvent;

public class ConversationActivity extends BaseActivity {

    @Bind(R.id.tb_conservation)
    Toolbar tbConservation;
    @Bind(R.id.sr_conversation)
    SwipeRefreshLayout srConversation;

    ConversationAdapter conversationAdapter;
    @Bind(R.id.rv_conversation)
    RecyclerView rv_conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_conservation);
        initHome();
        srConversation.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (ConversationUtil.getInstance().isConnect()){
                    conversationAdapter.setNewData(BmobIM.getInstance().loadAllConversation());
                }else {
                    Toast.makeText(ConversationActivity.this, "没有连接", Toast.LENGTH_SHORT).show();
                }
                srConversation.setRefreshing(false);
            }
        });

        if (ConversationUtil.getInstance().isConnect()){
            conversationAdapter = new ConversationAdapter(BmobIM.getInstance().loadAllConversation());
        }else {
            conversationAdapter = new ConversationAdapter(null);
        }
        rv_conversation.setLayoutManager(new LinearLayoutManager(this));
        rv_conversation.setItemAnimator(new DefaultItemAnimator());
        rv_conversation.setAdapter(conversationAdapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConversationUtil.getInstance().isConnect()){
            conversationAdapter.setNewData(BmobIM.getInstance().loadAllConversation());
        }else {
            Toast.makeText(ConversationActivity.this, "没有连接", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (ConversationUtil.getInstance().isConnect()){
            conversationAdapter.setNewData(BmobIM.getInstance().loadAllConversation());
        }else {
            Toast.makeText(ConversationActivity.this, "没有连接", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conservation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.find){
            startActivity(new Intent(this, FindActivity.class));
            finish();
            return true;
        }//

        return super.onOptionsItemSelected(item);
    }
}
