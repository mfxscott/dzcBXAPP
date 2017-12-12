package com.bixian365.dzc.fragment.my.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.fragment.my.buyer.ExtractAddBankCardActivity;

public class NoBindCardActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_bind_card);
        registerBack();
        setTitle("添加银行卡");
        Button btn = (Button) findViewById(R.id.no_add_card_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoBindCardActivity.this,ExtractAddBankCardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
