package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.SearchHotEntity;

import java.util.List;

/**
 * 热门词汇
 */
public class SearchHotAdapter extends BaseAdapter {
    private List<SearchHotEntity> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    public SearchHotAdapter(Activity context, List<SearchHotEntity> result) {
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
        SearchHotEntity info = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.search_hot_item, null);
            vh.name = (TextView) convertView.findViewById(R.id.search_hot_name);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        vh.name.setText(info.getHotNme()+"");
//        vh.cardName.setText(bankinfo.get("cardName")+"");
//        vh.cardNum.setText(bankinfo.get("cardNum")+"");
        return convertView;
    }
    class LifeViewHolder{
        TextView name;
    }
}