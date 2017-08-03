package com.express.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.express.R;
import com.express.adapter.AddressAdapter;
import com.express.bean.Address;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;


public class AddressActivity extends BaseActivity {

    @Bind(R.id.rv_address)
    RecyclerView rvAddress;

    private AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_address);
        initHome();
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 添加菜单按钮
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_address,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Address> addresses = DataSupport.findAll(Address.class);
        String userId = BmobUser.getCurrentUser().getObjectId();
        for (Address address : addresses) {
            if (!userId.equals(address.getUserId())){
                addresses.remove(address);
            }
        }
        addressAdapter = new AddressAdapter(addresses);
        rvAddress.setAdapter(addressAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.new_address){
            startActivity(new Intent(this,AddAddressActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


