package com.bixian365.dzc.fragment.car;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.CarCouponsRecyclerViewAdapter;
import com.bixian365.dzc.adapter.CarStoreRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.car.CarList;
import com.bixian365.dzc.entity.car.ShoppingListEntity;
import com.bixian365.dzc.entity.car.TakeNoPartInActivitiesEntity;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bixian365.dzc.R.id.car_notice_tv;
import static com.bixian365.dzc.fragment.MainFragmentActivity.badge1;
import static java.lang.Float.parseFloat;


/**
 * ***************************
 * 首页购物车
 * @author mfx
 * ***************************
 */
public class CarFragment extends Fragment implements View.OnClickListener{
    private  View view;
    private Activity activity;
    private Handler hand;
    private RecyclerView recyclerView;
    private RecyclerView  couponsRecyclerView;
    private TextView editDelTv;//编辑 确定
    private CarStoreRecyclerViewAdapter storesimpAdapter;
    private CarCouponsRecyclerViewAdapter couponsAdapter;
    private TextView payDelBtn;//购买删除按钮
    private LinearLayout delNumLin;
    public TextView   delNumTv;//显示选择条目
    private TextView noticTv;//满减活动
    private TextView totalTv;//购物车总价格
    //    private CheckBox allCheckBox;
    private RelativeLayout  allYhRel;//购物车优惠显示布局
    private  List<GoodsInfoEntity> carlist;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    //    private List<ShoppingListEntity> shopList = new ArrayList<>();//总购物车数量
    private RelativeLayout carContentLin;//商品主内容UI
    private CarList car;
    private List<ShoppingListEntity> shopList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_car, null);
        activity = getActivity();
        init();
        //注册事件
        EventBus.getDefault().register(this);
//        if(!AppClient.isFristLogin){
//            if(SXUtils.getInstance(activity).IsLogin()) {
        GetCarList();

//            }
//        }else{
//            AppClient.isFristLogin = false;
//        }
        return view;
    }
    private void init(){
        carContentLin = (RelativeLayout) view.findViewById(R.id.car_content_rely);
        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.car_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    if(SXUtils.getInstance(activity).IsLogin()) {
                        GetCarList();
                    }else {
                        mSwipyRefreshLayout.setRefreshing(false);
                    }
                }else{
                    GetCarList();
                }
            }
        });
        LinearLayout lin = (LinearLayout) view.findViewById(R.id.car_go_shop_lin);
        lin.setOnClickListener(this);
        noticTv = (TextView) view.findViewById(car_notice_tv);
        totalTv = (TextView) view.findViewById(R.id.car_total_tv);


        allYhRel = (RelativeLayout) view.findViewById(R.id.car_all_yh_rel);

        editDelTv = (TextView) view.findViewById(R.id.car_edit_del);
        editDelTv.setOnClickListener(this);

        payDelBtn = (TextView) view.findViewById(R.id.car_pay_del_btn);
        payDelBtn.setOnClickListener(this);

        delNumLin = (LinearLayout) view.findViewById(R.id.car_del_num_liny);
        delNumTv = (TextView) view.findViewById(R.id.car_del_num_tv);

