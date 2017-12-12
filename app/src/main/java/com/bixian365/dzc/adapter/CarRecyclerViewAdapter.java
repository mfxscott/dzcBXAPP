package com.bixian365.dzc.adapter;//package com.xianhao365.o2o.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v7.widget.RecyclerView;
//import android.util.TypedValue;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.xianhao365.o2o.R;
//import com.xianhao365.o2o.entity.car.ShoppingCartLinesEntity;
//import com.xianhao365.o2o.fragment.MainFragmentActivity;
//import com.xianhao365.o2o.utils.Logs;
//import com.xianhao365.o2o.utils.SXUtils;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// * 购物车中店铺内的商品
// * @author mfx
// */
//public  class CarRecyclerViewAdapter
//        extends RecyclerView.Adapter<CarRecyclerViewAdapter.ViewHolder> {
//
//    private final TypedValue mTypedValue = new TypedValue();
//    private int mBackground;
//    public List<ShoppingCartLinesEntity> mValues;
//    private Context context;
//    private Map<String,Boolean>  goodsMap = new HashMap<String ,Boolean>();
//    public int total=0;//统计选择总条数
//    private TextView numTv;
//    private ShoppingCartLinesEntity  shopCarinfo;
//    private int storePostion;//当前店铺索引
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public final View mView;
//        public final ImageView mImageView;
//        public final TextView nameTv;
//        public final CheckBox  checkbox;
//        public final TextView  sub;
//        public final TextView  number;
//        public final TextView  add;
//        public final TextView modelTv;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mImageView = (ImageView) view.findViewById(R.id.car_item_iv);
//            nameTv = (TextView) view.findViewById(R.id.car_item_name);
//            checkbox = (CheckBox) view.findViewById(R.id.car_item_checkbox);
//            sub = (TextView) view.findViewById(R.id.car_item_sub_tv);
//            number = (TextView) view.findViewById(R.id.car_item_num_edt);
//            add = (TextView) view.findViewById(R.id.car_item_add_tv);
//            modelTv = (TextView) view.findViewById(R.id.car_item_model_tv);
//        }
//        @Override
//        public String toString() {
//            return super.toString() + " '" + nameTv.getText();
//        }
//    }
//    public CarRecyclerViewAdapter(Context context, List<ShoppingCartLinesEntity> items,int position, TextView numTv) {
//        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
//        mBackground = mTypedValue.resourceId;
//        mValues = items;
//        this.context = context;
//        this.numTv = numTv;
//        this.storePostion = position;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.car_item, parent, false);
//        view.setBackgroundResource(mBackground);
//        return new ViewHolder(view);
//    }
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        shopCarinfo = mValues.get(position);
//        holder.nameTv.setText(shopCarinfo.getGoodsName());
//        holder.modelTv.setText("￥"+shopCarinfo.getSkuPrice()+"/"+shopCarinfo.getGoodsModel());
//        holder.number.setText(shopCarinfo.getQuantity()+"");
//        MainFragmentActivity.totalCarNum += Integer.parseInt(shopCarinfo.getQuantity());
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                SXUtils.getInstance(context).ToastCenter("===="+position);
//            }
//        });
//
//        holder.checkbox.setOnCheckedChangeListener(null);
//        holder.checkbox.setChecked(goodsMap.get(position+""));
//        getKeyValue();
//        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                goodsMap.put(position+"",isChecked);
//                if(isChecked){
//                    if(mValues.size() != total){
//                        total++;
//                    }
//                }else{
//                    if(total != 0){
//                        total--;
//                    }
//                }
//                numTv.setText("已选"+total+"项");
//                if(getAllItem()){
//                    new CarStoreRecyclerViewAdapter.ViewHolder(storePostion);
//                }
//            }
//        });
//        holder.sub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                carNum(false,1,holder.number);
//
//            }
//        });
//        holder.add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                carNum(true,1,holder.number);
//
//            }
//        });
//
////        if(position%2 ==0){
////            Glide.with(holder.mImageView.getContext()).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.img_dg).into(holder.mImageView);
////        }else{
////            Glide.with(holder.mImageView.getContext()).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.img_gdy).into(holder.mImageView);
////        }
//        SXUtils.getInstance(context).GlideSetImg(shopCarinfo.getGoodsImage(),holder.mImageView);
////        Glide.with(holder.mImageView.getContext())
////                .load("http://img4.imgtn.bdimg.com/it/u=3071322373,3354763627&fm=28&gp=0.jpg")
////                .fitCenter()
////                .into(holder.mImageView);
//    }
//    /**
//     * 判断是否是减，还是加入购物车
//     * @param issub  true 增加
//     * @param strcount  增加条数
//     */
//    public void carNum(boolean issub,int strcount,TextView textView){
//        String nowsize = textView.getText().toString();
//        int carTotalNum = Integer.parseInt(nowsize);
//        if(issub){
//            carTotalNum = carTotalNum+1;
//            if(carTotalNum >= 100){
//                textView.setText("99+");
//            }else{
//                textView.setText(carTotalNum+"");
//            }
//            textView.setTextColor(Color.BLACK);
//            SXUtils.getInstance(context).AddOrUpdateCar(shopCarinfo.getSkuBarcode(),"1");
//        }else{
//            carTotalNum = carTotalNum-1;
//            if(carTotalNum > 0){
//                if(carTotalNum <= 99){
//                    textView.setText(carTotalNum+"");
//                }else{
//                    textView.setText("99+");
//                }
//            }else{
//                textView.setText("0");
//                textView.setTextColor(Color.TRANSPARENT);
//            }
//            if(carTotalNum >= 0)
//                SXUtils.getInstance(context).AddOrUpdateCar(shopCarinfo.getSkuBarcode(),"0");
//        }
//    }
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }
//    @Override
//    public int getItemViewType(int position) {
////        Log.i("========",position+"");
//        return super.getItemViewType(position);
//    }
//    //  添加数据
//    public void addData(int position) {
////      在list中添加数据，并通知条目加入一条
//        notifyItemInserted(position);
//    }
//    public  void  getKeyValue(){
//        Iterator<String> iter = goodsMap.keySet().iterator();
//        while(iter.hasNext()){
//            String key=iter.next();
//            Boolean value = goodsMap.get(key);
//        }
//    }
//    //  删除数据
//    public void removeData() {
//        int i=0;
//        Iterator<String> iter = goodsMap.keySet().iterator();
//        while(iter.hasNext()){
//            String key=iter.next();
//            Boolean value = goodsMap.get(key);
//            i++;
//            if(value){
//                int postions = Integer.parseInt(key);
//                Logs.i(mValues.size()+"删除的key是多少====","==="+postions+"====="+i++);
//
//                goodsMap.remove(postions);
//                mValues.remove(mValues.size() == i ?i -1:i);
//            }
//            System.out.println(key+" "+value);
//        }
//        goodsMap.clear();
//        initDate();
//        notifyDataSetChanged();
//    }
//    //  删除数据
//    public void removeAllData() {
//        for(int i=0;i<mValues.size();i++){
//            goodsMap.remove(i);
//            mValues.remove(i);
//            notifyItemRemoved(i);
//        }
//        goodsMap.clear();
//        initDate();
//        notifyDataSetChanged();
//
//    }
//    /**
//     * 全选商品
//     */
//    public void selectAll() {
//        for (int i = 0; i < mValues.size(); i++) {
//            goodsMap.put(""+i,true);
//        }
//        total =mValues.size();
////        numTv.setText("已选"+total+"项");
//        notifyDataSetChanged();
//    }
//    /**
//     * 初始化
//     */
//    public void initDate() {
//        for (int i = 0; i < mValues.size(); i++) {
//            goodsMap.put(""+i,false);
//        }
//        total = 0;
////        numTv.setText("已选"+total+"项");
//        notifyDataSetChanged();
//
//    }
//
//    /**
//     * 判断当前店铺下的商品是否全部选中
//     * @return
//     */
//    public boolean getAllItem(){
//        for(int i =0;i<goodsMap.size();i++){
//            if(!goodsMap.get(i)){
//               return false;
//            }
//        }
//        return true;
//    }
//}