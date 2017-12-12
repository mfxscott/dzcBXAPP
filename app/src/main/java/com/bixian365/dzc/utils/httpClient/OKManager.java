package com.bixian365.dzc.utils.httpClient;

import android.app.Activity;
import android.text.TextUtils;

import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.bixian365.dzc.utils.httpClient.RequestReqMsgData.getReqsn;

/**
 * 封装请求工具类
 * @author mfx
 * 2016/10/26 15:10
 */
public class OKManager {
    private OkHttpClient client;
    private volatile  OKManager manager;
    private final String TAG = OKManager.class.getSimpleName();//获得类名
    public Activity activity;
//    private String  HttpUrl= "http://120.27.223.246:8080/xianhao365/api.do";
//    private String  HttpUrl= "http://139.224.60.232:8080/xianhao365/api.do";
    //    private String  HttpUrl= "http://139.224.60.232:8080/xianhao365/rest/mobile/13800138000/type/1";
    //    private Handler handler;
    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json");
    public OKManager(Activity context) {
        activity = context;
        client = new OkHttpClient.Builder()
//              .hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                Logs.i("域名验证==========="+hostname);
//                if(hostname.equals("www.sanxiapay.com")){
//                    return true;
//                }
//                Logs.i("域名验证false=============="+hostname);
//                return false;
//            }
//        })
//                .sslSocketFactory(https.getSocketFactory(context))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                //测试功能
//                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }
//    public   OKManager getInstance(Activity activitys){
////        if(manager == null){
//        manager = new OKManager(activitys);
////        }
//        activity = activitys;
//        return manager;
//    }

    /**
     * 向服务器提交String请求
     * @param requestBody  请求参数
     * @param rspMsgName  方法名
     * @param callBack 回调
     */
    public void sendStringByPostMethod(RequestBody requestBody, final String rspMsgName , final Func4 callBack) {
        Logs.i("请求地址参数======",requestBody.contentType()+"=");
        Logs.i("请求地址========",SXUtils.getInstance(activity).getApp().getHttpUrl()+"");
        Request request = new Request.Builder().url(SXUtils.getInstance(activity).getApp().getHttpUrl())
//        headMsgJson.put("X-App-Key","xianhao365");
//        //随机数
//        headMsgJson.put("X-Method",serviceCodeEnum);// "98d8b2e92be8c8fe640ef291c4fa843e305358e4f0652955b4dd9a5b5bfddef1"
//        //
//        headMsgJson.put("X-Timestamp", GetNowDateTime());
//        //协议版本（静态设值为：1.0）
//        headMsgJson.put("X-Version","1.0");
//        //用户ID 动态设值，用户登录必须设值，未登录不需要设值
//        headMsgJson.put("X-User-ID", TextUtils.isEmpty(AppClient.USER_ID) ? "" : AppClient.USER_ID);
//        //用户会话（动态设值，用户登录必须设值，未登录不需要设值）
//        headMsgJson.put("X-User-Session",TextUtils.isEmpty(AppClient.USER_SESSION) ? "" : AppClient.USER_SESSION);
//        //操作系统（静态设置：Android/iOS）
//        headMsgJson.put("X-OS","Android");
//        //操作系统版本（动态设值，当前系统版本号）
//        headMsgJson.put("X-OS-Version", SXUtils.getInstance(context).getClientDeviceInfo());
//        //APP版本（动态设值，当前APP版本号）
//        headMsgJson.put("X-App-Version",SXUtils.getInstance(context).getVersionName());
//        //客户端唯一标示（动态设值，UDID/IMEI）
//        headMsgJson.put("X-UDID", SXUtils.getInstance(context).getDeviceId());
//        //随机数
//        headMsgJson.put("X-Nonce",getReqsn());
                .addHeader("X-App-Key","XIANHAO365")
                .addHeader("X-Method",rspMsgName)
                .addHeader("X-Timestamp", SXUtils.getInstance(activity).GetNowDateTime())
                .addHeader("X-Version","1.0")
                .addHeader("X-User-ID", TextUtils.isEmpty(AppClient.USER_ID) ? "" : AppClient.USER_ID)
                .addHeader("X-User-Session",TextUtils.isEmpty(AppClient.USER_SESSION) ? "" : AppClient.USER_SESSION)
                .addHeader("X-OS","Android")
                .addHeader("X-OS-Version", SXUtils.getInstance(activity).getClientDeviceInfo())
                .addHeader("X-App-Version",SXUtils.getInstance(activity).getVersionName())
                .addHeader("X-UDID", SXUtils.getInstance(activity).getDeviceId())
                .addHeader("X-Nonce",getReqsn())
                //签名值（动态设值，获取方法 EBT.sign 见第2节）
//                .addHeader("X-Sign" MD5Utils.encrypt("123"))
                //签名方法（静态设值为：md5）
//                .addHeader("X-Sign-Method" MD5Utils.encrypt("123"))
//                .tag(activity)
                .post(requestBody).build();
        Logs.i("请求数据==================start=============================");
        Headers headers = request.headers();
//        Logs.i("==", "method : " + request.method());
//        Logs.i("==", "url : " + HttpUrl);
//        if (headers != null && headers.size() > 0) {
//            Log.e("==", "headers : \n");
//            Log.e("===", headers.toString());
//        }
        Logs.i("请求数据==================end=============================");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logs.i(rspMsgName+"连接服务器异常日志=======",e.toString()+"");
                client = new OkHttpClient.Builder()
//                        .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        Logs.i("域名验证111==========="+hostname);
//                        if(hostname.equals("www.sanxiapay.com")){
//                            return true;
//                        }
//                        Logs.i("域名验证false222=============="+hostname);
//                        return false;
//                    }
//                })
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                callBack.onResponseError("无法连接到服务器，请检查网络连接");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                Logs.i("==","url : " + clone.request().url());
                Logs.i("==", "code : " + clone.code());
                Logs.i("==", "head : " + clone.headers());
                Logs.i("==", "protocol : " + clone.protocol());
                if (response != null && response.isSuccessful()) {
                    Object result = response.body().string();
                    onSuccessJsonObjectMethod(result.toString(),rspMsgName,callBack);
                }
            }
        });
    }
    /**
     * 向服务器提交String请求
     * @param requestBody  请求参数
     * @param rspMsgName  方法名
     * @param callBack 回调
     */
    public void sendStringByPostMethod(String requestBody, final String rspMsgName , final Func4 callBack) {
        Logs.i("请求地址参数======",requestBody+"=");
        Request request = new Request.Builder().url(SXUtils.getInstance(activity).getApp().getHttpUrl())
                .addHeader("X-App-Key","XIANHAO365")
                .addHeader("X-Method",rspMsgName)
                .addHeader("X-Timestamp", SXUtils.getInstance(activity).GetNowDateTime())
                .addHeader("X-Version","1.0")
                .addHeader("X-User-ID", TextUtils.isEmpty(AppClient.USER_ID) ? "" : AppClient.USER_ID)
                .addHeader("X-User-Session",TextUtils.isEmpty(AppClient.USER_SESSION) ? "" : AppClient.USER_SESSION)
                .addHeader("X-OS","Android")
                .addHeader("X-OS-Version", SXUtils.getInstance(activity).getClientDeviceInfo())
                .addHeader("X-App-Version",SXUtils.getInstance(activity).getVersionName())
                .addHeader("X-UDID", SXUtils.getInstance(activity).getDeviceId())
                .addHeader("X-Nonce",getReqsn())
                //签名值（动态设值，获取方法 EBT.sign 见第2节）
//                .addHeader("X-Sign" MD5Utils.encrypt("123"))
                //签名方法（静态设值为：md5）
//                .addHeader("X-Sign-Method" MD5Utils.encrypt("123"))
//                .tag(activity)
                .post(RequestBody.create(JSON,requestBody)).build();
        Logs.i("请求数据==================start=============================");
        Headers headers = request.headers();
//        Logs.i("==", "method : " + request.method());
//        Logs.i("==", "url : " + HttpUrl);
//        if (headers != null && headers.size() > 0) {
//            Log.e("==", "headers : \n");
//            Log.e("===", headers.toString());
//        }
        Logs.i("请求数据==================end=============================");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logs.i(rspMsgName+"连接服务器异常日志=======",e.toString()+"");
                client = new OkHttpClient.Builder()
//                        .hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        Logs.i("域名验证111==========="+hostname);
//                        if(hostname.equals("www.sanxiapay.com")){
//                            return true;
//                        }
//                        Logs.i("域名验证false222=============="+hostname);
//                        return false;
//                    }
//                })
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
                callBack.onResponseError("无法连接到服务器，请检查网络连接");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Response.Builder builder = response.newBuilder();
                Response clone = builder.build();
                if (response != null && response.isSuccessful()) {
                    Object result = response.body().string();
                    onSuccessJsonObjectMethod(result.toString(),rspMsgName,callBack);
                }
            }
        });
    }
    /**
     * 返回响应的结果是json对象
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue, String rspMsgName, Func4 callBack) {
        Logs.i("返回报文解密前=====",jsonValue);
        JSONObject jsonObject;
        try {
//            String encryptStr = UXUNMSGEncrypt.getInstance().decrypt(new JSONObject(jsonValue.toString()));
//            Logs.i("返回报文解密后=====",encryptStr);
//            HeadRspMsgEntity remsg = GetMsgData(encryptStr,rspMsgName);
//            Logs.i("json获得响应码===", remsg.getMsgrsp().getRetcode());
            jsonObject = new JSONObject(jsonValue);
            if(jsonValue == null){
                callBack.onResponseError("连接服务器失败，请重试");
                return;
            }
            if("10000".equals(jsonObject.getString("responseCode"))){
                callBack.onResponse(jsonObject.get("responseData"));
            }
            else{
                callBack.onResponseError(jsonObject.getString("responseText"));
            }
        } catch (JSONException e) {
            callBack.onResponseError("返回数据解析失败");
        }
    }
    //    private HeadRspMsgEntity GetMsgData(String response, String rspMsgName){
//        JSONObject rspMsg = null;
//        JSONObject obj = null;
//        try {
//            obj = new JSONObject(response.toString());
//            rspMsg = obj.getJSONObject(rspMsgName);
//            Gson gson = new Gson();
//            HeadRspMsgEntity status = gson.fromJson(rspMsg.toString(), HeadRspMsgEntity.class);
//            return  status;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  null;
//    }
    public interface Func4 {
        void onResponse(Object jsonObject);
        void onResponseError(String strError);
    }


}
