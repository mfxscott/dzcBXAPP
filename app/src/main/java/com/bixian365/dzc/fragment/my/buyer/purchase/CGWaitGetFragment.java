package com.bixian365.dzc.fragment.my.buyer.purchase;

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
import com.bixian365.dzc.adapter.CGOrderListRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.cgListInfo.CGListInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CGWaitGetFragment extends Fragment {
    private RecyclerView recyclerView;
    private Activity activity;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private int indexPage=0;
    private Handler hand;
    private CGOrderListRecyclerViewAdapter simpAdapter;
    private List<CGListInfoEntity> cgList = new ArrayList<>();//采购列表数据
    private LinearLayout defaultNoDataLin;
    private  String receiveStateStr = "30";
    private View view;
//     case 11:
//    setTitle("待接单");
//                    break;
//                case 20:
//    setTitle("待发货");
//                    break;
//                case 30:
//    setTitle("待收货");
//                    break;
//                case 40:
//    setTitle("已完成");
//                    break;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cgbill_list, container, false);
        activity = getActivity();
        EventBus.getDefault().register(this);
        initView();
        initData();

        //注册事件
        return view;
    }
    public void initData(){
        new CGBIllListFragmentActivity().GetGYSBillListHttp(indexPage,receiveStateStr,hand);
    }
    private void initView(){
        defaultNoDataLin = (LinearLayout) view.findViewById(R.id.cg_order_list_lin);

        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.cg_list_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    new CGBIllListFragmentActivity().GetGYSBillListHttp(indexPage,receiveStateStr,hand);
                }else{
                    indexPage ++;
                    new CGBIllListFragmentActivity().GetGYSBillListHttp(indexPage,receiveStateStr,hand);
                }
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.cg_order_list_recycler);
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
                            simpAdapter = new CGOrderListRecyclerViewAdapter(activity,cgList,2);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag()==AppClient.EVENT100016){
            initData();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
