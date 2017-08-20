package com.express.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.dialog.picker.DataPickerDialog;
import com.express.R;
import com.express.bean.Address;
import com.express.bean.ExpressHelp;
import com.express.bean.User;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class PublishActivity extends BaseActivity {

    @Bind(R.id.tb_publish)
    Toolbar tbPublish;
    @Bind(R.id.et_pickup_code)
    EditText etPickupCode;
    @Bind(R.id.et_address_name)
    EditText etAddressName;
    @Bind(R.id.et_address_telephone)
    EditText etAddressTelephone;
    @Bind(R.id.et_address_accuracy)
    EditText etAddressAccuracy;
    @Bind(R.id.et_point_name)
    EditText etPointName;
    @Bind(R.id.et_remarks)
    EditText etRemarks;
    @Bind(R.id.et_express_sms)
    EditText etExpressSms;
    @Bind(R.id.ll_address_dormitory)
    LinearLayout llAddressDormitory;
    @Bind(R.id.ll_address_weight)
    LinearLayout llAddressWeight;
    @Bind(R.id.ll_address_point)
    LinearLayout llAddressPoint;
    @Bind(R.id.btn_publish)
    Button btnPublish;
    @Bind(R.id.tv_address_dormitory)
    TextView tvAddressDormitory;
    @Bind(R.id.tv_address_weight)
    TextView tvAddressWeight;
    @Bind(R.id.tv_address_point)
    TextView tvAddressPoint;

    private ExpressHelp expressHelp = new ExpressHelp();
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_publish);
        initHome();

        try {
            for (Address address : DataSupport.findAll(Address.class)) {
                if (address.isDefault()){
                    etAddressTelephone.setText(address.getPhoneNumber());
                    etAddressAccuracy.setText(address.getAddress());
                    etAddressName.setText(address.getReceiver());
                    break;
                }
            }
        } finally {

        }
    }


    @OnClick({R.id.ll_address_dormitory, R.id.ll_address_weight, R.id.ll_address_point, R.id.btn_publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_address_dormitory:
                DataPickerDialog.Builder builder1 = new DataPickerDialog.Builder(this);
                List<String> data1 = new ArrayList<>();
                String[] dormitory = getResources().getStringArray(R.array.dormitory);
                for (int i = 1; i < dormitory.length; i++) {
                    data1.add(dormitory[i]);
                }

                DataPickerDialog dialog1 = builder1.setUnit("").setData(data1).setSelection(1).setTitle("宿舍")
                        .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                            @Override
                            public void onDataSelected(String itemValue) {
                                expressHelp.setDormitory(itemValue);
                                tvAddressDormitory.setText(itemValue);

                            }
                        }).create();

                dialog1.show();
                break;
            case R.id.ll_address_weight:
                DataPickerDialog.Builder builder2 = new DataPickerDialog.Builder(this);
                List<String> data2 = new ArrayList<>();
                String[] weight = getResources().getStringArray(R.array.express_weight);
                for (int i = 1; i < weight.length; i++) {
                    data2.add(weight[i]);
                }

                DataPickerDialog dialog2 = builder2.setUnit("").setData(data2).setSelection(1).setTitle("重量")
                        .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                            @Override
                            public void onDataSelected(String itemValue) {
                                expressHelp.setWeight(itemValue);
                                tvAddressWeight.setText(itemValue);

                            }
                        }).create();

                dialog2.show();
                break;
            case R.id.ll_address_point:
                DataPickerDialog.Builder builder3 = new DataPickerDialog.Builder(this);
                List<String> data3 = new ArrayList<>();
                String[] point = getResources().getStringArray(R.array.express_point);
                for (int i = 1; i < point.length; i++) {
                    data3.add(point[i]);
                }

                DataPickerDialog dialog3 = builder3.setUnit("").setData(data3).setSelection(1).setTitle("重量")
                        .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                            @Override
                            public void onDataSelected(String itemValue) {
                                expressHelp.setExpressPoint(itemValue);
                                tvAddressPoint.setText(itemValue);

                            }
                        }).create();

                dialog3.show();
                break;
            case R.id.btn_publish:
                if (TextUtils.isEmpty(etPickupCode.getText().toString())){
                    Toast.makeText(mActivity, "取货码不能为空", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(etPointName.getText().toString())){
                    Toast.makeText(mActivity, "快递点名称不能为空", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(etAddressAccuracy.getText().toString())){
                    Toast.makeText(mActivity, "收货地址不能为空", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(etAddressName.getText().toString())){
                    Toast.makeText(mActivity, "收件人不能为空", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(etAddressTelephone.getText().toString())){
                    Toast.makeText(mActivity, "电话号码不能为空", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(tvAddressDormitory.getText().toString())){
                    Toast.makeText(mActivity, "宿舍不能为空", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(tvAddressWeight.getText().toString())){
                    Toast.makeText(mActivity, "重量不能为空", Toast.LENGTH_SHORT).show();

                }else if (TextUtils.isEmpty(tvAddressPoint.getText().toString())){
                    Toast.makeText(mActivity, "快递点不能为空", Toast.LENGTH_SHORT).show();

                }
                else{
                    expressHelp.setState(false);
                    expressHelp.setUser(BmobUser.getCurrentUser(User.class));
                    expressHelp.setAddressAccuracy(etAddressAccuracy.getText().toString());
                    expressHelp.setExpressSms(etExpressSms.getText().toString());
                    expressHelp.setAddressTelephone(etAddressTelephone.getText().toString());
                    expressHelp.setPickupCode(etPickupCode.getText().toString());
                    expressHelp.setAddressName(etAddressName.getText().toString());
                    expressHelp.setPointName(etPointName.getText().toString());
                    expressHelp.setRemarks(etRemarks.getText().toString());
                    expressHelp.setHelpUser(null);
                    expressHelp.setPublishTime(System.currentTimeMillis());
                    user = BmobUser.getCurrentUser(User.class);
                    if (user.getHelpSum()>0){
                        showProgressDialog();
                        expressHelp.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    User newUser = new User(user.getSum(),user.getHelpSum()-1);
                                    newUser.update(user.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            dissmiss();
                                            if(e==null){
                                                Toast.makeText(mActivity, "发布成功", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(PublishActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    finish();
                                }else {
                                    dissmiss();
                                    Toast.makeText(mActivity, e.getErrorCode()+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(mActivity, "你的可请求帮助次数不够", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                break;
        }   }
    }
}
