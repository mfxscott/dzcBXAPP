package com.bixian365.dzc.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.adapter.BillItemRecyclerViewAdapter;
import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.goods.GoodsDetailInfoEntity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.ObservableScrollView;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.NXHooldeView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lzy.okhttputils.model.HttpParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品详情
 * @author mfx
 * @time  2017/7/17 15:53
 */
public class GoodsDetailActivity extends BaseActivity implements ObservableScrollView.ScrollViewListener,View.OnClickListener,FoodActionCallback {
    private  Banner banner;
    private RelativeLayout titleRelay,disTitleRel;
    private Activity activity;
    private LinearLayout xxxxlin;//商品详细信息
    private TextView  xxTv,ggTv;
    private View   xxLine,ggLine;
    private ObservableScrollView  scro;
    private Handler hand;
    private String goodsId;//商品id
    @BindView(R.id.goods_detail_add_tv)
    TextView addcar;
    @BindView(R.id.goods_detail_car_num_tv)
    TextView carNum;
    @BindView(R.id.goods_detail_name_tv)
    TextView goodsNameTv;//商品名称
    @BindView(R.id.goods_detail_pf_price_tv)
    TextView pfPriceTv;//商品批发价
    @BindView(R.id.goods_detail_marke_price_tv)
    TextView marketPriceTv;//商品市场价
    @BindView(R.id.goods_detail_model_tv)
    TextView goodsModelTv;//商品规格
    @BindView(R.id.goods_detail_address_tv)
    TextView goodsAddress;//商品产地
    @BindView(R.id.goods_detail_level_tv)
    TextView goodsLevel;//商品等级
    @BindView(R.id.goods_detail_unit_tv)
    TextView goodsUnit;//商品包装
    @BindView(R.id.goods_detail_gg_model_tv)
    TextView goodsggModel;//商品规格
    @BindView(R.id.goods_detail_car_price_tv)
    TextView totalPrice;
    @BindView(R.id.goods_detail_list_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.goods_detail_goodslist_recycler)
    RecyclerView goodsListRecyclerView;
    @BindView(R.id.goods_detail_bill)
    ImageView billImg;
    @BindView(R.id.goods_detail_bill_tv)
    TextView billTv;
    private String skuBarcode;
    private  GoodsDetailInfoEntity goodsdetail;
    private ImageView img1,img2,img3,img4,img5;
    private String setBill;//加入常用清单
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        goodsId = this.getIntent().getStringExtra("cno");
        activity = this;
        ButterKnife.bind(activity);
//        setBanner();
        initView();
        EventBus.getDefault().register(this);
        if(TextUtils.isEmpty(goodsId)) {
            SXUtils.getInstance(activity).ToastCenter("商品ID为空");
            return;
        }
        SXUtils.showMyProgressDialog(activity,true);
        getHttpGoodsDetail();
    }
    private void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        goodsListRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        goodsListRecyclerView.setItemAnimator(new DefaultItemAnimator());


        LinearLayout   tabXXXXLin = (LinearLayout) findViewById(R.id.goods_detail_tab_xxxx_lin);
        tabXXXXLin.setOnClickListener(this);
        LinearLayout    tabGGCSLin = (LinearLayout) findViewById(R.id.goods_detail_tab_ggcs_lin);
        tabGGCSLin.setOnClickListener(this);
        LinearLayout  backlin = (LinearLayout) findViewById(R.id.goods_detail_goback_linlay);
        LinearLayout  disBackLin = (LinearLayout) findViewById(R.id.goods_detail_dis_goback_linlay);
        backlin.setOnClickListener(this);
        disBackLin.setOnClickListener(this);
        TextView  feedback = (TextView) findViewById(R.id.goods_detail_feedback_tv);
        feedback.setOnClickListener(this);
        TextView  gocar = (TextView) findViewById(R.id.goods_detail_gocar_btn);
        gocar.setOnClickListener(this);
        addcar.setOnClickListener(this);
        billImg.setOnClickListener(this);
        xxTv = (TextView) findViewById(R.id.goods_detail_tab_xxxx_tv);
        ggTv = (TextView) findViewById(R.id.goods_detail_tab_ggcs_tv);
        xxLine = findViewById(R.id.goods_detail_tab_xxxx_line_v);
        ggLine = findViewById(R.id.goods_detail_tab_ggcs_line);


        scro = (ObservableScrollView) findViewById(R.id.goods_detail_scroll_view);
        scro.setScrollViewListener(this);
        titleRelay = (RelativeLayout) findViewById(R.id.goods_detial_title_rel);
        disTitleRel = (RelativeLayout) findViewById(R.id.goods_detail_dis_title_rel);
        xxxxlin = (LinearLayout) findViewById(R.id.goods_detail_xxxx_lin);



        img1 = (ImageView) findViewById(R.id.goods_detail_img1);
        img2 = (ImageView) findViewById(R.id.goods_detail_img2);
        img3 = (ImageView) findViewById(R.id.goods_detail_img3);
        img4 = (ImageView) findViewById(R.id.goods_detail_img4);
        img5 = (ImageView) findViewById(R.id.goods_detail_img5);

        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        goodsdetail = (GoodsDetailInfoEntity) msg.obj;
                        if(goodsdetail.getSkuList() != null && goodsdetail.getSkuList().size()>0) {
                            Logs.i("多规格商品数量========="+goodsdetail.getSkuList().size());
//                            goodsModelTv.setText("/"+goodsdetail.getSkuList().get(0).getGoodsModel());
//                            marketPriceTv.setText("¥ "+goodsdetail.getSkuList().get(0).getMarketPrice());
//                            pfPriceTv.setText("¥ "+goodsdetail.getSkuList().get(0).getShopPrice());
//                            goodsggModel.setText(goodsdetail.getSkuList().get(0).getGoodsModel());
//                            marketPriceTv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                            BillItemRecyclerViewAdapter simpAdapter = new BillItemRecyclerViewAdapter(activity,goodsdetail.getGoodsUnit(), goodsdetail.getSkuList(),carNum);
                            goodsListRecyclerView.setAdapter(simpAdapter);
                        }
                        if(goodsdetail.getIsCommonGoos().equals("1")){
                            billImg.setImageResource(R.mipmap.cancel_bill);
                            billTv.setText("踢出常用清单");
                            billTv.setTextColor(getResources().getColor(R.color.col_999));
                        }else{
                            billImg.setImageResource(R.mipmap.add_bill);
                            billTv.setText("加入常用清单");
                            billTv.setTextColor(getResources().getColor(R.color.qblue));
                        }
