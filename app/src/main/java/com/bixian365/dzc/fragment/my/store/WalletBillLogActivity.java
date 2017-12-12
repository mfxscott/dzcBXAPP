package com.bixian365.dzc.fragment.my.store;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.WalletLogRecyclerViewAdapter;
import com.bixian365.dzc.entity.wallet.TransLogEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钱包中的收入明细
 */
public class WalletBillLogActivity extends BaseActivity {
  private RecyclerView recyclerView;
    private int indexPage=0;
    private Activity activity;
    private Handler hand;
    private WalletLogRecyclerViewAdapter simpAdapter;
    private List<TransLogEntity> transLogList = new ArrayList<>();
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private LinearLayout nodataLiny;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_bill_log);
        activity = this;
        registerBack();
        setTitle("收入明细");
        nodataLiny = (LinearLayout) findViewById(R.id.wallet_log_nodata_lin);
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.wallet_log_swipe);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    getTranLog();
                }else{
                    indexPage ++;
                    getTranLog();
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.wallet_log_recyclerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        List<TransLogEntity> transList = (List<TransLogEntity>) msg.obj;
                        if(transList == null || transList.size()<=0){
                            if(transLogList == null || transLogList.size()<=0){
                                nodataLiny.setVisibility(View.VISIBLE);
                            }
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                            break;
                        }
                        if(indexPage > 0 && transList.size()>1){
                            transLogList.addAll(transList);
                        }else{
                            transLogList.clear();
                            transLogList.addAll(transList);
                        }
                        if(transList.size()>=10){
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                        }else{
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                        }
                        if(indexPage >1){
                            if(simpAdapter != null)
                                simpAdapter.notifyDataSetChanged();
                        }else{
                            simpAdapter = new WalletLogRecyclerViewAdapter(activity,transLogList);
                            recyclerView.setAdapter(simpAdapter);
                        }
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
        SXUtils.showMyProgressDialog(activity,true);
        getTranLog();
    }
    /**
     * 收入明细列表
     */
    public void getTranLog() {
        HttpParams params = new HttpParams();
        params.put("pageSize","10");
        params.put("pageIndex",indexPage+"");
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
                msg.what = 1000;
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
