package com.bixian365.dzc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.cgListInfo.CGPurchaseLinesEntity;
import com.bixian365.dzc.utils.SXUtils;

import java.util.List;

/**
 * 采购订单详情
 * @author mfx
 */
public class CGOrderDetailRecyclerViewAdapter
        extends RecyclerView.Adapter<CGOrderDetailRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();

    private int mBackground;
    private List<CGPurchaseLinesEntity> mValues=null;
    private Context context;



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
      public ImageView itemImg;//
        public TextView name; //商品名称商品图片
        public TextView num;//数量
        public TextView shopPrice;//商铺售价
        public TextView totalPrice;//商品小计

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemImg = (ImageView) mView.findViewById(R.id.cg_order_detail_iv);
            name = (TextView) mView.findViewById(R.id.cg_order_goodsname_tv);
            num = (TextView) mView.findViewById(R.id.cg_order_detail_num_tv);
            shopPrice = (TextView) mView.findViewById(R.id.cg_order_detail_shopprice_tv);
            totalPrice = (TextView) mView.findViewById(R.id.cg_order_detail_total_tv);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public CGOrderDetailRecyclerViewAdapter(Context context, List<CGPurchaseLinesEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gc_order_detail_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CGPurchaseLinesEntity gcpurchase = mValues.get(position);
        holder.name.setText(gcpurchase.getGoodsName());
        holder.num.setText("数量："+gcpurchase.getGoodsNumber()+"/"+gcpurchase.getGoodsUnit());
        holder.shopPrice.setText("单价："+"¥"+gcpurchase.getGoodsPrice()+"");
        holder.totalPrice.setText("¥"+gcpurchase.getTotalAmount()+"");
        SXUtils.getInstance(context).GlideSetImg(gcpurchase.getThumbImg(),holder.itemImg);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",gcpurchase.getId());
//                holder.mView.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}