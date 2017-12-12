package com.bixian365.dzc.fragment.my.store.yhj;

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
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.car.OrderCouponsEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券
 * @author mfx
 * @time  2017/7/7 14:32
 */
public class YHJActivity extends BaseActivity {
    ViewPager viewPager;
    private String yhjTag;
    private Handler hand;
    private Activity activity;
    public static ArrayList<OrderCouponsEntity> couponsList = new ArrayList<>();
    public  ArrayList<OrderCouponsEntity> couponsList0 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yhj);
        yhjTag = this.getIntent().getStringExtra("yhjTag");
        Bundle bundle = this.getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("coupsons");
        if (list != null){
            couponsList.clear();
            ArrayList<OrderCouponsEntity>  coupons = (ArrayList<OrderCouponsEntity>) list.get(0);
            for(int i=0;i<coupons.size();i++){
                if(coupons.get(i).getIsUsable().equals("0")){
                    couponsList0.add(coupons.get(i));
                }
                else{
                    couponsList.add(coupons.get(i));
                }
            }
            couponsList.addAll(couponsList0);
        }
        activity = this;

        initView();
        initViewPager();
//        getCouponsUse();
    }
    private void initView(){
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        initViewPager();
                        break;
                    case 1001:
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
    private void initViewPager() {
        LinearLayout allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView allTitleName = (TextView) findViewById(R.id.all_title_name);
        allTitleName.setText("优惠券");

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        titles.add("未使用");
        titles.add("已使用");
        titles.add("已过期");
        if(Integer.parseInt(yhjTag) ==4){
            fragments.add(new PayOrderUseFragment());
        }else {
            fragments.add(new NoUseFragment());
        }
        fragments.add(new YHJUseFragment());
        fragments.add(new YHJUsedFragment());
        FragmentAdapter adatper = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(adatper);
        viewPager.setOffscreenPageLimit(1);
        //将TabLayout和ViewPager关联起来。
        XTabLayout tabLayout = (XTabLayout) findViewById(R.id.xTablayout);
        tabLayout.setupWithViewPager(viewPager);
        if(Integer.parseInt(yhjTag) ==1){
            viewPager.setCurrentItem(0);
        }else if(Integer.parseInt(yhjTag) ==2){
            viewPager.setCurrentItem(1);
        }else if(Integer.parseInt(yhjTag) ==3){
            viewPager.setCurrentItem(2);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                Logs.i("========="+position);
//               new NoUseFragment().newInstance((ArrayList<UserCouponEntity>) couponsuse,1);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == event.KEYCODE_BACK) {
//            finish();
//        }
//        return true;
//    }
}
