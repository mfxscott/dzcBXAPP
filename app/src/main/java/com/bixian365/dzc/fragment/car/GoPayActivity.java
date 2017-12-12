package com.bixian365.dzc.fragment.car;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.CGOrderGoodsListRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.address.AddressInfoEntity;
import com.bixian365.dzc.entity.car.FromOrderEntity;
import com.bixian365.dzc.entity.car.OrderCouponsEntity;
import com.bixian365.dzc.entity.car.OrderLinesEntity;
import com.bixian365.dzc.entity.car.PayTypeEntity;
import com.bixian365.dzc.fragment.my.pay.TopUpActivity;
import com.bixian365.dzc.fragment.my.store.order.MyOrderActivity;
import com.bixian365.dzc.fragment.my.store.yhj.YHJActivity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bixian365.dzc.R.id.gopay_use_yhj_rel;

/**
 * 购物车选择商品跳转到此界面
 * 商品预下单成功去支付界面
 */
public class GoPayActivity extends BaseActivity implements View.OnClickListener{
    private Activity activity;

    @BindView(R.id.gopay_address_name_tv)
    TextView addressName;
    @BindView(R.id.gopay_address_phone_tv)
    TextView  addressPhone;
    @BindView(R.id.gopay_address_info_tv)
    TextView  addressInfo;
    @BindView(R.id.go_pay_submit_order_recycler)
    RecyclerView recycler;
    @BindView(R.id.go_pay_price_tv)
    TextView  priceTv;
    @BindView(R.id.go_pay_total_tv)
    TextView  totalPriceTv;
    @BindView(R.id.go_pay_freightAmount_tv)
    TextView  freightAmountTv;//运费
    @BindView(R.id.gopay_getto_rel)
    RelativeLayout getToPayRely;
    @BindView(R.id.gopay_online_rel)
    RelativeLayout onLineRely;
    @BindView(R.id.gopay_online_iv)
    ImageView onlineTv;
    @BindView(R.id.gopay_getto_iv)
    ImageView gottoTv;
    @BindView(R.id.go_pay_coupons_tv)
    TextView  couponsTv;
    @BindView(R.id.go_pay_goods_num_tv)
    TextView  goodsNumTv;
    @BindView(R.id.go_pay_coupons_price_tv)
    TextView  couponPriceTv;
    @BindView(R.id.go_pay_order_detail_lin)
    LinearLayout orderDetailLin;
    @BindView( R.id.go_pay_send_tiime_tv)
    TextView sendTime;
    @BindView(R.id.gopay_check_coupons_tv)
    TextView checkCouponsNumTv;
    @BindView(R.id.gopay_cust_iv)
    ImageView  custPhone;
    @BindView(R.id.gopay_yj_ordertotal_tv)
    TextView orderTotalTv;
    private int  REQUESTCODE=1000;//接收选择地址返回
    private int  REQUESTCOUPONSCODE=1001;//接收优惠券选择返回

