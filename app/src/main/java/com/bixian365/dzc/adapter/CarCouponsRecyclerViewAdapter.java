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
import com.bixian365.dzc.entity.car.TakeNoPartInActivitiesEntity;
import com.bixian365.dzc.utils.SXUtils;

import java.util.List;

/**
 * ***************************
 * 购物车中优惠活动
 * @author mfx
 * ***************************
 */
public class CarCouponsRecyclerViewAdapter extends RecyclerView.Adapter<CarCouponsRecyclerViewAdapter.ViewHolder>{
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    List<TakeNoPartInActivitiesEntity> result;
    private Activity activity;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final  TextView notic;
        public final TextView  type;
        public ViewHolder(View view) {
            super(view);
            mView = view;

            notic = (TextView) view.findViewById(R.id.car_coupons_notic_tv);
              type = (TextView) view.findViewById(R.id.car_coupons_type_tv);
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
    public CarCouponsRecyclerViewAdapter(Activity context, List<TakeNoPartInActivitiesEntity> results) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        result = results;
        this.activity = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_coupons_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TakeNoPartInActivitiesEntity  transInfo = result.get(position);
        holder.notic.setText(transInfo.getNotice()+"");
        holder.type.setText(transInfo.getType());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String detailStr = transInfo.getDetail().replaceAll(" ", "");
//                String detailStr ="参与商品：结球类\n参与限制：不限制先到先得\n活动规则：满 0.1 元，打 5 折\n活动时间：2017-10-30 11:55/2017-11-29 11:55/n在法律允许的范围内，皕鲜拥有对此活动的解释权";
//                        = transInfo.getDetail().replaceAll("\\n", "\n");
                String detailStr = transInfo.getDetail().replace("\\n", "\n");
                SXUtils.getInstance(activity).ConfrimDialogView(activity,false,""+transInfo.getSlogan(), ""+detailStr, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                SXUtils.getInstance(activity).tipDialog.dismiss();
                    }
                });
            }
        });
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