package com.bixian365.dzc.fragment.my.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.BankCardListAdapter;
import com.bixian365.dzc.adapter.SRDetailListAdapter;
import com.bixian365.dzc.adapter.WalletLogRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.wallet.TransLogEntity;
import com.bixian365.dzc.entity.wallet.WalletInfoEntity;
import com.bixian365.dzc.fragment.my.buyer.ExtractAddBankCardActivity;
import com.bixian365.dzc.fragment.my.buyer.ExtractDetailActivity;
import com.bixian365.dzc.fragment.my.pay.TopUpActivity;
import com.bixian365.dzc.fragment.my.store.yhj.YHJActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.MyGridView;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 */
public class MyWalletActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout walletYhjRel;
    private Activity activity;
    private MyGridView gridView;
    private MyGridView detailGridView;//收入明细
    private Handler hand;
    private LinearLayout walletNoUseLin;
    private LinearLayout walletUseLin;
    private LinearLayout walletUsedLin;
    private RelativeLayout walletMyBankRel;
    private RelativeLayout walletAddBankRel;
    private RelativeLayout walletSrDetailRel;
    private ImageView myBankArrow;
    private String walletTag;//判断个人充值还是商户提现
    private WalletInfoEntity walletInfo;
    @BindView(R.id.wallet_total_amt_tv)
    TextView walletTotalAmt;
    @BindView(R.id.wallet_counp_liny)
    LinearLayout walletCouponLin;
    @BindView(R.id.acc_manage_info_lin)
    RelativeLayout  walletLin;
    @BindView(R.id.wallet_coupons_may_tv)
    TextView couponsMayTv;
    @BindView(R.id.wallet_coupons_no_tv)
    TextView couponsNoTv;
    @BindView(R.id.wallet_coupons_used_tv)
    TextView couponsUsedTv;
    @BindView(R.id.wallet_default_bank_lin)
    LinearLayout defaultBankLin;
    @BindView(R.id.store_my_rechange_rely)
    RelativeLayout storeRechangeRely;
    @BindView(R.id.store_my_tx_liny)
    LinearLayout  storeTxLiny;
    @BindView(R.id.store_my_cz_liny)
    LinearLayout storeCzLiny;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private int indexPage =0;
    private WalletLogRecyclerViewAdapter simpAdapter;
    private  ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        walletTag = this.getIntent().getStringExtra("walletTag");
        activity = this;
        EventBus.getDefault().register(this);
        initView();
        SXUtils.showMyProgressDialog(activity,true);
        initData();
    }
    private void initData(){

        GetUserWalletHttp();
        getTranLog();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        scrollView.smoothScrollTo(0, 0);
        super.onResume();
    }
    private void initView(){
        scrollView = (ScrollView) findViewById(R.id.wallet_scrollv);
        scrollView.scrollTo(0,0);
        walletTotalAmt.requestFocus();

        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.walle_swipe_container);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    initData();

                }
//                else if(direction == SwipyRefreshLayoutDirection.BOTTOM){
//                    indexPage ++;
////                    getTranLog(indexPage);
//                }
            }
        });


        myBankArrow = (ImageView) findViewById(R.id.wallet_mybank_arrow_iv);

        walletNoUseLin = (LinearLayout) findViewById(R.id.wallet_no_use_lin);
        walletUseLin = (LinearLayout) findViewById(R.id.wallet_use_lin);
        walletUsedLin = (LinearLayout) findViewById(R.id.wallet_used_lin);
        walletMyBankRel = (RelativeLayout) findViewById(R.id.wallet_my_bank_rel);
        walletAddBankRel = (RelativeLayout) findViewById(R.id.wallet_add_bank_rel);
        walletSrDetailRel = (RelativeLayout) findViewById(R.id.wallet_sr_detail_rel);
        walletNoUseLin.setOnClickListener(this);
        walletUseLin.setOnClickListener(this);
        walletUsedLin.setOnClickListener(this);
        walletMyBankRel.setOnClickListener(this);
        walletAddBankRel.setOnClickListener(this);
        walletSrDetailRel.setOnClickListener(this);
        storeRechangeRely.setOnClickListener(this);

        storeCzLiny.setOnClickListener(this);
        storeTxLiny.setOnClickListener(this);
        LinearLayout   allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        gridView = (MyGridView) findViewById(R.id.wallet_bank_gridv);
        gridView.setFocusable(false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SXUtils.getInstance(activity).ToastCenter("=="+position);
            }
        });
        detailGridView = (MyGridView) findViewById(R.id.wallet_srdetail_gridv);