//        allCheckBox = (CheckBox) view.findViewById(R.id.car_all_checkbox);
//        allCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                //获取到点击店铺check的商品 按钮数量
//                int storenum = Integer.parseInt(delNumTv.getText().toString());
//                if(isChecked){
//                    AppClient.storeMap.clear();
//                    AppClient.goodsMap.clear();
//                    storesimpAdapter.selectStoreAll();
//                    delNumTv.setText(getTotalItem()+"");
//                    totalTv.setText("¥"+car.getGrandTotal());
//                }else{
//                    storesimpAdapter.initStoreDate();
//                    delNumTv.setText("0");
//                    totalTv.setText("¥0.00");
//                }
////                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10006,"car"));
//            }
//        });
        couponsRecyclerView = (RecyclerView) view.findViewById(R.id.main_car_coupons_recyclerv);
        couponsRecyclerView.setLayoutManager(new LinearLayoutManager(couponsRecyclerView.getContext()));
        couponsRecyclerView.setItemAnimator(new DefaultItemAnimator());



        recyclerView = (RecyclerView) view.findViewById(R.id.main_car_recyclerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        car = (CarList) msg.obj;
                        if(car.getShoppingList().size()<=0){
                            if(null != recyclerView)
                                recyclerView.setAdapter(null);
                            if(null != couponsRecyclerView)
                                couponsRecyclerView.setAdapter(null);
                            carContentLin.setVisibility(View.GONE);
                            editDelTv.setVisibility(View.GONE);
                            allYhRel.setVisibility(View.VISIBLE);
                            delNumLin.setVisibility(View.GONE);
                            totalTv.setText("¥0.00");
                            AppClient.goodsMap.clear();
                            AppClient.storeMap.clear();
                            MainFragmentActivity.getInstance().setBadgeNum(0);
                            SXUtils.DialogDismiss();
                            AppClient.carSKUNumMap.clear();
                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refmapcar"));
                            break;
                        }

                        MainFragmentActivity.badge1.show();
                        String grandTotal = car.getGrandTotal();
                        String discount = car.getDiscountAmount();
//                        if(car.getShoppingList().size() >=10){
//                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
//                        }else{
                        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
//                        }
//                        if(indexPage == 0){
                        shopList = new ArrayList<>();
                        if(car.getShoppingList() != null && car.getShoppingList().size()>0){
                            shopList.addAll(car.getShoppingList());
                        }
//                            MainFragmentActivity.totalCarNum = 0;
                        AppClient.carNumber = shopList.get(0).getShoppingCartLines().size();
//                            MainFragmentActivity.totalCarNum = getCarTotalItem();
                        if(shopList.size()>0){
                            carContentLin.setVisibility(View.VISIBLE);
                            editDelTv.setVisibility(View.VISIBLE);
                        }else {
                            carContentLin.setVisibility(View.GONE);
                            editDelTv.setVisibility(View.GONE);
                        }
                        AppClient.carSKUNumMap.clear();
                        for(int i=0;i<shopList.get(0).getShoppingCartLines().size();i++){
                            AppClient.carSKUNumMap.put(shopList.get(0).getShoppingCartLines().get(i).getSkuBarcode(),shopList.get(0).getShoppingCartLines().get(i).getQuantity());
                        }
                        //刷新商品列表
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refmapcar"));
                        storesimpAdapter = new CarStoreRecyclerViewAdapter(getActivity(), shopList, delNumTv,car);
                        recyclerView.setAdapter(storesimpAdapter);
                        if(AppClient.isDelCarGoods){
                            allYhRel.setVisibility(View.GONE);
                            delNumLin.setVisibility(View.VISIBLE);
                            editDelTv.setText("完成");
                            payDelBtn.setText("删除");
                            payDelBtn.setEnabled(true);
                            payDelBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                        }else{
                            allYhRel.setVisibility(View.VISIBLE);
                            delNumLin.setVisibility(View.GONE);
                            payDelBtn.setText("结算");
                            editDelTv.setText("编辑");
                            if(car.getOrderFulfilment().equals("0")){
                                payDelBtn.setEnabled(false);
                                payDelBtn.setBackgroundColor(getResources().getColor(R.color.car_false_btn));
                            }else{
                                payDelBtn.setEnabled(true);
                                payDelBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                            }
                        }
//
//                        if(AppClient.isDelCarGoods){
//                            allYhRel.setVisibility(View.VISIBLE);
//                            delNumLin.setVisibility(View.GONE);
//                            payDelBtn.setText("结算");
//                            editDelTv.setText("编辑");
//                            AppClient.isDelCarGoods = false;
//                        }else{
//                            allYhRel.setVisibility(View.GONE);
//                            delNumLin.setVisibility(View.VISIBLE);
//                            editDelTv.setText("完成");
//                            payDelBtn.setText("删除");
//                            AppClient.isDelCarGoods = true;
//                        }

                        //多优惠选择显示，两个都有显示两个否则有就显示有的
                        List<TakeNoPartInActivitiesEntity> noticList = car.getTakeNoPartInActivities();
                        List<TakeNoPartInActivitiesEntity> partinTake = car.getTakePartInActivities();
                        if(noticList !=null && noticList.size() >0 && partinTake != null && partinTake.size()>0){
                            noticList.addAll(partinTake);
                            couponsAdapter = new CarCouponsRecyclerViewAdapter(getActivity(), noticList);
                            couponsRecyclerView.setAdapter(couponsAdapter);
                        }else if(noticList !=null && noticList.size() >0) {
                            couponsAdapter = new CarCouponsRecyclerViewAdapter(getActivity(), noticList);
                            couponsRecyclerView.setAdapter(couponsAdapter);
                        }else if(partinTake != null && partinTake.size()>0){
                            couponsAdapter = new CarCouponsRecyclerViewAdapter(getActivity(), partinTake);
                            couponsRecyclerView.setAdapter(couponsAdapter);
                        }else{
                            couponsRecyclerView.setAdapter(null);
                        }
//                                storesimpAdapter.setItmeList(getActivity(), shopList, delNumTv);
//                                storesimpAdapter.notifyDataSetChanged();
//                        }else{
//                            if(car.getShoppingList() != null && car.getShoppingList().size()>0){
//                                shopList.addAll(car.getShoppingList());
//                            }
////                            MainFragmentActivity.totalCarNum = getCarTotalItem();
//                            if(storesimpAdapter != null)
//                                storesimpAdapter.notifyDataSetChanged();
//                        }
                        //获取购物车商品数量
                        MainFragmentActivity.getInstance().setBadgeNum(getCarTotalItem());
//                        if(storesimpAdapter.simpAdapter != null){
//                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10006,"car"));
//                            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10007,getCarTotalMoney()+""));
//                            MainFragmentActivity.totalCarPrice = getCarTotalMoney();
//                        }else{
//                            float  totalPrice = Float.parseFloat(grandTotal)- Float.parseFloat(discount);
                        totalTv.setText("¥"+grandTotal);
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10007,grandTotal+""));
                        MainFragmentActivity.totalCarPrice = grandTotal+"";
