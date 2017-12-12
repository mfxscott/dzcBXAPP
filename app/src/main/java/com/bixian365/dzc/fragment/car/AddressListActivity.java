package com.bixian365.dzc.fragment.car;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.AddressRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.address.AddressInfoEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

public class AddressListActivity extends BaseActivity {
    private RecyclerView  addressRecy;
    private Activity activity;
    private Handler hand;
    private LinearLayout  defaultLiny;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        activity = this;
        //注册事件
        EventBus.getDefault().register(activity);
        initView();
        initData();
    }
    private  void initData(){
        SXUtils.showMyProgressDialog(activity,false);
        getHttpAddress();
    }
    private void initView(){
        registerBack();
        setTitle("收货信息");

        defaultLiny = (LinearLayout) findViewById(R.id.address_default_lin);
        addressRecy = (RecyclerView) findViewById(R.id.address_list_recyclerv);
        TextView  rightTv = (TextView) findViewById(R.id.all_title_right);
        rightTv.setText("新增");
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditAddAddressActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("tag","0");//0是新增  1是编辑
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        addressRecy.setLayoutManager(new LinearLayoutManager(addressRecy.getContext()));
        addressRecy.setItemAnimator(new DefaultItemAnimator());

        Button addBtn = (Button) findViewById(R.id.address_list_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, EditAddAddressActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("tag","0");//0是新增  1是编辑
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:

                        List<AddressInfoEntity> addressList = (List<AddressInfoEntity>) msg.obj;
                        if(addressList == null || addressList.size()<=0){
                            defaultLiny.setVisibility(View.VISIBLE);
                        }else{
                            defaultLiny.setVisibility(View.GONE);
                        }
                        AddressRecyclerViewAdapter simpAdapter = new AddressRecyclerViewAdapter(activity,addressList);
                        addressRecy.setAdapter(simpAdapter);
                        break;
                    case AppClient.ERRORCODE:
                        defaultLiny.setVisibility(View.VISIBLE);
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }

    /**
     * 获取收货地址
     */
    public void getHttpAddress() {
        HttpParams httpp = new HttpParams();
        HttpUtils.getInstance(activity).requestPost(false, AppClient.ADDRESS_SELECT, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("获取收货地址=====",jsonObject.toString());
                JSONObject jsonObject1 = null;
                List<AddressInfoEntity> orderFrom = ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(),AddressInfoEntity.class);
                Message msg = new Message();
                msg.what = 1000;
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
        if(messageEvent.getTag() ==AppClient.EVENT555){
            initData();
        }
    }
}