//        detailGridView.setLayoutManager(new LinearLayoutManager(detailGridView.getContext()));
//        detailGridView.setItemAnimator(new DefaultItemAnimator());

//        walletTopupBtn = (TextView) findViewById(R.id.wallet_topup_btn);
//
//        if(AppClient.USERROLETAG.equals("64")){
//            walletTopupBtn.setText("充值");
//        }else{
//            walletTopupBtn.setText("提现");
//        }
        if(AppClient.USERROLETAG.equals("32")||AppClient.USERROLETAG.equals("64")){
            walletCouponLin.setVisibility(View.VISIBLE);
        }
        walletYhjRel = (RelativeLayout) findViewById(R.id.wallet_yhj_rel);
//        walletTopupBtn.setOnClickListener(this);
        walletYhjRel.setOnClickListener(this);
        LinearLayout banklay = (LinearLayout) findViewById(R.id.wallet_bank_card_lin);
        if(AppClient.USERROLETAG.equals("64")){
//            banklay.setVisibility(View.GONE);
        }else if(AppClient.USERROLETAG.equals("32")){
//            banklay.setVisibility(View.VISIBLE);
        }

        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        walletInfo = (WalletInfoEntity) msg.obj;
                        walletTotalAmt.setText(TextUtils.isEmpty(walletInfo.getUsableAmt())?"0.00":walletInfo.getUsableAmt()+"");
                        if(TextUtils.isEmpty(walletInfo.getAccNo()) || TextUtils.isEmpty(walletInfo.getIdNo())){
                            defaultBankLin.setVisibility(View.VISIBLE);
                            walletAddBankRel.setVisibility(View.VISIBLE);
                        }
                        else{
                            walletAddBankRel.setVisibility(View.GONE);
                            defaultBankLin.setVisibility(View.GONE);
                            gridView.setAdapter(new BankCardListAdapter(activity,getBankData()));
                        }
                        break;
                    case 1001:
                        List<TransLogEntity> transList = (List<TransLogEntity>) msg.obj;
                        if(transList == null)
                            break;
                        SRDetailListAdapter   srAdapter = new SRDetailListAdapter(activity,transList);
                        detailGridView.setAdapter(srAdapter);
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
                        break;
                }
                if(mSwipyRefreshLayout != null){
                    mSwipyRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });

    }
    /**
     * 封装模拟银行卡数据
     * @return
     */
    private List<Map<String,String>> getBankData(){
        List<Map<String,String>> list = new ArrayList<>();

        for(int i=0;i<1;i++){
            Map<String,String>  map = new HashMap<>();
            map.put("cardNum",walletInfo.getAccNo());
            map.put("cardName",walletInfo.getBankName());
            map.put("cardId","cardId===="+i);
            list.add(map);
        }
        return list;
    }
    /**
     * 封装模拟银行卡数据
     * @return
     */
    private List<Map<String,String>> getSRDetailData(){
        List<Map<String,String>> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            Map<String,String>  map = new HashMap<>();
            map.put("name","皕鲜惠"+i);
            map.put("orderId","20170707"+i);
            map.put("orderTime",SXUtils.getInstance(activity).GetNowDateTime());
            map.put("orderPrice","+10"+i);
            map.put("orderState","未到账");
            list.add(map);
        }
        return list;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
