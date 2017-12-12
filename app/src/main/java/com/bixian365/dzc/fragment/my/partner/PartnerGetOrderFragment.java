package com.bixian365.dzc.fragment.my.partner;

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
import com.bixian365.dzc.adapter.PartnerOrderRecyclerViewAdapter;
import com.bixian365.dzc.entity.orderlist.OrderInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * 待收货订单列表
 */
public class PartnerGetOrderFragment extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private Activity activity;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private int indexPage=0;
    private Handler hand;
    private List<OrderInfoEntity> cgList = new ArrayList<>();//采购列表数据
    private PartnerOrderRecyclerViewAdapter simpAdapter;
    private LinearLayout defaultDataRel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_partner_all_order_list, container, false);
        initView();
        activity = getActivity();
//        SXUtils.showMyProgressDialog(activity,true);
        new PartnerOrderActivity().getParetnerOrderListHttp(indexPage,"20",hand);
        return view;
    }
    private void initView(){
        defaultDataRel = (LinearLayout) view.findViewById(R.id.partner_buy_order_list_waitpay_lin);
        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.partner_order_list_wait_done_swipe);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    new PartnerOrderActivity().getParetnerOrderListHttp(indexPage,"20",hand);
                }else{
                    indexPage ++;
                    new PartnerOrderActivity().getParetnerOrderListHttp(indexPage,"20",hand);
                }
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.partner_order_wait_done_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        final WaitPayRecyclerViewAdapter simpAdapter = new WaitPayRecyclerViewAdapter(getActivity(),getBankData(),4);
//        recyclerView.setAdapter(simpAdapter);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
//                        List<CGListInfoEntity> gde = (List<CGListInfoEntity>) msg.obj;
                        List<OrderInfoEntity> gde = (List<OrderInfoEntity>) msg.obj;
                        if(indexPage > 0 && gde.size()>0){
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
                            simpAdapter = new PartnerOrderRecyclerViewAdapter(getActivity(),cgList,3);
                            recyclerView.setAdapter(simpAdapter);
                        }
                        if(cgList == null || cgList.size()<1)
                            defaultDataRel.setVisibility(View.VISIBLE);
                        break;
                    case 1001:
                        break;
                    case AppClient.ERRORCODE:
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
}
