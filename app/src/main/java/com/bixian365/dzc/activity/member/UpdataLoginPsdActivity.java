package com.bixian365.dzc.activity.member;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdataLoginPsdActivity extends BaseActivity {
       private Activity activity;
    private Handler hand;
    @BindView(R.id.update_forget_save)
    Button  btn;
    @BindView(R.id.update_forget_code_edt)
    EditText  oldPsdEdt;
    @BindView(R.id.update_forget_new_psd_edt)
    EditText  newPsdEdt;
    @BindView(R.id.update_forget_confi_psd_edt)
    EditText newConfPsdEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_login_psd);
        ButterKnife.bind(this);
        activity = this;
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("修改登录密码");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldstr = oldPsdEdt.getText().toString().trim();
                String newPsdStr = newPsdEdt.getText().toString().trim();
                String newConStr = newConfPsdEdt.getText().toString().trim();
                if(TextUtils.isEmpty(oldstr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入原始密码");
                    return;
                }
                if(TextUtils.isEmpty(newPsdStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入新密码");
                    return;
                }
                if(TextUtils.isEmpty(newConStr)){
                    SXUtils.getInstance(activity).ToastCenter("请再次输入新密码");
                    return;
                }
                if(!newPsdStr.equals(newConStr)){
                    SXUtils.getInstance(activity).ToastCenter("两次密码输入不一致");
                    return;
                }
                SXUtils.showMyProgressDialog(activity,true);
                UpdataLoginPsdHttp(oldstr,newConStr);
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    //登录成功
                    case 1000:
                        SXUtils.DialogDismiss();
                        SXUtils.getInstance(activity).ToastCenter("修改成功");
//                        Intent intent = new Intent(activity, LoginNameActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("isEnter","12");
//                        startActivity(intent);
                        finish();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    public void UpdataLoginPsdHttp(String oldPsd,String newPsd) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("originalPassword", oldPsd);
        httpParams.put("newPassword",newPsd);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.UPDATE_LOGINPSD, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = "";
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }
}
