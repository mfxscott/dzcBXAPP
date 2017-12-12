package com.bixian365.dzc.fragment.my.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lzy.okhttputils.model.HttpParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.WCPayInfoEntity;
import com.bixian365.dzc.fragment.my.store.BankCardTopUpActivity;
import com.bixian365.dzc.fragment.my.store.order.MyOrderActivity;
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

import java.util.Map;

/**
 * 账号充值,商品支付  支付方式选择界面
 * @author mfx
 * @time  2017/7/6 17:42
 */
public class TopUpActivity extends BaseActivity implements OnClickListener {
    private Activity activity;
    private RelativeLayout topupWxpayRel;
    private ImageView topupWxCheckImg;
    private RelativeLayout topupZfbpayRel;
    private ImageView topupZfbPayIv;
    private RelativeLayout topupYhkpayRel;
    private ImageView topupYhkPayIv;
    private  int  payTag = 2;//1 微信 2 支付宝 3 银行卡选择支付方式标识
    private String TagStr,paySum;//标识充值还是支付，金额 tagStr 0  用个人用户充值 1 支付  12表示从生成订单过来点击返回打开代付款订单界面
    private EditText sumEdit;
    private String orderNo;
    private static final int SDK_PAY_FLAG = 116;
    private Handler hand;
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        TagStr = this.getIntent().getStringExtra("payTag");
        paySum = this.getIntent().getStringExtra("paySum");
        orderNo = this.getIntent().getStringExtra("orderNo");
        AppClient.PayTag = TagStr;
        activity = this;
        EventBus.getDefault().register(this);
        initView();
    }


    /**
     * 微信支付
     *
     //     * @param channelModel 通道模型（包含token和支付方式信息等）
     */
    private void startWXPay(@NonNull final  WCPayInfoEntity wxInfo) {

        if (!api.isWXAppInstalled()) {
            SXUtils.getInstance(activity).ToastCenter("您还没有安装微信");
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            SXUtils.getInstance(activity).ToastCenter("当前版本不支持支付功能");
            return;
        }
        if (wxInfo == null) {
            SXUtils.getInstance(activity).ToastCenter("微信返回参数错误");
            return;
        }
        PayReq req = new PayReq();
        req.appId = wxInfo.getAppid();
        req.partnerId = wxInfo.getPartnerid();
        req.prepayId = wxInfo.getPrepayid();
        req.nonceStr = wxInfo.getNoncestr();
        req.timeStamp = wxInfo.getTimestamp();
//        req.packageValue = wxInfo.getWxPackage();
        req.packageValue = "Sign=WXPay";
        req.sign = wxInfo.getSign();
        req.extData = "app data"; // optional
//     在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }
    private void initView(){
//        registerBack();
        LinearLayout allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TagStr.equals("12")) {
                    Intent payorder = new Intent(activity, MyOrderActivity.class);
                    payorder.putExtra("orderTag", "1");
                    startActivity(payorder);
                }
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
                finish();
            }
        });


        sumEdit = (EditText) findViewById(R.id.top_pay_sum_edit);
        TextView  hintTv = (TextView) findViewById(R.id.top_hint_tv);
        if(TagStr.equals("0")){
            setTitle("充值");
            hintTv.setText("充值金额(元)");
        }else{
            setTitle("支付");
            hintTv.setText("金额(元)");
            sumEdit.setText(paySum.indexOf("¥") ==-1 ?paySum:paySum.replace("¥","")+"");
            sumEdit.setEnabled(false);
        }
