package com.bixian365.dzc.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.bumptech.glide.Glide;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.activity.SearchActivity;
import com.bixian365.dzc.adapter.BannerAdapter;
import com.bixian365.dzc.adapter.HomeGridViewAdapter;
import com.bixian365.dzc.adapter.TypeInfoRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.bill.BillDataSetEntity;
import com.bixian365.dzc.entity.main.BannerSlidEntity;
import com.bixian365.dzc.entity.main.ButtonsEntity;
import com.bixian365.dzc.entity.main.SpecialsEntity;
import com.bixian365.dzc.fragment.CommonWebViewMainActivity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.fragment.my.store.MyWalletActivity;
import com.bixian365.dzc.fragment.my.store.order.MyOrderActivity;
import com.bixian365.dzc.utils.CacheData;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.ObservableScrollView;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.MyGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ***************************
 * 首页
 * @author mfx
 * ***************************
 */
public class HomeFragment extends Fragment implements View.OnClickListener,ObservableScrollView.ScrollViewListener {
    private Handler hand;//接受请求返回
    private View view;
    private Activity activity;
    private Button scanBtn;
    private RelativeLayout bannerLin;//广告位布局
    private ObservableScrollView scro;
    private Button mainGowebBtn;
    private Button mainGopayBtn;
    private Banner channelBanner ,banner;
    private RelativeLayout searchRel;
    private LinearLayout searchRels;
    private MyGridView gridView;
    public RecyclerView homebillRv;
    private XTabLayout scrollXtablayout;
    private XTabLayout tabLayout;
    private RelativeLayout goBillRel;
    private LinearLayout channelLin,homeGridLin;
    private SwipeRefreshLayout swipeRefreshLayout;
    //    public  HomeBillGridViewAdapter billsimpAdapter;
    public TypeInfoRecyclerViewAdapter billsimpAdapter;
    private  List<BillDataSetEntity> billlist;
    private  List<ButtonsEntity> buttonsList;
    private GridView  bannerGridv;
    private List<SpecialsEntity> specialsList;//专题页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        activity = getActivity();
//        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        SXUtils.getInstance().setSysStatusBar(activity,R.color.dialog_btn);
        initView(view);
        EventBus.getDefault().register(this);
        int height = bannerLin.getMeasuredHeight();
        SXUtils.showMyProgressDialog(activity,true);
        initData();
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        if (messageEvent.getTag() == AppClient.EVENT10003) {
            initData();
        }
        if (messageEvent.getTag() == AppClient.EVENT100026) {
            if(billsimpAdapter != null)
            billsimpAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 初始化接口数据
     */
    private void initData(){
//        GetMainCatche();
        getHomeData();
//        HttpParams httpParams = new HttpParams();
//        httpParams.put("position", "1");//1 首页
//        RequestHttpData.getInstance(activity).ChannelRequestHttp(httpParams, AppClient.APP_SWIPER,hand,1000);
//        if(SXUtils.getInstance(activity).IsLogin())
//            SXUtils.getInstance(activity).getBill(hand);
        //文件下载 版本升级
//        DownloadOkHttpUtils.DownFile(activity);
    }
    /**
     * 首页数据缓存数据
     */
    private void GetMainCatche(){
        String data  = CacheData.getInstance().ReadCacheData(SXUtils.getInstance(activity).getSDPath()+AppClient.CACHDATAPATH, AppClient.MAINBANNER);
        if(TextUtils.isEmpty(data))
            return;
        Message msg = new Message();
        msg.what = 1000;
        msg.obj = data;
        hand.sendMessage(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppClient.TAG1) {
            MainFragmentActivity.homeRb.setChecked(true);
            AppClient.TAG1 = false;
            return;
        }
        if (AppClient.TAG2) {
            MainFragmentActivity.goodsRb.setChecked(true);
            AppClient.TAG2 = false;
            return;
        }
        if (AppClient.TAG3) {
            MainFragmentActivity.billRb.setChecked(true);
            AppClient.TAG3 = false;
            return;
        }
        if (AppClient.TAG4) {
            MainFragmentActivity.carRb.setChecked(true);
            AppClient.TAG4 = false;
            return;
        }
        if (AppClient.TAG5) {
            MainFragmentActivity.myRb.setChecked(true);
            AppClient.TAG5 = false;
            return;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_search_lin:case R.id.home_search_gone_lin:
                Intent intent = new Intent(activity, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.home_go_bill_rel:
                //滚动到指定位置
//                scro.scrollTo(0, bannerLin.getHeight()+goBillRel.getHeight()+channelLin.getHeight()+homeGridLin.getHeight()-searchRel.getHeight()+41);
                MainFragmentActivity.billRb.setChecked(true);
                break;
//            case R.id.home_goweb_btn:
//                MainFragmentActivity.getInstance().setBadge(false,1);
//                Intent aa = new Intent(activity, StoreMapActivity.class);
//                activity.startActivity(aa);
//                break;
//            case R.id.home_gopay_btn:
//
//                Intent intent = new Intent(activity, LoginNameActivity.class);
//                activity.startActivity(intent);
//                //购物车远点加减
//                MainFragmentActivity.getInstance().setBadge(true,1);
//                break;
//            case R.id.home_scan_btn:
//
//                int h =  bannerLin.getHeight();
//                int hh = mainGowebBtn.getHeight();
//                //scrollview 滚动指定位置
//                scro.scrollTo(0, h+hh);
//
////                Intent intent = new Intent(activity, Regis1Activity.class);
////                activity.startActivity(intent);
//                break;
        }
    }

    /**
     * 滑动显示 tab
     */
    private void initViewPager(int postion) {
        scrollXtablayout.removeAllTabs();
        for(int i=0;i<billlist.size();i++){
//            scrollXtablayout.addTab(scrollXtablayout.newTab().setText(billlist.get(i).getCategoryName()));
//            if(postion == i)
            scrollXtablayout.addTab(scrollXtablayout.newTab().setText(billlist.get(i).getCategoryName()));
//            else
//                scrollXtablayout.addTab(scrollXtablayout.newTab().setText(billlist.get(i).getCategoryName()),false);
        }

//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addTab(tabLayout.newTab().setText("肉禽类"));
//        tabLayout.addTab(tabLayout.newTab().setText("新鲜蔬菜"));
//        tabLayout.addTab(tabLayout.newTab().setText("米面粮油"));
//        tabLayout.addTab(tabLayout.newTab().setText("水产冻货"));
//        tabLayout.addTab(tabLayout.newTab().setText("休闲酒饮"));
//        tabLayout.addTab(tabLayout.newTab().setText("面食面粉"));

        scrollXtablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).select();
                if(billlist.get(tab.getPosition()).getCategoryList() != null && billlist.get(tab.getPosition()).getCategoryList().size()>0){
//                    billsimpAdapter = new HomeBillGridViewAdapter(getActivity(),billlist.get(tab.getPosition()).getCategoryList(),new FoodActionCallback(){
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
//                    });
                    billsimpAdapter = new TypeInfoRecyclerViewAdapter(getActivity(),billlist.get(tab.getPosition()).getCategoryList(),"1");

//                    billsimpAdapter = new HomeBillRecyclerViewAdapter(getActivity(),billlist.get(tab.getPosition()).getCategoryList(),new FoodActionCallback(){
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
//                    });
                    homebillRv.setAdapter(billsimpAdapter);
                }else{
                    homebillRv.setAdapter(null);
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
    private void DisplayinitViewPager(int postion) {
        tabLayout.removeAllTabs();
        for(int i=0;i<billlist.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(billlist.get(i).getCategoryName()));
//            tabLayout.addTab(tabLayout.newTab().setText(billlist.get(i).getCategoryName()),false);
        }
//        setCurrentItem
//        tabLayout.setxTabDisplayNum(1);

//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.addTab(tabLayout.newTab().setText("肉禽类"));
//        tabLayout.addTab(tabLayout.newTab().setText("新鲜蔬菜"));
//        tabLayout.addTab(tabLayout.newTab().setText("米面粮油"));
//        tabLayout.addTab(tabLayout.newTab().setText("水产冻货"));
//        tabLayout.addTab(tabLayout.newTab().setText("休闲酒饮"));
//        tabLayout.addTab(tabLayout.newTab().setText("面食面粉"));
        tabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                scrollXtablayout.getTabAt(tab.getPosition()).select();
                if(billlist.get(tab.getPosition()).getCategoryList() != null && billlist.get(tab.getPosition()).getCategoryList().size()>0){
//                    billsimpAdapter = new HomeBillGridViewAdapter(getActivity(),billlist.get(tab.getPosition()).getCategoryList(),new FoodActionCallback(){
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
//                    });
                    billsimpAdapter = new TypeInfoRecyclerViewAdapter(getActivity(),billlist.get(tab.getPosition()).getCategoryList(),"1");

//                    billsimpAdapter = new HomeBillRecyclerViewAdapter(getActivity(),billlist.get(tab.getPosition()).getCategoryList(),new FoodActionCallback(){
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
//                    });
                    homebillRv.setAdapter(billsimpAdapter);
                }else{
                    homebillRv.setAdapter(null);
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


    private void initView(View view) {


        bannerGridv = (GridView) view.findViewById(R.id.banner_gridv);
        bannerGridv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(SXUtils.getInstance(activity).IsLogin()) {
                    Intent ztintent = new Intent(activity, CommonWebViewMainActivity.class);
                    ztintent.putExtra("tag", "2");
                    ztintent.putExtra("postUrl", AppClient.ACTIVITYURL + "" + specialsList.get(i).getId() + "");
                    startActivity(ztintent);
                }
            }
        });
        banner = (Banner) view.findViewById(R.id.banner);
        channelBanner = (Banner) view.findViewById(R.id.channel);
        view.findViewById(R.id.main_custom_service_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SXUtils.getInstance(activity).CallCustPhone();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_swipe_container);
        swipeRefreshLayout.setColorSchemeResources( R.color.qblue, R.color.red, R.color.btn_gray);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
//                myWebView.reload();
                initData();
//                swipeRefreshLayout.setRefreshing(false);
            }
        });


        scrollXtablayout = (XTabLayout) view.findViewById(R.id.main_scroll_Tablayout);
        tabLayout = (XTabLayout) view.findViewById(R.id.main_xTablayout);

        LinearLayout searchLin = (LinearLayout) view.findViewById(R.id.home_search_lin);
        searchLin.setOnClickListener(this);
        LinearLayout searchGoneLin = (LinearLayout) view.findViewById(R.id.home_search_gone_lin);
        searchGoneLin.setOnClickListener(this);

        goBillRel = (RelativeLayout) view.findViewById(R.id.home_go_bill_rel);
        goBillRel.setOnClickListener(this);
        channelLin = (LinearLayout) view.findViewById(R.id.main_channel_lin);
        homeGridLin = (LinearLayout) view.findViewById(R.id.home_grid_lin);

        homebillRv = (RecyclerView) view.findViewById(R.id.home_list_recyclerv);
