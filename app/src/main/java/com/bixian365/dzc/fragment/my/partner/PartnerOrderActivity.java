package com.bixian365.dzc.fragment.my.partner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.orderlist.OrderListEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import java.util.ArrayList;
import java.util.List;

public class PartnerOrderActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private String orderTag;
    private Activity activity;
    private Handler hand;
    private int indexPage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        activity = this;
        orderTag = this.getIntent().getStringExtra("orderTag");
        initView();
        initViewPager();
    }
    private void initView(){

    }
    private void initViewPager() {
        LinearLayout allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView allTitleName = (TextView) findViewById(R.id.all_title_name);
        allTitleName.setText("我的订单");

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("待确认");
        titles.add("待发货");
        titles.add("待收货");
        fragments.add(new PartnerAllOrderFragment());
        fragments.add(new PartnerConfirmOrderFragment());
        fragments.add(new PartnerSendOrderFragment());
        fragments.add(new PartnerGetOrderFragment());
        FragmentAdapter adatper = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager = (ViewPager) findViewById(R.id.order_viewPager);
        viewPager.setAdapter(adatper);
        viewPager.setOffscreenPageLimit(2);//预加载界面
        //将TabLayout和ViewPager关联起来。
        XTabLayout tabLayout = (XTabLayout) findViewById(R.id.order_xtablayout);
        tabLayout.setupWithViewPager(viewPager);
        if(Integer.parseInt(orderTag) ==1){
            viewPager.setCurrentItem(0);
        }else if(Integer.parseInt(orderTag) ==2){
            viewPager.setCurrentItem(1);
        }
        else if(Integer.parseInt(orderTag) ==3){
            viewPager.setCurrentItem(2);
        }
        else if(Integer.parseInt(orderTag) ==4){
            viewPager.setCurrentItem(3);
        }
//        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Logs.i("========="+position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    class FragmentAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);

        }
    }
    /**
     * 获取摊主订单列表
     * @param indexPage
     * @param status 0待确认，10待发货，20 待收货，all 全部
     * @param hand
     */
    public  void getParetnerOrderListHttp(int indexPage,final String status,final Handler hand) {
        HttpParams params = new HttpParams();
        params.put("pageSize","10");
        params.put("pageIndex",indexPage);
        params.put("status",status);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.USERROLETAG.equals("16")?AppClient.HHR_ORDER_LIST:AppClient.PARTNER_ORDER_LIST, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i(status+"合伙人订单列表========",jsonObject.toString());
                OrderListEntity gde =  ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),OrderListEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde.getDataset().getRows();
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


}
