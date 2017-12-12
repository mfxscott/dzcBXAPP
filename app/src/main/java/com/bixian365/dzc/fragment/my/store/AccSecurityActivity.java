package com.bixian365.dzc.fragment.my.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.activity.member.UpdataLoginPsdActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.fragment.CommonWebViewMainActivity;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 账号管理
 */
public class AccSecurityActivity extends BaseActivity {
    private Activity activity;
    private Handler hand;
    private UserInfoEntity userinfo;
    @BindView(R.id.security_bind_phone)
    TextView  bindPhoneTv;
    @BindView(R.id.security_updata_login_psd_rel)
    RelativeLayout  updataLoginPsdRel;
    @BindView(R.id.security_set_email_rel)
    RelativeLayout emailRel;
    @BindView(R.id.manage_email_tv)
    TextView  email;
    @BindView(R.id.security_phone_tv)
    TextView phone;
    @BindView(R.id.security_feedback_rel)
    RelativeLayout  feedbackRel;
    @BindView(R.id.security_help_rel)
    RelativeLayout  helpRel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_security);
        activity = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Bundle bundle = this.getIntent().getExtras();
        userinfo =  bundle.getParcelable("userinfo");
        activity = this;
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("账号管理");
//        phone.setText(userinfo.getMobile()+"");
        email.setText(TextUtils.isEmpty(userinfo.getEmail())?"未设置":userinfo.getEmail());
        bindPhoneTv.setText("已绑定手机号"+userinfo.getMobile()+"");
        updataLoginPsdRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forget = new Intent(activity, UpdataLoginPsdActivity.class);
                startActivity(forget);
            }
        });
        emailRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AddEmailActivity.class);
                intent.putExtra("email",TextUtils.isEmpty(userinfo.getEmail())?"":userinfo.getEmail());
                startActivity(intent);
            }
        });
        feedbackRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedintent = new Intent(activity, CommonWebViewMainActivity.class);
                feedintent.putExtra("tag","2");
                feedintent.putExtra("postUrl", AppClient.FEEDBACK);
                startActivity(feedintent);
            }
        });
        helpRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qintent = new Intent(activity, CommonWebViewMainActivity.class);
                qintent.putExtra("tag","2");
                qintent.putExtra("postUrl",AppClient.FAQ);
                startActivity(qintent);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        //传递1or2 登录成功刷新购物车 和点击加入购物车商品 都刷新购物车||订单提交成功刷新购物车
        if(messageEvent.getTag()==10005){
            userinfo.setEmail(messageEvent.getMessage());
            email.setText(messageEvent.getMessage());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
