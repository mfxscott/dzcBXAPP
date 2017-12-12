package com.bixian365.dzc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.car.OrderLinesEntity;
import com.bixian365.dzc.utils.SXUtils;

import java.util.List;

/**
 * 提交订单时查看订单列表详细商品信息
 */
public  class PayOrderDetailRecyclerViewAdapter
        extends RecyclerView.Adapter<PayOrderDetailRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<OrderLinesEntity> mValues;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final TextView price;
        public final TextView num;
        public final TextView name;
        public final TextView total;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.pay_order_detail_item_img);
            price = (TextView) view.findViewById(R.id.pay_order_detail_item_pricetv);
            num = (TextView) view.findViewById(R.id.pay_order_detail_item_numtv);
            name = (TextView) view.findViewById(R.id.pay_order_detail_item_nametv);
            total = (TextView) view.findViewById(R.id.pay_order_detail_item_totaltv);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public PayOrderDetailRecyclerViewAdapter(Context context, List<OrderLinesEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pay_order_detail_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final  OrderLinesEntity orderlins = mValues.get(position);
        holder.name.setText(orderlins.getGoodsName());
        holder.num.setText("数量："+orderlins.getQuantity());
        holder.price.setText("单价："+"¥"+orderlins.getSkuPrice()+"/"+orderlins.getGoodsModel());
        holder.total.setText("¥"+orderlins.getSubTotal());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",orderlins.getGoodsCode());
//                holder.mView.getContext().startActivity(intent);
            }
        });
        SXUtils.getInstance(context).GlideSetImg(orderlins.getGoodsImage(),holder.mImageView);
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