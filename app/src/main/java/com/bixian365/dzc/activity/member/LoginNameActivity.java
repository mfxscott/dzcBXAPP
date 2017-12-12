package com.bixian365.dzc.activity.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;

/**
 * 供应商测试账号
 * 13129343614  密码 1
 */
public class LoginNameActivity extends Activity implements View.OnClickListener {
    private EditText loginAccPhoneEdt;
    private EditText loginAccPsdEdt;
    private Button loginAccNext;
    private TextView loginAccUsecodeTv;
    private TextView loginAccForgetpsdTv;
     private String   nameStr="",psdStr="";
    private Handler hand;
    public static Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_name);
        activity = this;
        initView();
    }
    private void initView(){
        setTitle(getString(R.string.login_str));
        TextView backTv = (TextView) findViewById(R.id.all_title_back_tv);
        backTv.setOnClickListener(this);
        backTv.setBackgroundResource(R.mipmap.close);
        TextView  titleRight = (TextView) findViewById(R.id.all_title_right);
        titleRight.setText(getString(R.string.regist_str));
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setTextColor(getResources().getColor(R.color.qblue));
        titleRight.setOnClickListener(this);
          findViewById(R.id.cs_test).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  dialogChoice();
              }
          });
        loginAccPhoneEdt = (EditText) findViewById(R.id.login_acc_phone_edt);
        loginAccPhoneEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameStr = s.toString();
                //生产环境需要放开 用于检验输入框参数是否合法
                inputEditListener();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginAccPsdEdt = (EditText) findViewById(R.id.login_acc_psd_edt);
        loginAccPsdEdt.addTextChangedListener(new TextWatcher() {
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
        loginAccNext = (Button) findViewById(R.id.login_acc_next);
        loginAccNext.setOnClickListener(this);
        loginAccUsecodeTv = (TextView) findViewById(R.id.login_acc_usecode_tv);
        loginAccUsecodeTv.setOnClickListener(this);
        loginAccForgetpsdTv = (TextView) findViewById(R.id.login_acc_forgetpsd_tv);
        loginAccForgetpsdTv.setOnClickListener(this);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    //登录成功
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("登录成功");
                        if(MainFragmentActivity.homeRb == null ) {
//                            Intent mainintent = new Intent(activity, MainFragmentActivity.class);
//                            startActivity(mainintent);
                            SXUtils.getInstance(activity).IntentMain(activity);
//                            finish();
                        }else{
                            if(AppClient.LOGOUT){
                                SXUtils.getInstance(activity).IntentMain(activity);
                            }else {
                                //登录成功刷新购物车和我的数据
                                EventBus.getDefault().post(new MessageEvent(1, "login suc"));
                                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10003,"home"));
                                finish();
                            }
                        }
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100022, "webview"));
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_acc_next:
                String phone = loginAccPhoneEdt.getText().toString();
                String psd = loginAccPsdEdt.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    SXUtils.getInstance(activity).ToastCenter("请输入账号或手机号");
                    return;
                }
                if(TextUtils.isEmpty(psd)){
                    SXUtils.getInstance(activity).ToastCenter("请输入密码");
                    return;
                }
                SXUtils.showMyProgressDialog(activity,false);
//                psdLoginHttp(phone,psd);
                SXUtils.getInstance(activity).psdLoginHttp(hand,phone,psd);
                break;
            case R.id.login_acc_usecode_tv:
                Intent intent = new Intent(LoginNameActivity.this, LoginCodeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login_acc_forgetpsd_tv:
                Intent forget = new Intent(LoginNameActivity.this, ForGetPsdActivity.class);
                startActivity(forget);
                break;
            case R.id.all_title_right:
                Intent regist = new Intent(LoginNameActivity.this, RegistCheckActivity.class);
                startActivity(regist);
                break;
            case R.id.all_title_back_tv:
//                if(MainFragmentActivity.badge1 == null){
//                    Intent mainintent = new Intent(activity, MainFragmentActivity.class);
//                    startActivity(mainintent);
                if(AppClient.USERROLETAG.equals("32")||AppClient.USERROLETAG.equals("64")){
                    finish();
                }else{
                    SXUtils.getInstance(activity).IntentMain(activity);
//                    SXUtils.getInstance(activity).finishActivity();
                }

//                }
//                finish();

                break;
        }
    }
    /**
     * 判断三个参数是否满足条件才显示注册按钮
     */
    private void inputEditListener(){
        if(nameStr.equals("") || nameStr.length() <11 || psdStr.equals("")){//变态的测试环境密码是一去掉小于6位验证   || psdStr.length()<6
            loginAccNext.setEnabled(false);
            loginAccNext.setBackgroundResource(R.drawable.gray_round_shap);
        }else{
            loginAccNext.setEnabled(true);
            loginAccNext.setBackgroundResource(R.drawable.login_button_selector);
        }
    }
//    public void psdLoginHttp(final  String mobile,final String psdStr){
//        RequestBody requestBody = new FormBody.Builder()
//                .add("mobile", mobile)
//                .add("password",psdStr)
//                .add("loginType","1")//0=验证码登录,1=密码登录
//                .build();
//        new OKManager(this).sendStringByPostMethod(requestBody, AppClient.USER_LOGIN, new OKManager.Func4() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                Logs.i("密码登录发送成功返回参数=======",jsonObject.toString());
//                try {
//                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
//                    AppClient.USER_ID = jsonObject1.getString("uid");
//                    AppClient.USER_SESSION = jsonObject1.getString("sid");
//                    AppClient.USERROLETAG = jsonObject1.getString("tag");
//                    SXUtils.getInstance(activity).setSharePreferences("username",mobile);
//                    SXUtils.getInstance(activity).setSharePreferences("psd",psdStr);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.what = 1000;
//                msg.obj = "";
//                hand.sendMessage(msg);
//            }
//            @Override
//            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);
//            }
//        });
//    }
    /**
     * 单选
     */
    private void dialogChoice() {
        final String items[] = {"摊主:18165717671","合伙人:18165719379","联创中心:18665966074","供应商:18507386208"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this,3);
        builder.setTitle("测试账号");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = items[which];
                        loginAccPhoneEdt.setText(""+str.substring(str.length()-11,str.length()));
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(activity, "确定", Toast.LENGTH_SHORT)
//                        .show();
            }
        });
        builder.create().show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            return false;
//            SXUtils.getInstance(activity).IntentMain();
//            SXUtils.getInstance(activity).finishActivity();
        }
        return true;
    }
}