//                        if(MainFragmentActivity.getInstance().getBadgeNum() == 0){
//                            carNum.setVisibility(View.GONE);
//                        }else{
//                            carNum.setVisibility(View.VISIBLE);
//                            if(MainFragmentActivity.getInstance().getBadgeNum() >99){
//                                carNum.setText(99+"+");
//                            }else{
//                                carNum.setText(MainFragmentActivity.getInstance().getBadgeNum()+" ");
//                            }
//                        }
                        SXUtils.getInstance(activity).setGoodsBadeNum(carNum);
                        totalPrice.setText("¥"+MainFragmentActivity.totalCarPrice+"");

                        goodsNameTv.setText(goodsdetail.getGoodsName()+"");
                        goodsAddress.setText(goodsdetail.getGoodsPlace()+"");
                        goodsUnit.setText(goodsdetail.getPacking()+"");
                        goodsLevel.setText(goodsdetail.getFoodGrade()+"");
                        goodsggModel.setText(goodsdetail.getGoodsUnit()+"");
                        String [] desImg = goodsdetail.getGoodsDesc().split(",");
                        String [] bannerImg = goodsdetail.getAlbumImg().split(",");
//                        setInfoImg(desImg);
                        setBanner(bannerImg);
                        setInfoImg(desImg);
