package com.bixian365.dzc.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.member.LoginNameActivity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.OKManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ***************************
 * des
 * @author mfx
 * 日期：17/4/1 下午11:59
 * ***************************
 */
public class StartMainActivity extends Activity {
    private OKManager manager;//工具类

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int REQUEST_CODE = 0; // 请求码
    private MyCountDownTimer mc;
    private ViewPager viewPager;
    private List<View> viewContainter = new ArrayList<>();
    private LinearLayout guideLinlay;
    private ImageView logoIv;
    private TextView  countTv;
    private ImageView  addLogoIv;
    private RelativeLayout addLoglLin;
    private Activity activity;
    private Handler hand;
    private String seconds="3000";//广告图片显示时间
    private String imgUrl;//广告图片
    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.ACCESS_COARSE_LOCATION
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    /**
     * 图片资源id
     */
    private int[] imgIdArray ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //全屏
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);




//        WindowManager wm = this.getWindowManager();
//        AppClient.WIDTH  = wm.getDefaultDisplay().getWidth();
//        AppClient.HEIGHT   = wm.getDefaultDisplay().getHeight();
//        Logs.i("屏幕宽度==========",AppClient.WIDTH+"=====");
        activity = this;

    initDataView();

    }
    private void initDataView(){
        //自动升级下载apk 删除目录文件
//        SXUtils.getInstance(activity).deleteDir(SXUtils.getInstance(activity).getSDPath()+"/apk");
        //启动页面倒计时
        mc = new MyCountDownTimer(3000, 1000);
        mc.start();

        //这里以ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(StartMainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(StartMainActivity.this, PERMISSIONS_STORAGE,
                    1000);//自定义的code
        }


//        // 缺少权限时, 进入权限配置页面
//        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//            startPermissionsActivity();
//        }
//        //获取手机IMEI码
//        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = tm.getSimSerialNumber();
//        Log.i("手机唯一标示IMEI====",imei+"");
//        GetuserList();
        initView();
        String username =SXUtils.getInstance(activity).getSharePreferences("username");
        String psd =  SXUtils.getInstance(activity).getSharePreferences("psd");
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(psd)){
            //启动进行登录
            SXUtils.getInstance(activity).psdLoginHttp(hand,username,psd);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
//                Intent aa = new Intent(activity, StoreMapActivity.class);
//                activity.startActivity(aa);
            } else
            {
                Toast.makeText(activity, "手机权限被拒绝,将无法定位当前周边店铺.", Toast.LENGTH_SHORT).show();
//                Intent aa = new Intent(activity, StoreMapActivity.class);
//                activity.startActivity(aa);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }
    private void initView(){
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        if(activity != null)
                            Glide.with(activity)
                                    .load(imgUrl)
//                                    .error(R.mipmap.loading_img)
                                    .fitCenter()
                                    .into(addLogoIv);
//                        Glide.with(activity)
//                                .load(imgUrl)
////                                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501757540559&di=e90bccf64389faec7963e21e102c4367&imgtype=0&src=http%3A%2F%2Fwww.1tong.com%2Fuploads%2Fallimg%2F121024%2F1-1210241T0360-L.jpg")
//                                .fitCenter()
//                                .into(addLogoIv);
                        break;
                    case AppClient.UPDATEVER:
                        Map<String,String> map = (Map<String, String>) msg.obj;
//                        SXUtils.getInstance(activity).ToastCenter(map.get("verUrl")+"==");
                        break;
                    case AppClient.ERRORCODE:
//                        String errormsg = (String) msg.obj;
//                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
        LauncherHttp();
        logoIv = (ImageView) findViewById(R.id.start_logo_iv);
        guideLinlay = (LinearLayout) findViewById(R.id.start_guide_linlay);

        countTv = (TextView) findViewById(R.id.start_addlogo_time_tv);
        addLogoIv = (ImageView) findViewById(R.id.start_addlogo_iv);

        addLoglLin = (RelativeLayout) findViewById(R.id.start_addlogo_linlay);
        countTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StartMainActivity.this, MainFragmentActivity.class);
//                startActivity(intent);
                Glide.with(activity).pauseRequests();
                if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID)){
                    Intent intent = new Intent(StartMainActivity.this, LoginNameActivity.class);
                    startActivity(intent);
                }
                else{
                    SXUtils.getInstance(activity).IntentMain(activity);
                }
                mc.cancel();
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imgIdArray = new int[]{R.mipmap.guide_1, R.mipmap.guide_2};
        //设置Adapter
