package com.bixian365.dzc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.ScanPartnerConfirmEntity;
import com.bixian365.dzc.entity.ScanPartnerOrderLinesEntity;
import com.bixian365.dzc.fragment.my.store.order.OrderDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 合伙人扫描订单列表
 */
public  class PartnerScanRecyclerViewAdapter
        extends RecyclerView.Adapter<PartnerScanRecyclerViewAdapter.ViewHolder> {
    public Map<String,Boolean> couponsMap = new HashMap<String ,Boolean>();
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ArrayList<ScanPartnerConfirmEntity>  mValues;
    private Context context;
    private int tag;//标示订单类型进入显示不同按钮
    private ScanPartnerOrderLinesEntity scanPartner;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView order;
        public final TextView name;
        public final CheckBox  checkbox;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            order = (TextView) mView.findViewById(R.id.partner_scan_orderno_item_tv);
            name = (TextView) mView.findViewById(R.id.partner_scan_name_item_tv);
            checkbox = (CheckBox) mView.findViewById(R.id.partner_scan_item_checkbox);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public PartnerScanRecyclerViewAdapter(Context context, ArrayList<ScanPartnerConfirmEntity> items,ScanPartnerOrderLinesEntity scanPartner) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.scanPartner = scanPartner;
        initStoreDate();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partner_scan_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      final  ScanPartnerConfirmEntity  couponInfo = mValues.get(position);
        holder.order.setText("订单号："+couponInfo.getOrderNo()+"");
        holder.name.setText("买家名称："+couponInfo.getShopUserName()+"");
//        holder.price.setText(couponInfo.getCouponMoney()+"");
//        holder.des.setText(couponInfo.getCouponTerm());
//        holder.time.setText(couponInfo.getCouponTime());
        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(couponsMap.get(position+""));
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(couponInfo.getIsExclusive().equals("1")){
//                    if(!getCouponsGetUse()){
//                        SXUtils.getInstance(context).ToastCenter("此优惠券不能与其他优惠券同时使用");
//                        return;
//                    }
//                }
                couponsMap.put(position+"",isChecked);
                notifyDataSetChanged();
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderNo",couponInfo.getOrderNo());
                bundle.putString("tradeNo",couponInfo.getShopUserNo());
//                bundle.putString("orderAddress",orderInfo.getOrderAddress());
//                bundle.putString("orderTime",orderInfo.getOrderTime());
                bundle.putString("name",couponInfo.getShopUserName());
                bundle.putString("total","");
                intent.putExtra("orderTag","666");
                intent.putExtras(bundle);
                context.startActivity(intent);



//                Intent intent = new Intent(context, PartnerDetailInfoActivity.class);
//                intent.putExtra("no",couponInfo.getOrderNo());
//                intent.putExtra("time",scanPartner.getOutTime());
//                intent.putExtra("sendName",scanPartner.getPartnerUserName());
//                intent.putExtra("carNum",scanPartner.getVehicleNo());
//                intent.putExtra("driver",scanPartner.getDriverName());
//                intent.putExtra("shopName",couponInfo.getShopUserName());
//                intent.putExtra("phone",scanPartner.getDriverPhone());
//                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    @Override
    public int getItemViewType(int position) {
        Log.i("========",position+"");
        return super.getItemViewType(position);
    }
    /**
     * 初始化
     */
    public void initStoreDate() {
        for (int i = 0; i < mValues.size(); i++) {
            couponsMap.put(""+i,true);
        }
        notifyDataSetChanged();

    }
    /**
     * 初始化
     */
    public void initStoreDateFalse() {
        for (int i = 0; i < mValues.size(); i++) {
            couponsMap.put(""+i,false);
        }
        notifyDataSetChanged();

    }
    /**
     * 获取选中商品OrderNo
     * @return
     */
    public String getScanOrderNo(){
        String skuCodestr = "";
        Iterator<String> iter = couponsMap.keySet().iterator();
        for(int i=0;i<mValues.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = couponsMap.get(key);
                if(value){
                    int postions = Integer.parseInt(key);
                    skuCodestr += mValues.get(postions).getOrderNo()+",";
                }
            }
        }
        if(TextUtils.isEmpty(skuCodestr) || skuCodestr.equals(","))
            return "";
        return skuCodestr.substring(0,skuCodestr.length()-1)+"";
    }
    /**
     * 判断是否全部选中
     */
    public boolean getIsAllCheck(){
        Iterator<String> iter = couponsMap.keySet().iterator();
        for(int i=0;i<mValues.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = couponsMap.get(key);
                if(value){
                    return false;
                }
            }
        }
        return true;

    }
//    /**
//     * 获得选中优惠券的抵扣金额
//     * @return
//     */
//    public float getCouponsTotalPrice(){
//        float totalPrice = 0;
//        Iterator<String> iter = couponsMap.keySet().iterator();
//        for(int i=0;i<mValues.size();i++){
//            while (iter.hasNext()) {
//                String key = iter.next();
//                Boolean value = couponsMap.get(key);
//                if(value){
//                    int postions = Integer.parseInt(key);
//                    totalPrice += Float.parseFloat(mValues.get(postions).getCouponMoney());
//                }
//            }
//        }
//        return totalPrice;
//    }
}