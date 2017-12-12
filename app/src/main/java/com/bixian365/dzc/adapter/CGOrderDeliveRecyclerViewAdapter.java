package com.bixian365.dzc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.cgListInfo.CGPurchaseLinesEntity;
import com.bixian365.dzc.utils.SXUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合伙人发货界面，
 * @author mfx
 */
public class CGOrderDeliveRecyclerViewAdapter
        extends RecyclerView.Adapter<CGOrderDeliveRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();

    private int mBackground;
    private List<CGPurchaseLinesEntity> mValues=null;
    private Context context;
    public Map<String,String> editNumMap = new HashMap<>();


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public ImageView itemImg;//
        public TextView name; //商品名称商品图片
        public TextView modelPrice;
        public TextView markPrice;//市场价
        public EditText sendNum;//输入商品数量
        public TextView actual_num;//已发货数量

        public ViewHolder(View view) {
            super(view);
            mView = view;
            itemImg = (ImageView) mView.findViewById(R.id.cg_order_detail_iv);
            name = (TextView) mView.findViewById(R.id.cg_order_goodsname_tv);
            modelPrice = (TextView) mView.findViewById(R.id.cg_order_detail_modelprice_tv);
            markPrice = (TextView) mView.findViewById(R.id.cg_order_detail_shopprice_tv);
            sendNum = (EditText) mView.findViewById(R.id.cg_delive_submit_item_sendnum);
            actual_num = (TextView) mView.findViewById(R.id.cg_delive_actual_num);
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }

    public CGOrderDeliveRecyclerViewAdapter(Context context, List<CGPurchaseLinesEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gc_order_delive_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CGPurchaseLinesEntity gcpurchase = mValues.get(position);
        holder.name.setText(gcpurchase.getGoodsName());
        holder.modelPrice.setText(gcpurchase.getGoodsPrice()+"/"+gcpurchase.getGoodsUnit());
       final int sendNum = FloatOrInt(gcpurchase.getGoodsNumber())-FloatOrInt(gcpurchase.getActualNumber());
        holder.actual_num.setText("已发货数量 "+gcpurchase.getActualNumber()+"");
        holder.sendNum.setText(sendNum+"");
//        holder.markPrice.setText(gcpurchase.getGoodsPrice());
        holder.markPrice.setText("¥"+gcpurchase.getTotalAmount());

//        holder.markPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        SXUtils.getInstance(context).GlideSetImg(gcpurchase.getThumbImg(),holder.itemImg);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",gcpurchase.getId());
//                holder.mView.getContext().startActivity(intent);
            }
        });
        editNumMap.put(position+"",sendNum+"");
        holder.sendNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s))
                    return;
                if(FloatOrInt(s.toString())>sendNum){
                   SXUtils.getInstance(context).ToastCenter("发货数量不能大于剩余发货数量");
                    holder.sendNum.setText(sendNum+"");
                }else{
                    editNumMap.put(position+"",s.toString());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    public JSONArray getInputNum(){
        String inputNum = "";
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<mValues.size();i++){
            JSONObject jsonObjct=null;
                inputNum = editNumMap.get(i+"");
                if(TextUtils.isEmpty(inputNum)){
                    return null;
                }
                 jsonObjct = new JSONObject();
                try {
                    jsonObjct.put("skuCode", mValues.get(i).getSkuCode());
                    jsonObjct.put("actualNumber",inputNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            jsonArray.put(jsonObjct);
        }
        return jsonArray;
    }
    private int  FloatOrInt(String str){
        if(TextUtils.isEmpty(str))
            return 0;
       float  fstr =   Float.parseFloat(str);
        Float f = new Float(fstr);
        return f.intValue();
    }
}