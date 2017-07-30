package com.express.adapter;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.express.R;
import com.express.activity.AddAddressActivity;
import com.express.bean.Address;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by hyc on 2017/7/30 09:38
 */

public class AddressAdapter extends BaseQuickAdapter<Address> {

    public AddressAdapter(List<Address> data) {
        super(R.layout.item_address, data);

    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final Address address) {
        baseViewHolder.setText(R.id.tv_item_address_name,address.getReceiver());
        baseViewHolder.setText(R.id.tv_item_address,address.getAddress());
        baseViewHolder.setText(R.id.tv_item_tel,address.getPhoneNumber());
        baseViewHolder.setOnClickListener(R.id.btn_address_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("address",address);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        baseViewHolder.setOnClickListener(R.id.btn_address_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.delete() != -1){
                    int position = getData().indexOf(address);
                    getData().remove(position);
                    notifyItemRemoved(position);
                }
            }
        });



        final CheckBox checkBox = baseViewHolder.getView(R.id.cb_set_default);
        if (address.isDefault()){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Address address1 : getData()) {
                    if (address1.isDefault()){
                        address1.setDefault(false);
                        ContentValues values = new ContentValues();
                        values.put("isDefault",false);
                        notifyItemChanged(getData().indexOf(address1));
                        DataSupport.update(Address.class,values,address1.getId());
                        break;
                    }
                }
                if (checkBox.isChecked()){
                    address.setDefault(true);
                    ContentValues values = new ContentValues();
                    values.put("isDefault",true);
                    DataSupport.update(Address.class, values, address.getId());
                }


            }
        });

    }
}
