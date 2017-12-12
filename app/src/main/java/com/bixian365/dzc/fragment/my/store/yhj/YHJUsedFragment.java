package com.bixian365.dzc.fragment.my.store.yhj;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.YHJNoUseListAdapter;
import com.bixian365.dzc.entity.car.UserCouponEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class YHJUsedFragment extends Fragment {

    private Activity activity;
    private View view;
    private ListView gridView;
    private ArrayList<UserCouponEntity> yhj ;
    public static int tag;
    private Handler hand;
    public YHJUsedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_no_use, null);
        activity = getActivity();
        initView();
        getCouponsUse();
        return view;
    }
    private void initView(){
        gridView = (ListView) view.findViewById(R.id.yhj_nouse_gridv);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SXUtils.getInstance(activity).ToastCenter("=="+position);
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        yhj = (ArrayList<UserCouponEntity>) msg.obj;
                        if(yhj == null || yhj.size() <=0){
                            view.findViewById(R.id.wallet_default_coupons_lin).setVisibility(View.VISIBLE);
                        }
                        gridView.setAdapter(new YHJNoUseListAdapter(activity,yhj,3));
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
    public void getCouponsUse() {
        HttpParams httpp = new HttpParams();
        HttpUtils.getInstance(activity).requestPost(false, AppClient.COUPONS_PASSUSE, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("已过期返回参数======",jsonObject.toString());
                List<UserCouponEntity> couponsUse =  ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(),UserCouponEntity.class);
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
