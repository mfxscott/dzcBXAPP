package com.bixian365.dzc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.car.CarList;
import com.bixian365.dzc.entity.car.ShoppingCartLinesEntity;
import com.bixian365.dzc.entity.car.ShoppingListEntity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.fragment.car.CarNumPopupWindow;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.MyFoodActionCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

/**
 * 购物车中的店铺
 */
public  class CarStoreRecyclerViewAdapter
        extends RecyclerView.Adapter<CarStoreRecyclerViewAdapter.ViewHolder> {
    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    public List<ShoppingListEntity> mValues;
    private Context context;

    private ShoppingListEntity  shopCarinfo;
    private TextView delNumTv;
    public  CarRecyclerViewAdapter simpAdapter;
    private CarList carInfo;
    public int itemTotal;//总商品条数

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mImageView;
        public final TextView nameTv;
        public final CheckBox  checkbox;
        public final RecyclerView recyclerView;
        public final  TextView  freightTv;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (TextView) view.findViewById(R.id.car_item_store_flag_iv);
            nameTv = (TextView) view.findViewById(R.id.car_item_store_name);
            checkbox = (CheckBox) view.findViewById(R.id.car_item_store_checkbox);
            recyclerView = (RecyclerView) view.findViewById(R.id.car_goods_recyclerv);
            freightTv = (TextView) view.findViewById(R.id.car_item_freight_tv);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + nameTv.getText();
        }
    }
    public CarStoreRecyclerViewAdapter(Context context, List<ShoppingListEntity> items,TextView delNumTv,CarList carInfo) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        this.context = context;
        this.delNumTv = delNumTv;
        this.carInfo = carInfo;
        if(!AppClient.isSubAdd)
        {selectStoreAll();}
        //当时多选删除  重新设置checkbox 未未选中
        if(AppClient.isDelCarGoods){
            initStoreDate();
        }

    }
    public void setItmeList(Context context, List<ShoppingListEntity> items,TextView delNumTv){
        mValues = items;
        this.context = context;
        this.delNumTv = delNumTv;
//        notifyItemRangeInserted(0,items.size());
        notifyDataSetChanged();
        selectStoreAll();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_store_item, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        shopCarinfo = mValues.get(position);
        holder.nameTv.setText(shopCarinfo.getShopName()+"");
//        if(shopCarinfo.getIsOwner().equals("1")){
//            holder.mImageView.setVisibility(View.VISIBLE);
//        }else{
//            holder.mImageView.setVisibility(View.GONE);
//        }
        holder.freightTv.setText(carInfo.getOrderNotice()+"");
        if(carInfo.getOrderFulfilment().equals("0")){
            holder.freightTv.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            holder.freightTv.setTextColor(context.getResources().getColor(R.color.col_999));
        }
        holder.freightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainFragmentActivity.goodsRb.setChecked(true);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent( holder.mView.getContext(), GoodsDetailActivity.class);
//                intent.putExtra("cno",mValues.get(position).getShoppingCartLines().get(position).getGoodsCode());
//                holder.mView.getContext().startActivity(intent);
            }
        });
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (simpAdapter == null) {
            //当第一次进入是才加载店铺中的商品，后面notifychange不设置子商品，房子店铺刷新子view重置
            if (shopCarinfo.getShoppingCartLines() != null && shopCarinfo.getShoppingCartLines().size() > 0) {
                simpAdapter = new CarRecyclerViewAdapter(context, shopCarinfo.getShoppingCartLines(), position, delNumTv);
                holder.recyclerView.setAdapter(simpAdapter);
                if (AppClient.isSubAdd)
                    AppClient.isSubAdd = false;
                else
                    simpAdapter.selectAll();

                //当为true是标识为删除界面，
                if (AppClient.isDelCarGoods) {
                    simpAdapter.initDate();
                }
            }
        }
