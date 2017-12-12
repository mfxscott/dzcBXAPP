package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.bixian365.dzc.R;

import java.util.List;

/**
 * 商户注册成功选择店铺地址
 */
public class StoreMapListAdapter extends BaseAdapter {
    private List<PoiItem> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    public int mSelect=-1;
    public StoreMapListAdapter(Activity context, List<PoiItem>  result ) {
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
        PoiItem map = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.store_map_item, null);
            vh.title = (TextView) convertView.findViewById(R.id.store_map_item_title);
            vh.address = (TextView) convertView.findViewById(R.id.store_map_item_address);
            vh.img = (TextView) convertView.findViewById(R.id.store_map_item_iv);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        if(mSelect==position){
            vh.img.setVisibility(View.VISIBLE);
        }else{
            vh.img.setVisibility(View.GONE);
        }
        vh.title.setText(map.getTitle()+"");
        String addressStr =map.getProvinceName().toString().trim()+map.getCityName().toString().trim()+map.getAdName().toString().trim()+map.getSnippet().toString().trim()+"";
        vh.address.setText(addressStr);
        return convertView;
    }
    class LifeViewHolder{
        TextView title;
        TextView address;
        TextView img;
    }
}