//                        if(MainFragmentActivity.totalCarNum == 0){
//                            carNum.setVisibility(View.GONE);
//                        }else{
//                            carNum.setVisibility(View.VISIBLE);
//                            if(MainFragmentActivity.totalCarNum >99){
//                                carNum.setText(99+"+");
//                            }else{
//                                carNum.setText(MainFragmentActivity.totalCarNum+"");
//                            }
//                        }
//                        recyclerView.setAdapter(new GoodsDetailInfoRecyclerViewAdapter(activity,desImg));
                        break;
                    case 1006:
                        if(setBill.equals("0")){
                            billImg.setImageResource(R.mipmap.add_bill);
                            billTv.setText("加入常用清单");
                            billTv.setTextColor(getResources().getColor(R.color.qblue));
                        }else{
                            billImg.setImageResource(R.mipmap.cancel_bill);
                            billTv.setText("踢出常用清单");
                            billTv.setTextColor(getResources().getColor(R.color.col_999));
                        }
                        goodsdetail.setIsCommonGoos(setBill);
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10002,"bill"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10003,"home"));
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
    private void  setInfoImg(String [] infoImg){
        for (int i=0;i<infoImg.length;i++){
            switch (i){
                case 0:
                    img1.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(infoImg[0]).asBitmap()
                            .priority( Priority.HIGH )//优先级
                            .placeholder(R.mipmap.loading_img)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap,GlideAnimation<? super Bitmap> glideAnimation) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    ViewGroup.LayoutParams lp = img1.getLayoutParams();
                                    lp.width = AppClient.fullWidth;
                                    float tempHeight=height * ((float)lp.width / width);
                                    lp.height =(int)tempHeight ;
                                    img1.setLayoutParams(lp);
                                    img1.setImageBitmap(bitmap);
                                }
                            });
                    break;
                case 1:
                    img2.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(infoImg[1]).asBitmap()
                            .priority( Priority.HIGH )//优先级
                            .placeholder(R.mipmap.loading_img)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap,GlideAnimation<? super Bitmap> glideAnimation) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    ViewGroup.LayoutParams lp = img2.getLayoutParams();
                                    lp.width = AppClient.fullWidth;
                                    float tempHeight=height * ((float)lp.width / width);
                                    lp.height =(int)tempHeight ;
                                    img2.setLayoutParams(lp);
                                    img2.setImageBitmap(bitmap);
                                }
                            });
                    break;
                case 2:
                    img3.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(infoImg[2]).asBitmap()
                            .priority( Priority.HIGH )//优先级
                            .placeholder(R.mipmap.loading_img)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap,GlideAnimation<? super Bitmap> glideAnimation) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    ViewGroup.LayoutParams lp = img3.getLayoutParams();
                                    lp.width = AppClient.fullWidth;
                                    float tempHeight=height * ((float)lp.width / width);
                                    lp.height =(int)tempHeight ;
                                    img3.setLayoutParams(lp);
                                    img3.setImageBitmap(bitmap);
                                }
                            });
                    break;
                case 3:
                    img4.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(infoImg[3]).asBitmap()
                            .priority( Priority.HIGH )//优先级
                            .placeholder(R.mipmap.loading_img)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap,GlideAnimation<? super Bitmap> glideAnimation) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    ViewGroup.LayoutParams lp = img4.getLayoutParams();
                                    lp.width = AppClient.fullWidth;
                                    float tempHeight=height * ((float)lp.width / width);
                                    lp.height =(int)tempHeight ;
                                    img4.setLayoutParams(lp);
                                    img4.setImageBitmap(bitmap);
                                }
                            });
                    break;
                case 4:
                    img5.setVisibility(View.VISIBLE);
                    Glide.with(activity)
                            .load(infoImg[4]).asBitmap()
                            .priority( Priority.HIGH )//优先级
                            .placeholder(R.mipmap.loading_img)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap bitmap,GlideAnimation<? super Bitmap> glideAnimation) {
                                    int width = bitmap.getWidth();
                                    int height = bitmap.getHeight();
                                    ViewGroup.LayoutParams lp = img5.getLayoutParams();
                                    lp.width = AppClient.fullWidth;
                                    float tempHeight=height * ((float)lp.width / width);
                                    lp.height =(int)tempHeight ;
                                    img5.setLayoutParams(lp);
                                    img5.setImageBitmap(bitmap);
                                }
                            });
                    break;
            }

        }
