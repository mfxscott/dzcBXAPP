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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.MyFoodActionCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 首页商品分类列表。商品详细
 * @author mfx
 * @time  2017/7/10 20:47
 */
public  class TypeInfoRecyclerViewAdapter
        extends RecyclerView.Adapter<TypeInfoRecyclerViewAdapter.ViewHolder>{

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<GoodsInfoEntity> mValues;
    private FoodActionCallback callback;
    private Context context;
    private View searchView;
    private String tag;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final TextView modeTView;
        public final TextView mTextView;
        public  final LinearLayout  selectLin;
        public final ImageView typeadd1;
        //        public final ImageView addImage2;
        public final TextView  shopPrice;
        public final TextView  goodsUnit;
        public final TextView  goodsModel;
        public final TextView  marketPrice;
        public final RecyclerView recyclerView;
        public final LinearLayout  goodsLiny;
        public final TextView  lineTv;
        public final ImageView delImageView;
        public final TextView  carNum;
        public final ImageView subImage;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.type_info_item_iv);
            mTextView = (TextView) view.findViewById(R.id.type_info_item_name);
            modeTView = (TextView) view.findViewById(R.id.type_info_select_mode);
            selectLin = (LinearLayout) view.findViewById(R.id.type_goods_item_gg_liny);
            typeadd1 = (ImageView) view.findViewById(R.id.type_info_add);
//            addImage = (ImageView) view.findViewById(R.id.type_info_check1);
//            addImage2 = (ImageView) view.findViewById(R.id.type_info_check2);
            shopPrice = (TextView) view.findViewById(R.id.type_info_shop_price_tv);
            goodsUnit = (TextView) view.findViewById(R.id.type_info_item_goodsUnit_tv);
            goodsModel = (TextView) view.findViewById(R.id.type_info_item_goodsmodel_tv);
            marketPrice = (TextView) view.findViewById(R.id.type_info_item_market_price_tv);
            recyclerView = (RecyclerView) view.findViewById(R.id.type_info_item_recycler);
            goodsLiny = (LinearLayout) view.findViewById(R.id.type_info_item_frist_goods_lin);
            lineTv = (TextView) view.findViewById(R.id.bill_item_info_s);
            delImageView = (ImageView) view.findViewById(R.id.main_bill_item_del_iv);

            subImage = (ImageView) view.findViewById(R.id.type_info_sub_img);
            carNum = (TextView) view.findViewById(R.id.type_info_carnum_img);

        }
        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }
    public TypeInfoRecyclerViewAdapter(Context context, List<GoodsInfoEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
    }
    /**
     * 从搜索商品列表过来需要传递view
     * @param context
     * @param items
     * @param searchView
     */
    public TypeInfoRecyclerViewAdapter(Context context, List<GoodsInfoEntity> items,View searchView) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.searchView = searchView;
    }

    /**
     * 从用户清单过来显示 删除按钮
     * @param context
     * @param items
     * @param tag
     */
    public TypeInfoRecyclerViewAdapter(Context context, List<GoodsInfoEntity> items,String tag) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.tag = tag;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_type_info_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final  GoodsInfoEntity   goodsdetail = mValues.get(position);
//        if(goodsdetail.getSkuList() != null && goodsdetail.getSkuList().size()>0) {
//            Logs.i("多规格商品数量========="+goodsdetail.getSkuList().size());
//        holder.goodsModel.setText("/"+goodsdetail.getChirdren().get(0).getGoodsModel());
        holder.marketPrice.setText("¥"+goodsdetail.getChirdren().get(0).getMarketPrice()+"/"+goodsdetail.getChirdren().get(0).getGoodsModel()+"");
        holder.shopPrice.setText("¥"+goodsdetail.getChirdren().get(0).getShopPrice()+"");
        holder.mTextView.setText(goodsdetail.getGoodsName()+"");
        holder.goodsUnit.setText("/"+goodsdetail.getChirdren().get(0).getGoodsModel()+"");

        String skuNum = SXUtils.getInstance(context).CheckExistCar(goodsdetail.getChirdren().get(0).getSkuBarcode());
        if(TextUtils.isEmpty(skuNum)){
            holder.subImage.setVisibility(View.GONE);
            holder.carNum.setVisibility(View.GONE);
        }else{
            holder.subImage.setVisibility(View.VISIBLE);
            holder.carNum.setVisibility(View.VISIBLE);
            holder.carNum.setText(skuNum+"");
        }


        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        GoodsRecyclerViewAdapter simpAdapter;
        if(searchView != null){
            simpAdapter = new GoodsRecyclerViewAdapter(holder.recyclerView.getContext(), goodsdetail.getGoodsUnit(), goodsdetail.getChirdren(),searchView);
        }else {
            simpAdapter = new GoodsRecyclerViewAdapter(holder.recyclerView.getContext(), goodsdetail.getGoodsUnit(), goodsdetail.getChirdren());
        }
        holder.recyclerView.setAdapter(simpAdapter);
