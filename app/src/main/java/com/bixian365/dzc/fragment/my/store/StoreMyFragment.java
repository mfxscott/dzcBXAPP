package com.bixian365.dzc.fragment.my.store;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.entity.UserRenderInfoEntity;
import com.bixian365.dzc.fragment.CommonWebViewMainActivity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.fragment.my.store.order.MyOrderActivity;
import com.bixian365.dzc.fragment.my.store.yhj.YHJActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.GlideRoundTransform;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 摊主或者个人登录进入我的界面
 * 通过区分标识显示不同界面
 * @author mfx
 * @time  2017/7/5 16:22
 */
public class StoreMyFragment extends Fragment implements View.OnClickListener{
    private View view;
    private RelativeLayout myStoreMyorderRel;
    private LinearLayout myStoreWaitpayLin;
    private LinearLayout myStoreWaitsendLin;
    private LinearLayout myStoreWaitgetLin;
    private LinearLayout mystoreFeedBackLin;
    private LinearLayout myStoreCybillLin;
    private LinearLayout myStoreFootLin;
    private LinearLayout myStoreNewLin;
    private LinearLayout myStoreMyfpLin;
    private RelativeLayout myStoreHelpCenterRel;
    private LinearLayout myStoreQuesionLin;
    private LinearLayout myStoreFwcenterLin;
    private LinearLayout myStoreKffwLin;
    private LinearLayout couponsLiny;
    private LinearLayout  walletLiny;
    private Activity activity;
    private LinearLayout storeLin; //店铺账号类型显示
    private LinearLayout perLin; //个人账号类型显示
    private Handler hand;
    private UserInfoEntity userinfo;//个人所有用户信息
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView store_my_money,couponsNum;
    private TextView name;
    private ImageView headimg;
    private TextView orderNum1,orderNum2,orderNum3;
    private TextView  acclevel;
    private FrameLayout  messageLay;
    private TextView   messageNumTv;//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.BLACK);
//        }
        view = inflater.inflate(R.layout.fragment_store_my, null);
        initView();

        EventBus.getDefault().register(this);
        LoadData();
        return view;
    }
    /**
     * 初始化加载用户相关接口数据
     */
    private void LoadData(){
        if(!TextUtils.isEmpty(AppClient.USER_SESSION) && !TextUtils.isEmpty(AppClient.USER_ID)) {
//            SXUtils.showMyProgressDialog(activity,false);
            getUserInfoHttp();
            SXUtils.getInstance(activity).getUserNumberHttp(hand);
//            GetOrderListHttp();
//            GetUserWalletHttp();
        }else{
            if(swipeRefreshLayout != null){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    private void UserInfoNum(UserRenderInfoEntity userNum){
        store_my_money.setText("¥"+userNum.getUserableAmount());
        couponsNum.setText(userNum.getUserableCoupon());
        try {
            int  messageNum =  Integer.parseInt(userinfo.getUnreadNumbe());
            if (messageNum>0) {
                messageNumTv.setVisibility(View.VISIBLE);
                messageNumTv.setText(messageNum >99 ? "99+":messageNum+ "");

            } else {
                messageNumTv.setVisibility(View.GONE);
            }
        }catch (Exception e){
            messageNumTv.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(userNum.getToPayOrder()) ) {
            if(!userNum.getToPayOrder().equals("0")) {
                orderNum1.setVisibility(View.VISIBLE);
                orderNum1.setText(userNum.getToPayOrder() + "");
            }else{
                orderNum1.setVisibility(View.GONE);
            }
        }
        if(!TextUtils.isEmpty(userNum.getToDelivery())) {
            if (!userNum.getToDelivery().equals("0")){
                orderNum2.setVisibility(View.VISIBLE);
                orderNum2.setText(userNum.getToDelivery());
            }
            else{
                orderNum2.setVisibility(View.GONE);
            }
        }
        if(!TextUtils.isEmpty(userNum.getToReceive())) {
            if(!userNum.getToReceive().equals("0")) {
                orderNum3.setVisibility(View.VISIBLE);
                orderNum3.setText(userNum.getToReceive());
            }
            else{
                orderNum3.setVisibility(View.GONE);
            }
        }}

    /**
     * 退出登录
     * 制空用户数据
     */
    private void ExitUserInfoNull(){
        store_my_money.setText("¥0.00");
        couponsNum.setText("");
        orderNum1.setVisibility(View.GONE);
        orderNum2.setVisibility(View.GONE);
        orderNum3.setVisibility(View.GONE);
        acclevel.setVisibility(View.GONE);
        name.setText("未登录");
        Glide.with(activity).load(R.mipmap.default_head).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headimg);
    }
    /**
     * 初始化
     */
    private void initView(){
        //消息条目
        messageNumTv = (TextView) view.findViewById(R.id.store_message_num_tv);
        acclevel = (TextView) view.findViewById(R.id.my_acc_level_tv);

        store_my_money = (TextView) view.findViewById(R.id.store_my_money);
        couponsNum = (TextView) view.findViewById(R.id.store_my_coupons_tv);

        //订单状态订单数量
        orderNum1 = (TextView) view.findViewById(R.id.store_order_num1_tv);
        orderNum2 = (TextView) view.findViewById(R.id.store_order_num2_tv);
        orderNum3 = (TextView) view.findViewById(R.id.store_order_num3_tv);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.user_center_swipe_container);
//        swipeRefreshLayout.setColorSchemeResources( R.color.qblue, R.color.red, R.color.btn_gray);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
//                myWebView.reload();
//                getUserInfoHttp();
                LoadData();
            }
        });
