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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.orderlist.OrderInfoEntity;
import com.bixian365.dzc.fragment.my.store.order.OrderDetailActivity;

import java.util.List;

/**
 * 合伙人扫码确认收货
 */
public  class PartnerOrderScanRecyclerViewAdapter
        extends RecyclerView.Adapter<PartnerOrderScanRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<OrderInfoEntity> mValues;
    private Context context;
    private int tag;//标示订单类型进入显示不同按钮

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView  shopNameTv;
        public final TextView shopPriceTv;
        public final TextView  orderTotal;
        public final TextView marketPrice;
        public final TextView  num;
        public final ImageView  img;
        public final CheckBox checkBox;
        public final TextView model;
        public final TextView  unit;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            marketPrice = (TextView) view.findViewById(R.id.partner_scan_item_marketprice);
            shopNameTv = (TextView) view.findViewById(R.id.partner_scan_item_name);
            orderTotal = (TextView) view.findViewById(R.id.partner_scan_item_total);
            shopPriceTv = (TextView) view.findViewById(R.id.partner_scan_item_shopprice);
            num = (TextView) view.findViewById(R.id.partner_scan_item_num);
            img = (ImageView) view.findViewById(R.id.partner_scan_item_img);
            checkBox = (CheckBox) view.findViewById(R.id.partner_scan_item_checkbox);
            model = (TextView) view.findViewById(R.id.partner_scan_item_model);
            unit = (TextView) view.findViewById(R.id.partner_scan_item_unit);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public PartnerOrderScanRecyclerViewAdapter(Context context, List<OrderInfoEntity> items, int tag) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.tag = tag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_wait_pay_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       final OrderInfoEntity orderInfo = mValues.get(position);

        holder.shopNameTv.setText(orderInfo.getShopUserName()+"");
        holder.orderTotal.setText("¥ "+orderInfo.getGoodsTotalAmount());


        holder.marketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( holder.mView.getContext(), OrderDetailActivity.class);
                intent.putExtra("orderTag",tag+"");
                intent.putExtra("orderId","123456");
                holder.mView.getContext().startActivity(intent);
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