//        if(goodsdetail.getChirdren().size()>1){
//            holder.modeTView.setVisibility(View.VISIBLE);
//
//        }else{
//            holder.typeadd1.setVisibility(View.VISIBLE);
//
//        }
        if(goodsdetail.getChirdren().size()>1){
            holder.typeadd1.setVisibility(View.GONE);
            holder.modeTView.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(tag)){
                holder.lineTv.setVisibility(View.VISIBLE);
            }else{
                holder.lineTv.setVisibility(View.GONE);
            }
            holder.subImage.setVisibility(View.GONE);
            holder.carNum.setVisibility(View.GONE);

        }else{
            holder.typeadd1.setVisibility(View.VISIBLE);
            holder.modeTView.setVisibility(View.GONE);
            holder.lineTv.setVisibility(View.GONE);
        }
        holder.modeTView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.selectLin.isShown()){
                    holder.modeTView.setText("收起");
                    holder.selectLin.setVisibility(View.VISIBLE);
                    holder.goodsLiny.setVisibility(View.GONE);
                }else{
                    holder.modeTView.setText("选规格");
                    holder.selectLin.setVisibility(View.GONE);
                    holder.goodsLiny.setVisibility(View.VISIBLE);
                }
//                removeData(position);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
                intent.putExtra("cno",mValues.get(position).getId());
                holder.mView.getContext().startActivity(intent);
            }
        });
        holder.typeadd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchView != null){
                    callback = new MyFoodActionCallback((Activity) context,searchView,mValues.get(position).getChirdren().get(0).getSkuBarcode());
                }else{
                    callback = new MyFoodActionCallback((Activity) context,mValues.get(position).getChirdren().get(0).getSkuBarcode());
                }
                if(callback==null) return;
                callback.addAction(v);
                if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID)){
                    return;
                }
                String skuNum = SXUtils.getInstance(context).CheckExistCar(goodsdetail.getChirdren().get(0).getSkuBarcode());
                if(!TextUtils.isEmpty(skuNum)){
                    if(Integer.parseInt(skuNum)>=1) {
                        AppClient.carSKUNumMap.put(goodsdetail.getChirdren().get(0).getSkuBarcode(), (Integer.parseInt(skuNum) + 1) + "");
                    }
                }else{
                    AppClient.carSKUNumMap.put(goodsdetail.getChirdren().get(0).getSkuBarcode(), "1");
                }
                notifyDataSetChanged();
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refgoods"));

            }
        });
        holder.subImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skuNum = SXUtils.getInstance(context).CheckExistCar(goodsdetail.getChirdren().get(0).getSkuBarcode());
                if(!TextUtils.isEmpty(skuNum)){
                    if(Integer.parseInt(skuNum)>1){
                        SXUtils.getInstance(context).AddOrUpdateCar(goodsdetail.getChirdren().get(0).getSkuBarcode(),"-1");
                        AppClient.carSKUNumMap.put(goodsdetail.getChirdren().get(0).getSkuBarcode(),(Integer.parseInt(skuNum)-1)+"");
                    }else{
                        SXUtils.getInstance(context).AddOrUpdateCar(goodsdetail.getChirdren().get(0).getSkuBarcode(),"0");
                        holder.subImage.setVisibility(View.GONE);
                        holder.carNum.setVisibility(View.GONE);
                        AppClient.carSKUNumMap.remove(goodsdetail.getChirdren().get(0).getSkuBarcode());
                    }
                }
                notifyDataSetChanged();
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refgoods"));

            }
        });







        if(!TextUtils.isEmpty(tag)){
            if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID)) {
                holder.delImageView.setVisibility(View.GONE);
            }
            else {
                holder.delImageView.setVisibility(View.VISIBLE);
            }
            holder.delImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SXUtils.getInstance(context).MyDialogView(context,"温馨提示!", "是否删除"+goodsdetail.getGoodsName()+"?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SXUtils.getInstance(context).tipDialog.dismiss();
                            SXUtils.getInstance(holder.delImageView.getContext()).AddDelBill(0,goodsdetail.getGoodsCode(),null);
                        }
                    });
                }
            });
        }
        SXUtils.getInstance(holder.mImageView.getContext()).GlideSetImg(goodsdetail.getOriginalImg(),holder.mImageView);
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
        mValues.remove(position);
        notifyItemRemoved(position);
    }

}