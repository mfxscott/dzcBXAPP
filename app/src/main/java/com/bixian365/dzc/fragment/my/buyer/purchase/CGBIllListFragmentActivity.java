package com.bixian365.dzc.fragment.my.buyer.purchase;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.xtablayout.XTabLayout;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.cgListInfo.CGBillListEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NN on 2017/11/13.
 */
public class CGBIllListFragmentActivity extends BaseActivity {
    public ViewPager viewPager;
    private String orderTag;
    private Activity activity;
    private int indexPage=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cg_fragment_order);
        activity = this;
        orderTag = this.getIntent().getStringExtra("state");
        EventBus.getDefault().register(this);
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
        allTitleName.setText("采购清单");

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("待接单");
        titles.add("待发货");
        titles.add("待收货");
        titles.add("已完成");
        fragments.add(new CGWaitConfrimFragments());
        fragments.add(new CGWaitSendFragment());
        fragments.add(new CGWaitGetFragment());
        fragments.add(new CGWaitDoneFragment());
        FragmentAdapter adatper = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager = (ViewPager) findViewById(R.id.cg_order_viewPager);
        viewPager.setAdapter(adatper);
        viewPager.setOffscreenPageLimit(1);//预加载界面
        //将TabLayout和ViewPager关联起来。
        XTabLayout tabLayout = (XTabLayout) findViewById(R.id.cg_order_xtablayout);
        tabLayout.setupWithViewPager(viewPager);
        if(Integer.parseInt(orderTag) ==1){
            viewPager.setCurrentItem(0);
        }else if(Integer.parseInt(orderTag) ==2){
            viewPager.setCurrentItem(1);
        }
        else if(Integer.parseInt(orderTag) ==3){
            viewPager.setCurrentItem(2);
        }else if(Integer.parseInt(orderTag) ==4){
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
     * 获取供应商采购列表
     */
    public void GetGYSBillListHttp(int indexPage,String receiveState,final Handler hand) {
        HttpParams params = new HttpParams();
        params.put("pageSize","10");
        params.put("pageIndex",indexPage);
        params.put("receiveState",receiveState);
        HttpUtils.getInstance(activity).requestPost(false,AppClient.GYS_BILLLIST, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                CGBillListEntity gde = (CGBillListEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),CGBillListEntity.class);
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
        if(messageEvent.getTag()==AppClient.EVENT100018){
            viewPager.setCurrentItem(Integer.parseInt(messageEvent.getMessage()));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}