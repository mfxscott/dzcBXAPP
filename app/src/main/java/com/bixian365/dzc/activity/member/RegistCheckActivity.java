package com.bixian365.dzc.activity.member;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;

/**
 * 注册前选择注册角色  老板OR个人
 * @author mfx
 * @time  2017/7/6 10:03
 */
public class RegistCheckActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_check);
        Button sell = (Button) findViewById(R.id.regist_check_sell_btn);
        Button pay = (Button) findViewById(R.id.regist_check_pay_btn);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent aa = new Intent(RegistCheckActivity.this, StoreMapActivity.class);
//                startActivity(aa);
                Intent sellregist = new Intent(RegistCheckActivity.this, Regis1Activity.class);
                sellregist.putExtra("registRole","32");
                startActivity(sellregist);
                finish();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regist = new Intent(RegistCheckActivity.this, Regis1Activity.class);
                regist.putExtra("registRole","64");
                startActivity(regist);
                finish();
            }
        });
    }
}
