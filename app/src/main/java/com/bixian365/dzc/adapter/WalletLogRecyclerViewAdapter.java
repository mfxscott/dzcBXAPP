package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class WalletLogRecyclerViewAdapter extends RecyclerView.Adapter<WalletLogRecyclerViewAdapter.ViewHolder>{
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private  List<TransLogEntity> result;
    private Activity activity;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final  TextView total;
        public final  TextView state;
        public final TextView type;
        public final TextView time;
        public final TextView order;
        public ViewHolder(View view) {
            super(view);
            mView = view;

            order = (TextView) view.findViewById(R.id.wallet_trans_billno_tv);
            total = (TextView) view.findViewById(R.id.wallet_trans_total_tv);
            state = (TextView) view.findViewById(R.id.wallet_trans_state_tv);
            type = (TextView) view.findViewById(R.id.wallet_trans_type_tv);
            time = (TextView) view.findViewById(R.id.wallet_trans_time_tv);

        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
    public WalletLogRecyclerViewAdapter(Activity context, List<TransLogEntity> results) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        result = results;
        this.activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_srdetail_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TransLogEntity  transInfo = result.get(position);
        holder.total.setText(transInfo.getAmt()+"");

        String tradeType = transInfo.getTradeType().toString().trim();
        if(tradeType.equals("ORDER_PAY")){
            holder.type.setText("订单支付");
        }else if(tradeType.equals("IN_RECHARGE")){
            holder.type.setText("充值收入");
        }else if(tradeType.equals("ORDER_CANCEL")){
            holder.type.setText("订单取消");
        }
        else if(tradeType.equals("ORDER_COMMISSION")){
            holder.type.setText("团购订单提成");
        }
        else if(tradeType.equals("PURCHASE_ORDER")){
            holder.type.setText("供应商采购单收入");
        }
        else if(tradeType.equals("ORDER_SALE_COMMISSION")){
            holder.type.setText("订单售卖收入");
        }
        else if(tradeType.equals("WITHDRAW")){
            holder.type.setText("提现");
        }
        holder.time.setText(transInfo.getCreated());
        holder.order.setText(transInfo.getBillNo()+"");
        holder.state.setText(transInfo.getState()+"");
    }
    @Override
    public int getItemCount() {
        return result.size();
    }
    @Override
    public int getItemViewType(int position) {
        Log.i("========",position+"");
        return super.getItemViewType(position);
    }
}