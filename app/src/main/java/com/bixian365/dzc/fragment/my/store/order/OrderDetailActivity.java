package com.bixian365.dzc.fragment.my.store.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.WaitPayGoodsRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.orderlist.OrderDetailEntity;
import com.bixian365.dzc.fragment.my.pay.TopUpActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private String orderTag,orderId,orderAddress,orderTime,orderTotal,name,tradeNo;
    private TextView takeOrder,cancelTv,cancelOrder;
    private LinearLayout  btnLin;
    private  Activity activity;
    private RecyclerView recyclerView;
    //    private List<OrderGoodsInfoEntity> orderGoodsList;
    @BindView(R.id.order_detail_name_tv)
    TextView nameTv;
    @BindView(R.id.order_detail_total_tv)
    TextView orderTotalTv;
    @BindView(R.id.order_detail_no_tv)
    TextView orderNoTv;
    @BindView(R.id.order_detail_time_tv)
    TextView orderTimeTv;
    @BindView(R.id.order_detail_paytype_tv)
    TextView orderPayTypeTv;
    @BindView(R.id.order_detail_address_tv)
    TextView orderAddressTv;
    @BindView(R.id.order_detail_pay_total_tv)
    TextView payTotalTv;
    private OrderDetailEntity orderDetail;
    @BindView(R.id.order_detail_revice_phone_tv)
    TextView  revicePhoneTv;
    @BindView(R.id.order_detail_revice_tv)
    TextView reviceTv;
    @BindView(R.id.order_detail_revice_liny)
    LinearLayout reviceLiny;
    @BindView(R.id.order_detail_revice_phone_liny)
    LinearLayout  revicePhoneLiny;
    @BindView(R.id.order_detail_pay_yf_tv)
    TextView    freightTv;
    @BindView(R.id.order_detail_pay_yh_tv)
    TextView  yhTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        activity = this;
        ButterKnife.bind(activity);
        orderTag = this.getIntent().getStringExtra("orderTag");
        Bundle bundle = this.getIntent().getExtras();
//        ArrayList list = bundle.getParcelableArrayList("orderDetail");
        orderId = bundle.getString("orderNo");
        tradeNo = bundle.getString("tradeNo");
//        orderAddress = bundle.getString("orderAddress");
//        orderTime = bundle.getString("orderTime");
//        name = bundle.getString("name");
        orderTotal = bundle.getString("total");