//                        }
                        //刷新调用web方法
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100021,"webcar"));
                        badge1.setVisibility(View.VISIBLE);
                        badge1.setTextColor(Color.WHITE); // 文本颜色
                        badge1.setBadgeBackgroundColor(getResources().getColor(R.color.orange)); // 提醒信息的背景颜色，自己设置
                        Logs.i("11111=============car==",MainFragmentActivity.totalCarNum+"============");
//                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10007,getCarTotalMoney()+""));//修改商品详情金额数量
                        break;
                    case 1001:
                        totalTv.setText("¥ 00.00");
                        carContentLin.setVisibility(View.GONE);
                        editDelTv.setVisibility(View.GONE);
                        String clearcar = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(clearcar+"");
                        //清除购物车成功
                        recyclerView.setAdapter(null);
                        allYhRel.setVisibility(View.VISIBLE);
                        delNumLin.setVisibility(View.GONE);
                        payDelBtn.setText("结算");
                        editDelTv.setText("编辑");
                        AppClient.isDelCarGoods = false;
                        MainFragmentActivity.getInstance().setBadgeNum(0);
                        MainFragmentActivity.totalCarPrice="0.00";
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10007,0.00+""));
                        AppClient.carSKUNumMap.clear();
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refmapcar"));
                        storesimpAdapter.notifyDataSetChanged();
                        break;
                    case 1003:
//                        FromOrderEntity fromOrder = (FromOrderEntity) msg.obj;
                        //订单结算成功返回
