package com.bixian365.dzc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.TypeInfoRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.goodsinfo.GoodsInfoEntity;
import com.bixian365.dzc.entity.goodstype.GoodsDataSetEntity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;
import com.bixian365.dzc.utils.view.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchDetailActivity extends BaseActivity{
private Activity activity;
    private RecyclerView recyclerView;
    private String searchValueStr;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private Handler hand;
    private int indexPage=0;
    private TextView carNum;
    private FrameLayout frameLay;
    private TextView totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_detail);
        activity = this;
        searchValueStr = this.getIntent().getStringExtra("searchValue");
        initView();
        EventBus.getDefault().register(this);
        GetGoodsTypeInfoHttp(searchValueStr+"");
    }
    private void initView(){
        totalPrice = (TextView) findViewById(R.id.goods_detail_car_price_tv);
        TextView  gocar = (TextView) findViewById(R.id.goods_detail_gocar_btn);
        gocar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100011,"search"));
                AppClient.TAG4 = true;
                finish();
            }
        });
        frameLay = (FrameLayout) findViewById(R.id.search_goods_detial_car_num_liny);
        frameLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MainFragmentActivity.carRb.setChecked(true);
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100011,"search"));
                AppClient.TAG4 = true;
                finish();
//                SXUtils.getInstance(activity).finishActivity();
            }
        });
//        carNum = (TextView) findViewById(R.id.search_goods_detail_car_num_tv);
        carNum = (TextView) findViewById(R.id.goods_detail_car_num_tv);

//        if(MainFragmentActivity.getInstance().getBadgeNum() == 0){
//            carNum.setVisibility(View.GONE);
//        }else{
//            carNum.setVisibility(View.VISIBLE);
//            if(MainFragmentActivity.getInstance().getBadgeNum() >99){
//                carNum.setText(99+"+");
//            }else{
//                carNum.setText(MainFragmentActivity.getInstance().getBadgeNum()+"");
//            }
//        }
        SXUtils.getInstance(activity).setGoodsBadeNum(carNum);
        totalPrice.setText("¥"+MainFragmentActivity.totalCarPrice+"");
        mSwipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.search_detail_swipyrefreshlayout);
        SXUtils.getInstance(activity).setColorSchemeResources(mSwipyRefreshLayout);
        mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP){
                    indexPage = 0;
//                    hand.sendEmptyMessage(1);
                    GetGoodsTypeInfoHttp(searchValueStr+"");
//                    HttpLiveSp(indexPage);
                }else{
//                    hand.sendEmptyMessage(1);
                    indexPage ++;
                    GetGoodsTypeInfoHttp(searchValueStr+"");
//                    HttpLiveSp(indexPage);
                }
            }
        });

        EditText  searchEdt = (EditText) findViewById(R.id.search_detail_editv);
        searchEdt.setText(searchValueStr+"");
        //点击按下键盘搜索按键事件监听处理
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    if(!TextUtils.isEmpty(arg0.getText().toString()))
                    GetGoodsTypeInfoHttp(arg0.getText().toString());
                    else
                    Toast.makeText(activity,"请输入商品名称",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        LinearLayout backLin = (LinearLayout) findViewById(R.id.search_detail_goback_linlay);
        backLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.search_detail_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        final TypeInfoRecyclerViewAdapter simpAdapter = new TypeInfoRecyclerViewAdapter(activity,getTypeInfoData());
//        recyclerView.setAdapter(simpAdapter);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    //热词搜索成功
                    case 1000:
                        List<GoodsInfoEntity> goodsDetaiLIst = (List<GoodsInfoEntity>) msg.obj;
                        if(goodsDetaiLIst == null || goodsDetaiLIst.size()<=0) {
                            break;
                        }
                        if(goodsDetaiLIst.size() >9){
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
                        }else{
                            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
                        }
                        findViewById(R.id.wallet_default_search_lin).setVisibility(View.GONE);
                        TypeInfoRecyclerViewAdapter   simpAdapter = new TypeInfoRecyclerViewAdapter(activity,goodsDetaiLIst,carNum);
                        recyclerView.setAdapter(simpAdapter);
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
                        break;
                }
                if(mSwipyRefreshLayout != null){
                    mSwipyRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }

    /**
     * 商品分类详情商品
     * @return
     */
    private List<GoodsInfoEntity> getTypeInfoData()
    {
        List<GoodsInfoEntity> typeList=new ArrayList<>();
        for(int i=0;i<10;i++){
            GoodsInfoEntity type = new GoodsInfoEntity();
            switch (i){
                case 0:
                    type.setGoodsName("鸡肉");
                    break;
                case 1:
                    type.setGoodsName("鲜蔬菜");
                    break;
                case 2:
                    type.setGoodsName("豆芽");
                    break;
                case 3:
                    type.setGoodsName("牛肉");
                    break;
                case 4:
                    type.setGoodsName("鸭肉");
                    break;
                default:
                    type.setGoodsName("西瓜");

            }
            typeList.add(type);

        }
        return typeList;
    }
    public void GetGoodsTypeInfoHttp(String goodsName) {
        if (recyclerView != null)
            recyclerView.setAdapter(null);
        HttpParams httpParams = new HttpParams();
        httpParams.put("goodsName", goodsName);
//        httpParams.put("categoryCode",cid);
        HttpUtils.getInstance(activity).requestPost(true, AppClient.GOODS_LIST, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                String jsobj = "";
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                    jsobj = jsonObject1.getString("responseData");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GoodsDataSetEntity gde = (GoodsDataSetEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsobj, GoodsDataSetEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde.getDataset();
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
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMoonEvent(MessageEvent messageEvent){
            if(messageEvent.getTag()==AppClient.EVENT10007){
                if(null !=  MainFragmentActivity.badge1)
                MainFragmentActivity.badge1.show();
                SXUtils.getInstance(activity).setGoodsBadeNum(carNum);
                totalPrice.setText("¥"+messageEvent.getMessage()+"");
            }else  if(messageEvent.getTag()==AppClient.EVENT100011){
                finish();
            }
        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
            OkHttpUtils.getInstance().cancelTag(this);
        }

}
