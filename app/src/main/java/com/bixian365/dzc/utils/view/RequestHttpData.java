package com.bixian365.dzc.utils.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

/**
 * 网络请求公共类
 * 存放大部分网络请求方法，供界面直接调用，减少界面代码过长，繁琐
 */
public class RequestHttpData {
    private static RequestHttpData mInstance;
    private Context mContext;

    private RequestHttpData(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static RequestHttpData getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SXUtils.class) {
                if (mInstance == null) {
                    mInstance = new RequestHttpData(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 首页轮播图
     */
    public void ChannelRequestHttp(HttpParams httpParams, String url,final Handler hand,final int handCOde) {
        HttpUtils.getInstance(mContext).requestPost(false, url, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Message msg = new Message();
                if(handCOde == 0)
                    msg.what = 1000;
                else
                    msg.what = handCOde;
                msg.obj = jsonObject.toString();
                hand.sendMessage(msg);

            }
            @Override
            public void onResponseError(String strError){
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);
            }
        });
    }
}
