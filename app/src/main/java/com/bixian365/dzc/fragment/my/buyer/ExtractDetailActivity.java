package com.bixian365.dzc.fragment.my.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.wallet.WalletInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

/**
 * 商户，店主提现
 * @author mfx
 * @time  2017/8/4 11:09
 */
public class ExtractDetailActivity extends BaseActivity implements View.OnClickListener {
    private Activity activity;
    private TextView extractMoneyCardTv;
    private EditText extractPaySumEdit;
    private TextView extractNowMoneyTv;
    private TextView extractAllMoneyTv;
    private Button extractBtn;
    private LinearLayout extractNoCardLiny;
    private Button extractAddBankCardBtn;
    private WalletInfoEntity walletInfo;
    private Handler hand;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_detail);

        Bundle bundle = this.getIntent().getExtras();
        walletInfo =bundle.getParcelable("walletInfo");
        activity = this;
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("提现");
        extractMoneyCardTv = (TextView) findViewById(R.id.extract_money_card_tv);
        extractPaySumEdit = (EditText) findViewById(R.id.extract_pay_sum_edit);
        extractPaySumEdit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        extractNowMoneyTv = (TextView) findViewById(R.id.extract_now_money_tv);
        extractAllMoneyTv = (TextView) findViewById(R.id.extract_all_money_tv);
        extractAllMoneyTv.setOnClickListener(this);
        extractBtn = (Button) findViewById(R.id.extract_btn);
        extractBtn.setOnClickListener(this);
        extractNoCardLiny = (LinearLayout) findViewById(R.id.extract_no_card_liny);
        extractAddBankCardBtn = (Button) findViewById(R.id.extract_add_bank_card_btn);
//        String accno = walletInfo.getAccNo();
//        if(!TextUtils.isEmpty(accno))
//        extractMoneyCardTv.setText(accno.substring(0,4)+" **** **** "+accno.substring(accno.length()-4,accno.length()));
        extractMoneyCardTv.setText(walletInfo.getAccNo()+"");
        extractNowMoneyTv.setText("当前可提现金额"+walletInfo.getUsableAmt()+"元，");
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("提现申请成功");
                        finish();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
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
            //提现按钮
            case R.id.extract_btn:
                String withdraw = extractPaySumEdit.getText().toString();
                if(TextUtils.isEmpty(withdraw)) {
                    SXUtils.getInstance(activity).ToastCenter("请输入提现金额");
                    return;
                }
                float inputpay = Float.parseFloat(withdraw);
                float totalPay = Float.parseFloat(walletInfo.getUsableAmt());
                if(inputpay>totalPay){
                    SXUtils.getInstance(activity).ToastCenter("提现金额超出可提金额");
                    return;
                }
                if(checkPriceNum(withdraw)){
                    SXUtils.getInstance(activity).ToastCenter("输入金额有误，请重新输入");
                    return;
                }
                SXUtils.showMyProgressDialog(activity,true);
                getWithdrawApply(inputpay);
                break;
            case R.id.extract_add_bank_card_btn:
                //检测没有添加过银行卡，提现添加银行卡
                Intent intent = new Intent(activity,ExtractAddBankCardActivity.class);
                startActivity(intent);
                break;
            //提现全部金额
            case R.id.extract_all_money_tv:
                if(!TextUtils.isEmpty(walletInfo.getUsableAmt())&& Float.parseFloat(walletInfo.getUsableAmt())==0){
                    SXUtils.getInstance(activity).ToastCenter("当前没有可提现金额");
                    return;
                }
                extractPaySumEdit.setText(walletInfo.getUsableAmt()+"");
                break;
        }
    }
    /**
     * 发起提现申请
     */
    public void getWithdrawApply(float applySum) {
        HttpParams params = new HttpParams();
        params.put("applySum",applySum);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.WITHDRAWAPPLY, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
//                UserInfoEntity gde = null;
//                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = "";
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
     * 判断输入的金额是否合法
     * @param priceNum
     * @return
     */
    private boolean checkPriceNum(String priceNum){
        if(priceNum.substring(0,1).equals(".")){
            return true;
        }
        return false;
    }
}
