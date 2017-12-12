package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;

import java.util.List;
import java.util.Map;

/**
 * 合伙人，联创中心，供货商  采购清单，缺货少货
 * @author mfx
 * @time  2017/7/27 12:07
 */
public class BuyerBillGridViewAdapter extends BaseAdapter {
    private List<Map<String,String>> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    public BuyerBillGridViewAdapter(Activity context, List<Map<String,String>> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.result = result;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,String> gridvlist = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.buyer_bill_item, null);
            vh.imgv = (ImageView) convertView.findViewById(R.id.buyer_bill_item_img);
            vh.name = (TextView) convertView.findViewById(R.id.buyer_bill_item_tv);
            vh.hintImg = (ImageView) convertView.findViewById(R.id.buyer_bill_item_hint_tv);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        switch (position){
            case 0:
                vh.hintImg.setImageResource(R.mipmap.cg1);
                break;
            case 1:
                vh.hintImg.setImageResource(R.mipmap.cg2);
                break;
            case 2:
                vh.hintImg.setImageResource(R.mipmap.cg3);
                break;
            case 3:
                vh.hintImg.setImageResource(R.mipmap.cg4);
                break;

        }
//        vh.hintImg.setVisibility(View.VISIBLE);
//                vh.imgv.setImageResource(R.mipmap.buyer_bill_df);
//        Glide.with(context).load(gridvlist.get("imageUrl")).centerCrop().into(vh.imgv);
        vh.name.setText(gridvlist.get("name"));
        return convertView;
    }
    class LifeViewHolder{
        ImageView imgv;
        TextView name;
        ImageView  hintImg;
    }
}