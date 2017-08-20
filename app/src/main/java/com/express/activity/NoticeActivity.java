package com.express.activity;

import android.os.Bundle;
import com.express.R;

public class NoticeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        setToolBar(R.id.tb_notice);
        initHome();
    }
}
