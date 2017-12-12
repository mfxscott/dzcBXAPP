package com.bixian365.dzc.fragment.car;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.PayOrderDetailRecyclerViewAdapter;
import com.bixian365.dzc.entity.car.FromOrderEntity;
import com.bixian365.dzc.entity.car.OrderLinesEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mfx-t224 on 2017/9/27.
 */

public class PayOrderDetailActivity extends BaseActivity {
    private Activity activity;
    @BindView(R.id.pay_order_detail_list_recycler)
    RecyclerView  recyclerView;
    @BindView(R.id.pay_order_detail_name)
    TextView nameTv;
    @BindView(R.id.pay_order_detail_total)
    TextView totalTv;
    FromOrderEntity fromOrder;
    private ArrayList<OrderLinesEntity> orderList;
    private PayOrderDetailRecyclerViewAdapter simpAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_order_detail_list);
        ButterKnife.bind(this);
        activity =this;
        Bundle bundle = this.getIntent().getExtras();
        fromOrder = bundle.getParcelable("fromOrder");
        ArrayList list = bundle.getParcelableArrayList("orderLine");
        orderList= (ArrayList<OrderLinesEntity>) list.get(0);
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("商品详情");
//        nameTv.setText(fromOrder.getSettlementAmount()+"鲜好");
        totalTv.setText("总价："+"¥"+fromOrder.getTransactionAmount()+"");
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        simpAdapter = new PayOrderDetailRecyclerViewAdapter(activity,orderList);
        recyclerView.setAdapter(simpAdapter);
    }
}