//        homebillRv.setFocusable(false);
        //添加此方法有效解决RecyclerView 与scrollview的滑动不顺畅问题
        homebillRv.setNestedScrollingEnabled(false);
        homebillRv.setLayoutManager(new LinearLayoutManager(homebillRv.getContext()));
        homebillRv.setItemAnimator(new DefaultItemAnimator());
//        final HomeBillRecyclerViewAdapter simpAdapter = new HomeBillRecyclerViewAdapter(getActivity(),getTypeInfoData());
//        homebillRv.setAdapter(simpAdapter);
//        homebillRv = (MyGridView) view.findViewById(R.id.home_list_recyclerv);
//        homebillRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });

        gridView = (MyGridView) view.findViewById(R.id.main_gridv);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int action = Integer.parseInt(buttonsList.get(position).getImgAction());
                switch (action){
                    case 1:
                        //1=首页
                        break;
                    case 2:
                        //2=商品类目
                        MainFragmentActivity.goodsRb.setChecked(true);
                        break;
                    case 3:
                        //3=清单单
                        MainFragmentActivity.billRb.setChecked(true);
                        break;
                    case 4:
                        //4=购物车
                        MainFragmentActivity.carRb.setChecked(true);
                        break;
                    case 5:
                        //5=我的
                        MainFragmentActivity.myRb.setChecked(true);
                        break;
                    case 6:
                        //7=商品页
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("cno",buttonsList.get(position).getImgParam());
                        startActivity(intent);
                        break;
                    case 7:
                        //6=专题页
                        if(SXUtils.getInstance(activity).IsLogin()) {
                            Intent ztintent = new Intent(activity, CommonWebViewMainActivity.class);
                            ztintent.putExtra("tag", "2");
                            ztintent.putExtra("postUrl", AppClient.ACTIVITYURL + "" + buttonsList.get(position).getImgParam() + "");
                            startActivity(ztintent);
                        }
                        break;
                    case 8:
                        if(SXUtils.getInstance(activity).IsLogin()) {
                            //8=我的订单
                            Intent sendorder = new Intent(activity,MyOrderActivity.class);
                            sendorder.putExtra("orderTag","1");
                            startActivity(sendorder);
                        }
                        break;
                    case 9:
                        //9=我的钱包
                        if(SXUtils.getInstance(activity).IsLogin()) {
                            Intent wallet = new Intent(activity, MyWalletActivity.class);
                            startActivity(wallet);
                        }
                        break;
                    case 10:
                        //10H5页面
                        Intent Hintent = new Intent(activity, CommonWebViewMainActivity.class);
                        Hintent.putExtra("tag", "2");
                        Hintent.putExtra("postUrl", buttonsList.get(position).getImgParam() + "");
                        startActivity(Hintent);
                        break;
                }
            }
        });

        searchRel = (RelativeLayout) view.findViewById(R.id.main_search_rel);
        searchRels = (LinearLayout) view.findViewById(R.id.main_search_rels);

        scro = (ObservableScrollView) view.findViewById(R.id.main_scroll_view);
        scro.setScrollViewListener(this);

        bannerLin = (RelativeLayout) view.findViewById(R.id.main_banner_lin);


