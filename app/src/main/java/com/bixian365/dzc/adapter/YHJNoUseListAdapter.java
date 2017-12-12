package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.car.UserCouponEntity;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * ***************************
 * 优惠券未使用
 * @author mfx
 * ***************************
 */
public class YHJNoUseListAdapter extends BaseAdapter {
    private ArrayList<UserCouponEntity> result;
    private final LayoutInflater mLayoutInflater;
    private int tag;
    private Activity context;
    public YHJNoUseListAdapter(Activity context, ArrayList<UserCouponEntity> result, int tag) {
        mLayoutInflater = LayoutInflater.from(context);
        this.result = result;
        this.context = context;
        this.tag = tag;
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
        UserCouponEntity info = result.get(position);
        LifeViewHolder vh;
        if (convertView == null) {
            vh = new LifeViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.yhj_nouse_item, null);
            vh.rel = (RelativeLayout) convertView.findViewById(R.id.yhj_item_bg_rel);
            vh.price = (TextView) convertView.findViewById(R.id.yhj_item_price_tv);
            vh.des = (TextView) convertView.findViewById(R.id.yhj_item_des_tv);
            vh.time = (TextView) convertView.findViewById(R.id.yhj_item_time_tv);
            vh.hs = (TextView) convertView.findViewById(R.id.yhj_item_hs_tv);
            convertView.setTag(vh);
        } else {
            vh = (LifeViewHolder) convertView.getTag();
        }
        vh.price.setText(info.getCouponMoney()+"");
        vh.des.setText(info.getCouponTerm());
        vh.time.setText(info.getCouponTime());

        if(tag==2){
            vh.rel.setBackgroundResource(R.mipmap.yhj_nouse_img);
            vh.hs.setText("已使用");
        }else if(tag==3){
            vh.rel.setBackgroundResource(R.mipmap.yhj_nouse_img);
            vh.hs.setText("已过期");
        }else{
            vh.rel.setBackgroundResource(R.mipmap.yhj_use_img);
            vh.hs.setText("立刻使用");
        }
        vh.hs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppClient.TAG2 = true;
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100011,"wall"));
                context.finish();
            }
        });
//        vh.cardName.setText(bankinfo.get("cardName")+"");
//        vh.cardNum.setText(bankinfo.get("cardNum")+"");
        return convertView;
    }
    class LifeViewHolder{
        TextView price;
        TextView des;
        TextView time;
        TextView  hs;
        RelativeLayout rel;
    }
}