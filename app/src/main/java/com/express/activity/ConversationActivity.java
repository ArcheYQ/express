package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.express.R;
import com.express.adapter.ConversationAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.newim.bean.BmobIMConversation;

public class ConversationActivity extends BaseActivity {

    @Bind(R.id.tb_conservation)
    Toolbar tbConservation;
    private RecyclerView rv_conversation;
    ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conservation);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_conservation);
        initHome();
        rv_conversation = (RecyclerView) findViewById(R.id.rv_conversation);
        List<BmobIMConversation> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BmobIMConversation bmob = new BmobIMConversation();
            list.add(bmob);

        }
        conversationAdapter = new ConversationAdapter(list);
        rv_conversation.setLayoutManager(new LinearLayoutManager(this));
        rv_conversation.setItemAnimator(new DefaultItemAnimator());
        rv_conversation.setAdapter(conversationAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conservation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this,FindActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
