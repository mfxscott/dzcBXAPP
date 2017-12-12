package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.goodstype.TypeChildrenEntity;

import java.util.List;
/**
 * 全部商品 左侧二级分类展示
 * @author mfx
 * @time  2017/7/10 17:58
 */
public class MainGoodsTypeAdapter extends BaseAdapter {
    private List<TypeChildrenEntity> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    int mSelect = 0;   //选中项
    public MainGoodsTypeAdapter(Activity context, List<TypeChildrenEntity> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.result = result;
        this.context = context;
    }
    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
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
        TypeChildrenEntity info = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.main_goods_type_item, null);
            vh.TypeName = (TextView) convertView.findViewById(R.id.main_goods_type_item_tv);
            vh.linV = convertView.findViewById(R.id.main_goods_type_line_v);
            vh.RlinV = convertView.findViewById(R.id.main_goods_type_item_rv);
            vh.rel = (RelativeLayout) convertView.findViewById(R.id.main_goods_type_rel);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        vh.TypeName.setText(info.getCategoryName());
//        if(position == 0){
//            vh.linV.setVisibility(View.VISIBLE);
//        }else{
//            vh.linV.setVisibility(View.GONE);
//        }
        if(mSelect==position){
            vh.rel.setBackgroundResource(R.color.app_bg);  //选中项背景
            vh.RlinV.setVisibility(View.GONE);
        }else{
            vh.rel.setBackgroundResource(R.drawable.white_appbg_selector);  //其他项背景
            vh.RlinV.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
    class LifeViewHolder{
        TextView TypeName;
        View    linV;
        View    RlinV;
        RelativeLayout rel;
    }
}