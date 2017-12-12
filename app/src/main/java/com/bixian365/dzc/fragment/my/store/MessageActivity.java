package com.bixian365.dzc.fragment.my.store;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;

/**
 * 用户消息管理
 * @author mfx
 * @time  2017/7/6 16:42
 */
public class MessageActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        registerBack();
        setTitle("消息中心");
        RelativeLayout orderRel = (RelativeLayout) findViewById(R.id.message_order_note_rel);
        RelativeLayout myMoneyRel = (RelativeLayout) findViewById(R.id.message_my_money_rel);
        RelativeLayout yhacRel = (RelativeLayout) findViewById(R.id.message_yh_ac_rel);
        orderRel.setOnClickListener(this);
        myMoneyRel.setOnClickListener(this);
        yhacRel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message_order_note_rel:
                break;
            case R.id.message_my_money_rel:
                break;
            case R.id.message_yh_ac_rel:
                break;
        }

    }
}