//        orderGoodsList= (List<OrderGoodsInfoEntity>) list.get(0);//强转成你自己定义的list，这样list2就是你传过来的那个list了。
        initView();
        EventBus.getDefault().register(this);
        SXUtils.getInstance(activity).showMyProgressDialog(activity,true);
        getOrderDetailHttp();
    }
    private void initView(){

        recyclerView = (RecyclerView) findViewById(R.id.order_detail_item_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        takeOrder = (TextView) findViewById(R.id.order_detail_wait_pay_take_btn);
        takeOrder.setOnClickListener(this);
        btnLin = (LinearLayout) findViewById(R.id.order_detail_btn_lin);
        cancelTv = (TextView) findViewById(R.id.order_detail_done_tv);
        cancelOrder = (TextView) findViewById(R.id.order_detail_wait_pay_cancel_btn);
        cancelOrder.setOnClickListener(this);
        RelativeLayout   rel1 = (RelativeLayout) findViewById(R.id.order_detail_wait_pay_rel1);
        RelativeLayout rel2 = (RelativeLayout) findViewById(R.id.order_detail_wait_pay_rel2);
        rel1.setOnClickListener(this);
        rel2.setOnClickListener(this);
        registerBack();
        setTitle("订单详情");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_detail_wait_pay_take_btn:
                switch (Integer.parseInt(orderDetail.getStatus())) {
                    case 1:
                        Intent pay = new Intent(activity, TopUpActivity.class);
                        pay.putExtra("payTag","1");
                        pay.putExtra("paySum",orderDetail.getSettlementAmount());
                        startActivity(pay);
                        break;
                    case 10:
                        SXUtils.getInstance(activity).showMyProgressDialog(activity,true);
                        new WaitSendFragment().getRemindHttp(orderId,tradeNo,hand);
                        break;
                    case 20:case 30:
                        SXUtils.getInstance(activity).showMyProgressDialog(activity,true);
//                        new WaitTakeFragment().getOrderConfirmHttp(orderId,null);
                        getOrderConfirmHttp(orderId);
                        break;
                    case 50:
                        Intent intent = new Intent(activity, StockSubmitNumberActivity.class);
                        Bundle bundle = new Bundle();
                        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                        list.add(orderDetail.getMyOrderLineVos());
                        bundle.putParcelableArrayList("orderLines",list);
                        intent.putExtra("orderNo",orderDetail.getOrderNo());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.order_detail_wait_pay_cancel_btn:
                SXUtils.getInstance(activity).MyDialogView(activity,"温馨提示!", "确定取消该笔订单吗?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SXUtils.getInstance(activity).tipDialog.dismiss();
                        getCancelOrderHttp(orderId);
                    }
                });

                break;
            case R.id.order_detail_wait_pay_rel1:case R.id.order_detail_wait_pay_rel2:
//                Intent intent = new Intent(activity, GoodsDetailActivity.class);
//                startActivity(intent);
                break;
        }
    }
    public Handler hand = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    orderDetail = (OrderDetailEntity) msg.obj;
                    nameTv.setText("皕鲜商店");

                    orderTotalTv.setText("¥"+ (TextUtils.isEmpty(orderDetail.getTransactionAmount())?"":orderDetail.getTransactionAmount()));
                    orderTimeTv.setText(orderDetail.getCreated());
                    orderNoTv.setText(orderDetail.getOrderNo());

                    if(!TextUtils.isEmpty(orderDetail.getPhoneNumber())|| !TextUtils.isEmpty(orderDetail.getConsigneeName())) {
                        reviceLiny.setVisibility(View.VISIBLE);
                        revicePhoneLiny.setVisibility(View.VISIBLE);
                        revicePhoneTv.setText(orderDetail.getPhoneNumber()+"");
                        reviceTv.setText(orderDetail.getConsigneeName()+"");
                    }
                    orderPayTypeTv.setText(TextUtils.isEmpty(orderDetail.getSettlementMode())?"":orderDetail.getSettlementMode());
                    orderAddressTv.setText(orderDetail.getAddress());
                    payTotalTv.setText("¥" + orderDetail.getSettlementAmount()+"");
                    freightTv.setText("¥" +orderDetail.getFreightAmount()+"");
                    yhTv.setText("¥" +orderDetail.getDiscountAmount()+"");
