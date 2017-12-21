package com.bixian365.dzc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.bill.BillChirdrenEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 全部商品分类
 */
public  class GoodsRecyclerViewAdapter
        extends RecyclerView.Adapter<GoodsRecyclerViewAdapter.ViewHolder>{

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    //    private GoodsInfoEntity category;
    private List<BillChirdrenEntity> billchirdrenList;
    private FoodActionCallback callback;
    private Context context;
    private String goodsUnit;
    private View carTagV=null;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView addImage;
        public final TextView shopPrice;
        //        public final TextView  goodsUnit;
        public final TextView  goodsModel;
        public final TextView  marketPrice;
        public final TextView  carNum;
        public final ImageView subImage;



        public ViewHolder(View view) {
            super(view);
            addImage = (ImageView) view.findViewById(R.id.main_bill_item_addcar_tv);
//            shopPrice = (TextView) view.findViewById(R.id.type_info_shop_price_tv);
            shopPrice = (TextView) view.findViewById(R.id.main_bill_item_unit_tv);
            goodsModel = (TextView) view.findViewById(R.id.main_bill_item_model_tv);
            marketPrice = (TextView) view.findViewById(R.id.main_bill_item_market_tv);
            subImage = (ImageView) view.findViewById(R.id.main_bill_item_subcar_tv);
            carNum = (TextView) view.findViewById(R.id.main_bill_item_carnum_tv);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public GoodsRecyclerViewAdapter(Context context,String unit,List<BillChirdrenEntity> category) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.context = context;
        goodsUnit = unit;
        billchirdrenList =category;
    }
    public GoodsRecyclerViewAdapter(Context context,String unit,List<BillChirdrenEntity> category,View view) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        this.context = context;
        goodsUnit = unit;
        billchirdrenList =category;
        this.carTagV = view;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goods_modle_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final  BillChirdrenEntity billchirdren = billchirdrenList.get(position);
        holder.marketPrice.setText("¥"+billchirdren.getShopPrice()+"/"+billchirdren.getGoodsModel());
//        holder.shopPrice.setText(billchirdren.getShopPrice());
        holder.shopPrice.setText("¥"+billchirdren.getShopPrice());
        holder.goodsModel.setText("/"+billchirdren.getGoodsModel());
        String skuNum = SXUtils.getInstance(context).CheckExistCar(billchirdren.getSkuBarcode());
        if(TextUtils.isEmpty(skuNum)){
            holder.subImage.setVisibility(View.GONE);
            holder.carNum.setVisibility(View.GONE);
        }else{
            holder.subImage.setVisibility(View.VISIBLE);
            holder.carNum.setVisibility(View.VISIBLE);
            holder.carNum.setText(skuNum+"");
        }
//        holder.goodsModel.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",category.getId());
//                holder.mView.getContext().startActivity(intent);
//            }
//        });
        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商品详情点击需要加入到不同动画view
                SXUtils.getInstance(context).addGetGoodsType(billchirdren.getSkuBarcode());

//                if(carTagV != null){
//                    callback = new MyFoodActionCallback((Activity) context,carTagV,billchirdren.getSkuBarcode());
//                }else{
//                    callback = new MyFoodActionCallback((Activity) context,billchirdren.getSkuBarcode());
//                }
//                if(callback==null) return;
//                callback.addAction(v);
//                if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID)){
//                    return;
//                }
//                String skuNum = SXUtils.getInstance(context).CheckExistCar(billchirdren.getSkuBarcode());
//                if(!TextUtils.isEmpty(skuNum)){
//                    if(Integer.parseInt(skuNum)>=1) {
//                        AppClient.carSKUNumMap.put(billchirdren.getSkuBarcode(), (Integer.parseInt(skuNum) + 1) + "");
//                    }
//                }else{
//                    AppClient.carSKUNumMap.put(billchirdren.getSkuBarcode(), "1");
//                }
//                notifyDataSetChanged();
//                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refgoods"));
            }
        });
        holder.subImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skuNum = SXUtils.getInstance(context).CheckExistCar(billchirdren.getSkuBarcode());
                if(!TextUtils.isEmpty(skuNum)){
                    if(Integer.parseInt(skuNum)>1){
                        SXUtils.getInstance(context).AddOrUpdateCar(billchirdren.getSkuBarcode(),"-1");
                        AppClient.carSKUNumMap.put(billchirdren.getSkuBarcode(),(Integer.parseInt(skuNum)-1)+"");
                    }else{
                        SXUtils.getInstance(context).AddOrUpdateCar(billchirdren.getSkuBarcode(),"0");
                        holder.subImage.setVisibility(View.GONE);
                        holder.carNum.setVisibility(View.GONE);
                        AppClient.carSKUNumMap.remove(billchirdren.getSkuBarcode());
                    }
                }
                notifyDataSetChanged();
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100026,"refgoods"));

            }
        });
    }
    @Override
    public int getItemCount() {
        return billchirdrenList.size();
    }
    @Override
    public int getItemViewType(int position) {
        Log.i("========",position+"");
        return super.getItemViewType(position);
    }
}