//        mainGowebBtn = (Button) view.findViewById(R.id.home_goweb_btn);
//        mainGowebBtn.setOnClickListener(this);
//        scanBtn = (Button) view.findViewById(R.id.home_scan_btn);
//        scanBtn.setOnClickListener(this);
////        RequestReqMsgData.UpdateVersion(manager, activity, hand);
//        mainGopayBtn = (Button) view.findViewById(R.id.home_gopay_btn);
//        mainGopayBtn.setOnClickListener(this);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        String objs = (String) msg.obj;
                        Object commonsjs=null;
                        Object swipersOj = null;
                        Object buttonsOj = null;
                        Object specialsOj = null;
                        try {
                            JSONObject jsobj = new JSONObject(objs.toString());
                            commonsjs = jsobj.get("commons");
                            swipersOj = jsobj.get("swipers");
                            buttonsOj = jsobj.get("buttons");
                            specialsOj = jsobj.get("specials");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Logs.i("常用清单commonsjs====="+commonsjs);
                        Logs.i("广告数据====="+swipersOj);
                        Logs.i("专题页面====="+specialsOj);
                        Logs.i("九宫格数据====="+buttonsOj.toString());
                        specialsList = ResponseData.getInstance(activity).parseJsonArray(specialsOj.toString(), SpecialsEntity.class);
//                        setChannel(specialsList);
                        if(null != specialsList && specialsList.size()>0)
                            bannerGridv.setAdapter(new BannerAdapter(activity,specialsList));
                        buttonsList = ResponseData.getInstance(activity).parseJsonArray(buttonsOj.toString(), ButtonsEntity.class);
                        gridView.setAdapter(new HomeGridViewAdapter(activity,buttonsList));
                        //首页轮播广告
                        if(!TextUtils.isEmpty(swipersOj.toString()) || !swipersOj.toString().equals("null")) {
                            List<BannerSlidEntity> goodsTypeList = ResponseData.getInstance(activity).parseJsonArray(swipersOj.toString(), BannerSlidEntity.class);
                            setBanner(goodsTypeList);
                        }
                        //常用清单
                        if(billlist != null){
                            billlist.clear();
                        }
                        billlist = ResponseData.getInstance(activity).parseJsonArray(commonsjs.toString(), BillDataSetEntity.class);
                        if(billlist == null || billlist.size()<=0) {
                            if (tabLayout != null) {
                                tabLayout.removeAllTabs();
                                scrollXtablayout.removeAllTabs();
                            }
                            if (billlist.size() < 1) {
                                billlist.clear();
                                homebillRv.setAdapter(null);
                            }
                            break;
                        }
                        setBill(billlist);
                        break;
                    case AppClient.ERRORCODE:
                        String str = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(str);
                        break;
                    case AppClient.UPDATEVER:
//                        SXUtils.getInstance().ToastCenter(activity, "版本更新");
                        break;
                }
                if(swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
//        setBanner();
//        setChannel();
    }
