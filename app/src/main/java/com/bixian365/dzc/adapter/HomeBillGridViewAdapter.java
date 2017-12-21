package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.MyFoodActionCallback;

import java.util.List;

/**
 * 常用清单
 * @author mfx
 * @time  2017/7/10 10:13
 */
public class HomeBillGridViewAdapter extends BaseAdapter implements View.OnClickListener{
    private   List<GoodsInfoEntity> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    private FoodActionCallback callback;
    public HomeBillGridViewAdapter(Activity context, List<GoodsInfoEntity> result, FoodActionCallback callback) {
        mLayoutInflater = LayoutInflater.from(context);
        this.result = result;
        this.context = context;
        this.callback = callback;
    }
    @Override
    public void onClick(View v) {
        if(callback==null) return;
        callback.addAction(v);
    }
    public int getCount() {
        return result.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        final GoodsInfoEntity categInfo = result.get(position);
        final  LifeViewHolder vh;
        if (view == null) {
            vh = new LifeViewHolder();
            view = mLayoutInflater.inflate(R.layout.main_bill_item, null);
            vh.mImageView = (ImageView) view.findViewById(R.id.main_bill_item_iv);
            vh.mTextView = (TextView) view.findViewById(R.id.main_bill_item_name);
            vh.delImageView = (ImageView) view.findViewById(R.id.main_bill_item_del_iv);
//            vh.addcar1 = (TextView) view.findViewById(R.id.main_bill_addcar_tv);
//            vh.addcar2 = (TextView) view.findViewById(R.id.main_bill_addcar_tv2);
            vh.recyclerView = (RecyclerView) view.findViewById(R.id.bill_item_recycler);
            vh.liny = (RelativeLayout) view.findViewById(R.id.home_bill_item_liny);
            vh.marketPrice = (TextView) view.findViewById(R.id.bill_item_market_tv);

            vh.shopPrice = (TextView) view.findViewById(R.id.bill_item_shopprice_tv);
            vh.model = (TextView) view.findViewById(R.id.bill_item_model_tv);
            vh.selectTv = (TextView) view.findViewById(R.id.bill_item_info_select_mode);
            vh.addImage = (ImageView) view.findViewById(R.id.bill_item_info_add);
            vh.moreLiny = (LinearLayout) view.findViewById(R.id.bill_item_more_model_lin);
            vh.infoS = (TextView) view.findViewById(R.id.bill_item_info_s);
            view.setTag(vh);
        } else {
            vh = (LifeViewHolder) view.getTag();
        }

        if(categInfo.getChirdren().size()>1){
            vh.addImage.setVisibility(View.GONE);
            vh.selectTv.setVisibility(View.VISIBLE);
            vh.infoS.setVisibility(View.VISIBLE);

        }else{
            vh.selectTv.setVisibility(View.GONE);
            vh.addImage.setVisibility(View.VISIBLE);
            vh.infoS.setVisibility(View.GONE);

        }

        vh.selectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vh.moreLiny.isShown()){
                    vh.selectTv.setText("收起");
                    vh.moreLiny.setVisibility(View.VISIBLE);
                }else{
                    vh.selectTv.setText("选规格");
                    vh.moreLiny.setVisibility(View.GONE);
                }
            }
        });

        vh.model.setText("/"+categInfo.getChirdren().get(0).getGoodsModel());
        vh.shopPrice.setText(" ¥"+categInfo.getChirdren().get(0).getShopPrice());
        vh.marketPrice.setText("¥ "+categInfo.getChirdren().get(0).getMarketPrice()+"/"+categInfo.getChirdren().get(0).getGoodsModel());
        vh.mTextView.setText(categInfo.getGoodsName());
        vh.recyclerView.setLayoutManager(new LinearLayoutManager(vh.recyclerView.getContext()));
        vh.recyclerView.setItemAnimator(new DefaultItemAnimator());
        BillItemRecyclerViewAdapter simpAdapter = new BillItemRecyclerViewAdapter(vh.recyclerView.getContext(),categInfo.getGoodsUnit(),categInfo.getChirdren());
        vh.recyclerView.setAdapter(simpAdapter);
        SXUtils.getInstance(context).GlideSetImg(categInfo.getOriginalImg(),vh.mImageView);
        if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID))
            vh.delImageView.setVisibility(View.GONE);
        vh.delImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(context).MyDialogView(context,"温馨提示!", "是否删除"+categInfo.getGoodsName()+"?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SXUtils.getInstance(context).tipDialog.dismiss();
//                        SXUtils.getInstance(context).AddDelBill(0,categInfo.getGoodsCode(),null);
                    }
                });

//                result.remove(position);
//                notifyDataSetChanged();
//                removeData(position);
            }
        });
        vh.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商品详情点击需要加入到不同动画view
                    callback = new MyFoodActionCallback((Activity) context,categInfo.getChirdren().get(0).getSkuBarcode());
                if(callback==null) return;
                callback.addAction(v);
            }
        });
        vh.liny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("cno",categInfo.getId());
                context.startActivity(intent);
            }
        });
        return view;
    }
    class LifeViewHolder{
        ImageView mImageView;
        ImageView delImageView;
        TextView mTextView;
        TextView  marketPrice;
        TextView addcar1,addcar2;
        RecyclerView recyclerView;
        RelativeLayout liny;
        TextView shopPrice;
        TextView  model;
        TextView        selectTv;
        ImageView  addImage;
        LinearLayout        moreLiny;
        TextView infoS;
    }
}