package com.bixian365.dzc.fragment.bill;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.SearchActivity;
import com.bixian365.dzc.adapter.TypeInfoRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.bill.BillDataSetEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * ***************************
 * 首页采购清单
 * @author mfx
 * ***************************
 */
public class BillFragment extends Fragment {
    private  View view;
    private Activity activity;
    private Handler hand;
    private int indexPage= 0;
    private RecyclerView recyclerView;
    private SwipyRefreshLayout   mSwipyRefreshLayout;
    private TypeInfoRecyclerViewAdapter simpAdapter;
    private LinearLayout  billListLin,noDataLin;
    private List<BillDataSetEntity> billlist;
    private ImageView  custImg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bill, null);
        activity = getActivity();
        init();
//        HttpLiveSp(indexPage);
//        SXUtils.getInstance().setSysStatusBar(activity,R.color.white);
        EventBus.getDefault().register(this);
        initData();
        return view;
    }
    private  void initData(){


        if(SXUtils.getInstance(activity).IsLogin()) {
//            SXUtils.showMyProgressDialog(activity,true);
            indexPage = 0;
            SXUtils.getInstance(activity).getBill(hand,indexPage);
        }else{
            if(mSwipyRefreshLayout != null){
                mSwipyRefreshLayout.setRefreshing(false);
            }
        }
    }
    private void init(){
        custImg = (ImageView) view.findViewById(R.id.main_custom_service_ivs);
        custImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(activity).CallCustPhone();
            }
        });
        billListLin = (LinearLayout) view.findViewById(R.id.bill_list_liny);
        noDataLin = (LinearLayout) view.findViewById(R.id.bill_default_nodata_tv);

        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.bill_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
                    initData();
                }else{
                    indexPage ++;
                    initData();
//                    HttpLiveSp(indexPage);
                }
            }
        });
        LinearLayout searchlin = (LinearLayout) view.findViewById(R.id.bill_search_liny);
        searchlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.main_bill_gridv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//
//        billInfoAdapter= new HomeBillRecyclerViewAdapter(activity,getTypeInfoData());
//        gridView.setAdapter(billInfoAdapter);
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                SXUtils.getInstance(activity).ToastCenter("=="+position);
////                billInfoAdapter.changeSelected(position);//刷新
//            }
//        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1009:
                         billlist = (List<BillDataSetEntity>) msg.obj;
                        if(billlist == null || billlist.size()<=0) {
                            noDataLin.setVisibility(View.VISIBLE);
                            if(tabLayout != null)
                            tabLayout.removeAllTabs();
                            if(billlist.size()<1){
                                billlist.clear();
                                recyclerView.setAdapter(null);
                            }
                            break;
                        }
                        noDataLin.setVisibility(View.GONE);
                        if(billlist.size() >9){
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                        }else{
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                        }
                        initViewPager(billlist);
                        break;
                    case AppClient.ERRORCODE:
                        String str = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(str+"");
                        noDataLin.setVisibility(View.VISIBLE);
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
    private XTabLayout tabLayout;
    private void initViewPager(final List<BillDataSetEntity> billList) {
         tabLayout = (XTabLayout) view.findViewById(R.id.bill_xTablayout);
        tabLayout.removeAllTabs();
//        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<billList.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(billList.get(i).getCategoryName()));
        }

        simpAdapter = new TypeInfoRecyclerViewAdapter(getActivity(),billList.get(0).getCategoryList(),"1");
//                (getActivity(),billList.get(0).getCategoryList(),new FoodActionCallback(){
//            @Override
//            public void addAction(View view) {
//                NXHooldeView nxHooldeView = new NXHooldeView(activity);
//                int position[] = new int[2];
//                view.getLocationInWindow(position);
//                nxHooldeView.setStartPosition(new Point(position[0], position[1]));
//                ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
//                rootView.addView(nxHooldeView);
//                int endPosition[] = new int[2];
//                badge1.getLocationInWindow(endPosition);
//                nxHooldeView.setEndPosition(new Point(endPosition[0], endPosition[1]));
//                nxHooldeView.startBeizerAnimation();
//                MainFragmentActivity.getInstance().setBadge(true,1);
//            }
//        });
        recyclerView.setAdapter(simpAdapter);

//        tabLayout.addTab(tabLayout.newTab().setText("肉禽类"));
//        tabLayout.addTab(tabLayout.newTab().setText("新鲜蔬菜"));
//        tabLayout.addTab(tabLayout.newTab().setText("米面粮油"));
//        tabLayout.addTab(tabLayout.newTab().setText("水产冻货"));
//        tabLayout.addTab(tabLayout.newTab().setText("休闲酒饮"));
//        tabLayout.addTab(tabLayout.newTab().setText("面食面粉"));
        tabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                if(billList.get(tab.getPosition()).getCategoryList() != null && billList.get(tab.getPosition()).getCategoryList().size()>0){
                    simpAdapter = new TypeInfoRecyclerViewAdapter(getActivity(),billList.get(tab.getPosition()).getCategoryList(),"1");
//                    simpAdapter = new HomeBillRecyclerViewAdapter(getActivity(),billList.get(tab.getPosition()).getCategoryList(),new FoodActionCallback() {
//                        @Override
//                        public void addAction(View view) {
//                            NXHooldeView nxHooldeView = new NXHooldeView(activity);
//                            int position[] = new int[2];
//                            view.getLocationInWindow(position);
//                            nxHooldeView.setStartPosition(new Point(position[0], position[1]));
//                            ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
//                            rootView.addView(nxHooldeView);
//                            int endPosition[] = new int[2];
//                            badge1.getLocationInWindow(endPosition);
//                            nxHooldeView.setEndPosition(new Point(endPosition[0], endPosition[1]));
//                            nxHooldeView.startBeizerAnimation();
//                            MainFragmentActivity.getInstance().setBadge(true,1);
//                        }
//
//                    });
                    recyclerView.setAdapter(simpAdapter);
                }else{
                    recyclerView.setAdapter(null);
                }
            }
            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {
                Logs.i("tab===============222222222="+ tab.getPosition());
            }
            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
                Logs.i("tab===============3333333333="+ tab.getPosition());
            }
        });
    }
    private AdapterView.OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        //发货和确认采购订单成功，刷新列表
        if (messageEvent.getTag() == AppClient.EVENT10002) {
            initData();
        }
        if (messageEvent.getTag() == AppClient.EVENT100026) {
            simpAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
