//                        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
//                        list.add(cgInfo.getPurchaseLineVos());
//                        bundle.putParcelableArrayList("PurchaseList",list);
//                        bundle.putParcelable("orderList", cgInfo);
//                        intent.putExtras(bundle);
//                        Intent pay = new Intent(activity,GoPayActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("skucode",sku);
//                        pay.putExtras(bundle);
//                        startActivity(pay);
//                        bundle.putParcelable("fromOrder",fromOrder);
//                        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
//                        list.add(fromOrder.getOrderLines());
//                        bundle.putParcelableArrayList("orderLine",list);
//
//                        ArrayList couponslist = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
//                        couponslist.add(fromOrder.getOrderCoupons());
//                        bundle.putParcelableArrayList("coupsons",couponslist);
//
//
//                        ArrayList payTypelist = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
//                        payTypelist.add(fromOrder.getSettlementModes());
//                        bundle.putParcelableArrayList("payType",payTypelist);

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
                Logs.i("=====","====");
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.car_edit_del:
                if(AppClient.isDelCarGoods){
                    allYhRel.setVisibility(View.VISIBLE);
                    delNumLin.setVisibility(View.GONE);
                    payDelBtn.setText("结算");
                    editDelTv.setText("编辑");
                    AppClient.isDelCarGoods = false;
                    if(car.getOrderFulfilment().equals("0")){
                        payDelBtn.setEnabled(false);
                        payDelBtn.setBackgroundColor(getResources().getColor(R.color.car_false_btn));
                    }else{
                        payDelBtn.setEnabled(true);
                        payDelBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                    }
                    storesimpAdapter.notifyDataSetChanged();
                }else{
                    allYhRel.setVisibility(View.GONE);
                    delNumLin.setVisibility(View.VISIBLE);
                    editDelTv.setText("完成");
                    payDelBtn.setText("删除");
                    AppClient.isDelCarGoods = true;
                    payDelBtn.setEnabled(true);
                    payDelBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                    storesimpAdapter.initStoreDate();
                }
                storesimpAdapter.notifyDataSetChanged();
