package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.car.ShoppingCartLinesEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用清单列表
 * @author mfx
 */
public  class PadCarGoodsListRecyclerViewAdapter
        extends RecyclerView.Adapter<PadCarGoodsListRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<ShoppingCartLinesEntity> mValues = new ArrayList<>();
    private Activity activity;
    public int mSelect= -1;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView xh;
        public final TextView goodsNameTv;
        public final  TextView numberTv;
        public final  TextView  unitTv;
        public final  TextView  priceTv;
        public final  TextView  totalPriceTv;
        public final LinearLayout  carLiny;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            xh = (TextView) view.findViewById(R.id.car_list_item_xh_tv);
            goodsNameTv = (TextView) view.findViewById(R.id.car_list_item_goodsname_tv);
            numberTv = (TextView) view.findViewById(R.id.car_list_item_number_tv);
            unitTv = (TextView) view.findViewById(R.id.car_list_item_unit_tv);
            priceTv = (TextView) view.findViewById(R.id.car_list_item_price_tv);
            totalPriceTv = (TextView) view.findViewById(R.id.car_list_item_total_price_tv);
            carLiny = (LinearLayout) view.findViewById(R.id.pad_car_item_liny);

        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
    public PadCarGoodsListRecyclerViewAdapter(Activity context, List<ShoppingCartLinesEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pad_car_goods_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ShoppingCartLinesEntity  goods = mValues.get(position);
        holder.xh.setText(position+"");
        holder.goodsNameTv.setText(goods.getGoodsName()+"");
        holder.numberTv.setText(goods.getQuantity()+"");
        holder.unitTv.setText(goods.getGoodsModel()+"");
        holder.priceTv.setText(goods.getSkuPrice()+"元");
        holder.totalPriceTv.setText(Float.parseFloat(goods.getQuantity())*Float.parseFloat(goods.getSkuPrice())+"元");

        if(mSelect==position){
            holder.carLiny.setBackgroundResource(R.color.car_item_on_bg);  //选中项背景
        }else{
            holder.carLiny.setBackgroundResource(R.color.white);  //其他项背景
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelected(position);
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
    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }
    //  添加数据
    public void addData(int position) {
//      在list中添加数据，并通知条目加入一条
        notifyItemInserted(position);
    }
    //  删除数据
    public void removeData() {
        if(mSelect>-1) {
            mValues.remove(mSelect);
            notifyItemRemoved(mSelect);
            notifyDataSetChanged();
        }

    }

    /**
     *  修改购物车商品参数
     * @param isPrice 0 修改数量  1 修改价格
     * @param value
     */
    public void updateData(String isPrice,String value) {
        final ShoppingCartLinesEntity  goods = mValues.get(mSelect);
        if(isPrice.equals("0")){
            goods.setSkuPrice(value+"");
        }else{
            goods.setQuantity(value+"");
        }
        notifyDataSetChanged();

    }

}