//    public  void setListViewHeightBasedOnChildren(ListView listView) {
//            //获取ListView对应的Adapter
//            ListAdapter listAdapter = listView.getAdapter();
//            if (listAdapter == null) {
//                // pre-condition
//                return;
//            }
//            int totalHeight = 0;
//            for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
//                View listItem = listAdapter.getView(i, null, listView);
//                listItem.measure(0, 0); //计算子项View 的宽高
//                totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
//            }
//
//            ViewGroup.LayoutParams params = listView.getLayoutParams();
//            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//            //listView.getDividerHeight()获取子项间分隔符占用的高度
//            //params.height最后得到整个ListView完整显示需要的高度
//            listView.setLayoutParams(params);
//        }
    /**
     * 设置首页常用清单数据
     */
    private void setBill(List<BillDataSetEntity> billlist){
        if(billlist == null || billlist.size()<=0) {
            return;
        }
        billsimpAdapter = new TypeInfoRecyclerViewAdapter(getActivity(),billlist.get(0).getCategoryList(),"1");
//        billsimpAdapter = new HomeBillRecyclerViewAdapter(getActivity(),billlist.get(0).getCategoryList(),new FoodActionCallback(){
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

//        billsimpAdapter = new HomeBillGridViewAdapter(getActivity(),billlist.get(0).getCategoryList(),new FoodActionCallback(){
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
        homebillRv.setAdapter(billsimpAdapter);
        initViewPager(0);
        DisplayinitViewPager(0);
    }

    /**
     * 首页轮播图
     * @param goodsTypeList
     */
    private void setBanner(final List<BannerSlidEntity> goodsTypeList){
        try {


        if(null == banner)
            return ;
//        List<String> images = new ArrayList<String>();
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497598051&di=136b6c564a6d8d59e77ce349616996e9&imgtype=jpg&er=1&src=http%3A%2F%2Fm.qqzhi.com%2Fupload%2Fimg_0_72213646D1378690088_23.jpg");
//        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3145185115,3541103163&fm=26&gp=0.jpg");
//        images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4280343775,3437702687&fm=26&gp=0.jpg");
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                int  action = Integer.parseInt(goodsTypeList.get(position).getImgAction());
                //动作，1=首页,2=品类,3=必抢靖单,4=购物车,5=我的,6=专题页,7=商品页,8=我的订单,9=我的钱包
                switch (action){
                    case 1:
                        //1=首页
                        break;
                    case 2:
                        //2=商品类目
                        MainFragmentActivity.goodsRb.setChecked(true);
                        break;
                    case 3:
                        //3=清单单
                        MainFragmentActivity.billRb.setChecked(true);
                        break;
                    case 4:
                        //4=购物车
                        MainFragmentActivity.carRb.setChecked(true);
                        break;
                    case 5:
                        //5=我的
                        MainFragmentActivity.myRb.setChecked(true);
                        break;
                    case 6:
                        //7=商品页
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("cno",goodsTypeList.get(position).getImgParam());
                        startActivity(intent);
                        break;
                    case 7:
                        //6=专题页
                        if(SXUtils.getInstance(activity).IsLogin()) {
                            Intent ztintent = new Intent(activity, CommonWebViewMainActivity.class);
                            ztintent.putExtra("tag", "2");
                            ztintent.putExtra("postUrl", AppClient.ACTIVITYURL + "" + goodsTypeList.get(position).getImgParam() + "");
                            startActivity(ztintent);
                        }
                        break;
                    case 8:
                        //8=我的订单
                        if(SXUtils.getInstance(activity).IsLogin()) {
                            Intent sendorder = new Intent(activity,MyOrderActivity.class);
                            sendorder.putExtra("orderTag","1");
                            startActivity(sendorder);
                        }
                        break;
                    case 9:
                        //9=我的钱包
                        if(SXUtils.getInstance(activity).IsLogin()) {
                            Intent wallet = new Intent(activity, MyWalletActivity.class);
                            startActivity(wallet);
                        }
                        break;
                    case 10:
                        //10H5页面
                        Intent Hintent = new Intent(activity, CommonWebViewMainActivity.class);
                        Hintent.putExtra("tag", "2");
                        Hintent.putExtra("postUrl", goodsTypeList.get(position).getImgParam() + "");
                        startActivity(Hintent);
                        break;
                }
            }
        });
        List<String> images = new ArrayList<String>();
        for(int i=0;i<goodsTypeList.size();i++){
            images.add(goodsTypeList.get(i).getImgUrl()+"");
        }