//        Glide.with(activity).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.img_dy).into(img1);
//        Glide.with(activity).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.img_hlg).into(img2);
//        Glide.with(activity).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.img_dg).into(img3);
//        Glide.with(activity).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.img_jd).into(img4);
    }
    private void setBanner(String [] bannerImg){
        banner = (Banner) findViewById(R.id.goods_detail_banner);
        List<String> images = new ArrayList<String>();
        List<String> titlestr = new ArrayList<String>();

        for(int i=0;i<bannerImg.length;i++){
            images.add(bannerImg[i]);
            titlestr.add("照片仅供参考 以实物为准");
        }
//        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497598051&di=136b6c564a6d8d59e77ce349616996e9&imgtype=jpg&er=1&src=http%3A%2F%2Fm.qqzhi.com%2Fupload%2Fimg_0_72213646D1378690088_23.jpg");
//        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3145185115,3541103163&fm=26&gp=0.jpg");
//        images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4280343775,3437702687&fm=26&gp=0.jpg");
//        List<Integer> images = new ArrayList<Integer>();
//        images.add(R.mipmap.img_jd);
//        images.add(R.mipmap.img_dg);
//        images.add(R.mipmap.img_dy);


//        titlestr.add("我是第2个图片");
//        titlestr.add("我是第3个图片");
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
//        //显示标题样式水平显示
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
//        CIRCLE_INDICATOR 圆形
//        BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE
//        BannerConfig.CIRCLE_INDICATOR_TITLE
//        //设置标题文本
        banner.setBannerTitles(titlestr);
//        banner.setBannerTitle();

//        setBannerTitle(String[] titles)	设置轮播要显示的标题和图片对应（如果不传默认不显示标题）	1.3.3结束
//        banner.setBannerTitleList(List titles);//	设置轮播要显示的标题和图片对应（如果不传默认不显示标题）	1.3.3结束
//        setBannerTitles(List titles)
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        //DepthPag折叠
        banner.setBannerAnimation(Transformer.Default);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {
            disTitleRel.setVisibility(View.VISIBLE);
            titleRelay.setVisibility(View.GONE);
            //40 为清单两段间距距离
        }
        else if (y > 0 && y <= (banner == null? 0:banner.getHeight())-disTitleRel.getHeight()) {
            titleRelay.setVisibility(View.GONE);
            disTitleRel.setVisibility(View.VISIBLE);
        } else {
            titleRelay.setVisibility(View.VISIBLE);
            disTitleRel.setVisibility(View.GONE);
        }
//        Logs.i(y+"============"+(xxxxlin.getHeight()+"==="+banner.getHeight()));
        if(y >= xxxxlin.getHeight()/2){
            xxTv.setTextColor(getResources().getColor(R.color.col_999));
            xxLine.setBackgroundResource(R.color.transparent);
            ggLine.setBackgroundResource(R.color.orange);
            ggTv.setTextColor(getResources().getColor(R.color.orange));

        }else{
            xxTv.setTextColor(getResources().getColor(R.color.orange));
            xxLine.setBackgroundResource(R.color.orange);
            ggLine.setBackgroundResource(R.color.transparent);
            ggTv.setTextColor(getResources().getColor(R.color.col_999));
        }
        Logs.i("返回滑动值====="+y);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goods_detail_tab_xxxx_lin:
                xxTv.setTextColor(getResources().getColor(R.color.orange));
                xxLine.setBackgroundResource(R.color.orange);
                ggLine.setBackgroundResource(R.color.transparent);
                ggTv.setTextColor(getResources().getColor(R.color.col_999));
                scro.scrollTo(0,banner.getHeight()+10);
                break;
            case R.id.goods_detail_tab_ggcs_lin:
                xxTv.setTextColor(getResources().getColor(R.color.col_999));
                xxLine.setBackgroundResource(R.color.transparent);
                ggLine.setBackgroundResource(R.color.orange);
                ggTv.setTextColor(getResources().getColor(R.color.orange));
                scro.scrollTo(0, xxxxlin.getHeight());
                Logs.i(xxxxlin.getHeight()+banner.getHeight()+"=======");
                break;
            case  R.id.goods_detail_goback_linlay: case  R.id.goods_detail_dis_goback_linlay:
                finish();
                break;
            case R.id.goods_detail_feedback_tv:
                break;
            case R.id.goods_detail_gocar_btn:
                if(!SXUtils.getInstance(activity).IsLogin()){
                    return ;
                }
                AppClient.TAG4 = true;
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100011,"search"));

