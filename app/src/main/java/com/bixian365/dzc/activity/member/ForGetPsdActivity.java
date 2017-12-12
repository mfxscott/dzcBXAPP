package com.bixian365.dzc.activity.member;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.OKManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ForGetPsdActivity extends BaseActivity implements View.OnClickListener{
    private EditText forgetInputPhoneEdt;
    private TextView forgetGetcodeTv;
    private EditText forgetCodeEdt;
    private EditText forgetNewPsdEdt;
    private EditText forgetConfiPsdEdt;
    private TextView forgetHintSendCodeTv;
    private Button forgetSave;
    private String phoneStr="",codeStr="",newPsdStr="",confPsdStr="";
    private MyCountDownTimer mc;
    private Activity activity;
    private Handler hand;
    private String token;//忘记第一步获取token，用于第二次请求验证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_get_psd);

        activity = this;
        initView();
    }
    private void initView(){
        registerBack();
        setTitle(getString(R.string.forget_psd_str));


        forgetInputPhoneEdt = (EditText) findViewById(R.id.forget_input_phone_edt);
        forgetInputPhoneEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneStr = s.toString();
                inputEditListener();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        forgetGetcodeTv = (TextView) findViewById(R.id.forget_getcode_tv);
        forgetGetcodeTv.setOnClickListener(this);
        forgetCodeEdt = (EditText) findViewById(R.id.forget_code_edt);
        forgetCodeEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeStr = s.toString();
                inputEditListener();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        forgetNewPsdEdt = (EditText) findViewById(R.id.forget_new_psd_edt);
        forgetNewPsdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPsdStr = s.toString();
                inputEditListener();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        forgetConfiPsdEdt = (EditText) findViewById(R.id.forget_confi_psd_edt);
        forgetConfiPsdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                confPsdStr = s.toString();
                inputEditListener();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        forgetHintSendCodeTv = (TextView) findViewById(R.id.forget_hint_send_code_tv);
        forgetSave = (Button) findViewById(R.id.forget_save);
        forgetSave.setOnClickListener(this);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    //第一步获取token
                    case 1000:
                        String jsonToken = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(jsonToken);
                            token = jsonObject.getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ForgetPsd2Http(mobilestr,token,towPsd);
                        return true;
                    //第二步修改忘记密码
                    case 1002:

                        break;
                    //验证码发送成功
                    case AppClient.GETCODEMSG:
                        int secs = Integer.parseInt((String)msg.obj);
                        mc = new MyCountDownTimer(secs*1000, 1000);
                        mc.start();
                        forgetGetcodeTv.setEnabled(false);
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
    /**
     * 倒计时
     */
    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            mc.cancel();
            forgetGetcodeTv.setEnabled(true);
            forgetGetcodeTv.setTextColor(getResources().getColor(R.color.col_hint));
            forgetGetcodeTv.setText(getString(R.string.get_code_str));
        }
        @Override
        public void onTick(long millisUntilFinished) {
            forgetHintSendCodeTv.setVisibility(View.VISIBLE);
            forgetGetcodeTv.setTextColor(getResources().getColor(R.color.qblue));
            forgetGetcodeTv.setText(getString(R.string.regist_send_code_yes_str)+millisUntilFinished / 1000+getString(R.string.regist_second_str));
        }
    }
    private String mobilestr;
    private String towPsd;
    @Override
    public void onClick(View v) {
        mobilestr = forgetInputPhoneEdt.getText().toString();
        switch(v.getId()){
            case R.id.forget_getcode_tv:

                if(TextUtils.isEmpty(mobilestr)) {
                    SXUtils.getInstance(activity).ToastCenter("请输入手机号码");
                    return;
                }
                if(mobilestr.length()<11){
                    SXUtils.getInstance(activity).ToastCenter("请输入正确的手机号码");
                    return;
                }
                SXUtils.showMyProgressDialog(activity,true);
                SXUtils.getInstance(activity).getCodeMsgHttp(activity,mobilestr,"3",hand);
                break;
            case R.id.forget_save:
                String codemsg = forgetCodeEdt.getText().toString();
                if(mobilestr.length()<11){
                    SXUtils.getInstance(activity).ToastCenter("请输入正确的手机号码");
                    return;
                }
                if(codemsg.length() != 6){
                    SXUtils.getInstance(activity).ToastCenter("请输入有效验证码");
                    return;
                }

                if(!confPsdStr.equals(newPsdStr)) {
                    SXUtils.getInstance(this).ToastCenter(getString(R.string.psd_two_error));
                    return;
                }
                if(forgetGetcodeTv.isEnabled()) {
                    SXUtils.getInstance(activity).ToastCenter("请获取验证码");
                    return;
                }
                towPsd = forgetConfiPsdEdt.getText().toString();
                SXUtils.showMyProgressDialog(activity,true);
                ForgetPsd1Http(mobilestr,codemsg);
                break;
        }
    }
    /**
     * 判断三个参数是否满足条件才显示注册按钮
     */
    private void inputEditListener(){
        if(phoneStr.equals("") || phoneStr.length() <11 || codeStr.equals("") ||codeStr.length()<6
            ||newPsdStr.equals("")||newPsdStr.length()<6 ||confPsdStr.equals("")||confPsdStr.length()<6){
            forgetSave.setEnabled(false);
            forgetSave.setBackgroundResource(R.drawable.gray_round_shap);
        }else{
                forgetSave.setEnabled(true);
                forgetSave.setBackgroundResource(R.drawable.login_button_selector);

        }
    }
    public void ForgetPsd1Http(String mobile, String codeStr){
        RequestBody requestBody = new FormBody.Builder()
                .add("mobile", mobile)
                .add("vcode", codeStr)
                .add("type", "3")//拉取类型(1=登录,2=注册,3=忘记密码,4=安全密码)
                .build();
        new OKManager(activity).sendStringByPostMethod(requestBody, AppClient.FORGET_PSD1, new OKManager.Func4() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("忘记密码1发送成功返回参数=======",jsonObject.toString());
                Message msg = new Message();
                msg.what = 1000;
                msg.obj =jsonObject+"";
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
    public void ForgetPsd2Http( String mobile, String token,String psd){
        RequestBody requestBody = new FormBody.Builder()
                .add("mobile", mobile)
                .add("password", psd)
                .add("token", token)//票据
                .build();
        new OKManager(activity).sendStringByPostMethod(requestBody, AppClient.FORGET_PSD2, new OKManager.Func4() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("忘记密码2发送成功返回参数=======",jsonObject.toString());
                Message msg = new Message();
                msg.what = 1002;
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
