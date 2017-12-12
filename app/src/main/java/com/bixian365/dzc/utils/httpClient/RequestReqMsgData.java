package com.bixian365.dzc.utils.httpClient;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.dncry.wsc.UXUNMSGEncrypt;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


/**
 * Created by scott on 16/9/19.
 */
public class RequestReqMsgData {
    /**
     * 封装请求参数head头部固定信息
     * @param context 上下文
     * @param json   请求报文内容
     * @param serviceCodeEnum     枚举值
     * @param  tag  是否需要加密 1  加密 0 不加密
     * @return
     */
    public static String SetJsonHeadReqMsg(Activity context, JSONObject json, ServiceCodeEnum serviceCodeEnum, int tag) {
        JSONObject params = new JSONObject();
        JSONObject headMsgJson = new JSONObject();
        String encryptEntity = null;
        try {
            //交易参数
            JSONObject jsonObject = new JSONObject();
            //如果是登录不需传值// 195 设置手势密码
            if(serviceCodeEnum.getCommonId() == 74 || serviceCodeEnum.getCommonId() == 195) {
                //头信息封装
                headMsgJson.put("authcode", "");
            }
//            220 227 221 224 226 311
            else if(serviceCodeEnum.getCommonId() == 220 || serviceCodeEnum.getCommonId() == 227
                    || serviceCodeEnum.getCommonId() == 221 || serviceCodeEnum.getCommonId() == 224
                    || serviceCodeEnum.getCommonId() == 226 || serviceCodeEnum.getCommonId() == 311){
                //约定wscommon  不涉及会员相关接口全部传123456
                headMsgJson.put("authcode", "123456");
            }else{

                //头信息封装 涉及authcode
//                headMsgJson.put("authcode", TextUtils.isEmpty(AppClient.TOKENSTR) ? "123456" : AppClient.TOKENSTR);
            }
            //设备信息
            headMsgJson.put("devno",SXUtils.getInstance(context).getClientDeviceInfo());
//            headMsgJson.put("tokenstr",AppClient.TOKENSTR+"");
            //随机数
            headMsgJson.put("reqsn",getReqsn());// "98d8b2e92be8c8fe640ef291c4fa843e305358e4f0652955b4dd9a5b5bfddef1"
            //交易时间
            headMsgJson.put("trandatetime", SXUtils.getInstance(context).GetNowDateTime());
            //渠道标示 007 移动端 005 PC端
            headMsgJson.put("tranchannel", "007");
            //交易名称
            headMsgJson.put("servicename", serviceCodeEnum.getServiceName());
            //app版本名
            headMsgJson.put("version",SXUtils.getInstance(context).getVersionName());//getVersionName(context)+""
            //头信息
            jsonObject.putOpt("msghead", headMsgJson);
            //参数封装
            jsonObject.putOpt("msgreq", json);
            params.putOpt(serviceCodeEnum.getBaseName(), jsonObject);
//            encryptEntity = params.toString();
            Logs.i("请求报文========",params.toString());
            if(tag ==1){
                encryptEntity = UXUNMSGEncrypt.getInstance().encrypt(params.toString(), serviceCodeEnum.getCommonId());
            }else{
                encryptEntity = params.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptEntity;
    }


    /**
     * 封装请求参数head头部固定信息
     * @param context 上下文
     * @param requestData   请求报文内容
     * @param serviceCodeEnum     枚举值
     * @param  tag  是否需要加密 1  加密 0 不加密
     * @return
     */
    public static String setJsonHeadXHHReqMsg(Activity context, JSONObject requestData, String serviceCodeEnum, int tag) {
//        JSONObject params = new JSONObject();
        JSONObject headMsgJson = new JSONObject();
        String encryptEntity = null;
        try {
            //交易参数
//            JSONObject jsonObject = new JSONObject();
                //头信息封装

                //头信息封装 涉及authcode
                headMsgJson.put("X-App-Key","xianhao365");
            //随机数
            headMsgJson.put("X-Method",serviceCodeEnum);// "98d8b2e92be8c8fe640ef291c4fa843e305358e4f0652955b4dd9a5b5bfddef1"
            //
            headMsgJson.put("X-Timestamp", SXUtils.getInstance(context).GetNowDateTime());
            //签名值（动态设值，获取方法 EBT.sign 见第2节）
//            try {
//                headMsgJson.put("X-Sign",MD5Utils.encrypt("123"));
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            //签名方法（静态设值为：md5）
//            try {
//                headMsgJson.put("X-Sign-Method", MD5Utils.encrypt("123"));
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            //协议版本（静态设值为：1.0）
            headMsgJson.put("X-Version","1.0");
            //用户ID 动态设值，用户登录必须设值，未登录不需要设值
            headMsgJson.put("X-User-ID",TextUtils.isEmpty(AppClient.USER_ID) ? "" : AppClient.USER_ID);
            //用户会话（动态设值，用户登录必须设值，未登录不需要设值）
            headMsgJson.put("X-User-Session",TextUtils.isEmpty(AppClient.USER_SESSION) ? "" : AppClient.USER_SESSION);
            //操作系统（静态设置：Android/iOS）
            headMsgJson.put("X-OS","Android");
            //操作系统版本（动态设值，当前系统版本号）
            headMsgJson.put("X-OS-Version",SXUtils.getInstance(context).getClientDeviceInfo());
            //APP版本（动态设值，当前APP版本号）
            headMsgJson.put("X-App-Version",SXUtils.getInstance(context).getVersionName());
            //客户端唯一标示（动态设值，UDID/IMEI）
            headMsgJson.put("X-UDID", SXUtils.getInstance(context).getDeviceId());
            //随机数
            headMsgJson.put("X-Nonce",getReqsn());
//            headMsgJson.put("params",requestData);
            //头信息
//            jsonObject.putOpt("msghead", headMsgJson);
//            //参数封装
//            jsonObject.putOpt("msgreq", json);
//            params.putOpt("requestData", jsonObject);
//            encryptEntity = params.toString();
            Logs.i("请求报文========",headMsgJson.toString());
            if(tag ==1){
//                encryptEntity = UXUNMSGEncrypt.getInstance().encrypt(params.toString(), serviceCodeEnum.getCommonId());
            }else{
                encryptEntity = headMsgJson.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptEntity;
    }

    /**
     * 检查新版本
     * @param activity
     * @param hand  结果返回
     */
    public static void  UpdateVersion(Activity activity, final Handler hand){
        HttpUtils.getInstance(activity).requestPost(false, AppClient.APP_UPDATE, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("注册发送成功返回参数=======",jsonObject.toString());
                String userId = "";
                if(!TextUtils.isEmpty(jsonObject.toString()) && !jsonObject.toString().trim().equals("null")){
                    if(null != hand){
                        Message msg = new Message();
                        msg.what = AppClient.UPDATEVER;
                        msg.obj = jsonObject.toString();
                        hand.sendMessage(msg);
                    }else {
                        EventBus.getDefault().post(new MessageEvent(AppClient.UPDATEVER, jsonObject.toString() + ""));
                    }
                }else {
                    if(null != hand){
                        Message msg = new Message();
                        msg.what = AppClient.ERRORCODE;
                        msg.obj = "查询异常";
                        hand.sendMessage(msg);
                    }
                }
            }
            @Override
            public void onResponseError(String strError) {
                Logs.i("服务器连接异常","========"+strError.toString());
            }
        });



//        RequestBody requestBody = new FormBody.Builder()
//                .build();
//        new OKManager(activity).sendStringByPostMethod(requestBody,AppClient.APP_UPDATE, new OKManager.Func4() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                Logs.i("版本返回====","========"+jsonObject);
////                Map<String,String> map = GetRspMsgData.getUpdateVer(jsonObject.toString());
//////                Logs.i("获取到版本更新内容======",map.get("verCode")+"=="+map.get("verUrl")+map.get("verContent"));
////                if(map == null){
////                    return;
////                }
//                if(!TextUtils.isEmpty(jsonObject.toString()) && !jsonObject.toString().trim().equals("null")){
//                    EventBus.getDefault().post(new MessageEvent(AppClient.UPDATEVER, jsonObject.toString()+""));
//                }else {
//
//                }
//
//            }
//            @Override
//            public void onResponseError(String strError) {
////                Message msg = new Message();
////                msg.what = AppClient.ERRORCODE;
////                msg.obj = strError;
////                hand.sendMessage(msg);
//                Logs.i("服务器连接异常","========"+strError.toString());
//            }
//        });
    }

//    /**
//     * 得到设备信息
//     * @return
//     */
//    public static String getClientDeviceInfo(Context context) {
//        String deviceID = "";
//        String serial = "";
//        //		tm.getDeviceId();
//        deviceID = getDeviceId(context);
//        try {
//            Class<?> c = Class.forName("android.os.SystemProperties");
//            Method get = c.getMethod("get", String.class);
//            serial = (String) get.invoke(c, "ro.serialno");
//        } catch (Exception e) {
//            Logs.e("TAG", "get the system sn ERROR!", e);
//        }
//        String buildVersion = android.os.Build.VERSION.RELEASE;
//        return deviceID + "|system"  + buildVersion + "";
//        //		return deviceID + "|android" + "|android|" + buildVersion + "|android";
//    }

    /**
     * 取得请求序列号
     *
     * @return
     */
    public static String getReqsn() {
        Date date = new Date();
        Format formmatterLong = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        Random generator = new Random();
        String randomNumber = String.valueOf((long) (generator.nextDouble() * 1000000));
        String rspno = randomNumber+formmatterLong.format(date);
        //		AESEDncryption aes = new AESEDncryption();
//        AESEDncryption a = new AESEDncryption();

        String AesStr="";
        try {
//            AesStr = a.bytesToHex(a.encryptByte(rspno));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rspno;
    }


    /**
     * 获取版本号
     * @return
     */
    public static int getVersionCode(Context context){
        PackageManager manager = context.getPackageManager();//获取包管理器
        try {
            //通过当前的包名获取包的信息
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);//获取包对象信息
            return  info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }



}