//        Glide.with(activity).load("https://www.baidu.com/img/bdlogo.png").transform(new GlideRoundTransform(activity, 10)).into(headimg);

//        Glide.with(activity)
//                .load("")
//                .placeholder(R.mipmap.ic_launcher)
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .bitmapTransform(new RoundedCornersTransformation(activity)) //使用圆形变换，还可以使用其他的变换
//                .into(headimg);
//
//
//        Glide.with(this).load("").bitmapTransform(new RoundedCornersTransformation(this, 30, 0, RoundedCornersTransformation.CornerType.BOTTOM)).crossFade(1000).into(image5);

        RelativeLayout rel = (RelativeLayout) view.findViewById(R.id.my_per_wallet);
        rel.setOnClickListener(this);

        storeLin = (LinearLayout) view.findViewById(R.id.my_store_dis_lin);
        storeLin.setOnClickListener(this);
        perLin = (LinearLayout) view.findViewById(R.id.my_per_dis_lin);
        perLin.setOnClickListener(this);
        if(AppClient.USERROLETAG .equals("64")){
            perLin.setVisibility(View.VISIBLE);
            storeLin.setVisibility(View.GONE);
        }else{
            perLin.setVisibility(View.GONE);
            storeLin.setVisibility(View.VISIBLE);
        }
        TextView accTv = (TextView) view.findViewById(R.id.my_acc_mamage_tv);
        accTv.setOnClickListener(this);
        ImageView messageIv = (ImageView) view.findViewById(R.id.per_my_message_iv);
        messageIv.setOnClickListener(this);
        messageLay = (FrameLayout) view.findViewById(R.id.store_my_message_framelay);
        messageLay.setOnClickListener(this);
        myStoreMyorderRel = (RelativeLayout) view.findViewById(R.id.my_store_myorder_rel);
        myStoreWaitpayLin = (LinearLayout) view.findViewById(R.id.my_store_waitpay_lin);
        myStoreWaitsendLin = (LinearLayout) view.findViewById(R.id.my_store_waitsend_lin);
        myStoreWaitgetLin = (LinearLayout) view.findViewById(R.id.my_store_waitget_lin);
        mystoreFeedBackLin = (LinearLayout) view.findViewById(R.id.my_store_feedback_lin);
        myStoreCybillLin = (LinearLayout) view.findViewById(R.id.my_store_cybill_lin);
        myStoreFootLin = (LinearLayout) view.findViewById(R.id.my_store_foot_lin);
        myStoreNewLin = (LinearLayout) view.findViewById(R.id.my_store_new_lin);
        myStoreMyfpLin = (LinearLayout) view.findViewById(R.id.my_store_myfp_lin);
        myStoreHelpCenterRel = (RelativeLayout) view.findViewById(R.id.my_store_help_center_rel);
        myStoreQuesionLin = (LinearLayout) view.findViewById(R.id.my_store_quesion_lin);
        myStoreFwcenterLin = (LinearLayout) view.findViewById(R.id.my_store_fwcenter_lin);
        myStoreKffwLin = (LinearLayout) view.findViewById(R.id.my_store_kffw_lin);
        couponsLiny =  (LinearLayout)view.findViewById(R.id.store_my_coupons_liny);
        walletLiny = (LinearLayout) view.findViewById(R.id.store_my_wallet_liny);
        walletLiny.setOnClickListener(this);
        couponsLiny.setOnClickListener(this);
        myStoreMyorderRel.setOnClickListener(this);
        myStoreWaitpayLin.setOnClickListener(this);
        myStoreWaitsendLin.setOnClickListener(this);
        myStoreWaitgetLin.setOnClickListener(this);
        mystoreFeedBackLin.setOnClickListener(this);
        myStoreCybillLin.setOnClickListener(this);
        myStoreFootLin.setOnClickListener(this);
        myStoreNewLin.setOnClickListener(this);
        myStoreMyfpLin.setOnClickListener(this);
        myStoreHelpCenterRel.setOnClickListener(this);
        myStoreQuesionLin.setOnClickListener(this);
        myStoreFwcenterLin.setOnClickListener(this);
        myStoreKffwLin.setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.user_info_name_tv);
        headimg = (ImageView) view.findViewById(R.id.my_head_img);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        userinfo = (UserInfoEntity) msg.obj;
                        AppClient.USERPHONE = userinfo.getMobile();
                        name.setText(userinfo.getUsername()+"");
                        acclevel.setVisibility(View.VISIBLE);
                        Glide.with(activity).load(TextUtils.isEmpty(userinfo.getShopLogo())?userinfo.getIcon():userinfo.getShopLogo()).placeholder(R.mipmap.default_head)
                                .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headimg);
                        break;
                    case 1001:
                        //用户数量信息
                        UserRenderInfoEntity userNum = (UserRenderInfoEntity) msg.obj;
                        UserInfoNum(userNum);
                        break;
                    case 1002:
                        //钱包
                        break;
                    case AppClient.ERRORCODE:
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
                        break;
                }
                if(swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    private List<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.my_acc_mamage_tv:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                Intent accm = new Intent(activity,AccManageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("userinfo",userinfo);
                accm.putExtras(bundle);
                startActivity(accm);
                break;
            case R.id.per_my_message_iv:case R.id.store_my_message_framelay:
                //消息
//                Intent msg = new Intent(activity,MessageActivity.class);
//                startActivity(msg);
                Intent Hintent = new Intent(activity, CommonWebViewMainActivity.class);
                Hintent.putExtra("tag","2");
                Hintent.putExtra("postUrl",AppClient.MESSAGE+"");
                startActivity(Hintent);

                break;
            case R.id.my_store_myorder_rel:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                Intent order = new Intent(activity,MyOrderActivity.class);
                order.putExtra("orderTag","0");
                startActivity(order);
                break;
            case R.id.my_store_waitpay_lin:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                Intent payorder = new Intent(activity,MyOrderActivity.class);
                payorder.putExtra("orderTag","1");
                startActivity(payorder);
                break;
            case R.id.my_store_waitsend_lin:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                Intent sendorder = new Intent(activity,MyOrderActivity.class);
                sendorder.putExtra("orderTag","2");
                startActivity(sendorder);
                break;
            case R.id.my_store_waitget_lin:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                Intent getorder = new Intent(activity,MyOrderActivity.class);
                getorder.putExtra("orderTag","3");
                startActivity(getorder);
                break;
//            case R.id.my_store_mykey_lin:
            case R.id.my_store_feedback_lin:
                Intent feedintent = new Intent(activity, CommonWebViewMainActivity.class);
                feedintent.putExtra("tag","2");
                feedintent.putExtra("postUrl",AppClient.FEEDBACK);
                startActivity(feedintent);
                break;
            case R.id.my_store_foot_lin:
                Intent footintent = new Intent(activity, CommonWebViewMainActivity.class);
                footintent.putExtra("tag","2");
                footintent.putExtra("postUrl",AppClient.MYFOOT);
                startActivity(footintent);
                break;
            case R.id.my_store_new_lin:
                Intent intent = new Intent(activity, CommonWebViewMainActivity.class);
                intent.putExtra("tag","2");
                intent.putExtra("postUrl",AppClient.MYNEWDEMAND);
                startActivity(intent);
                break;
            case R.id.my_store_myfp_lin:
                Intent fpintent = new Intent(activity, CommonWebViewMainActivity.class);
                fpintent.putExtra("tag","2");
                fpintent.putExtra("postUrl",AppClient.MYINVOICE);
                startActivity(fpintent);
                break;
//            case R.id.my_store_help_center_rel:
            case R.id.my_store_quesion_lin:
                Intent qintent = new Intent(activity, CommonWebViewMainActivity.class);
                qintent.putExtra("tag","2");
                qintent.putExtra("postUrl",AppClient.FAQ);
                startActivity(qintent);
                break;
            case R.id.my_store_fwcenter_lin:
                Intent mykey = new Intent(activity, CommonWebViewMainActivity.class);
                mykey.putExtra("tag","2");
                mykey.putExtra("postUrl",AppClient.SERVICECENTER);
                startActivity(mykey);
                break;
            case R.id.my_store_kffw_lin:
                SXUtils.getInstance(activity).CallCustPhone();
                //拨打客服电话
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                Uri data = Uri.parse("tel:" + AppClient.CUST_TELEPHONE);
//                intent.setData(data);
//                startActivity(intent);
                break;
            case R.id.my_store_cybill_lin:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                MainFragmentActivity.billRb.setChecked(true);
                break;
            case R.id.store_my_coupons_liny:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                //跳转优惠劵列表
                Intent yhj = new Intent(activity,YHJActivity.class);
                yhj.putExtra("yhjTag","1");
                startActivity(yhj);
                break;
            case R.id.my_store_dis_lin:case R.id.store_my_wallet_liny: case R.id.my_per_wallet:case R.id.my_per_dis_lin:
                if(!SXUtils.getInstance(activity).IsLogin())
                    return ;
                Intent txwall = new Intent(activity,MyWalletActivity.class);
                startActivity(txwall);
                break;
        }
    }
    /**
     * 获取不同用户信息
     */
    public void getUserInfoHttp() {
        HttpUtils.getInstance(activity).requestPost(false,AppClient.USER_INFO, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                UserInfoEntity gde = null;
                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what =AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);

            }
        });
    }
