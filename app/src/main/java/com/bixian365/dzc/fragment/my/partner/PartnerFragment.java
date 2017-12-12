package com.bixian365.dzc.fragment.my.partner;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.entity.UserRenderInfoEntity;
import com.bixian365.dzc.fragment.CommonWebViewMainActivity;
import com.bixian365.dzc.fragment.my.store.AccManageActivity;
import com.bixian365.dzc.fragment.my.store.MyWalletActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.view.GlideRoundTransform;
import com.bixian365.dzc.utils.zxing.BaseQRScanActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 皕鲜合伙人,联创中心
 */
public class PartnerFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Unbinder unbinder;
    private Activity activity;
    @BindView(R.id.partner_wallet_rely)
    RelativeLayout walletRely;
    @BindView(R.id.partner_acc_mamage_tv)
    TextView  accManageTv;
    @BindView(R.id.partner_center_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.partner_head_img)
    ImageView  headImg;
    @BindView(R.id.partner_name_tv)
    TextView name;
    @BindView(R.id.partner_scan_tv)
    LinearLayout scanTv;
    @BindView(R.id.partner_orderlsit_rely)
    RelativeLayout  orderRel;
    @BindView(R.id.partner_wait_get_lin)
    LinearLayout  waitGetLin;
    @BindView(R.id.partner_wait_send_lin)
    LinearLayout  waitSendLin;
    @BindView(R.id.partner_wait_confirm_lin)
    LinearLayout  waitConfirmLin;
    @BindView(R.id.partner_scan_lin)
    LinearLayout  scanLin;
    @BindView(R.id.partner_buyer_topup_btn)
    TextView partnerWalletTv;
    @BindView(R.id.partner_order_get_tv)
    TextView orderGetTv;
    @BindView(R.id.partner_order_send_tv)
    TextView orderSendTv;
    @BindView(R.id.partner_order_wait_confirm_tv)
    TextView orderConfrimTv;
    @BindView(R.id.partner_balance_tv)
    TextView balanceTv;
    @BindView(R.id.partner_today_roder_num_tv)
    TextView  todayOrderNumTv;
    @BindView(R.id.partner_today_roder_xse_tv)
    TextView  todayOrderXseTv;
    @BindView(R.id.partner_today_roder_yj_tv)
    TextView  todayOrderYJTv;
    @BindView(R.id.my_cg_message)
    ImageView message;
    @BindView(R.id.partner_my_message_framelay)
    FrameLayout  messageLay;
    @BindView(R.id.partner_message_num_tv)
    TextView   messageNumTv;
    private Handler hand;
    private UserInfoEntity userinfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_partner, null);
        activity = getActivity();
        unbinder =   ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }
    private void initData() {
        if (SXUtils.getInstance(activity).IsLogin()) {
//            SXUtils.showMyProgressDialog(activity,true);
            SXUtils.getInstance(activity).getUserInfoHttp(hand);
            SXUtils.getInstance(activity).getUserNumberHttp(hand);
        }
        else{
            if(swipeRefreshLayout != null){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    /**
     * 用户信息
     * @param userInfo
     */
    private void initUserInfo(UserInfoEntity userInfo){
        Glide.with(activity).load(userInfo.getIcon()).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head).transform( new GlideRoundTransform(activity)).into(headImg);
        name.setText(userInfo.getUsername()+"");
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

    }
    private void initUerInfoRender(UserRenderInfoEntity userRender){
        if(!TextUtils.isEmpty(userRender.getToReceive()) && !userRender.getToReceive().equals("0")){
            orderGetTv.setVisibility(View.VISIBLE);
            orderGetTv.setText(userRender.getToReceive()+"");
            int Num = Integer.parseInt(userRender.getToReceive());
            if(Num>99){
                orderGetTv.setText("99+");
            }else{
                orderGetTv.setText(Num+"");
            }
        }else{
            orderGetTv.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(userRender.getToDelivery()) && !userRender.getToDelivery().equals("0")){
            orderSendTv.setVisibility(View.VISIBLE);
            orderSendTv.setText(userRender.getToDelivery()+"");
            int Num = Integer.parseInt(userRender.getToDelivery());
            if(Num>99){
                orderSendTv.setText("99+");
            }else{
                orderSendTv.setText(Num+"");
            }
        }else{
            orderSendTv.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(userRender.getToCommitOrders()) && !userRender.getToCommitOrders().equals("0")){
            orderConfrimTv.setVisibility(View.VISIBLE);
            int commitnNum = Integer.parseInt(userRender.getToCommitOrders());
            if(commitnNum>99){
                orderConfrimTv.setText("99+");
            }else{
                orderConfrimTv.setText(userRender.getToCommitOrders()+"");
            }
        }else{
            orderConfrimTv.setVisibility(View.GONE);
        }
        balanceTv.setText("¥"+userRender.getUserableAmount()+"");
        todayOrderNumTv.setText(userRender.getTodayOrder()+"");
        todayOrderXseTv.setText("¥"+userRender.getTodaySellAmount()+"");
        todayOrderYJTv.setText("¥"+userRender.getTodayComm()+"");
    }
    private void exitLogin(){
        Glide.with(activity).load(R.mipmap.default_head).placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head).transform( new GlideRoundTransform(activity)).into(headImg);
        orderGetTv.setText("0");
        orderSendTv.setText("0");
        orderConfrimTv.setText("0");
        orderSendTv.setVisibility(View.GONE);
        orderConfrimTv.setVisibility(View.GONE);
        orderGetTv.setVisibility(View.GONE);


        balanceTv.setText("0");
        todayOrderNumTv.setText("0");
        todayOrderXseTv.setText("0");
        todayOrderYJTv.setText("0");
        name.setText("未登录");
    }
    private void initView(){
        message.setOnClickListener(this);
        orderRel.setOnClickListener(this);
        scanTv.setOnClickListener(this);
        waitGetLin.setOnClickListener(this);
        waitSendLin.setOnClickListener(this);
        waitConfirmLin.setOnClickListener(this);
        partnerWalletTv.setOnClickListener(this);
        walletRely.setOnClickListener(this);
        accManageTv.setOnClickListener(this);
        messageLay.setOnClickListener(this);
        if(AppClient.USERROLETAG.equals("8")){
            scanLin.setVisibility(View.GONE);
        }else{
            scanLin.setVisibility(View.VISIBLE);
        }
//        swipeRefreshLayout.setColorSchemeResources( R.color.qblue, R.color.red, R.color.btn_gray);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        userinfo = (UserInfoEntity) msg.obj;
                        initUserInfo(userinfo);
                        break;
                    case 1001:
                        UserRenderInfoEntity     render = (UserRenderInfoEntity) msg.obj;
                        initUerInfoRender(render);
                        break;
                    case AppClient.ERRORCODE:
                        String str = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(str+"");
                }
                if(swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(false);
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    //       PartnerFragment.this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//            1);
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //申请成功，可以拍照
                Intent intent = new Intent(activity, BaseQRScanActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(activity, "相机权限已拒绝，请前往设置-权限管理中开启", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if(!SXUtils.getInstance(activity).IsLogin())
            return ;
        switch (v.getId()){
            case R.id.partner_acc_mamage_tv:
                Intent manage = new Intent(activity, AccManageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("userinfo", userinfo);
                manage.putExtras(bundle);
                startActivity(manage);
                break;
            case R.id.partner_wallet_rely:
                Intent walllin = new Intent(activity,MyWalletActivity.class);
                walllin.putExtra("walletTag","1");
                startActivity(walllin);
                break;
            case R.id.partner_scan_tv:
                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    PartnerFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA},1000);

//                            new String[]{Manifest.permission.CAMERA},
//                            1000);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(activity, BaseQRScanActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.partner_orderlsit_rely:
                Intent order = new Intent(activity,PartnerOrderActivity.class);
                order.putExtra("orderTag","1");
                startActivity(order);
                break;
            case R.id.partner_wait_confirm_lin:
                Intent order2 = new Intent(activity,PartnerOrderActivity.class);
                order2.putExtra("orderTag","2");
                startActivity(order2);
                break;
            case R.id.partner_wait_send_lin:
                Intent order3 = new Intent(activity,PartnerOrderActivity.class);
                order3.putExtra("orderTag","3");
                startActivity(order3);
                break;
            case R.id.partner_wait_get_lin:
                Intent order4 = new Intent(activity,PartnerOrderActivity.class);
                order4.putExtra("orderTag","4");
                startActivity(order4);
                break;
            case R.id.partner_buyer_topup_btn:
                Intent wall = new Intent(activity,MyWalletActivity.class);
                wall.putExtra("walletTag","1");
                startActivity(wall);
                break;
            case R.id.my_cg_message:case R.id.partner_my_message_framelay:
                Intent Hintent = new Intent(activity, CommonWebViewMainActivity.class);
                Hintent.putExtra("tag","2");
                Hintent.putExtra("postUrl",AppClient.MESSAGE+"");
                startActivity(Hintent);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag()==4444){
            exitLogin();
        }else if(messageEvent.getTag()==AppClient.EVENT10008){
            initData();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}
