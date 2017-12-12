package com.bixian365.dzc.fragment.my.store.yhj;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.PayCouponsRecyclerViewAdapter;
import com.bixian365.dzc.entity.car.OrderCouponsEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import java.util.ArrayList;
import java.util.List;

import static com.bixian365.dzc.fragment.my.store.yhj.YHJActivity.couponsList;

/**
 * 确定订单，生成订单时选择可使用优惠劵
 */
public class PayOrderUseFragment extends Fragment {
    private Activity activity;
    private View view;
    private ArrayList<OrderCouponsEntity> yhj ;
    public static int tag;
    private Handler hand;
    private RecyclerView recyclerView;
    private PayCouponsRecyclerViewAdapter payCouponsRecyApdapter;
    private LinearLayout nowUseLin;

    public PayOrderUseFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pay_order_use, null);
        activity = getActivity();
        initView();
//        getCouponsUse();
        return view;
    }
    private void initView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.yhj_pay_order_nouse_recyclerv);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(couponsList != null || couponsList.size()>0)
            view.findViewById(R.id.wallet_default_coupons_lin).setVisibility(View.GONE);
        payCouponsRecyApdapter = new PayCouponsRecyclerViewAdapter(activity,YHJActivity.couponsList,1);
        recyclerView.setAdapter(payCouponsRecyApdapter);

        nowUseLin = (LinearLayout) view.findViewById(R.id.pay_order_nowuse_lin);
        nowUseLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  CouponNo =  payCouponsRecyApdapter.getCouponsStrCouponNo();
                if(TextUtils.isEmpty(CouponNo)){
                    SXUtils.getInstance(activity).ToastCenter("请选择优惠券");
                    return ;
                }
                float  totalPrice = payCouponsRecyApdapter.getCouponsTotalPrice();
//                SXUtils.getInstance(activity).ToastCenter(CouponNo+""+totalPrice);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("couponNo",CouponNo);
                bundle.putFloat("couponPrice",totalPrice);
                intent.putExtras(bundle);
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                activity.setResult(1001, intent);
                activity.finish();
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        view.findViewById(R.id.wallet_default_coupons_lin).setVisibility(View.GONE);
                        yhj = (ArrayList<OrderCouponsEntity>) msg.obj;
                        payCouponsRecyApdapter = new PayCouponsRecyclerViewAdapter(activity, couponsList,1);
                        recyclerView.setAdapter(payCouponsRecyApdapter);
                        break;
                    case 1001:
                        break;
                    case AppClient.ERRORCODE:
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
        } else {

        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {
        }
    }

    /**
     * 查询未使用的优惠券
     */
    public void getCouponsUse() {
        HttpParams httpp = new HttpParams();//COUPONS_PASSUSE==COUPONS_USE
        HttpUtils.getInstance(activity).requestPost(false, AppClient.COUPONS_USE, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("可使用优惠券返回参数======",jsonObject.toString());
                List<OrderCouponsEntity> couponsUse =  ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(),OrderCouponsEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = couponsUse;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);

            }
        });
    }
}
