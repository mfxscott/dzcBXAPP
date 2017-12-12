package com.bixian365.dzc.fragment.my.buyer.purchase;

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
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.CGOrderDetailRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.cgListInfo.CGListInfoEntity;
import com.bixian365.dzc.entity.cgListInfo.CGPurchaseLinesEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 采购订单详情
 * @author mfx
 * @time  2017/9/1 17:57
 */
public class CGOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private String orderTag;
    @BindView(R.id.cg_order_detail_take_btn)
    TextView  takeOrder;
    private Activity activity;
    private CGListInfoEntity cgListInfo;
    private List<CGPurchaseLinesEntity> purchaseList;
    @BindView(R.id.cg_order_detail_total_tv)
    TextView  totalTv;
    @BindView(R.id.cg_order_get_time_tv)
    TextView  getTimeTv;
    @BindView(R.id.cg_order_detail_send_addr_tv)
    TextView  sendAddrTv;
    @BindView(R.id.cg_order_detail_recycler)
    RecyclerView recycler;
//    @BindView(R.id.cg_order_detail_no_tv)
//    TextView  purCodeTv;
    @BindView(R.id.cg_order_detail_no_tv)
    TextView nameTv;
    @BindView(R.id.cg_order_detail_paymode_tv)
    TextView  payModeTv;
    private String gettag; //等于空 为待发货进入详情，1为待发货隐藏继续发货
    private Handler hand;
    private String purchaseCodeStr,actualNumberStr,skuCodeStr;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cg_order_detail);
        activity = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
         bundle = this.getIntent().getExtras();
        gettag = bundle.getString("gettag");
        ArrayList list = bundle.getParcelableArrayList("PurchaseList");
        cgListInfo = bundle.getParcelable("orderList");
        purchaseList= (List<CGPurchaseLinesEntity>) list.get(0);//强转成你自己定义的list，这样list2就是你传过来的那个list了。
        initView();
    }
    private void initView(){
        totalTv.setText("总价："+"¥ "+cgListInfo.getPurchaseAmount()+"元");
        getTimeTv.setText(cgListInfo.getCreated()+"");
        sendAddrTv.setText(TextUtils.isEmpty(cgListInfo.getReceiverAddr())? "" : cgListInfo.getReceiverAddr());
//        purCodeTv.setText(""+cgListInfo.getPurchaseCode());
       payModeTv.setText(cgListInfo.getPaymentMode()+"");
        nameTv.setText("采购单号："+cgListInfo.getPurchaseCode());
        purchaseCodeStr = cgListInfo.getPurchaseCode();
        actualNumberStr= purchaseList.get(0).getActualNumber();
        skuCodeStr = purchaseList.get(0).getSkuCode();

        takeOrder.setOnClickListener(this);
        registerBack();
        setTitle("采购清单详情");
        recycler.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        CGOrderDetailRecyclerViewAdapter simpAdapter = new CGOrderDetailRecyclerViewAdapter(this,purchaseList);
        recycler.setAdapter(simpAdapter);
        switch (Integer.parseInt(cgListInfo.getReceiveState())){
            case 11:
                takeOrder.setText("确认接单");
                break;
            case 20:
                takeOrder.setText("立即发货");
                break;
            case 30:
                if(cgListInfo.getReceiveResult().equals("20")) {
                    takeOrder.setText("待收货");
                    takeOrder.setTextColor(getResources().getColor(R.color.orange));
                    takeOrder.setBackgroundResource(R.color.transparent);
                }else {
                    if(gettag.equals("2")){
                        takeOrder.setText("待收货");
                        takeOrder.setTextColor(getResources().getColor(R.color.orange));
                        takeOrder.setBackgroundResource(R.color.transparent);
                    }else{
                        takeOrder.setText("继续发货");
                    }

                }
                break;
            case 40:
                takeOrder.setText("已完成");
                takeOrder.setTextColor(getResources().getColor(R.color.orange));
                takeOrder.setBackgroundResource(R.color.transparent);
                break;
        }
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("接单成功");
//                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10001,"cgList"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100018,"1"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100014,"confirm"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100015,"send"));
                        finish();
                        break;
                    case 1001:
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
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cg_order_detail_take_btn:
                switch (Integer.parseInt(cgListInfo.getReceiveState())) {
                    case 11:
                        SXUtils.getInstance(activity).showMyProgressDialog(activity,false);
                        getConfirmPurchaseHttp(cgListInfo.getPurchaseCode());
                        break;
                    case 20:
                        Intent intent = new Intent(activity, CGOrderDeliveActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 30:
                        if(!cgListInfo.getReceiveResult().equals("20") &&  !gettag.equals("2"))
                        {
                            Intent deliveintent = new Intent(activity, CGOrderDeliveActivity.class);
                            deliveintent.putExtras(bundle);
                            startActivity(deliveintent);
                        }
                        break;
                }
                break;
        }
    }
    /**
     *供应商确认采购订单
     */
    public void getConfirmPurchaseHttp(String purchaseCode) {
        HttpParams params = new HttpParams();
        params.put("purchaseCode",purchaseCode);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.GYS_CONFIRM_PURCHASE, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = jsonObject.toString();
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if (messageEvent.getTag() == 4444 || messageEvent.getMessage().equals("cgDetail")) {
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        OKManager.cancelTag(this);//取消以Activity.this作为tag的请求
    }
}