//        banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//                Intent intent = new Intent( activity, GoodsDetailActivity.class);
////                intent.putExtra("cno",goodsTypeList.get);
//                startActivity(intent);
//            }
//        });
//        images.add(R.mipmap.banner11);
//        images.add(R.mipmap.banner33);
//        images.add(R.mipmap.banner44);
//        images.add(R.mipmap.banner55);
//        images.add(R.mipmap.banner66);
//        List<String> titlestr = new ArrayList<String>();
//        titlestr.add("我是第一个图片");
//        titlestr.add("我是第2个图片");
//        titlestr.add("我是第3个图片");
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
//        //显示标题样式水平显示
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        //设置标题文本
//        banner.setBannerTitles(titlestr);
        //设置图片集合
        banner.setImages(images);
        if(goodsTypeList != null && goodsTypeList.size()>0)
            banner.setDelayTime(Integer.parseInt(goodsTypeList.get(0).getSeconds())*1000);
        //设置banner动画效果
        //DepthPag折叠
        banner.setBannerAnimation(Transformer.Default);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        }catch (Exception e){

        }
    }

    /**
     * 活动也轮播  暂时不用
     * @param specialsList
     */
    private void setChannel( List<SpecialsEntity> specialsList){
        channelBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                startActivity(intent);
            }
        });
