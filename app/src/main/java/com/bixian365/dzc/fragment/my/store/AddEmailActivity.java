package com.bixian365.dzc.fragment.my.store;

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
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmailActivity extends BaseActivity {
  private EditText emailEdt;
    private Button  btn;
    private Activity activity;
    private Handler hand;
    private String emailStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_email);
        emailStr = this.getIntent().getStringExtra("email");
        activity = this;
        registerBack();
        setTitle("设置邮箱");

        emailEdt = (EditText) findViewById(R.id.set_email_edt);
        emailEdt.setText(emailStr+"");
        btn = (Button) findViewById(R.id.set_email_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 emailStr = emailEdt.getText().toString().trim();
                if(TextUtils.isEmpty(emailStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入邮箱");
                    return;
                }
                if(checkEmaile(emailStr)){
                setUserEmailHttp(emailStr);
                }else{
                    SXUtils.getInstance(activity).ToastCenter("请输入邮箱格式错误");
                }
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("设置成功");
                        EventBus.getDefault().post(new MessageEvent(10005,emailStr+""));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"my"));
                        finish();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg + "");
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }

    /**
     * 正则表达式校验邮箱
     * @param emaile 待匹配的邮箱
     * @return 匹配成功返回true 否则返回false;
     */
    private static boolean checkEmaile(String emaile){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(emaile);
        //进行正则匹配
        return m.matches();
    }
    /**
     * 设置邮箱
     */
    public void setUserEmailHttp(String emailStr) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("email",emailStr);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.UPDATE_USER_INFO, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                UserInfoEntity gde = null;
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = "";
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what =AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);

            }
        });
    }
}
