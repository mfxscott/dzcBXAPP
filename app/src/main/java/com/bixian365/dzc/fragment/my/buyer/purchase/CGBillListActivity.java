package com.bixian365.dzc.fragment.my.buyer.purchase;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.CGOrderListRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.cgListInfo.CGBillListEntity;
import com.bixian365.dzc.entity.cgListInfo.CGListInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心采购清单列表，供应商使用
 * @author mfx
 * @time  2017/8/30 17:11
 */
public class CGBillListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private Activity activity;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private int indexPage=0;
    private Handler hand;
    private CGOrderListRecyclerViewAdapter simpAdapter;
    private List<CGListInfoEntity> cgList = new ArrayList<>();//采购列表数据
    private String receiveStateStr;//订单状态值
    private LinearLayout defaultNoDataLin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgbill_list);
        activity = this;
        EventBus.getDefault().register(this);
        receiveStateStr = this.getIntent().getStringExtra("state");
        initView();
        initData();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        //发货和确认采购订单成功，刷新列表
        if (messageEvent.getTag() == AppClient.EVENT10001) {
            initData();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void initData(){
        SXUtils.showMyProgressDialog(activity,true);
        GetGYSBillListHttp(indexPage,receiveStateStr);
    }
    private void initView(){
        defaultNoDataLin = (LinearLayout) findViewById(R.id.cg_order_list_lin);
        registerBack();
        if(!TextUtils.isEmpty(receiveStateStr)){
            switch(Integer.parseInt(receiveStateStr)){
                case 0:
                    setTitle("采购清单");
                    break;
                case 11:
                    setTitle("待接单");
                    break;
                case 20:
                    setTitle("待发货");
                    break;
                case 30:
                    setTitle("待收货");
                    break;
                case 40:
                    setTitle("已完成");
                    break;
            }
        }
        else{
            setTitle("采购清单列表");
        }

        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.cg_list_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    GetGYSBillListHttp(indexPage,receiveStateStr);
                }else{
                    indexPage ++;
                    GetGYSBillListHttp(indexPage,receiveStateStr);
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.cg_order_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        List<CGListInfoEntity> gde = (List<CGListInfoEntity>) msg.obj;
                        if(indexPage > 0 && gde.size()>0){
                            cgList.addAll(gde);
                        }else{
                            cgList.clear();
                            cgList.addAll(gde);
                        }
                        if(cgList.size()>0){
                            defaultNoDataLin.setVisibility(View.GONE);
                        }else{
                            defaultNoDataLin.setVisibility(View.VISIBLE);
                        }
                        if(gde.size()>=10){
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);

                        }else{
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                        }
                        if(indexPage >=1){
                            if(simpAdapter != null)
                                simpAdapter.notifyDataSetChanged();
                        }else{
                            simpAdapter = new CGOrderListRecyclerViewAdapter(activity,cgList,1);
                            recyclerView.setAdapter(simpAdapter);
                        }
                        break;
                    case 1001:
                        break;
                    case AppClient.ERRORCODE:
                        defaultNoDataLin.setVisibility(View.VISIBLE);
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
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
     * 获取供应商采购列表
     */
    public void GetGYSBillListHttp(int indexPage,String receiveState) {
        HttpParams params = new HttpParams();
        params.put("pageSize","10");
        params.put("pageIndex",indexPage);
        params.put("receiveState",receiveState);
        HttpUtils.getInstance(activity).requestPost(false,AppClient.GYS_BILLLIST, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                CGBillListEntity gde = (CGBillListEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),CGBillListEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde.getDataset();
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
