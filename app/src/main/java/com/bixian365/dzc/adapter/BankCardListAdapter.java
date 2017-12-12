package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bixian365.dzc.R;

import java.util.List;
import java.util.Map;

/**
 * ***************************
 * 我的钱包银行卡列表适配器
 * @author mfx
 * ***************************
 */
public class BankCardListAdapter extends BaseAdapter {
    private List<Map<String,String>> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    public BankCardListAdapter(Activity context, List<Map<String,String>> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.result = result;
        this.context = context;
    }
    public int getCount() {
        if(result.size() >3){
            return 3;
        }else{
            return result.size();
        }
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String,String> bankinfo = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.bank_card_item, null);
            vh.cardName = (TextView) convertView.findViewById(R.id.bank_card_name_tv);
            vh.cardNum = (TextView) convertView.findViewById(R.id.bank_card_number_tv);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        vh.cardName.setText(bankinfo.get("cardName")+"");
        vh.cardNum.setText(bankinfo.get("cardNum")+"");
        return convertView;
    }
    class LifeViewHolder{
        TextView cardNum;
        TextView cardName;
    }
}