//                MainFragmentActivity.carRb.setChecked(true);
//                SXUtils.getInstance(activity).finishActivity();
                finish();
                return;
            case R.id.goods_detail_add_tv:
                if(!SXUtils.getInstance(activity).IsLogin()){
                    return ;
                }
                this.addAction(v);
                break;
            case R.id.goods_detail_bill:
                if(!SXUtils.getInstance(activity).IsLogin()){
                    return ;
                }
                SXUtils.showMyProgressDialog(activity,true);
                //删除或者添加到常用清单
                if(goodsdetail.getIsCommonGoos().equals("1")){
                    setBill = "0";
//                    SXUtils.getInstance(activity).AddDelBill(0,goodsdetail.getGoodsCode(),hand);
                }else{
                    setBill = "1";
//                    SXUtils.getInstance(activity).AddDelBill(1,goodsdetail.getGoodsCode(),hand);
                }
                break;
        }
    }
    private int goodsCar=0;
    @Override
    public void addAction(View view) {
        goodsCar++;
        NXHooldeView nxHooldeView = new NXHooldeView(activity);
        int position[] = new int[2];
        view.getLocationInWindow(position);
        nxHooldeView.setStartPosition(new Point(position[0], position[1]));
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        rootView.addView(nxHooldeView);
        int endPosition[] = new int[2];
        carNum.getLocationInWindow(endPosition);
        nxHooldeView.setEndPosition(new Point(endPosition[0], endPosition[1]));
        nxHooldeView.startBeizerAnimation();
        if(goodsCar == 0){
            carNum.setVisibility(View.GONE);
        }else{
            carNum.setVisibility(View.VISIBLE);
            carNum.setText(goodsCar+"");
        }
        totalPrice.setText("¥"+(goodsCar*(Float.parseFloat(goodsdetail.getSkuList().get(0).getShopPrice())))+"");
        SXUtils.getInstance(activity).AddOrUpdateCar(goodsdetail.getSkuList().get(0).getSkuBarcode(),"1");
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            Glide
                    .with(context)
                    .load((String) path)
                    .placeholder(R.mipmap.default_big_load_img)
                    .error(R.mipmap.default_big_load_img)
                    .fitCenter()
                    .into(imageView);
//            SXUtils.getInstance(activity).GlideSetImg((String) path,imageView);
//            SXUtils.getInstance(context).GlidFullSetImg(path,imageView);
        }
    }


    /**
     * 获取商品详情
     */
    public void getHttpGoodsDetail(){
        HttpParams params = new HttpParams();
        params.put("id", goodsId);
        HttpUtils.getInstance(activity).requestPost(false,AppClient.GOODS_DETAIL, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                String jsonstr="";
                try {
                    JSONObject jso = new JSONObject(jsonObject.toString());
                    jsonstr = jso.getString("dataset");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                GoodsDetailInfoEntity gde = (GoodsDetailInfoEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonstr.toString(),GoodsDetailInfoEntity.class);
                if(gde != null){
                    Message msg = new Message();
                    msg.what = 1000;
                    msg.obj = gde;
                    hand.sendMessage(msg);
                }else{
                    Message msg = new Message();
                    msg.what = AppClient.ERRORCODE;
                    msg.obj = "数据解析异常";
                    hand.sendMessage(msg);
                }

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
//            if(MainFragmentActivity.totalCarNum == 0){
//                carNum.setVisibility(View.GONE);
//            }else{
//                carNum.setVisibility(View.VISIBLE);
//                if(MainFragmentActivity.totalCarNum >99){
//                    carNum.setText(99+"+");
//                }else{
//                    carNum.setText(MainFragmentActivity.totalCarNum+"");
//                }
//            }
            SXUtils.getInstance(activity).setGoodsBadeNum(carNum);
            totalPrice.setText("¥"+messageEvent.getMessage()+"");
//            totalPrice.setText("¥"+(goodsCar*(Float.parseFloat(goodsdetail.getSkuList().get(0).getShopPrice())))+"");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