//        List<Integer> images = new ArrayList<Integer>();
        List<String> images = new ArrayList<String>();
        for(int i=0;i<specialsList.size();i++){
            images.add(specialsList.get(i).getImgUrl()+"");
        }
//        images.add(R.mipmap.banner22);
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497598051&di=136b6c564a6d8d59e77ce349616996e9&imgtype=jpg&er=1&src=http%3A%2F%2Fm.qqzhi.com%2Fupload%2Fimg_0_72213646D1378690088_23.jpg");
//        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3145185115,3541103163&fm=26&gp=0.jpg");
//        images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4280343775,3437702687&fm=26&gp=0.jpg");
        //设置图片加载器
        channelBanner.setImageLoader(new GlideImageLoader());
        channelBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
//        //显示标题样式水平显示
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
//        //设置标题文本
//        banner.setBannerTitles(titlestr);
        channelBanner.isAutoPlay(true);
        //设置图片集合
        //设置图片集合
        channelBanner.setImages(images);
        //设置banner动画效果
        //DepthPag折叠
        channelBanner.setBannerAnimation(Transformer.Default);
        //banner设置方法全部调用完毕时最后调用
        channelBanner.start();
    }
    //接收线程回调
    public void getHomeData() {
        HttpParams httpParams = new HttpParams();
        HttpUtils.getInstance(activity).requestPost(false, AppClient.HOME_DATA, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
//                CacheData.getInstance().WirtCacheData(SXUtils.getInstance(activity).getSDPath()+AppClient.CACHDATAPATH, AppClient.MAINBANNER, jsonObject.toString());
                Logs.i("首页发送成功返回参数=======",jsonObject.toString());
//                List<BillDataSetEntity> goodsTypeList = ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(), BillDataSetEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = jsonObject.toString();
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
     * NAIN UI主线程
     BACKGROUND 后台线程
     POSTING 和发布者处在同一个线程
     ASYNC 异步线程
     * @param
     *
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(Object event) {
//        Logs.i("onMessageEvent11111111=============");
//        MessageEventEntity msg = (MessageEventEntity) event;
//        int msgint = msg.tag;
//        switch (msgint){
//            case 1:
//                Toast.makeText(activity,msg.tag+"=====",Toast.LENGTH_LONG).show();
//                break;
//            case 2:
//                Toast.makeText(activity,msg.obj.toString()+"=====",Toast.LENGTH_LONG).show();
//                break;
//        }
//    };
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainStringThread(MessageEventEntity event) {
//        Logs.i("onEventMainThread222222=============");
//        Toast.makeText(activity,event+"2222",Toast.LENGTH_LONG).show();
//    };
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        channelBanner.startAutoPlay();
        banner.startAutoPlay();
    }
    @Override
    public void onStop() {
        super.onStop();
        channelBanner.stopAutoPlay();
        banner.stopAutoPlay();
    }
    //scrollview 滑动事件
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // Log.i("TAG", "y--->" + y + "    height-->" + height);
        if (y <= 0) {
//            searchRel.setBackgroundColor(Color.argb((int) y, y, y, y));//AGB由相关工具获得，或者美工提供
            searchRel.setVisibility(View.VISIBLE);
            searchRels.setVisibility(View.GONE);
//            scrollXtablayout.setVisibility(View.GONE);
            searchRel.setBackgroundColor(Color.argb((int) 0, (int) 0, (int) 0, (int) 0));
        } else if (y > 0 && y <= bannerLin.getHeight()+goBillRel.getHeight()+channelLin.getHeight()+homeGridLin.getHeight()-searchRel.getHeight()+80) {
            float scale = (float) y / (bannerLin.getHeight()+goBillRel.getHeight()+channelLin.getHeight()+homeGridLin.getHeight()-searchRel.getHeight()+80);
            float alpha = (255 * scale);
            // 只是layout背景透明(仿知乎滑动效果)
            searchRel.setBackgroundColor(Color.argb((int) alpha, (int) alpha, (int) alpha, (int) alpha));
            searchRel.setVisibility(View.VISIBLE);
            searchRels.setVisibility(View.GONE);
        } else {
//            scrollXtablayout.setVisibility(View.VISIBLE);
            searchRels.setVisibility(View.VISIBLE);
            searchRel.setVisibility(View.GONE);
//            searchRels.setBackgroundColor(Color.argb((int) 255, 227, 29, 26));
        }
        Log.i("====","==========="+y);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            //Glide 加载图片简单用法
            Glide.with(context).load(path).placeholder(R.mipmap.default_big_load_img).error(R.mipmap.default_big_load_img).into(imageView);
            //Picasso 加载图片简单用法
//            Picasso.with(context).load(path).into(imageView);

            //用fresco加载图片简单用法，记得要写下面的createImageView方法
//            Uri uri = Uri.parse((String) path);
//            imageView.setImageURI(uri);
        }
    }
}