//        if (getStoreAllItem()) {
//            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10005, "1"));
//        } else {
//            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10005, "0"));
//        }

        holder.checkbox.setOnCheckedChangeListener(null);
        if (AppClient.isDelCarGoods) {
            holder.checkbox.setChecked(AppClient.storeMap.get(position + ""));
        } else {
            if (isAllCheckGoods(shopCarinfo.getShoppingCartLines())) {
                holder.checkbox.setChecked(true);
            }else{
                holder.checkbox.setChecked(false);
            }
        }

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(AppClient.isDelCarGoods) {
                    AppClient.storeMap.put(position + "", isChecked);
                    //获取到点击店铺check的商品 按钮数量
                    int storenum = Integer.parseInt(delNumTv.getText().toString());
                    if (isChecked) {
                        simpAdapter.selectAll();
                        delNumTv.setText(getCheckTrue() + "");
                    } else {
                        simpAdapter.initDate();
                        delNumTv.setText(getCheckTrue() + "");
                    }
                    EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10006, "car"));
                }else{
                    if(isChecked){
//                        SXUtils.showMyProgressDialog(context,false);
                        SXUtils.showMyProgressDialog(context,true);
                        SXUtils.getInstance(context).CheckBoxCar(getSkuState(shopCarinfo.getShoppingCartLines(),"1"));
                    }else{
                        SXUtils.showMyProgressDialog(context,true);
//                        SXUtils.showMyProgressDialog(context,false);
                        SXUtils.getInstance(context).CheckBoxCar(getSkuState(shopCarinfo.getShoppingCartLines(),"0"));
                    }
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
    //  删除数据
    public void removeAllData() {
        for(int i=0;i<mValues.size();i++){
            AppClient.storeMap.remove(i);
            mValues.remove(i);
            notifyItemRemoved(i);
        }
        AppClient.storeMap.clear();
        initStoreDate();
        notifyDataSetChanged();


    }
    //子商品全部选中时调用 选择店铺多选框
    public void  addCheckBox(int postion,boolean value){
        AppClient.storeMap.put(postion+"",value);
        notifyDataSetChanged();

    }
    /**
     * 全选商品
     */
    public void selectStoreAll() {
        for (int i = 0; i < mValues.size(); i++) {
            AppClient.storeMap.put(""+i,true);
        }
        if(simpAdapter != null)
            simpAdapter.selectAll();
        itemTotal =mValues.size();
        delNumTv.setText(itemTotal+"");
        notifyDataSetChanged();
    }
    /**
     * 初始化
     */
    public void initStoreDate() {
        for (int i = 0; i < mValues.size(); i++) {
            AppClient.storeMap.put(""+i,false);
            if(simpAdapter != null)
                simpAdapter.initDate();

        }
        itemTotal = 0;
        delNumTv.setText(itemTotal+"");
        Logs.i("size大小============"+mValues.size());
        notifyDataSetChanged();

    }
    /**
     * 得到所有商品被选中的条数
     * @return
     */
    public String getCheckTrue(){
        int priceTotal = 0;
        Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
        for(int i=0;i<mValues.size();i++){
            while (iter.hasNext()) {
                String key = iter.next();
                Boolean value = AppClient.goodsMap.get(key);
                if(value){
                    priceTotal ++;

                }
            }
        }
        return priceTotal+"";
    }
    /**
     * 判断当前店铺下
     * 循环遍历店铺被全部选中chexkBox为选中状态，反之
     * @return
     */
    public boolean getStoreAllItem(){
        Iterator<String> iter = AppClient.storeMap.keySet().iterator();
        while(iter.hasNext()){
            String key=iter.next();
            Boolean value = AppClient.storeMap.get(key);
            if(!value){
                return false;
            }
        }
        return true;
    }
    //*********店铺商品中的item****************************************************************************************
    public  class CarRecyclerViewAdapter
            extends RecyclerView.Adapter<CarRecyclerViewAdapter.GoodsViewHolder> {
        private FoodActionCallback callback;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        public List<ShoppingCartLinesEntity> mValues;
        private Context context;
        public int total=0;//统计选择总条数
        private TextView numTv;
        private int storePostion;//当前店铺索引
        private CarNumPopupWindow carNumPopupWindow;
        private String skuCodeStr;
        private String skuNumStr;
        private String goodsNameStr;
        private int goodsPostion=-1;//商品当前位置
        public  class GoodsViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final TextView nameTv;
            public final CheckBox  checkbox;
            public final TextView  sub;
            public final TextView  number;
            public final TextView  add;
            public final TextView modelTv;
            public final  TextView marketPrice;
            public final LinearLayout  numLin;


            public GoodsViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.car_item_iv);
                nameTv = (TextView) view.findViewById(R.id.car_item_name);
                checkbox = (CheckBox) view.findViewById(R.id.car_item_checkbox);
                sub = (TextView) view.findViewById(R.id.car_item_sub_tv);
                number = (TextView) view.findViewById(R.id.car_item_num_edt);
                add = (TextView) view.findViewById(R.id.car_item_add_tv);
                modelTv = (TextView) view.findViewById(R.id.car_goods_model_tv);
                marketPrice = (TextView) view.findViewById(R.id.type_info_price_tv);
                numLin = (LinearLayout) view.findViewById(R.id.car_add_sub_num_liny);

            }
            @Override
            public String toString() {
                return super.toString() + " '" + nameTv.getText();
            }
        }
        public CarRecyclerViewAdapter(Context context, List<ShoppingCartLinesEntity> items,int position, TextView numTv) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.context = context;
            this.numTv = numTv;
            this.storePostion = position;
        }

        @Override
        public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.car_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new GoodsViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final GoodsViewHolder holder, final int position) {
            final ShoppingCartLinesEntity shopCarinfo = mValues.get(position);
            holder.nameTv.setText(shopCarinfo.getGoodsName());
//            holder.modelTv.setText("/" + shopCarinfo.getGoodsModel(shopCarinfo.getGoodsUnit()));
            holder.number.setText(shopCarinfo.getQuantity() + "");
            holder.marketPrice.setText("¥" + shopCarinfo.getSkuPrice());
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(holder.mView.getContext(), GoodsDetailActivity.class);
                    intent.putExtra("cno", shopCarinfo.getGoodsId());
                    holder.mView.getContext().startActivity(intent);
                }
            });
            holder.number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    carNumPopupWindow = new CarNumPopupWindow((Activity) context, ShareOnclick);
                    //显示窗口
                    carNumPopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    skuNumStr = shopCarinfo.getQuantity();