//                totalTv.setText("¥ 00.00");
//                allCheckBox.setChecked(false);

                break;
            case R.id.car_pay_del_btn:
                if(AppClient.isDelCarGoods) {
                    final  String  skuCodeS = getCarTotalStrSkucode();
                    if(TextUtils.isEmpty(skuCodeS)){
                        SXUtils.getInstance(activity).ToastCenter("请选择要删除的商品");
                    }else{
                        SXUtils.getInstance(activity).MyDialogView(activity,"温馨提示!", "确定删除选中的商品吗?", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SXUtils.getInstance(activity).tipDialog.dismiss();
                                SXUtils.showMyProgressDialog(activity,true);
                                if(storesimpAdapter.simpAdapter.getAllItem()) {
                                    AppClient.isDelCarGoods = false;
                                    clearCarList();
                                }else{
                                    AppClient.isDelCarGoods = true;
                                    SXUtils.getInstance(activity).AddOrUpdateCar(skuCodeS,"0");
                                    AppClient.goodsMap.clear();
                                    AppClient.storeMap.clear();
                                }
                            }
                        });
                    }
                }else{
                    String suk=  getCarTotalStrSkucode();
                    if(!TextUtils.isEmpty(suk)) {
                        Intent pay = new Intent(activity,GoPayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("skuCode",suk);
                        pay.putExtras(bundle);
                        startActivity(pay);
//                        getFromOrder(suk);
                    }else{
                        SXUtils.getInstance(activity).ToastCenter("请选择购买商品");
                    }
                }
                break;
            case R.id.car_go_shop_lin:
                MainFragmentActivity.goodsRb.setChecked(true);
                break;
        }
    }
    /**
     * 获取购物车列表
     */
    public void GetCarList() {
        HttpParams params = new HttpParams();
        params.put("pageSize","50");
        params.put("pageIndex","0");
        HttpUtils.getInstance(activity).requestPost(false,AppClient.CARLIST, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {

                Logs.i("购物车成功返回参数=======",jsonObject.toString());
                AppClient.CarJSONInfo = jsonObject.toString();
                CarList car = (CarList) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),CarList.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = car;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                AppClient.CarJSONInfo = "";
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);

            }
        });
    }
    /**
     * 清空购物车
     */
    public void clearCarList() {
        HttpUtils.getInstance(activity).requestPost(false,AppClient.CLEARCAR, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                AppClient.CarJSONInfo = "";
                Logs.i("清空购物车成功返回参数=======",jsonObject.toString());
                AppClient.goodsMap.clear();
                AppClient.storeMap.clear();
                JSONObject jsonObject1 = null;
                Message msg = new Message();
                msg.what = 1001;
                msg.obj = "删除商品成功";
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
    private String tagAddSub;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        //传递1or2 登录成功刷新购物车 和点击加入购物车商品 都刷新购物车||订单提交成功刷新购物车
        if(messageEvent.getTag()==1 || messageEvent.getTag()==2){
            tagAddSub = messageEvent.getMessage();
            GetCarList();
        }else if(messageEvent.getTag() == AppClient.EVENT10006){
            //购物车列表 点击商品checkBox调用改变购物车总价格
            totalTv.setText("¥"+MainFragmentActivity.totalCarPrice);
        }else if(messageEvent.getTag() == 4444){
            storesimpAdapter.removeAllData();
            MainFragmentActivity.getInstance().setBadgeNum(0);
        }else if(messageEvent.getTag() == AppClient.EVENT100024){

        }
//        else if(messageEvent.getTag() == AppClient.EVENT10005){
//            if(messageEvent.getMessage().equals("1")){
//                allCheckBox.setChecked(true);
//            }else{
//                allCheckBox.setChecked(false);
//            }
//        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 获取被选中的商品价格及
     * @return
     */
    public String getCarTotalMoney(){
        float priceTotal = 0;
        Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
        for(int i=0;i<shopList.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = AppClient.goodsMap.get(key);
                if(value){
                    int postions = Integer.parseInt(key);
                    priceTotal += parseFloat(shopList.get(i).getShoppingCartLines().get(postions).getSkuPrice())* parseFloat(shopList.get(i).getShoppingCartLines().get(postions).getQuantity());
                }
            }
        }
        return SXUtils.getInstance(activity).getFloatPrice(priceTotal)+"";
    }
    /**
     * 获取选中商品skucode 用于结算订单
     * @return
     */
    public String getCarTotalStrSkucode(){
        if(null == shopList || shopList.size()<=0) {
            return "";
        }
        if(!AppClient.isDelCarGoods){
            String skuCodestr = "";
            for (int i = 0; i <shopList.get(0).getShoppingCartLines().size(); i++) {
                if (shopList.get(0).getShoppingCartLines().get(i).getIsChecked().equals("1")) {
                    skuCodestr += shopList.get(0).getShoppingCartLines().get(i).getSkuBarcode() + ",";
                }
            }
            if (TextUtils.isEmpty(skuCodestr) || skuCodestr.equals(","))
                return "";
            return skuCodestr.substring(0, skuCodestr.length() - 1) + "";
        }else {
            String skuCodestr = "";
            Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
            Logs.i("++++++++++",AppClient.goodsMap.size()+"");
            for(int i=0;i<shopList.size();i++){
                while (iter.hasNext()) {
                    String key = iter.next();
                    Boolean value = AppClient.goodsMap.get(key);
                    if(value){
                        int postions = Integer.parseInt(key);
                        skuCodestr += shopList.get(i).getShoppingCartLines().get(postions).getSkuBarcode()+",";
                    }
                }
            }
            if(TextUtils.isEmpty(skuCodestr) || skuCodestr.equals(","))
                return "";
            return skuCodestr.substring(0,skuCodestr.length()-1)+"";
        }
    }
    /**
     * 获取被选中的商品skucode 用于删除单选商品
     * @return
     */
    public List<String> getCarTotalSkucode(){
        List<String> skuList = new ArrayList<>();
        float priceTotal = 0;
        Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
        for(int i=0;i<shopList.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = AppClient.goodsMap.get(key);
                if(value){
                    int postions = Integer.parseInt(key);
                    skuList.add(shopList.get(i).getShoppingCartLines().get(postions).getSkuBarcode());
//                    priceTotal += Float.parseFloat(shopList.get(i).getShoppingCartLines().get(postions).getSkuPrice());
                }
            }
        }
        return skuList;
    }
    /**
     * 得到所有商品加入购物车数量
     * @return
     */
    public int getCarTotalItem(){
        int carItem = 0;
        for (int i = 0; i < shopList.size(); i++) {
            for (int j = 0; j < shopList.get(i).getShoppingCartLines().size(); j++) {
                carItem += Integer.parseInt(shopList.get(i).getShoppingCartLines().get(j).getQuantity());
            }
        }
        return carItem;
    }
    /**
     * 得到所有商品总条数
     * @return
     */
    public int getTotalItem(){
        int carItem = 0;
        for (int i = 0; i < shopList.size(); i++) {
            carItem += shopList.get(i).getShoppingCartLines().size();
        }
        return carItem;
    }
}
