    private String PayModel="2";//支付方式
    private FromOrderEntity fromOrder;
    private ArrayList<OrderLinesEntity> orderList;
    private ArrayList<OrderCouponsEntity> couponsList;
    private ArrayList<PayTypeEntity> payTypeList;
    private Handler hand;
    private Bundle bundle;
    private AddressInfoEntity address;
    private String skuCode="";//商品
    private String couponNos="";//优惠劵数量
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_pay);
        activity = this;
        ButterKnife.bind(this);
        bundle = this.getIntent().getExtras();
        skuCode = bundle.getString("skuCode");
        initView();
        SXUtils.showMyProgressDialog(activity,true);
        getFromOrder(skuCode,couponNos);

    }
    private void initView(){
        custPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(activity).CallCustPhone();
            }
        });

        LinearLayout  goback = (LinearLayout) findViewById(R.id.gopay_title_goback_linlay);
        goback.setOnClickListener(this);
        RelativeLayout reladdress = (RelativeLayout) findViewById(R.id.gopay_check_address_rel);
        reladdress.setOnClickListener(this);
        RelativeLayout useYhj = (RelativeLayout) findViewById(R.id.gopay_use_yhj_rel);
        useYhj.setOnClickListener(this);
        TextView paytv = (TextView) findViewById(R.id.gopay_btn);
        paytv.setOnClickListener(this);



        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        if(PayModel.equals("2")){
                            //订单成功刷新购物车
                            String orderNo = (String) msg.obj;
                            Intent pay = new Intent(activity, TopUpActivity.class);
                            pay.putExtra("payTag","12");
                            pay.putExtra("paySum",totalPriceTv.getText().toString()+"");
                            pay.putExtra("orderNo",orderNo);
                            startActivity(pay);
                        }else{
                            Intent sendorder = new Intent(activity,MyOrderActivity.class);
                            sendorder.putExtra("orderTag","2");
                            startActivity(sendorder);
                            finish();
                        }
                        EventBus.getDefault().post(new MessageEvent(2,"car"));
                        finish();
                        break;
                    case 1001:
                        break;
                    case 1003:
                        fromOrder = (FromOrderEntity) msg.obj;
                        orderList= fromOrder.getOrderLines();
                        couponsList= fromOrder.getOrderCoupons();
                        payTypeList= fromOrder.getSettlementModes();
                        address = fromOrder.getDefaultAddress();
                        initOrderInfoData(fromOrder);
                        initData(address);
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
    private void initOrderInfoData(FromOrderEntity fromOrder){
        couponsTv.setText(couponsList.size()+"张");
        totalPriceTv.setText("¥"+fromOrder.getSettlementAmount());
        freightAmountTv.setText("¥"+fromOrder.getFreightAmount());
        priceTv.setText("¥"+fromOrder.getSettlementAmount());
        sendTime.setText(fromOrder.getDeliveryTime()+"");
        orderTotalTv.setText("¥"+fromOrder.getTransactionAmount());
        couponPriceTv.setText("-¥"+fromOrder.getDiscountAmount());


        goodsNumTv.setText("共"+orderList.size()+"类");
        onLineRely.setOnClickListener(this);
        getToPayRely.setOnClickListener(this);
        orderDetailLin.setOnClickListener(this);

        if(payTypeList != null && payTypeList.size()>0){
            for(int i=0;i<payTypeList.size();i++){
                if(payTypeList.get(i).getValue().equals("1")){
                    getToPayRely.setVisibility(View.VISIBLE);
                }else if(payTypeList.get(i).getValue().equals("2")){
                    onLineRely.setVisibility(View.VISIBLE);
                }
            }
        }
        recycler.setLayoutManager(new LinearLayoutManager(activity));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        CGOrderGoodsListRecyclerViewAdapter simpAdapter = new CGOrderGoodsListRecyclerViewAdapter(activity,orderList,1);
        recycler.setAdapter(simpAdapter);


    }
    private void initData(AddressInfoEntity address){
        if(address == null){
            return;
        }
        addressName.setText(address.getConsigneeName());
        addressPhone.setText(address.getConsigneeMobile());
        addressInfo.setText(address.getProvinceName()+address.getCityName()+address.getDistrictName()+address.getAddress());
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.gopay_title_goback_linlay:
                finish();
                break;
            case R.id.gopay_check_address_rel:
                Intent addressList = new Intent(activity, AddressListActivity.class);
                startActivityForResult(addressList, REQUESTCODE);
                // 意图实现activity的跳转
                break;
            case gopay_use_yhj_rel:
//                Intent intent = new Intent(activity, GoPayCheckCouponsActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
                if(couponsList == null || couponsList.size() <=0){
                    SXUtils.getInstance(activity).ToastCenter("暂无可使用优惠券");
                    return;
                }
                Intent yhj = new Intent(activity,YHJActivity.class);

                Bundle cbundle = new Bundle();
                ArrayList couponslist = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                couponslist.add(fromOrder.getOrderCoupons());
                cbundle.putParcelableArrayList("coupsons",couponslist);
                yhj.putExtra("yhjTag","4");
                yhj.putExtras(cbundle);
                startActivityForResult(yhj, REQUESTCOUPONSCODE);
                break;
            case R.id.gopay_btn:
//                String strId = fromOrder.getDefaultAddress().getConsigneeId();
                if(address != null){
                    SXUtils.showMyProgressDialog(activity,false);
                    submitOrder(address.getConsigneeId());
                }else{
                    SXUtils.getInstance(activity).ToastCenter("请添加收货地址");
                }
//                Intent pay = new Intent(activity, TopUpActivity.class);
//                pay.putExtra("payTag","1");
//                pay.putExtra("paySum","1000");
//                startActivity(pay);
                break;
            case R.id.gopay_online_rel:
                onlineTv.setVisibility(View.VISIBLE);
                gottoTv.setVisibility(View.GONE);
                PayModel = "2";
                break;
            case R.id.gopay_getto_rel:
                onlineTv.setVisibility(View.GONE);
                gottoTv.setVisibility(View.VISIBLE);
                PayModel = "1";
                break;
            case R.id.go_pay_order_detail_lin:
                //点击共多少类跳转订单详情
                Intent payOrder = new Intent(activity, PayOrderDetailActivity.class);
                Bundle ibundle = new Bundle();
                ibundle.putParcelable("fromOrder",fromOrder);
                ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                list.add(fromOrder.getOrderLines());
                ibundle.putParcelableArrayList("orderLine",list);
                payOrder.putExtras(ibundle);
                startActivity(payOrder);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (requestCode == REQUESTCODE) {
            if(data == null)
                return;
            Bundle bundle = data.getExtras();
            address = (AddressInfoEntity) bundle.get("addressInfo");
            initData(address);
        }else if(requestCode == REQUESTCOUPONSCODE){
            if(data == null)
                return;
            Bundle bundle = data.getExtras();

            float  price = bundle.getFloat("couponPrice");
//            if(price>Float.parseFloat(fromOrder.getTransactionAmount())){
//                      return;
//            }
            couponNos = bundle.getString("couponNo");
            String [] coupons = couponNos.split(",");
            if(coupons.length>=1){
                checkCouponsNumTv.setText("选择了"+coupons.length+"张优惠券");
            }
            if(!TextUtils.isEmpty(couponNos)) {
                getFromOrder(skuCode, couponNos);
            }
//            totalPriceTv.setText("¥ "+SXUtils.getInstance(activity).getFloatPrice(Float.parseFloat(fromOrder.getTransactionAmount())-price));
//            couponPriceTv.setText("-¥"+price);
//            priceTv.setText("¥"+(Float.parseFloat(fromOrder.getTransactionAmount())-price));
        }
    }
    /**
     * 第二步 订单结算
     */
    public void submitOrder(String addressId) {
        HttpParams httpp = new HttpParams();
        httpp.put("couponNos",couponNos);//优惠券 用逗号隔开
        httpp.put("consigneeId",addressId);//收货地址ID
        httpp.put("settlementMode",PayModel);//PayModel支付方式 1 货到付款 2 在线支付
        httpp.put("skuBarcodes",returnSkuCode());//优化劵 用逗号隔开
        HttpUtils.getInstance(activity).requestPost(false, AppClient.ORDER_SUBMIT, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("结算订单成功返回参数======",jsonObject.toString());
                String   orderNo ="";
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                    orderNo = jsonObject1.getString("orderNo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = orderNo;
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
     * 第一步 订单提交订单
     */
    public void getFromOrder(String sku,String couponsStr) {
        HttpParams httpp = new HttpParams();
        httpp.put("couponNos",couponsStr);//优化劵 用逗号隔开
        httpp.put("skuBarcodes",sku);//skucode 逗号隔开
        HttpUtils.getInstance(activity).requestPost(false,AppClient.ORDER_FORM, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("订单提交返回参数=======",jsonObject.toString());
                JSONObject jsonObject1 = null;
                FromOrderEntity orderFrom = (FromOrderEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),FromOrderEntity.class);
                Message msg = new Message();
                msg.what = 1003;
                msg.obj = orderFrom;
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
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag()==AppClient.COUPONS_RETRURN){
            couponNos = messageEvent.getMessage();
        }
    }
    /**
     * 获取支付skucode
     * @return
     */
    private String returnSkuCode(){
        String skuStr = "";
        for(int i=0;i<orderList.size();i++){
            skuStr += orderList.get(i).getSkuBarcode()+",";
        }
        if(TextUtils.isEmpty(skuStr) || skuStr.equals(","))
            return "";
        return skuStr.substring(0,skuStr.length()-1);
    }

}