//                    skuCodeStr = shopCarinfo.getSkuBarcode(shopCarinfo.getChirdren().get(0).getShopPrice());
                    goodsNameStr = shopCarinfo.getGoodsName();
                    goodsPostion = position;
                }
            });

            holder.checkbox.setOnCheckedChangeListener(null);
            if (AppClient.isDelCarGoods) {
                holder.checkbox.setChecked(AppClient.goodsMap.get(position + ""));
            } else{
                if (shopCarinfo.getIsChecked().equals("1")) {
                    holder.checkbox.setChecked(true);
                } else {
                    holder.checkbox.setChecked(false);
                }
            }

            getKeyValue();
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (AppClient.isDelCarGoods) {
                        total = Integer.parseInt(numTv.getText().toString());
                        AppClient.goodsMap.put(position + "", isChecked);
                        if (isChecked) {
                            if (mValues.size() != total) {
                                total++;
                            }
                        } else {
                            if (total != 0) {
                                total--;
                            }
                        }
                        numTv.setText(total + "");
                        //循环遍历如果店铺想的商品被全部选中则店铺chexkBox为选中状态，反之
                        if (getAllItem()) {
                            addCheckBox(storePostion, true);
                        } else {
                            addCheckBox(storePostion, false);
                        }
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10006, "car"));
                    }else{
                        if(isChecked){
                            SXUtils.showMyProgressDialog(context,false,1);
//                            SXUtils.getInstance(context).CheckBoxCar(shopCarinfo.getSkuBarcode(goodsinfo.getChirdren().get(0).getShopPrice())+":1");
                        }else{
                            SXUtils.showMyProgressDialog(context,false,1);
//                            SXUtils.getInstance(context).CheckBoxCar(shopCarinfo.getSkuBarcode(goodsinfo.getChirdren().get(0).getShopPrice())+":0");
                        }
                    }
                }
            });
