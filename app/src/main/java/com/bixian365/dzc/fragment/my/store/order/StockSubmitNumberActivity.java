package com.bixian365.dzc.fragment.my.store.order;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.StockSubmitRecyclerViewAdapter;
import com.bixian365.dzc.entity.orderlist.OrderGoodsInfoEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 用户订单列表缺货少货上报界面
 */

public class StockSubmitNumberActivity extends BaseActivity{
    private Activity activity;
    @BindView(R.id.stock_submit_recyclerv)
    RecyclerView  recyclerView;
    @BindView(R.id.stock_submit_tv)
    TextView  submitTv;
    @BindView(R.id.stock_submit_orderno_tv)
    TextView  orderNoTv;
    private List<OrderGoodsInfoEntity>  orderLines;
    private String orderNoStr;
    private StockSubmitRecyclerViewAdapter simpAdapter;
    private  Handler   hand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_submit_number);
        activity = this;
        Bundle bundle = this.getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("orderLines");
        orderNoStr = bundle.getString("orderNo");
        orderLines= (List<OrderGoodsInfoEntity>) list.get(0);
        ButterKnife.bind(activity);
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("缺货少货上报");
        orderNoTv.setText("订单号："+orderNoStr+"");
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
          simpAdapter = new StockSubmitRecyclerViewAdapter(activity,orderLines);
        recyclerView.setAdapter(simpAdapter);
        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SXUtils.getInstance(activity).ToastCenter(simpAdapter.getInputNum()+"====");
                Logs.i("上报少货======================>>"+simpAdapter.getInputNum()+"");
                   if(simpAdapter.getInputNum() == null){
                       SXUtils.getInstance(activity).ToastCenter("请输入商品实际收货数量");
                           return ;
                   }
                geStockSubmitHttp(simpAdapter.getInputNum());
            }
        });
     hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("上报成功");
                        finish();
                        break;
                    case 1001:
                        break;
                    case AppClient.ERRORCODE:
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 缺货少货上报
     * @param orderLines
     */
    public  void geStockSubmitHttp(JSONArray orderLines) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderNo",orderNoStr);
            jsonObject.put("orderLines",orderLines);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        HttpParams httpParams = new HttpParams();
//        httpParams.put("orderNo",orderNoStr);
//        httpParams.put("orderLines",orderLines.toString());

        HttpUtils.getInstance(activity).requestStringPost(false, AppClient.STOCK_SUBMIT,jsonObject.toString() , new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
//                OrderListEntity gde =  ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),OrderListEntity.class);
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


}
