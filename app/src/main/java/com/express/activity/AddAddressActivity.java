package com.express.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.express.R;
import com.express.bean.Address;
import com.express.bean.User;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;


public class AddAddressActivity extends BaseActivity {
/////
    @Bind(R.id.tb_new_address)
    Toolbar tbNewAddress;
    @Bind(R.id.et_add_address_name)
    EditText etAddAddressName;
    @Bind(R.id.et_add_address_telephone)
    EditText etAddAddressTelephone;
    @Bind(R.id.et_address_add_accuracy)
    EditText etAddressAddAccuracy;
    @Bind(R.id.btn_add_address)
    Button btnAddAddress;

    private boolean isEdit = false;

    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_new_address);
        initHome();
        address = (Address) getIntent().getSerializableExtra("address");
        if (address !=null ){
            isEdit = true;
            etAddAddressName.setText(address.getReceiver());
            etAddressAddAccuracy.setText(address.getAddress());
            etAddAddressTelephone.setText(address.getPhoneNumber());
            btnAddAddress.setText("修改");
        }
    }

    @OnClick(R.id.btn_add_address)
    public void onViewClicked() {
        if (isEdit){
            for (Address address1 : DataSupport.findAll(Address.class)) {
                if (address1.getUserId().equals(address.getUserId())&&address1.getReceiver()
                        .equals(address.getReceiver())&&address1.getPhoneNumber()
                        .equals(address.getPhoneNumber())&&address1.getAddress()
                        .equals(address.getAddress())&&address1.isDefault() == address.isDefault()){
                    address1.delete();
                    break;
                }
            }
            address.setReceiver(etAddAddressName.getText().toString());
            address.setAddress(etAddressAddAccuracy.getText().toString());
            address.setPhoneNumber(etAddAddressTelephone.getText().toString());

            if (address.save()){
                Toast.makeText(mActivity, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {
            Connector.getDatabase();
            Address address= new Address();
            address.setUserId(BmobUser.getCurrentUser(User.class).getObjectId());
            address.setAddress(etAddressAddAccuracy.getText().toString());
            address.setReceiver(etAddAddressName.getText().toString());
            address.setPhoneNumber(etAddAddressTelephone.getText().toString());
            address.setDefault(false);
            if (address.save()){
                Toast.makeText(mActivity, "添加成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }
}
