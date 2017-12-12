package com.bixian365.dzc.fragment.my.partner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.PartnerScanRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.ScanPartnerOrderLinesEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 合伙人扫码收货
 */
public class PartnerConfirmTakeActivity extends BaseActivity {
    private String result;//扫码获取到的订单号R.id.
    @BindView(R.id.partner_orderno_edt)
    EditText orderNoEdt;
    @BindView(R.id.partner_scan_recycler)
    RecyclerView recyclerView;
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
    @BindView(R.id.partner_send_info_lin)
    LinearLayout  sendLin;//发货信息
    @BindView(R.id.partner_order_receive_tv)
    TextView  receiveTv;//收货人
    @BindView(R.id.partner_confirm_all_comfirm_lin)
    RelativeLayout allLiny;
    private Activity activity;
    private Handler hand;
    private TextView scan_send_confrim;
    private TextView selectTv;
    private CheckBox checkBox;
    private PartnerScanRecyclerViewAdapter partnerScanRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_confirm_take);
        activity = this;
        ButterKnife.bind(this);
        result = this.getIntent().getStringExtra("result");
        initView();

        if(!TextUtils.isEmpty(result)) {
                SXUtils.showMyProgressDialog(activity,false);
            getScanGetHttp(result,hand);
        }
    }
    private void initView(){
        registerBack();
        setTitle("扫码收货");
        orderNoEdt.setText(result+"");
        selectTv = (TextView) findViewById(R.id.partner_send_select_tv);
        selectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  resultOrderno = orderNoEdt.getText().toString().trim();
                if(!TextUtils.isEmpty(resultOrderno)) {
                    SXUtils.showMyProgressDialog(activity, true);
                    getScanGetHttp(resultOrderno, hand);
                }
                else {
                    SXUtils.getInstance(activity).ToastCenter("请输入订单号");
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.partner_scan_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        checkBox = (CheckBox) findViewById(R.id.partner_scan_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(partnerScanRecyclerViewAdapter != null)
                    partnerScanRecyclerViewAdapter.initStoreDate();
                }
                else{
                    if(partnerScanRecyclerViewAdapter != null)
                    partnerScanRecyclerViewAdapter.initStoreDateFalse();
                }
            }
        });
        scan_send_confrim = (TextView) findViewById(R.id.scan_send_confrim);
        scan_send_confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(partnerScanRecyclerViewAdapter != null) {
                    SXUtils.showMyProgressDialog(activity, true);
                    getCONFIRMScanGetHttp(hand, partnerScanRecyclerViewAdapter.getScanOrderNo());
                }
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        ScanPartnerOrderLinesEntity scanPartner = (ScanPartnerOrderLinesEntity) msg.obj;
                        Logs.i("========="+scanPartner.getOrderList().size());
                        if(scanPartner.getOrderList() != null && scanPartner.getOrderList().size()>0) {
                            partnerScanRecyclerViewAdapter = new PartnerScanRecyclerViewAdapter(activity, scanPartner.getOrderList(),scanPartner);
                            recyclerView.setAdapter(partnerScanRecyclerViewAdapter);
                            sendLin.setVisibility(View.VISIBLE);
                            allLiny.setVisibility(View.VISIBLE);
                        }
                        time.setText(scanPartner.getOutTime()+"");
                        sendName.setText(scanPartner.getOutAuditor()+"");
                        carNum.setText(scanPartner.getVehicleNo()+"");
                        driver.setText(scanPartner.getDriverName()+"");
                        phone.setText(scanPartner.getDriverPhone()+"");
                        receiveTv.setText(scanPartner.getPartnerUserName());
                        break;
                    case 1001:
                        SXUtils.getInstance(activity).ToastCenter("收货成功");
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10008,"partner"));
                        finish();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg + "");
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    /**
     * 扫码收货订单列表查询
     * @param hand
     */
    public  void getScanGetHttp(String results,final Handler hand) {

        HttpParams params = new HttpParams();
        params.put("outNo",results);//出库单号
        HttpUtils.getInstance(activity).requestPost(false, AppClient.SELECT_SCAN_SEND, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                ScanPartnerOrderLinesEntity scan = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),ScanPartnerOrderLinesEntity.class);
//                List<ScanPartnerConfirmEntity> gde =  ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(),ScanPartnerConfirmEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj =scan;
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
     * 确认批量扫码收货
     * @param hand
     */
    public  void getCONFIRMScanGetHttp(final Handler hand,String orderNos) {
        HttpParams params = new HttpParams();
        params.put("orderNos",orderNos);//订单号
        Logs.i("收货单号======"+orderNos);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.SCAN_CONFIRM, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
//                OrderListEntity gde =  ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),OrderListEntity.class);
                Message msg = new Message();
                msg.what = 1001;
                msg.obj =jsonObject.toString();
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
