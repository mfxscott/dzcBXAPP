package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.wallet.TransLogEntity;

import java.util.List;

/**
 * ***************************
 * 钱包收入明细适配器
 * @author mfx
 * ***************************
 */
public class SRDetailListAdapter extends BaseAdapter {
    private  List<TransLogEntity> result;
    private final LayoutInflater mLayoutInflater;
    private Activity context;
    public SRDetailListAdapter(Activity context,  List<TransLogEntity> result) {
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
        TransLogEntity transInfo = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.wallet_srdetail_item, null);
            vh.order = (TextView) convertView.findViewById(R.id.wallet_trans_billno_tv);
            vh.total = (TextView) convertView.findViewById(R.id.wallet_trans_total_tv);
            vh.state = (TextView) convertView.findViewById(R.id.wallet_trans_state_tv);
            vh.type = (TextView) convertView.findViewById(R.id.wallet_trans_type_tv);
            vh.time = (TextView) convertView.findViewById(R.id.wallet_trans_time_tv);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        vh.total.setText(transInfo.getAmt()+"");

        String tradeType = transInfo.getTradeType().toString().trim();
        if(tradeType.equals("ORDER_PAY")){
            vh.type.setText("订单支付");
        }else if(tradeType.equals("IN_RECHARGE")){
            vh.type.setText("充值收入");
        }else if(tradeType.equals("ORDER_CANCEL")){
            vh.type.setText("订单取消");
        }
        else if(tradeType.equals("ORDER_COMMISSION")){
            vh.type.setText("团购订单提成");
        }
        else if(tradeType.equals("PURCHASE_ORDER")){
            vh.type.setText("供应商采购单收入");
        }
        else if(tradeType.equals("ORDER_SALE_COMMISSION")){
            vh.type.setText("订单售卖收入");
        }
        else if(tradeType.equals("WITHDRAW")){
            vh.type.setText("提现");
        }
        vh.time.setText(transInfo.getCreated());
        vh.order.setText(transInfo.getBillNo()+"");
//        if(transInfo.getFundType().equals("2")){
//            vh.state.setText("已到账");
//        }else{
            vh.state.setText(transInfo.getState()+"");
//        }

        return convertView;
    }
    class LifeViewHolder{
        TextView total;
        TextView state;
        TextView type;
        TextView time;
        TextView order;
    }
}