package com.bixian365.dzc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.orderlist.OrderInfoEntity;
import com.bixian365.dzc.fragment.my.pay.TopUpActivity;
import com.bixian365.dzc.fragment.my.store.order.OrderDetailActivity;
import com.bixian365.dzc.fragment.my.store.order.StockSubmitNumberActivity;
import com.bixian365.dzc.fragment.my.store.order.WaitSendFragment;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 *  摊主订单列表
 * @author mfx
 * @time  2017/7/11 12:24
 */
public  class WaitPayRecyclerViewAdapter
        extends RecyclerView.Adapter<WaitPayRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<OrderInfoEntity> mValues;
    private Context context;
    private int tag;//标示订单类型进入显示不同按钮

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView mImageView;
        public final ImageView mImageView1;
        public final TextView  shopNameTv;
        public final TextView orderTime;
        public final TextView  orderTv;
        public final TextView  orderTotal;
        public final TextView  cancelOrder;
        public final TextView  takeOrder,cancelTv;
        public final LinearLayout  btnLin;
        public final RecyclerView recyclerView;
        public final TextView sendTimeTv;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mImageView = (ImageView) view.findViewById(R.id.order_wait_pay_item_iv);
            mImageView1 = (ImageView) view.findViewById(R.id.order_wait_pay_item_iv2);
            shopNameTv = (TextView) view.findViewById(R.id.order_wait_pay_shopname_tv);
            orderTime = (TextView) view.findViewById(R.id.order_wait_pay_ordertime_tv);
            orderTotal = (TextView) view.findViewById(R.id.order_wait_pay_ordertotal_tv);
            cancelOrder = (TextView) view.findViewById(R.id.order_wait_pay_cancel_btn);
            takeOrder = (TextView) view.findViewById(R.id.order_wait_pay_take_btn);
            cancelTv = (TextView) view.findViewById(R.id.order_done_tv);
            btnLin = (LinearLayout) view.findViewById(R.id.order_btn_lin);
            recyclerView = (RecyclerView) view.findViewById(R.id.order_item_recycler);
            orderTv = (TextView) view.findViewById(R.id.order_item_total_tv);
            sendTimeTv = (TextView) view.findViewById(R.id.order_item_send_time_tv);
        }
        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
    public WaitPayRecyclerViewAdapter(Context context, List<OrderInfoEntity> items, int tag) {
        if(context != null) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }
        mValues = items;
        this.context = context;
        this.tag = tag;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_wait_pay_item, parent, false);
        if(mBackground != 0)
            view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderInfoEntity orderInfo = mValues.get(position);

        holder.shopNameTv.setText("订单号："+orderInfo.getOrderNo()+"");
        holder.orderTime.setText(orderInfo.getOrderTime()+"");
        holder.orderTotal.setText("¥"+orderInfo.getSettlementAmount()+"");

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        final WaitPayGoodsRecyclerViewAdapter simpAdapter = new WaitPayGoodsRecyclerViewAdapter(context,orderInfo.getOrderLines(),new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                list.add(orderInfo.getOrderLines());
                bundle.putParcelableArrayList("orderDetail",list);
                bundle.putString("orderNo",orderInfo.getOrderNo());
                bundle.putString("tradeNo",orderInfo.getTradeNo());
                bundle.putString("orderAddress",orderInfo.getOrderAddress());
                bundle.putString("orderTime",orderInfo.getOrderTime());
                bundle.putString("name",orderInfo.getShopUserName());
                bundle.putString("total",orderInfo.getTransactionAmount());
                intent.putExtras(bundle);
                intent.putExtra("orderTag",tag+"");
                context.startActivity(intent);
            }
        });
        holder.recyclerView.setAdapter(simpAdapter);
        if(TextUtils.isEmpty(orderInfo.getDeliveryTime())){
            holder.sendTimeTv.setVisibility(View.GONE);
        }else{
            holder.sendTimeTv.setVisibility(View.VISIBLE);
            holder.sendTimeTv.setText("发货时间："+orderInfo.getDeliveryTime()+"");
        }
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), OrderDetailActivity.class);
//                intent.putExtra("orderTag",tag+"");
//                intent.putExtra("orderId",orderInfo.getOrderNo());
//                holder.mView.getContext().startActivity(intent);
//            }
//        });
        switch (Integer.parseInt(orderInfo.getStates())){
            case 1:
                holder.cancelOrder.setVisibility(View.VISIBLE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.takeOrder.setText("立即付款");
                holder.orderTv.setText("合计：");
                holder.takeOrder.setBackgroundResource(R.drawable.comfirm_take_selector);
                holder.takeOrder.setVisibility(View.VISIBLE);
                break;
            case 10:
                holder.orderTv.setText("合计：");
                holder.cancelOrder.setVisibility(View.GONE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.takeOrder.setText("提醒发货");
                holder.takeOrder.setTextColor(context.getResources().getColor(R.color.orange));
                holder.takeOrder.setBackgroundResource(R.drawable.cancel_order_selector);
                holder.takeOrder.setVisibility(View.VISIBLE);
                break;
            case 30:
                holder.orderTv.setText("合计：");
                holder.cancelOrder.setVisibility(View.GONE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.takeOrder.setText("确定收货");
                holder.takeOrder.setBackgroundResource(R.drawable.comfirm_take_selector);
                holder.takeOrder.setVisibility(View.VISIBLE);
                break;
            case 50:
//                    holder.btnLin.setVisibility(View.GONE);
//                    holder.cancelTv.setVisibility(View.VISIBLE);
                if(AppClient.USERROLETAG.equals("64")){
                    holder.takeOrder.setVisibility(View.GONE);
                }else{
                    holder.takeOrder.setText("缺货少货上报");
                    holder.takeOrder.setVisibility(View.VISIBLE);
                }
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.orderTv.setText("状态：");
                holder.orderTotal.setText("已完成");
                holder.cancelOrder.setVisibility(View.GONE);
                break;
            case 8:case 32:
                holder.cancelOrder.setVisibility(View.GONE);
                holder.takeOrder.setVisibility(View.GONE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.orderTv.setText("状态：");
                holder.orderTotal.setText("已取消");
                break;
            case 4:
                holder.cancelOrder.setVisibility(View.GONE);
                holder.takeOrder.setVisibility(View.GONE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.orderTv.setText("状态：");
                holder.orderTotal.setText("已挂起");
                break;
            case -1:
                holder.cancelOrder.setVisibility(View.GONE);
                holder.takeOrder.setVisibility(View.GONE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.orderTv.setText("状态：");
                holder.orderTotal.setText("异常订单");
                break;
            default:
                holder.cancelOrder.setVisibility(View.GONE);
                holder.takeOrder.setVisibility(View.GONE);
                holder.btnLin.setVisibility(View.VISIBLE);
                holder.orderTv.setText("状态：");
                holder.orderTotal.setText(orderInfo.getStates()+"");
                break;
        }
        holder.takeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Integer.parseInt(orderInfo.getStates())) {
                    case 1:
                        Intent pay = new Intent(context, TopUpActivity.class);
                        pay.putExtra("payTag","1");
                        pay.putExtra("orderNo",orderInfo.getOrderNo());
                        pay.putExtra("paySum",orderInfo.getSettlementAmount());
                        context.startActivity(pay);
                        break;
                    case 10:
//                        SXUtils.getInstance(context).ToastCenter("提醒发货");
                        new WaitSendFragment().getRemindHttp(orderInfo.getOrderNo(),orderInfo.getTradeNo(),null);
                        break;
                    case 30:
//                        SXUtils.getInstance(context).ToastCenter("确认发货");
                        getOrderConfirmHttp(orderInfo.getOrderNo());
                        break;
                    case 50:
//                        Intent intent = new Intent(context, StockSubmitNumberActivity.class);
//                        intent.putExtra("orderId",orderInfo.getOrderNo());
//                        context.startActivity(intent);

                        Intent intent = new Intent(context, StockSubmitNumberActivity.class);
                        Bundle bundle = new Bundle();
                        ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                        list.add(orderInfo.getOrderLines());
                        bundle.putParcelableArrayList("orderLines",list);
                        intent.putExtra("orderNo",orderInfo.getOrderNo());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        //已完成，缺货少货上报
                        break;

                }
            }
        });
        holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(context).MyDialogView(context,"温馨提示!", "确定取消该笔订单吗?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SXUtils.getInstance(context).tipDialog.dismiss();
                        getCancelOrderHttp(orderInfo.getOrderNo());
                    }
                });

            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                ArrayList list = new ArrayList(); //这个list用于在budnle中传递 需要传递的ArrayList<Object>
                list.add(orderInfo.getOrderLines());
                bundle.putParcelableArrayList("orderDetail",list);
                bundle.putString("orderNo",orderInfo.getOrderNo());
                bundle.putString("tradeNo",orderInfo.getTradeNo());
                bundle.putString("orderAddress",orderInfo.getOrderAddress());
                bundle.putString("orderTime",orderInfo.getOrderTime());
                bundle.putString("name",orderInfo.getShopUserName());
                bundle.putString("total",orderInfo.getTransactionAmount());
                intent.putExtras(bundle);
                intent.putExtra("orderTag",tag+"");
                context.startActivity(intent);
            }
        });
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
    /**
     * 用户取消订单
     * @param orderNo  订单ID
     */
    private  void getCancelOrderHttp(String orderNo) {
        HttpParams params = new HttpParams();
        params.put("orderNo",orderNo);
        HttpUtils.getInstance(context).requestPost(false, AppClient.USER_CANCEL_ORDER, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                EventBus.getDefault().post(new MessageEvent(1003,"waitpay"));
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"user"));
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100010,"done"));

//                Logs.i("取消订单========",jsonObject.toString());
//                Message msg = new Message();
//                msg.what = 1001;
//                msg.obj = "";
//                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                EventBus.getDefault().post(new MessageEvent(1004,"waitpay"));

//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);
            }
        });
    }
    /**
     * 确认收货
     * @param orderNo  订单ID
     */
    public  void getOrderConfirmHttp(String orderNo) {
        HttpParams params = new HttpParams();
        params.put("orderNo",orderNo);
        String confrimStr = "";
        if(AppClient.USERROLETAG.equals("64")){
            confrimStr = AppClient.USER_CONFRIM_GET;
        }else{
            confrimStr = AppClient.ORDER_CONFIRM;
        }
        HttpUtils.getInstance(context).requestPost(false, confrimStr, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                EventBus.getDefault().post(new MessageEvent(1005,"waittake"));
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"user"));
            }
            @Override
            public void onResponseError(String strError) {
                EventBus.getDefault().post(new MessageEvent(1006,"waittake"));
            }
        });
    }
}