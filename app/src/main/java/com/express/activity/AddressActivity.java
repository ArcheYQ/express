package com.express.activity;

import android.os.Bundle;
import com.express.R;


public class AddressActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        setToolBar(R.id.tb_address);
        initHome();
    }


}