//        viewPager.setCurrentItem((mImageViews.length) * 100);
        View view1 = LayoutInflater.from(this).inflate(R.layout.guide, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.guide2, null);
//        View view3 = LayoutInflater.from(this).inflate(R.layout.guide3, null);
//        View view4 = LayoutInflater.from(this).inflate(R.layout.guide4, null);

        viewContainter.add(view1);
        viewContainter.add(view2);
        viewPager.setAdapter(new PagerAdapter() {
            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }
            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ((ViewPager) container).removeView(viewContainter.get(position));
            }
            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                if(position == 1){
                    RelativeLayout tv = (RelativeLayout) viewContainter.get(position).findViewById(R.id.guide_lin_intent_rel);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            guideLinlay.setVisibility(View.GONE);
                            addLoglLin.setVisibility(View.VISIBLE);
                            //广告显示倒计时
                            mc = new MyCountDownTimer(Long.parseLong(seconds), 1000);
                            mc.start();
//                            Intent intent = new Intent(StartMainActivity.this, MainFragmentActivity.class);
//                            startActivity(intent);
                        }
                    });
                }
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
    }
    /**
     * 倒计时
     */
    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            mc.cancel();
            if(addLoglLin.getVisibility() != View.VISIBLE) {
                String firstStr = SXUtils.getInstance(activity).getSharePreferences("isFirst");
                if(firstStr == null || firstStr.equals("0")){
                    SXUtils.getInstance(activity).setSharePreferences("isFirst","1");
                    guideLinlay.setVisibility(View.VISIBLE);
                    logoIv.setVisibility(View.GONE);
                }
                else{
                    guideLinlay.setVisibility(View.GONE);
                    addLoglLin.setVisibility(View.VISIBLE);
//                    mc = new MyCountDownTimer(2000, 1000);
//                    mc.start();
                    mc = new MyCountDownTimer( Long.parseLong(seconds), 1000);
                    mc.start();
                }
            }else{
                Glide.with(activity).pauseRequests();
                if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID)){
                    Intent intent = new Intent(StartMainActivity.this, LoginNameActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    SXUtils.getInstance(activity).IntentMain(activity);
                }
//                Intent intent = new Intent(StartMainActivity.this, MainFragmentActivity.class);
//                startActivity(intent);
//                if(TextUtils.isEmpty(AppClient.USER_SESSION) || TextUtils.isEmpty(AppClient.USER_ID)){
//                    Intent intent = new Intent(StartMainActivity.this, LoginNameActivity.class);
//                    startActivity(intent);
//                }
//                else{
//                SXUtils.getInstance(activity).IntentMain();
//                }
                finish();
            }

        }
        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("PortMainActivity", millisUntilFinished + "");
            countTv.setText("跳过"+millisUntilFinished / 1000);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            System.exit(0);
        }
        return true;
    }
    public void LauncherHttp() {
        HttpParams httpParams = new HttpParams();
        HttpUtils.getInstance(activity).requestPost(false,AppClient.APP_LAUNCH, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("启动图发送成功返回参数=======",jsonObject.toString());
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(jsonObject.toString());
                    String secondsStr = jsonObject1.getString("seconds");
                    if(!TextUtils.isEmpty(secondsStr)){
                        seconds = secondsStr+"000";
                    }else{
                        seconds = "3000";
                    }
                    imgUrl = jsonObject1.getString("imgUrl");
                } catch (JSONException e) {
                    Message msg = new Message();
                    msg.what = AppClient.ERRORCODE;
                    msg.obj = jsonObject.toString();
                    hand.sendMessage(msg);
                }
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = imgUrl;
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