//         if(!TextUtils.isEmpty(orderNo)){
//             SXUtils.getInstance(activity).ToastCenter("订单号为"+orderNo);
//         }
        topupWxpayRel = (RelativeLayout) findViewById(R.id.topup_wxpay_rel);
        topupWxCheckImg = (ImageView) findViewById(R.id.topup_wx_check_img);
        topupZfbpayRel = (RelativeLayout) findViewById(R.id.topup_zfbpay_rel);
        topupZfbPayIv = (ImageView) findViewById(R.id.topup_zfb_check_img);
        topupYhkpayRel = (RelativeLayout) findViewById(R.id.topup_yhkpay_rel);
        topupYhkPayIv = (ImageView) findViewById(R.id.topup_yhk_check_img);
        Button topupBtn = (Button) findViewById(R.id.topup_btn);
        topupBtn.setOnClickListener(this);
        topupWxpayRel.setOnClickListener(this);
        topupZfbpayRel.setOnClickListener(this);
        topupYhkpayRel.setOnClickListener(this);

        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        switch (payTag){
                            case 1:
                                break;
                            case 2:
                                final  String orderInfo = (String) msg.obj;
                                Runnable payRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        PayTask alipay = new PayTask(activity);
                                        Map<String, String> result = alipay.payV2(orderInfo, true);
                                        Log.i("msp", result.toString());

                                        Message msg = new Message();
                                        msg.what = SDK_PAY_FLAG;
                                        msg.obj = result;
                                        hand.sendMessage(msg);
                                    }
                                };

                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                                break;
                            case 3:
                                Intent intent = new Intent(activity,BankCardTopUpActivity.class);
                                startActivity(intent);
                                break;
                        }
                        break;
                    case 1001:
                        break;
                    case AppClient.ERRORCODE:
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
                        break;
                    case SDK_PAY_FLAG:
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                            Intent payorder = new Intent(activity,MyOrderActivity.class);
                            payorder.putExtra("orderTag","2");
                            startActivity(payorder);
                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
                            EventBus.getDefault().post(new MessageEvent(1003,"store"));
                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100013,"store"));
                            finish();
                        }else if(TextUtils.equals(resultStatus, "6001")){
                            Toast.makeText(activity, "您取消了支付", Toast.LENGTH_SHORT).show();
                            if(!TagStr.equals("0")){
                                intentPayResult();
                            }
//                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100013,"store"));
                        } else {
                            //6001 取消支付
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                            intentPayResult();
//                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100013,"store"));
//                            Intent payorder = new Intent(activity,MyOrderActivity.class);
//                            payorder.putExtra("orderTag","1");
//                            startActivity(payorder);
//                            finish();
                        }
                        Logs.i("支付宝返回错误信息================"+resultInfo);
                        break;
                    case 117:
                        String wexInfo = (String) msg.obj;
                        WCPayInfoEntity wxInfo = (WCPayInfoEntity) ResponseData.getInstance(activity).parseJsonWithGson(wexInfo,WCPayInfoEntity.class);
                        api = WXAPIFactory.createWXAPI(activity, wxInfo.getAppid(), false);
                        api.registerApp(wxInfo.getAppid());
                        startWXPay(wxInfo);
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
            case R.id.topup_wxpay_rel:
                topupWxCheckImg.setVisibility(View.VISIBLE);
                topupZfbPayIv.setVisibility(View.GONE);
                topupYhkPayIv.setVisibility(View.GONE);
                payTag = 1;
                break;
            case R.id.topup_zfbpay_rel:
                topupWxCheckImg.setVisibility(View.GONE);
                topupZfbPayIv.setVisibility(View.VISIBLE);
                topupYhkPayIv.setVisibility(View.GONE);
                payTag = 2;
                break;
            case R.id.topup_yhkpay_rel:
                topupWxCheckImg.setVisibility(View.GONE);
                topupZfbPayIv.setVisibility(View.GONE);
                topupYhkPayIv.setVisibility(View.VISIBLE);
                payTag = 3;
                break;
            case R.id.topup_btn:
                if(TagStr.equals("0")){
                    String  totalFeeStr = sumEdit.getText().toString().trim();
                    if(TextUtils.isEmpty(totalFeeStr)){
                        SXUtils.getInstance(activity).ToastCenter("请输入充值金额");
                        return;
                    }
                    SXUtils.showMyProgressDialog(activity,true);
                    if (payTag == 1) {
                        PayRechargeOrder(totalFeeStr,"WE_CHAT");
                    } else {
                        PayRechargeOrder(totalFeeStr,"ALIPAY");
                    }
                }else {
                    SXUtils.showMyProgressDialog(activity,true);
                    if (payTag == 1) {
                        PayOrder("WE_CHAT");
                    } else {
                        PayOrder("ALIPAY");
                    }
                }
                break;

        }

    }
    /**
     * 支付订单
     */
    public void PayOrder(final String payTypeModel) {
        HttpParams httpp = new HttpParams();
        httpp.put("payload",orderNo);
        httpp.put("paymentMode",payTypeModel);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.PAY, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("支付宝返回参数======",jsonObject.toString());
                if(payTag == 2){
                    String   payStr ="";
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                        payStr = jsonObject1.getString("payArgs");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                JSONObject jsonObject1 = null;
//                FromOrderEntity orderFrom = (FromOrderEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),FromOrderEntity.class);
                    Message msg = new Message();
                    msg.what = 1000;
                    msg.obj = payStr;
                    hand.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = 117;
                    msg.obj = jsonObject.toString();
                    hand.sendMessage(msg);
                }
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
     * 摊主充值
     */
    public void PayRechargeOrder(String totalfee,String payType) {
        HttpParams httpp = new HttpParams();
        httpp.put("totalFee",totalfee+"");
        httpp.put("payType",payType);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.RECHARGE_PAY, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("账户充值返回参数======",jsonObject.toString());
                if(payTag == 2){
                    String   payStr ="";
                    try {
                        JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                        payStr = jsonObject1.getString("payArgs");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                JSONObject jsonObject1 = null;
//                FromOrderEntity orderFrom = (FromOrderEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),FromOrderEntity.class);
                    Message msg = new Message();
                    msg.what = 1000;
                    msg.obj = payStr;
                    hand.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = 117;
                    msg.obj = jsonObject.toString();
                    hand.sendMessage(msg);
                }
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            intentPayResult();
        }
        return true;
    }

    /**
     * 跳转
     */
    private void intentPayResult(){
        if(TagStr.equals("12")){
            Intent payorder = new Intent(activity,MyOrderActivity.class);
            payorder.putExtra("orderTag","1");
            startActivity(payorder);
        }
        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
        EventBus.getDefault().post(new MessageEvent(1003,"store"));
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag() ==AppClient.EVENT100025){
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
