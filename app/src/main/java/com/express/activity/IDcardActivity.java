package com.express.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.model.ApiCallback;
import com.alibaba.cloudapi.sdk.model.ApiRequest;
import com.alibaba.cloudapi.sdk.model.ApiResponse;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.express.R;
import com.express.client.Demo_数据服务_5_1_身份证识别;
import com.express.client.HttpApiClient_数据服务_5_1_身份证识别;
import com.express.util.IDUtil;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IDcardActivity extends BaseActivity {

    @Bind(R.id.iv_personal_bg)
    ImageView ivPersonalBg;
    @Bind(R.id.iv_cammer)
    ImageView ivCammer;
    @Bind(R.id.tv_true_name)
    TextView tvTrueName;
    @Bind(R.id.tv_student_sex)
    TextView tvStudentSex;
    @Bind(R.id.tv_student_nationality)
    TextView tvStudentNationality;
    @Bind(R.id._student_age)
    TextView StudentAge;
    @Bind(R.id.tv_student_idNum)
    TextView tvStudentIdNum;
    @Bind(R.id.ll_information)
    LinearLayout llInformation;
    @Bind(R.id.tb_personal)
    Toolbar tbPersonal;
    @Bind(R.id.btn_id_ensure)
    Button btnIdEnsure;
    public static final int REQUEST_CODE_IMAGE = 200;
    private String info = "";
    String[] infos = new String[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.iv_cammer, R.id.btn_id_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cammer:
                if (getCcamra() && getStorage()) {
                    SImagePicker
                            .from(IDcardActivity.this)
                            .maxCount(1)
                            .pickMode(SImagePicker.MODE_IMAGE)
                            .forResult(REQUEST_CODE_IMAGE);
                } else {
                    Toast.makeText(mActivity, "请给予权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_id_ensure:
                if(tvTrueName.getText().equals("")){
                    new AlertView("提示", "认证失败，请重新拍照上传", null, new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
                        @Override
                        public void onItemClick(Object o, int position) {
                        }
                    }).show();
                }else {
                    Intent intent = new Intent();
                    intent.setClass(IDcardActivity.this,ImfamationActivity.class);
                    intent.putExtra("idInfo_name",infos[0]);
                    intent.putExtra("idInfo_sex",infos[3]);
                    startActivity(intent);
                }

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            Log.i("1", "pathBase64" + pathList.get(0));
            String base = IDUtil.imageToBase64(pathList.get(0));
            Demo_数据服务_5_1_身份证识别.印刷文字识别_身份证识别HttpTest(IDUtil.imageToBase64(pathList.get(0)));
            String real = "{\n" +
                    "\t\"image\":\""+base+"\",\n" +
                    "\t\t\t\t\"configure\":{\n" +
                    "\t\t\t\t\t\"side\":\"face\"\n" +
                    "\t\t\t\t}\n" +
                    "}";
            real.replace("123",base);
            IDUtil.saveFile(real);
            HttpApiClient_数据服务_5_1_身份证识别.getInstance().印刷文字识别_身份证识别(real.getBytes(SdkConstant.CLOUDAPI_ENCODING), new ApiCallback() {
                @Override
                public void onFailure(ApiRequest apiRequest, Exception e) {

                }

                @Override
                public void onResponse(ApiRequest apiRequest, ApiResponse apiResponse) {
                    try {
                        info = Demo_数据服务_5_1_身份证识别.getResultString(apiResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (info.equals("")){

        }else {
            infos = IDUtil.parseJOSNWithGSON(info);
            /**
             * info[0] = jsonObject.getString("name");
             info[1] = jsonObject.getString("nationality");
             info[2] = jsonObject.getString("num");
             info[3] = jsonObject.getString("sex");
             info[4] = jsonObject.getString("birth");
             */
            tvTrueName.setText(infos[0]);
            tvStudentNationality.setText(infos[1]);
            tvStudentIdNum.setText(infos[2]);
            tvStudentSex.setText(infos[3]);
            StudentAge.setText(infos[4]);
        }
    }
}