//            holder.sub.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    goodsPostion = position;
//                    carNum(false,holder.number,shopCarinfo);
//
//                }
//            });
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsPostion = position;
                    carNum(true,holder.number,shopCarinfo);

                }
            });
            SXUtils.getInstance(context).GlideSetImg(shopCarinfo.getGoodsImage(),holder.mImageView);

        }
        /**
         * 判断是否是减，还是加入购物车
         * @param issub  true 增加
         */
        public void carNum(boolean issub, TextView textView, final ShoppingCartLinesEntity  shopcar){
            String nowsize = textView.getText().toString();
            int carTotalNum = Integer.parseInt(nowsize);
            AppClient.isDelCarGoods = false;
            if(issub){
                SXUtils.showMyProgressDialog(context,false,1);
                textView.setText((carTotalNum++)+"");
//                SXUtils.getInstance(context).AddOrUpdateCar(shopcar.getSkuBarcode(),"1");
//                callback = new MyFoodActionCallback((Activity) context,shopcar.getSkuBarcode(goodsinfo.getChirdren().get(0).getShopPrice()));
                callback.addAction(textView);
                AppClient.isSubAdd=true;
                AppClient.goodsMap.put(""+goodsPostion,true);
            }else{
                if(carTotalNum == 1){
                    SXUtils.getInstance(context).MyDialogView(context,"温馨提示!", "是否删除"+shopcar.getGoodsName()+"?", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SXUtils.getInstance(context).tipDialog.dismiss();
                            SXUtils.showMyProgressDialog(context,false);
//                            SXUtils.getInstance(context).AddOrUpdateCar(shopcar.getSkuBarcode(goodsinfo.getChirdren().get(0).getShopPrice()),"0");
                            AppClient.goodsMap.clear();
                            AppClient.storeMap.clear();
                        }
                    });
                }else {

                    SXUtils.showMyProgressDialog(context,false,1);
                    textView.setText((carTotalNum - 1) + "");
//                    SXUtils.getInstance(context).AddOrUpdateCar(shopcar.getSkuBarcode(goodsinfo.getChirdren().get(0).getShopPrice()),"-1");
                    AppClient.isSubAdd=true;
                    AppClient.goodsMap.put(""+goodsPostion,true);
                }
            }

            if(getAllItem()){
                addCheckBox(storePostion,true);
            }else{
                addCheckBox(storePostion,false);
            }

        }
        @Override
        public int getItemCount() {
            return mValues.size();
        }
        @Override
        public int getItemViewType(int position) {
//        Log.i("========",position+"");
            return super.getItemViewType(position);
        }
        public  void  getKeyValue(){
            Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
            while(iter.hasNext()){
                String key=iter.next();
                Boolean value = AppClient.goodsMap.get(key);
            }
        }
        View.OnClickListener ShareOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carNumPopupWindow != null)
                    carNumPopupWindow.dismiss();
                switch (v.getId()) {
                    case R.id.car_input_num_cancel_tv:
                        break;
                    case R.id.car_input_num_confrim_tv:
                        String s  = carNumPopupWindow.getInputNum();
                        if(TextUtils.isEmpty(s.toString()) || s.toString().equals("0")){
                            SXUtils.getInstance(context).MyDialogView(context,"温馨提示!", "是否删除"+goodsNameStr+"?", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SXUtils.getInstance(context).tipDialog.dismiss();
                                    SXUtils.showMyProgressDialog(context,false);
                                    SXUtils.getInstance(context).AddOrUpdateCar(skuCodeStr,"0");
                                    AppClient.isDelCarGoods = false;
                                }
                            });
                        }else{
                            int num = Integer.parseInt(s.toString());
                            int carNum = Integer.parseInt(skuNumStr);
                            int subNum=0;
                            if(num>carNum){
                                subNum =   num - carNum;
                                SXUtils.showMyProgressDialog(context,false);
                                SXUtils.getInstance(context).AddOrUpdateCar(skuCodeStr,subNum+"");
                            }else{
                                subNum = carNum -   num;
                                SXUtils.showMyProgressDialog(context,false);
                                SXUtils.getInstance(context).AddOrUpdateCar(skuCodeStr,"-"+subNum+"");
                            }
                        }
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10006,"car"));
                        break;
                }
            }
        };