//                    states":"XXX",   //0：待确认；10:待发货； 20:待（合伙人）收货； 30:待摊主收货；
                    //合伙人订单详情
                    if (orderTag.equals("666")) {
                        switch (Integer.parseInt(orderDetail.getStatus())){
                            case 0:case 1:
                                cancelTv.setText("待确认");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 10:
                                cancelTv.setText("待发货");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 20:
                                if(AppClient.USERROLETAG.equals("16")) {
                                    //合伙人
                                    btnLin.setVisibility(View.VISIBLE);
                                    takeOrder.setText("立即收货");
                                    takeOrder.setBackgroundResource(R.drawable.comfirm_take_selector);
                                }else{
                                    cancelTv.setText("待合伙人收货");
                                    btnLin.setVisibility(View.GONE);
                                    cancelTv.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 30:
                                cancelTv.setText("待摊主收货");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 50:
                                cancelTv.setText("摊主已领货");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 8:
                                cancelTv.setText("已取消");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                cancelTv.setText("已挂起");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case -1:
                                cancelTv.setText("异常订单");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    else{
                        switch (Integer.parseInt(orderDetail.getStatus())) {
                            case 0:
                                cancelTv.setText("待确认");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 30:
                                btnLin.setVisibility(View.VISIBLE);
                                takeOrder.setText("确定收货");
                                takeOrder.setBackgroundResource(R.drawable.comfirm_take_selector);
                                break;
                            case 1:
                                btnLin.setVisibility(View.VISIBLE);
                                takeOrder.setText("立刻付款");
                                takeOrder.setBackgroundResource(R.drawable.comfirm_take_selector);
                                cancelOrder.setVisibility(View.VISIBLE);
                                break;
                            case 10:
                                btnLin.setVisibility(View.VISIBLE);
                                takeOrder.setText("提醒发货");
                                takeOrder.setTextColor(activity.getResources().getColor(R.color.orange));
                                takeOrder.setBackgroundResource(R.drawable.cancel_order_selector);
                                break;
                            case 20:
                                btnLin.setVisibility(View.VISIBLE);
                                takeOrder.setText("确定收货");
                                takeOrder.setBackgroundResource(R.drawable.comfirm_take_selector);
                                break;
                            case 50:
//                                cancelTv.setText("已完成");
//                                btnLin.setVisibility(View.GONE);
//                                cancelTv.setVisibility(View.VISIBLE);
//                                holder.btnLin.setVisibility(View.VISIBLE);
//                                holder.takeOrder.setText("缺货少货上报");
//                                holder.orderTv.setText("状态：");
//                                takeOrder.orderTotal.setText("已完成");

                                if(AppClient.USERROLETAG.equals("64")){
                                    takeOrder.setVisibility(View.GONE);
                                    cancelTv.setVisibility(View.VISIBLE);
                                    cancelTv.setText("已完成");
                                }else{
                                    takeOrder.setText("缺货少货上报");
                                    takeOrder.setTextColor(activity.getResources().getColor(R.color.orange));
                                    takeOrder.setBackgroundResource(R.drawable.cancel_order_selector);
//                                    payTotalTv.setText("状态：已完成");
                                }
                                btnLin.setVisibility(View.VISIBLE);

                                break;
                            case 8:
                                cancelTv.setText("已取消");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                cancelTv.setText("已挂起");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                            case -1:
                                cancelTv.setText("异常订单");
                                btnLin.setVisibility(View.GONE);
                                cancelTv.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
//                        List<CGListInfoEntity> gde = (List<CGListInfoEntity>) msg.obj;
//                    List<OrderInfoEntity> gde = (List<OrderInfoEntity>) msg.obj;
                    WaitPayGoodsRecyclerViewAdapter simpAdapter = new WaitPayGoodsRecyclerViewAdapter(activity,orderDetail.getMyOrderLineVos(),1);
                    recyclerView.setAdapter(simpAdapter);
                    break;
                case 1001:
                    switch (Integer.parseInt(orderTag)) {
                        case 1:
                            break;
                        case 2:
                            SXUtils.getInstance(activity).ToastCenter("提醒发货成功");
                            break;
                        case 3:
                            SXUtils.getInstance(activity).ToastCenter("确认收货成功");
                            break;
                    }
                    break;
                case AppClient.ERRORCODE:
                    String msgs = (String) msg.obj;
                    SXUtils.getInstance(activity).ToastCenter(msgs);
                    break;
            }
            SXUtils.DialogDismiss();
            return true;
        }
    });
    /**
     * 获取订单详情
     */
    public void getOrderDetailHttp() {
        HttpParams params = new HttpParams();
        params.put("orderNo",orderId);
        String  url;
//        if(orderTag.equals("666")){
//            url = AppClient.PARTNER_ORDER_DETAIL;
//        }
//        else{
        url = AppClient.ORDER_DETAIL;
//        }
        HttpUtils.getInstance(activity).requestPost(false, url, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                OrderDetailEntity gde = null;
                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),OrderDetailEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }
    /**
     * 用户取消订单
     * @param orderNo  订单ID
     */
    private  void getCancelOrderHttp(String orderNo) {
        HttpParams params = new HttpParams();
        params.put("orderNo",orderNo);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.USER_CANCEL_ORDER, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                SXUtils.getInstance(activity).ToastCenter("取消订单成功");
                EventBus.getDefault().post(new MessageEvent(1003,"waitpay"));
                SXUtils.DialogDismiss();
                finish();
//                Logs.i("取消订单========",jsonObject.toString());
//                Message msg = new Message();
//                msg.what = 1001;
//                msg.obj = "";
//                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }
    /**
     * 确认收货
     * @param orderNo  订单ID
     */
    public  void getOrderConfirmHttp(String orderNo) {
        HttpParams params = new HttpParams();
        params.put("orderNo",orderNo);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.ORDER_CONFIRM, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                SXUtils.getInstance(activity).ToastCenter("确认收货成功");
                EventBus.getDefault().post(new MessageEvent(1005,"waittake"));
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"user"));
                SXUtils.DialogDismiss();
                finish();
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag() == AppClient.EVENT100011){
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
