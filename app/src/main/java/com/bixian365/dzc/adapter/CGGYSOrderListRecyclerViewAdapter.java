package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.cgListInfo.CGListInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用清单列表
 * @author mfx
 * @time  2017/7/11 12:24
 */
public  class CGGYSOrderListRecyclerViewAdapter
        extends RecyclerView.Adapter<CGGYSOrderListRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<CGListInfoEntity> mValues = new ArrayList<>();
    private Activity activity;
    private int dfposiont=-1;
    private boolean isOnclick=false;//判断只要点击过设置默认地址就不已默认字段标示为判断

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
//        public  TextView namePhone;
//        public  TextView cityStreet;
//        public  CheckBox  isDefaultCb;
//        public   TextView editTv;
//        public   TextView  delTv;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            namePhone = (TextView) view.findViewById(R.id.address_item_name);
//            cityStreet = (TextView) view.findViewById(R.id.address_item_city_street);
//            isDefaultCb = (CheckBox) view.findViewById(R.id.address_item_cb);
//            editTv = (TextView) view.findViewById(R.id.address_item_edit);
//            delTv = (TextView) view.findViewById(R.id.address_item_del);

        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
    public CGGYSOrderListRecyclerViewAdapter(Activity context, List<CGListInfoEntity> items) {
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
//        final AddressInfoEntity  addrss = mValues.get(position);
//        holder.namePhone.setText(addrss.getConsigneeName()+"    "+addrss.getConsigneeMobile());
//        holder.cityStreet.setText(addrss.getProvinceName()+addrss.getCityName()+addrss.getDistrictName()+addrss.getAddress());
//        holder.isDefaultCb.setOnCheckedChangeListener(null);
//        if(isOnclick){
//            if (dfposiont == position) {
//                holder.isDefaultCb.setChecked(true);
//            } else {
//                holder.isDefaultCb.setChecked(false);
//            }
//        }else {
//            if (addrss.getIsDefault().equals("0")) {
//                holder.isDefaultCb.setChecked(true);
//            } else {
//                holder.isDefaultCb.setChecked(false);
//
//            }
//        }
//        holder.isDefaultCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    dfposiont = position;
//                }else{
//                    dfposiont = -1;
//                }
//                isOnclick = true;
//                notifyDataSetChanged();
//            }
//        });
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("addressInfo",addrss);
//                intent.putExtras(bundle);
//                //通过intent对象返回结果，必须要调用一个setResult方法，
//                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
//                activity.setResult(1000, intent);
//                activity.finish();
//            }
//        });
//        holder.delTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeData(position);
//                SXUtils.getInstance(activity).delAddress(addrss.getConsigneeId());
//            }
//        });
//        holder.editTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(holder.mView.getContext(), EditAddAddressActivity.class);
//                Bundle mBundle = new Bundle();
//                mBundle.putString("tag","1");
//                mBundle.putParcelable("address", mValues.get(position));
//                intent.putExtras(mBundle);
//                holder.mView.getContext().startActivity(intent);
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return 3;
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
}