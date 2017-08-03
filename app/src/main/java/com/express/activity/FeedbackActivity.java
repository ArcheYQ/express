package com.express.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.express.R;
import com.express.bean.Feedback;
import com.express.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends BaseActivity {

    @Bind(R.id.tb_feedback)
    Toolbar tbFeedback;
    @Bind(R.id.cb_suggestion)
    CheckBox cbSuggestion;
    @Bind(R.id.tv_false)
    TextView tvFalse;
    @Bind(R.id.cb_false)
    CheckBox cbFalse;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.et_way)
    EditText etWay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setToolBar(R.id.tb_feedback);
        initHome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_suggestion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sumbit_suggestion){
            if (!cbSuggestion.isChecked()&&!cbFalse.isChecked()){
                Toast.makeText(this, "请选择一个反馈类型", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etContent.getText().toString())) {
                Toast.makeText(this, "请填写您的反馈内容", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etWay.getText().toString())){
                Toast.makeText(this, "请填写您的联系方式", Toast.LENGTH_SHORT).show();
            }else{
                Feedback feedback = new Feedback();
                feedback.setContent(etContent.getText().toString());
                feedback.setSuggestion(cbSuggestion.isChecked());
                feedback.setWay(etWay.getText().toString());
                feedback.setUser(BmobUser.getCurrentUser(User.class));
                showProgressDialog();
                feedback.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        dissmiss();
                        if (e == null){
                            Toast.makeText(FeedbackActivity.this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            if (e.getErrorCode()==9016){
                                Toast.makeText(FeedbackActivity.this, "网络不给力╮(╯_╰)╭", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(FeedbackActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.cb_suggestion, R.id.cb_false})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_suggestion:
                if (cbSuggestion.isChecked()){
                    cbFalse.setChecked(false);
                }
                break;
            case R.id.cb_false:
                if (cbFalse.isChecked()){
                    cbSuggestion.setChecked(false);
                }
                break;
        }
    }
}
