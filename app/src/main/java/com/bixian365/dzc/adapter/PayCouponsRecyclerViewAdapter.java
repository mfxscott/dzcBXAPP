package com.bixian365.dzc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.car.OrderCouponsEntity;
import com.bixian365.dzc.utils.SXUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 提交订单选择优惠券列表
 */
public  class PayCouponsRecyclerViewAdapter
        extends RecyclerView.Adapter<PayCouponsRecyclerViewAdapter.ViewHolder> {
    public Map<String,Boolean> couponsMap = new HashMap<String ,Boolean>();
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private ArrayList<OrderCouponsEntity>  mValues;
    private Context context;
    private int tag;//标示订单类型进入显示不同按钮

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView price;
        public final TextView des;
        public final TextView time;
        //        public final TextView  hs;
        public final  RelativeLayout rel;
        public final CheckBox  checkbox;
        public final  TextView  bkyTv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            rel = (RelativeLayout) mView.findViewById(R.id.pay_coupons_item_bg_rel);
            price = (TextView) mView.findViewById(R.id.pay_coupons_item_price_tv);
            des = (TextView) mView.findViewById(R.id.pay_coupons_item_des_tv);
            time = (TextView) mView.findViewById(R.id.pay_coupons_item_time_tv);
//            hs = (TextView) mView.findViewById(R.id.pay_coupons_item_hs_tv);
            checkbox = (CheckBox) mView.findViewById(R.id.pay_coupons_item_checkbox);
            bkyTv =(TextView) mView.findViewById(R.id.yhj_order_item_bky_tv);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public PayCouponsRecyclerViewAdapter(Context context, ArrayList<OrderCouponsEntity> items, int tag) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.tag = tag;
        initStoreDate();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gopay_check_coupons_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final  OrderCouponsEntity  couponInfo = mValues.get(position);
        holder.price.setText(couponInfo.getCouponMoney()+"");
        holder.des.setText(couponInfo.getCouponTerm());
        holder.time.setText(couponInfo.getCouponTime());

        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(couponsMap.get(position+""));
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(couponInfo.getIsExclusive().equals("1")){
                    if(!getCouponsGetUse()){
                        SXUtils.getInstance(context).ToastCenter("此优惠券不能与其他优惠券同时使用");
                        return;
                    }
                }
                couponsMap.put(position+"",isChecked);
                notifyDataSetChanged();
            }
        });
        if(couponInfo.getIsUsable().equals("0")){
            //isUsable 为 0 的优惠券要使用灰色的图片，并且不能选择，
            holder.rel.setBackgroundResource(R.mipmap.yhj_nouse_img);
            holder.bkyTv.setText("不可用");
            holder.bkyTv.setVisibility(View.VISIBLE);
            holder.checkbox.setVisibility(View.GONE);
        }else{
            holder.rel.setBackgroundResource(R.mipmap.pay_order_coupons);
            holder.bkyTv.setText("");
            holder.bkyTv.setVisibility(View.GONE);
            holder.checkbox.setVisibility(View.VISIBLE);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkbox.isChecked()) {
                    couponsMap.put(position + "", false);
                }
                else {
                    if(!couponInfo.getIsUsable().equals("0")) {
                        couponsMap.put(position + "", true);
                    }else{
                        SXUtils.getInstance(context).ToastCenter("不可用优惠劵");
                    }
                }
                notifyDataSetChanged();
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
            couponsMap.put(""+i,false);
        }
        notifyDataSetChanged();

    }
    /**
     * 获取选中商品skucode 用于结算订单
     * @return
     */
    public String getCouponsStrCouponNo(){
        String skuCodestr = "";
        Iterator<String> iter = couponsMap.keySet().iterator();
        for(int i=0;i<mValues.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = couponsMap.get(key);
                if(value){
                    int postions = Integer.parseInt(key);
                    skuCodestr += mValues.get(postions).getCouponNo()+",";
                }
            }
        }
        if(TextUtils.isEmpty(skuCodestr) || skuCodestr.equals(","))
            return "";
        return skuCodestr.substring(0,skuCodestr.length()-1)+"";
    }

    /**
     * 判断是否有选中其他优惠券
     * 由于判断某些优惠券不可叠加使用
     * @return  false  表示已选择其他优惠券  true 未选择其他优惠
     */
    public boolean getCouponsGetUse(){
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
    /**
     * 获得选中优惠券的抵扣金额
     * @return
     */
    public float getCouponsTotalPrice(){
        float totalPrice = 0;
        Iterator<String> iter = couponsMap.keySet().iterator();
        for(int i=0;i<mValues.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = couponsMap.get(key);
                if(value){
                    int postions = Integer.parseInt(key);
                    totalPrice += Float.parseFloat(mValues.get(postions).getCouponMoney());
                }
            }
        }
        return totalPrice;
    }
}