//    public void getUserInfoHttp(){
//        RequestBody requestBody = new FormBody.Builder()
//                .build();
//        new OKManager(activity).sendStringByPostMethod(requestBody, AppClient.USER_INFO, new OKManager.Func4() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                try {
//                    UserInfoEntity userinfo =  ResponseData.getInstance(activity).getUserInfo(jsonObject);
//                    Message msg = new Message();
//                    msg.what = 1000;
//                    msg.obj = userinfo;
//                    hand.sendMessage(msg);
//                } catch (JSONException e) {
//                    Message msg = new Message();
//                    msg.what = AppClient.ERRORCODE;
//                    msg.obj = e.toString();
//                    hand.sendMessage(msg);
//                }
//
//            }
//            @Override
//            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);
//            }
//        });
//    }
    /**
     * 获取订单信息
     */
    public void GetOrderListHttp() {
        HttpUtils.getInstance(activity).requestPost(false,AppClient.USER_ORDERS, null, new HttpUtils.requestCallBack() {

            @Override
            public void onResponse(Object jsonObject) {
//                UserInfoEntity gde = null;
//                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = "订单条目"+jsonObject.toString();
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = "获取用户订单信息="+strError;
                hand.sendMessage(msg);

            }
        });
    }
    //    /**
//     * 获取用户余额
//     */
//    public void GetUserWalletHttp() {
//        HttpUtils.getInstance(activity).requestPost(false,AppClient.USER_WALLET, null, new HttpUtils.requestCallBack() {
//            @Override
//            public void onResponse(Object jsonObject) {
////                UserInfoEntity gde = null;
////                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = "余额="+jsonObject.toString();
//                hand.sendMessage(msg);
//            }
//            @Override
//            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = "获取钱包余额="+strError;
//                hand.sendMessage(msg);
//
//            }
//        });
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag()==AppClient.EVENT1){
            LoadData();
        }else if(messageEvent.getTag() == 4444){
            ExitUserInfoNull();
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
