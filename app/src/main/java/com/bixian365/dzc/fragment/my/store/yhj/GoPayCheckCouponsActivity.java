package com.bixian365.dzc.fragment.my.store.yhj;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.PayCouponsRecyclerViewAdapter;
import com.bixian365.dzc.entity.car.OrderCouponsEntity;
import com.bixian365.dzc.utils.Logs;

import java.util.ArrayList;

/**
 * 购买商品选择优惠券
 */
public class GoPayCheckCouponsActivity extends BaseActivity {
    private Activity activity;
    private ArrayList<OrderCouponsEntity> couponsList;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_pay_check_coupons);
        activity = this;
        Bundle bundle = this.getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("coupsons");
        couponsList= (ArrayList<OrderCouponsEntity>) list.get(0);
        Logs.i("++++++++++++++++++"+couponsList.size());
        initView();
    }

    private void initView(){
        registerBack();
        setTitle("选择优惠券");
        recyclerView = (RecyclerView) findViewById(R.id.gopay_coupons_recyclerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new PayCouponsRecyclerViewAdapter(activity,couponsList,1));
    }


}
