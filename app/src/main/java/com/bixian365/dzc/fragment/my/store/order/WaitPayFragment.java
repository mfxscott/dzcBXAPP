package com.bixian365.dzc.fragment.my.store.order;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.WaitPayRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.entity.orderlist.OrderInfoEntity;
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

public class WaitPayFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private Activity activity;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private int indexPage=0;
    private List<OrderInfoEntity> cgList = new ArrayList<>();//采购列表数据
    private WaitPayRecyclerViewAdapter simpAdapter;
    private LinearLayout  nodataLiny;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wait_pay, container, false);

        activity = getActivity();
        initView();
        //注册事件
        EventBus.getDefault().register(this);
        initData();
        return view;
    }
    private void initData(){
        new MyOrderActivity().getOrderListHttp(indexPage,"1",hand);
    }
    private void initView(){
        nodataLiny = (LinearLayout) view.findViewById(R.id.buy_order_list_waitpay_lin);

        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.order_list_wait_pay_swipe);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    initData();
                }else{
                    indexPage ++;
                    initData();
                }
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.order_wait_pay_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        final WaitPayRecyclerViewAdapter simpAdapter = new WaitPayRecyclerViewAdapter(getActivity(),getBankData(),1);
//        recyclerView.setAdapter(simpAdapter);


    }
    public Handler   hand = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
//                        List<CGListInfoEntity> gde = (List<CGListInfoEntity>) msg.obj;
                    List<OrderInfoEntity> gde = (List<OrderInfoEntity>) msg.obj;
                    if(indexPage > 0 ){
                        cgList.addAll(gde);
                    }else{
                        cgList.clear();
                        cgList.addAll(gde);
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
                        simpAdapter = new WaitPayRecyclerViewAdapter(getActivity(),cgList,1);
                        recyclerView.setAdapter(simpAdapter);
                    }
                    if(cgList == null || cgList.size() <=0){
                       nodataLiny.setVisibility(View.VISIBLE);
                    }else{
                        nodataLiny.setVisibility(View.GONE);
                    }
                    EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
                    break;
                case 1001:
                    initData();
                    break;
                case AppClient.ERRORCODE:
                    String msgs = (String) msg.obj;
                    SXUtils.getInstance(activity).ToastCenter(msgs);
                    break;
            }
            if(mSwipyRefreshLayout != null){
                mSwipyRefreshLayout.setRefreshing(false);
            }
            return true;
        }
    });
    /**
     * @return
     */
    private ArrayList<GoodsInfoEntity> getBankData(){
        ArrayList<GoodsInfoEntity> list = new ArrayList<>();

        for(int i=0;i<2;i++){
            GoodsInfoEntity  info = new GoodsInfoEntity();
            info.setGoodsName("新鲜上市的西红柿");
            info.setShopPrice("￥10.00");
            list.add(info);
        }
        return list;
    }
    /**
     * 获取普通用户订单
     */
    public void getUserOrderListHttp() {
        HttpUtils.getInstance(activity).requestPost(false,AppClient.USER_ORDERS, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                UserInfoEntity gde = null;
                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde;
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
        if(messageEvent.getTag()==1003 || messageEvent.getMessage().equals("waitpay")){
            Message msg = new Message();
            msg.what = 1001;
            msg.obj = "";
            hand.sendMessage(msg);

        }else if(messageEvent.getTag()==1004 || messageEvent.getMessage().equals("waitpay")){
            Message msg = new Message();
            msg.what = AppClient.ERRORCODE;
            msg.obj = "请求异常";
            hand.sendMessage(msg);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}