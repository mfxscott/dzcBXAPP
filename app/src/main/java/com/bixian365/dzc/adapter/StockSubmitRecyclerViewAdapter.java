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
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.orderlist.OrderGoodsInfoEntity;
import com.bixian365.dzc.utils.SXUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 订单缺货少货上报
 * @author mfx
 * @time  2017/10/10 16:42
 */
public class StockSubmitRecyclerViewAdapter
        extends RecyclerView.Adapter<StockSubmitRecyclerViewAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();

    private int mBackground;
    private  List<OrderGoodsInfoEntity> mValues=null;
    private Context context;
    public Map<String,String> editNumMap = new HashMap<>();



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView  name;
        public  final TextView  number;
        public  final EditText   sendNum;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) mView.findViewById(R.id.stock_submit_item_name);
            number = (TextView) mView.findViewById(R.id.stock_submit_item_num);
            sendNum = (EditText) mView.findViewById(R.id.stock_submit_item_sendnum);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public StockSubmitRecyclerViewAdapter(Context context,  List<OrderGoodsInfoEntity> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        initStoreDate();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_submit_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final  OrderGoodsInfoEntity orderInfo = mValues.get(position);
        holder.name.setText(orderInfo.getSkuName());
        holder.number.setText(orderInfo.getSkuNumber()+"");
        holder.sendNum.setText(orderInfo.getSkuNumber()+"");
        holder.sendNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    SXUtils.getInstance(context).ToastCenter("请输入收货数量");
                    editNumMap.put(position+"","");
                }else{
                    editNumMap.put(position+"",s.toString());
                }


//                SXUtils.getInstance(context).ToastCenter(s.toString()+"");
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",gcpurchase.getId());
//                holder.mView.getContext().startActivity(intent);
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
    /**
     * 初始化给map赋值
     */
    public void initStoreDate() {
        for (int i = 0; i < mValues.size(); i++) {
            editNumMap.put(""+i,mValues.get(i).getSkuNumber()+"");
        }

    }
    public JSONArray getInputNum(){
        String inputNum = "";
        JSONArray jsonArray = new JSONArray();
        Iterator<String> iter = editNumMap.keySet().iterator();
        for(int i=0;i<mValues.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                inputNum = editNumMap.get(key);
                if(TextUtils.isEmpty(inputNum)){
                    return null;
                }
                JSONObject jsonObjct = new JSONObject();
                try {
                    jsonObjct.put("id",mValues.get(i).getId());
                    jsonObjct.put("deliveredNumber",inputNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(jsonObjct);
            }
        }
        return jsonArray;
    }
}