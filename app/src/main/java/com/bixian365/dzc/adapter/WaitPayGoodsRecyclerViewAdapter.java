package com.bixian365.dzc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.entity.orderlist.OrderGoodsInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import java.util.List;

/**
 * 订单列表中的商品信息
 */
public  class WaitPayGoodsRecyclerViewAdapter
        extends RecyclerView.Adapter<WaitPayGoodsRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<OrderGoodsInfoEntity> mValues;
    private Context context;
    private int tag=0;//标示订单类型进入显示不同按钮 有参数标识从订单详情显示，由于服务端返回字段名字不一样导致必须要重新set 参数
    private View.OnClickListener ClickListener = null;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final TextView marketPrice;
        public final TextView skuPrice;
        public final  TextView skuTotal;
        public final TextView   num;
        public final TextView unit;
        public final  TextView name;
        public final  TextView  modelPrice;
        public final TextView model;


        public ViewHolder(View view) {
            super(view);
            mView = view;

            mImageView = (ImageView) view.findViewById(R.id.order_wait_pay_item_iv);
            marketPrice = (TextView) view.findViewById(R.id.order_wait_pay_item_mp_tv);
            skuPrice = (TextView) view.findViewById(R.id.order_wait_pay_goods_skuprice_tv);
            unit = (TextView) view.findViewById(R.id.order_wait_pay_goods_unit_tv);
            skuTotal = (TextView) view.findViewById(R.id.order_wait_pay_goods_total_tv);
            num = (TextView) view.findViewById(R.id.order_wait_pay_num_tv);
            name = (TextView) view.findViewById(R.id.order_wait_pay_goods_name_tv);
            modelPrice = (TextView) view.findViewById(R.id.order_wait_pay_goods_model_price_tv);
            model = (TextView) view.findViewById(R.id.order_wait_pay_goods_model_tv);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public WaitPayGoodsRecyclerViewAdapter(Context context, List<OrderGoodsInfoEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.tag = tag;
    }
    public WaitPayGoodsRecyclerViewAdapter(Context context, List<OrderGoodsInfoEntity> items,int tag) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.tag = tag;
    }
    public WaitPayGoodsRecyclerViewAdapter(Context context, List<OrderGoodsInfoEntity> items, View.OnClickListener onClickListener) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.ClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_wait_pay_goods_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      final   OrderGoodsInfoEntity orderInfo = mValues.get(position);
        if(tag ==1){
            SXUtils.getInstance(context).GlideSetImg(orderInfo.getImageSrc(),holder.mImageView);
            holder.name.setText(orderInfo.getSkuName());
            holder.skuPrice.setText("¥ "+orderInfo.getSkuPrice());
            holder.model.setText("/"+orderInfo.getGoodsModel());
            holder.modelPrice.setText("¥ "+orderInfo.getSkuPrice());
            holder.marketPrice.setText("¥ "+orderInfo.getOriginSkuPrice()+"/"+orderInfo.getGoodsModel());
            holder.num.setText("x "+orderInfo.getSkuNumber()+"/"+orderInfo.getGoodsModel());
            holder.skuTotal.setText("¥ "+orderInfo.getTotalAmount()+"");
        }else {
//合伙人订单类别
            SXUtils.getInstance(context).GlideSetImg(orderInfo.getSkuImage(), holder.mImageView);
            holder.skuPrice.setText("¥ "+orderInfo.getSkuPrice());
            holder.model.setText("/"+orderInfo.getGoodsUnit());
            holder.num.setText("x "+orderInfo.getSkuNumber()+"/"+orderInfo.getGoodsUnit());
            holder.name.setText(orderInfo.getSkuName());
            holder.unit.setText("/"+orderInfo.getGoodsUnit());
            holder.modelPrice.setText("¥"+orderInfo.getSkuPrice());
            holder.skuTotal.setText("¥"+orderInfo.getTotalAmount()+"");
            holder.marketPrice.setText("¥"+orderInfo.getOriginSkuPrice()+"/"+orderInfo.getGoodsUnit());
        }
        holder.marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );

       if(ClickListener != null )
        holder.mView.setOnClickListener(ClickListener);
       else
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppClient.USERROLETAG.equals("32")||AppClient.USERROLETAG.equals("64")) {
                    Intent intent = new Intent(holder.mView.getContext(), GoodsDetailActivity.class);
                    intent.putExtra("cno", orderInfo.getGoodsId());
                    holder.mView.getContext().startActivity(intent);
                }
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

}