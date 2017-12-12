package com.bixian365.dzc.fragment.my.partner;

import android.os.Bundle;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by NN on 2017/10/23.
 */

public class PartnerDetailInfoActivity extends BaseActivity{
    @BindView(R.id.partner_order_detail_no_tv)
    TextView  no;
    @BindView(R.id.partner_order_detail_shopname_tv)
    TextView  shopName;
    @BindView(R.id.partner_order_detail_time_tv)
    TextView  time;
    @BindView(R.id.partner_order_detail_sendname_tv)
    TextView  sendName;
    @BindView(R.id.partner_order_detail_carnum_tv)
    TextView  carNum;
    @BindView(R.id.partner_order_detail_driver_tv)
    TextView  driver;
    @BindView(R.id.partner_order_detail_phone_tv)
    TextView  phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_detail_info);
        ButterKnife.bind(this);
        registerBack();
        setTitle("订单详情");
        no.setText(this.getIntent().getStringExtra("no"));
        shopName.setText(this.getIntent().getStringExtra("shopName"));
        time.setText(this.getIntent().getStringExtra("time"));
        sendName.setText(this.getIntent().getStringExtra("sendName"));
        carNum.setText(this.getIntent().getStringExtra("carNum"));
        driver.setText(this.getIntent().getStringExtra("driver"));
        phone.setText(this.getIntent().getStringExtra("phone"));
    }
}
