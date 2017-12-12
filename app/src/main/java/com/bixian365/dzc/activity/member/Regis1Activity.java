package com.bixian365.dzc.activity.member;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.fragment.CommonWebViewMainActivity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class Regis1Activity extends BaseActivity implements View.OnClickListener{
    private EditText registInputPhoneEdt;
    private TextView registGetcodeTv;
    private EditText registCodeEdt;
    private EditText registInputPsdEdt;
    private CheckBox registSeeTv;
    private Button  registBtn;
    private TextView sendCodeHintTv;//提示验证码发送
    private String  phoneStr="",codeStr="",psdStr="";
    private MyCountDownTimer mc;
    private TextView registRuleTv;//注册条款
    private Activity activity;
    private Handler   hand;
    private String userTag; //64 个人 32 商户判断用户商户还是个人注册
    private RelativeLayout relativeLayout;
    private EditText registRefereesPhoneEdt;
    private String   userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis1);
        userTag = this.getIntent().getStringExtra("registRole");
        activity = this;
        initView();

    }
    private void initView(){
        registerBack();
        setTitle(getString(R.string.regist_str));
        relativeLayout = (RelativeLayout) findViewById(R.id.regist_referees_rely);
        if(userTag.equals("32")){
            relativeLayout.setVisibility(View.VISIBLE);
        }
        registRefereesPhoneEdt = (EditText) findViewById(R.id.regist_referees_phone_edt);
        registRuleTv = (TextView) findViewById(R.id.regist_rule_tv);
        registRuleTv.setOnClickListener(this);
        sendCodeHintTv = (TextView) findViewById(R.id.regist_hint_send_code_tv);
        registInputPhoneEdt = (EditText) findViewById(R.id.regist_input_phone_edt);
        registInputPhoneEdt.addTextChangedListener(new TextWatcher() {
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
        registGetcodeTv = (TextView) findViewById(R.id.regist_getcode_tv);
        registGetcodeTv.setOnClickListener(this);
        registCodeEdt = (EditText) findViewById(R.id.regist_code_edt);
        registCodeEdt.addTextChangedListener(new TextWatcher() {
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
        registInputPsdEdt = (EditText) findViewById(R.id.regist_input_psd_edt);
        registInputPsdEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                psdStr = s.toString();
                inputEditListener();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registBtn = (Button) findViewById(R.id.regist_next);
        registBtn.setOnClickListener(this);
        registBtn.setEnabled(true);//测试时放开按钮
        registSeeTv = (CheckBox) findViewById(R.id.regist_see_tv);
        registSeeTv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//选择状态 显示明文--设置为可见的密码
                    registInputPsdEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
//默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    registInputPsdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("注册成功"+"");
                        if(userTag.equals("64")){
                            Intent intent = new Intent(activity,LoginNameActivity.class);
                            startActivity(intent);
                        }else {
                            //这里以ACCESS_COARSE_LOCATION为例
                            if (ContextCompat.checkSelfPermission(Regis1Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //申请WRITE_EXTERNAL_STORAGE权限
                                ActivityCompat.requestPermissions(Regis1Activity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        1000);//自定义的code
                            }else{
                                 userid = (String) msg.obj;
                                Intent aa = new Intent(activity, StoreMapActivity.class);
                                aa.putExtra("userID",userid);
                                activity.startActivity(aa);
                                finish();
                            }
                        }

//                        SXUtils.getInstance(activity).finishActivity();
                        break;
                    case AppClient.GETCODEMSG:
                        //验证码发送成功
                        int secs = Integer.parseInt((String)msg.obj);
                        mc = new MyCountDownTimer(secs*1000, 1000);
                        mc.start();
                        registGetcodeTv.setEnabled(false);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent aa = new Intent(activity, StoreMapActivity.class);
                aa.putExtra("userID",userid);
                activity.startActivity(aa);
                finish();
            } else
            {
                Toast.makeText(activity, "定位权限被拒绝,将无法定位当前周边店铺.", Toast.LENGTH_SHORT).show();
                Intent aa = new Intent(activity, StoreMapActivity.class);
                aa.putExtra("userID",userid);
                activity.startActivity(aa);
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }
    String mobilestr;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regist_next:
//                Message msg = new Message();
//                msg.what = 1000;
//                msg.obj = "23";
//                hand.sendMessage(msg);
//                break;
//                if(Tag.equals("64")){
//                    Intent intent = new Intent(activity,LoginNameActivity.class);
//                    startActivity(intent);
//                }else {
//                    Intent aa = new Intent(activity, StoreMapActivity.class);
//                    activity.startActivity(aa);
//                }




                mobilestr = registInputPhoneEdt.getText().toString();
                String codemsg = registCodeEdt.getText().toString();
                String psdstr = registInputPsdEdt.getText().toString();
                String  refereesPhoneStr= registRefereesPhoneEdt.getText().toString();
                if(TextUtils.isEmpty(mobilestr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入手机号码");
                    return;
                }
                if(TextUtils.isEmpty(codemsg)){
                    SXUtils.getInstance(activity).ToastCenter("请输入验证码");
                    return;
                }
                if(TextUtils.isEmpty(psdstr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入密码");
                    return;
                }
                if(userTag.equals("32")){
                    if(TextUtils.isEmpty(refereesPhoneStr)){
                        SXUtils.getInstance(activity).ToastCenter("请输入推荐人手机号码");
                        return;
                    }
                }
                SXUtils.showMyProgressDialog(activity,true);
                RegistHttp(mobilestr,psdstr,codemsg,refereesPhoneStr);
                break;
            case R.id.regist_getcode_tv:
                mobilestr = registInputPhoneEdt.getText().toString();
                if(!TextUtils.isEmpty(mobilestr) && mobilestr.length() == 11 && mobilestr.substring(0,1).equals("1")){
                    SXUtils.showMyProgressDialog(activity,true);
                    SXUtils.getInstance(activity).getCodeMsgHttp(activity,mobilestr,"2",hand);
                }
                else{
                    SXUtils.getInstance(activity).ToastCenter("输入手机格式不正确");
                }
                break;
            case R.id.regist_rule_tv:
                Intent intent = new Intent(Regis1Activity.this, CommonWebViewMainActivity.class);
                intent.putExtra("tag","2");
                intent.putExtra("postUrl",AppClient.SERVICERULE);
                startActivity(intent);
                break;
        }
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
            registGetcodeTv.setEnabled(true);
            registGetcodeTv.setTextColor(getResources().getColor(R.color.col_hint));
            registGetcodeTv.setText(getString(R.string.get_code_str));
        }
        @Override
        public void onTick(long millisUntilFinished) {
            sendCodeHintTv.setVisibility(View.VISIBLE);
            registGetcodeTv.setTextColor(getResources().getColor(R.color.qblue));
            registGetcodeTv.setText(getString(R.string.regist_send_code_yes_str)+millisUntilFinished / 1000+getString(R.string.regist_second_str));
        }
    }
    /**
     * 判断三个参数是否满足条件才显示注册按钮
     */
    private void inputEditListener(){
        if(phoneStr.equals("") || phoneStr.length() <11 || codeStr.equals("") ||codeStr.length()<6 ||psdStr.equals("") || psdStr.length()<6){
            registBtn.setEnabled(false);
            registBtn.setBackgroundResource(R.drawable.gray_round_shap);
        }else{
            registBtn.setEnabled(true);
            registBtn.setBackgroundResource(R.drawable.login_button_selector);
        }
    }
    public  void RegistHttp(String mobile,String psdStr,String codeStr,String majorAccount) {
        HttpParams params = new HttpParams();
        params.put("mobile", mobile);
        params.put("vcode", codeStr);
        params.put("registerType", "0");//0=手机,1=微信,2=QQ
        params.put("password", psdStr);
        params.put("majorAccount",majorAccount);
        params.put("tag",userTag);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.USER_REGIST, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("注册发送成功返回参数=======",jsonObject.toString());
                String userId = "";
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                    userId =  jsonObject1.getString("userId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = userId;
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