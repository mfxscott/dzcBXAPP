package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.MyFoodActionCallback;

import java.util.List;

/**
 * 常用清单列表
 * @author mfx
 * @time  2017/7/11 12:24
 */
public  class HomeBillRecyclerViewAdapter
        extends RecyclerView.Adapter<HomeBillRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    public List<GoodsInfoEntity> mValues;
    private FoodActionCallback callback;
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final ImageView delImageView;
        public final TextView mTextView;
        //        public final TextView addcar1,addcar2;
        public final RecyclerView recyclerView;
        public final TextView  marketPrice;
        public final TextView  shopPrice;
        public final TextView model;
        public final TextView selectTv;
        public final  ImageView  addImage;
        public final LinearLayout  moreLiny;
        public final TextView    infoS;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.main_bill_item_iv);
            mTextView = (TextView) view.findViewById(R.id.main_bill_item_name);
            delImageView = (ImageView) view.findViewById(R.id.main_bill_item_del_iv);
//            addcar1 = (TextView) view.findViewById(R.id.main_bill_addcar_tv);
//            addcar2 = (TextView) view.findViewById(R.id.main_bill_addcar_tv2);
            recyclerView = (RecyclerView) view.findViewById(R.id.bill_item_recycler);
            marketPrice = (TextView) view.findViewById(R.id.bill_item_market_tv);

            shopPrice = (TextView) view.findViewById(R.id.bill_item_shopprice_tv);
            model = (TextView) view.findViewById(R.id.bill_item_model_tv);
            selectTv = (TextView) view.findViewById(R.id.bill_item_info_select_mode);
            addImage = (ImageView) view.findViewById(R.id.bill_item_info_add);
            moreLiny = (LinearLayout) view.findViewById(R.id.bill_item_more_model_lin);
            infoS = (TextView) view.findViewById(R.id.bill_item_info_s);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }
    @Override
    public void onClick(View v) {
        if(callback==null) return;
        callback.addAction(v);
    }
    public HomeBillRecyclerViewAdapter(Context context, List<GoodsInfoEntity> items, FoodActionCallback callback) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.callback = callback;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_bill_item, parent, false);
        view.setBackgroundResource(mBackground);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GoodsInfoEntity categInfo = mValues.get(position);
        if(categInfo.getChirdren().size()>1){
            holder.addImage.setVisibility(View.GONE);
            holder.selectTv.setVisibility(View.VISIBLE);
            holder.infoS.setVisibility(View.VISIBLE);
        }else{
            holder.infoS.setVisibility(View.GONE);
            holder.selectTv.setVisibility(View.GONE);
            holder.addImage.setVisibility(View.VISIBLE);
        }

        holder.selectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.moreLiny.isShown()){
                    holder.selectTv.setText("收起");
                    holder.moreLiny.setVisibility(View.VISIBLE);
                }else{
                    holder.selectTv.setText("选规格");
                    holder.moreLiny.setVisibility(View.GONE);
                }
//                removeData(position);
            }
        });

        holder.model.setText("/"+categInfo.getChirdren().get(0).getGoodsModel());
        holder.shopPrice.setText(" ¥"+categInfo.getChirdren().get(0).getShopPrice());
        holder.mTextView.setText(categInfo.getGoodsName());
        holder.marketPrice.setText(" ¥"+categInfo.getChirdren().get(0).getMarketPrice()+"/"+categInfo.getChirdren().get(0).getGoodsModel());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        BillItemRecyclerViewAdapter simpAdapter = new BillItemRecyclerViewAdapter(holder.recyclerView.getContext(),categInfo.getGoodsUnit(),categInfo.getChirdren());
        holder.recyclerView.setAdapter(simpAdapter);




        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
                intent.putExtra("cno",categInfo.getId());
                holder.mView.getContext().startActivity(intent);
            }
        });
        if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID))
            holder.delImageView.setVisibility(View.GONE);
        holder.delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(context).MyDialogView(context,"温馨提示!", "是否删除"+categInfo.getGoodsName()+"?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SXUtils.getInstance(context).tipDialog.dismiss();
                        SXUtils.getInstance(holder.delImageView.getContext()).AddDelBill(0,categInfo.getGoodsCode(),null);
                    }
                });
            }
        });




        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    callback = new MyFoodActionCallback((Activity) context,mValues.get(position).getChirdren().get(0).getSkuBarcode());
                if(callback==null) return;
                callback.addAction(v);
            }
        });


        SXUtils.getInstance(context).GlideSetImg(categInfo.getOriginalImg(),holder.mImageView);
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
    //  添加数据
    public void addData(int position) {
//      在list中添加数据，并通知条目加入一条
        notifyItemInserted(position);
    }
    //  删除数据
    public void removeData(int position) {
        Logs.i("删除id==="+position);
        mValues.remove(position);
//        notifyItemRemoved(position);
//        notifyDataSetChanged();
        notifyItemRemoved(position);
        if (position != mValues.size()) {
            notifyItemRangeChanged(position, mValues.size() - position);
        }
    }
    private AdapterView.OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}