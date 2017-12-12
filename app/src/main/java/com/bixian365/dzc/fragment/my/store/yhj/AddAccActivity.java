package com.bixian365.dzc.fragment.my.store.yhj;

import android.os.Bundle;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;

public class AddAccActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acc);
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("员工子账号管理");
    }
}