//        //  删除数据
//        public void removeData() {
//            int i=0;
//            Iterator<String> iter = goodsMap.keySet().iterator();
//            while(iter.hasNext()){
//                String key=iter.next();
//                Boolean value = goodsMap.get(key);
//                i++;
//                if(value){
//                    int postions = Integer.parseInt(key);
//                    Logs.i(mValues.size()+"删除的key是多少====","==="+postions+"====="+i++);
//                    goodsMap.remove(postions);
//                    mValues.remove(mValues.size() == i ?i -1:i);
//                }
//                System.out.println(key+" "+value);
//            }
//            goodsMap.clear();
//            initDate();
//            notifyDataSetChanged();
//        }
        /**
         * 全选商品
         */
        public void selectAll() {
            for (int i = 0; i < mValues.size(); i++) {
                AppClient.goodsMap.put("" + i, true);
            }
            numTv.setText(total-getMapKeyNum()+mValues.size()+"");
            total =mValues.size();
            notifyDataSetChanged();
        }
        /**
         * 初始化
         */
        public void initDate() {
            for (int i = 0; i < mValues.size(); i++) {
                AppClient.goodsMap.put(""+i,false);
            }
            total = 0;
            notifyDataSetChanged();

        }

        /**
         * 获取店铺中商品被选中数量
         * @return
         */
        public int getMapKeyNum(){
            int num= 0;
            Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
            while(iter.hasNext()){
                String key=iter.next();
                Boolean value = AppClient.goodsMap.get(key);
                if(value){
                    num++;
                }
            }
            return num;
        }

        /**
         * 判断当前店铺下的商品是否全部选中,
         * 循环遍历如果店铺想的商品被全部选中则店铺chexkBox为选中状态，反之
         * @return true 为商品全选
         */
        public boolean getAllItem(){
            Iterator<String> iter = AppClient.goodsMap.keySet().iterator();
            while(iter.hasNext()){
                String key=iter.next();
                Boolean value = AppClient.goodsMap.get(key);
                if(!value){
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 获取选中店铺chekc 选中拼接所有商品sku
     * @param state
     * @param goodsCar
     * @return
     */
    public String getSkuState(List<ShoppingCartLinesEntity> goodsCar,String state){
        if(null == goodsCar && goodsCar.size()<=0)
        {
            return "";
        }
        String  skuStr="";
        for(int i=0;i<goodsCar.size();i++) {
//            skuStr += goodsCar.get(i).getSkuBarcode(goodsinfo.getChirdren().get(0).getShopPrice())+":"+state+"|";
        }

        return skuStr.substring(0,skuStr.length()-1);
    }

    /**
     * 判断是否全部选中 商品
     * @param goodsCar
     * @return  true  为全部选中  false 没有全部选中
     */
    public boolean isAllCheckGoods(List<ShoppingCartLinesEntity> goodsCar){
        boolean  skuStr=true;
        for(int i=0;i<goodsCar.size();i++) {
            if(goodsCar.get(i).getIsChecked().equals("0")){
                return false;
            }
        }
        return skuStr;
    }
}