//            case R.id.store_my_rechange_rely:
//                Intent rechange = new Intent(activity,TopUpActivity.class);
//                rechange.putExtra("payTag","0");
//                rechange.putExtra("paySum","0");
//                startActivity(rechange);
//                break;
//            case R.id.wallet_topup_btn:
//                if(AppClient.USERROLETAG.equals("64")){
//                    Intent intent = new Intent(activity,TopUpActivity.class);
//                    intent.putExtra("payTag","0");
//                    intent.putExtra("paySum",walletInfo.getTotalAmt());
//                    startActivity(intent);
//                }else{
//                    if(TextUtils.isEmpty(walletInfo.getAccNo()) || TextUtils.isEmpty(walletInfo.getIdNo())){
//                        Intent intent = new Intent(activity,NoBindCardActivity.class);
//                        startActivity(intent);
//                        return ;
//                    }
//                    Intent intent = new Intent(activity,ExtractDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable("walletInfo",walletInfo);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//                break;
            case R.id.store_my_cz_liny:
                Intent rechange = new Intent(activity,TopUpActivity.class);
                rechange.putExtra("payTag","0");
                rechange.putExtra("paySum","0");
                startActivity(rechange);
                break;
            case R.id.store_my_tx_liny:
                if(TextUtils.isEmpty(walletInfo.getAccNo()) || TextUtils.isEmpty(walletInfo.getIdNo())){
                    Intent intent = new Intent(activity,NoBindCardActivity.class);
                    startActivity(intent);
                    return ;
                }
                Intent intent = new Intent(activity,ExtractDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("walletInfo",walletInfo);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.wallet_yhj_rel:case R.id.wallet_no_use_lin:
                Intent yhj = new Intent(activity,YHJActivity.class);
                yhj.putExtra("yhjTag","1");
                startActivity(yhj);
                break;
            case R.id.wallet_use_lin:
                Intent yhj2 = new Intent(activity,YHJActivity.class);
                yhj2.putExtra("yhjTag","2");
                startActivity(yhj2);
                break;
            case R.id.wallet_used_lin:
                Intent yhj3 = new Intent(activity,YHJActivity.class);
                yhj3.putExtra("yhjTag","3");
                startActivity(yhj3);
                break;
            case R.id.wallet_my_bank_rel:
//                if(bankListLay.getVisibility() == View.VISIBLE){
//                    bankListLay.setVisibility(View.GONE);
//                    myBankArrow.setBackgroundResource(R.mipmap.arrow_right);
//                }else{
//                    bankListLay.setVisibility(View.VISIBLE);
//                    myBankArrow.setBackgroundResource(R.mipmap.arrow_down);
//                }
                break;
            case R.id.wallet_add_bank_rel:
//                Intent noBind = new Intent(activity,NoBindCardActivity.class);
//                startActivity(noBind);
                Intent addCardintent = new Intent(activity,ExtractAddBankCardActivity.class);
                startActivity(addCardintent);
                break;
            case R.id.wallet_sr_detail_rel:
                Intent logintent = new Intent(activity,WalletBillLogActivity.class);
                startActivity(logintent);
                break;
        }
    }
    /**
     * 钱包信息
     */
    public void GetUserWalletHttp() {
        HttpUtils.getInstance(activity).requestPost(false,AppClient.USER_WALLET, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                walletLin.setVisibility(View.VISIBLE);
                WalletInfoEntity gde = null;
                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),WalletInfoEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = "";
//                hand.sendMessage(msg);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getMessage().equals("wallet")||messageEvent.getTag()==1){
            initData();
        }else  if(messageEvent.getTag() == AppClient.EVENT100011){
            finish();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 收入明细列表
     */
    public void getTranLog() {
        HttpParams params = new HttpParams();
        params.put("pageSize","3");
        params.put("pageIndex","0");
        HttpUtils.getInstance(activity).requestPost(false,AppClient.MYTRADELOG, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Object obj=null;
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                    obj = jsonObject1.get("dataset");
                } catch (JSONException e) {
                    Message msg = new Message();
                    msg.what = AppClient.ERRORCODE;
                    msg.obj = "未查询到相关明细";
                    hand.sendMessage(msg);
                    return;
                }
                List<TransLogEntity> gde = (List<TransLogEntity>) ResponseData.getInstance(activity).parseJsonArray(obj.toString(),TransLogEntity.class);
                Message msg = new Message();
                msg.what = 1001;
                msg.obj =gde;
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
}
