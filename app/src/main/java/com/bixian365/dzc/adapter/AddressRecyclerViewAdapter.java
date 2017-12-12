package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.address.AddressInfoEntity;
import com.bixian365.dzc.fragment.car.EditAddAddressActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用清单列表
 * @author mfx
 * @time  2017/7/11 12:24
 */
public  class AddressRecyclerViewAdapter
        extends RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<AddressInfoEntity> mValues = new ArrayList<>();
    private Activity activity;
    private AddressInfoEntity  address;
    private int dfposiont=-1;
    private boolean isOnclick=false;//判断只要点击过设置默认地址就不已默认字段标示为判断

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView namePhone;
        public final TextView cityStreet;
        public final CheckBox  isDefaultCb;
        public final  TextView editTv;
        public final  TextView  delTv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            namePhone = (TextView) view.findViewById(R.id.address_item_name);
            cityStreet = (TextView) view.findViewById(R.id.address_item_city_street);
            isDefaultCb = (CheckBox) view.findViewById(R.id.address_item_cb);
            editTv = (TextView) view.findViewById(R.id.address_item_edit);
            delTv = (TextView) view.findViewById(R.id.address_item_del);

        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
    public AddressRecyclerViewAdapter(Activity context, List<AddressInfoEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AddressInfoEntity  addrss = mValues.get(position);
        holder.namePhone.setText(addrss.getConsigneeName()+"    "+addrss.getConsigneeMobile());
        holder.cityStreet.setText(addrss.getProvinceName()+addrss.getCityName()+addrss.getDistrictName()+addrss.getAddress());
        holder.isDefaultCb.setOnCheckedChangeListener(null);
//        if(isOnclick){
//            if (dfposiont == position) {
//                holder.isDefaultCb.setChecked(true);
//            } else {
//                holder.isDefaultCb.setChecked(false);
//            }
//        }else {
            if (addrss.getIsDefault().equals("1")) {
                holder.isDefaultCb.setChecked(true);
            } else {
                holder.isDefaultCb.setChecked(false);

            }
//        }
        holder.isDefaultCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getUpdateAddress(addrss,"1");
                }else{
                    getUpdateAddress(addrss,"0");
                }
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("addressInfo",addrss);
                intent.putExtras(bundle);
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                activity.setResult(1000, intent);
                activity.finish();
            }
        });
        holder.delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(activity).MyDialogView(activity,"温馨提示!", "亲，您确定要删除该收获地址吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SXUtils.getInstance(activity).tipDialog.dismiss();
                        removeData(position);
                        SXUtils.getInstance(activity).delAddress(addrss.getConsigneeId());
                    }
                });

            }
        });
        holder.editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.mView.getContext(), EditAddAddressActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("tag","1");
                mBundle.putParcelable("address", mValues.get(position));
                intent.putExtras(mBundle);
                holder.mView.getContext().startActivity(intent);
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
    //  添加数据
    public void addData(int position) {
//      在list中添加数据，并通知条目加入一条
        notifyItemInserted(position);
    }
    //  删除数据
    public void removeData(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();

    }
    /**
     * 修改收货地址
     */
    public void getUpdateAddress(AddressInfoEntity address,String tag) {
        HttpParams httpp = new HttpParams();
        httpp.put("consignee",address.getConsigneeName());
        httpp.put("id",address.getConsigneeId());
        httpp.put("mobile",address.getConsigneeMobile());
        httpp.put("provinceCode",address.getProvinceCode());
        httpp.put("provinceName",address.getProvinceName());
        httpp.put("cityCode",address.getCityCode());
        httpp.put("cityName",address.getCityName());
        httpp.put("districtCode",address.getDistrictCode());
        httpp.put("districtName",address.getDistrictName());
        httpp.put("address",address.getAddress());
        httpp.put("isDefault",tag+"");
        HttpUtils.getInstance(activity).requestPost(false,AppClient.ADDRESS_UPDATE, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                EventBus.getDefault().post(new MessageEvent(555,"orderList"));
//                JSONObject jsonObject1 = null;
////                FromOrderEntity orderFrom = (FromOrderEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),FromOrderEntity.class);
//                Message msg = new Message();
//                msg.what = 1000;
//                msg.obj = "";
//                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);

            }
        });
    }
}