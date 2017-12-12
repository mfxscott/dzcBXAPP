package com.bixian365.dzc.activity;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import com.lzy.okhttputils.OkHttpUtils;
import com.bixian365.dzc.utils.NetworkConnectChangedReceiver;

/**
 * des
 *
 * @author mfx  17/2/21 16:33
 */

public class MyApplication extends Application{

    private String  HttpUrl= "http://120.27.223.246:8080/xianhao365/api.do";
    //测试本机
//    private String HttpUrl = "http://192.168.1.102:9080/xianhao365/api.do";

//    private String HttpUrl = "http://192.168.1.211:9080/xianhao365/api.do";//杨才明本机IP
    public String getHttpUrl() {
        return HttpUrl;
    }
    public Context getContext(){
        return this.getApplicationContext();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
//        LeakCanary.install(this);
        // Normal app init code...
        //设置该CrashHandler为程序的默认处理器
//        CrashHandler catchExcep = new CrashHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(catchExcep);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkConnectChangedReceiver(), filter);
    }

    public void setHttpUrl(String httpUrl) {
        HttpUrl = httpUrl;
    }
}
