package com.bixian365.dzc.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.bixian365.dzc.activity.GoodsDetailActivity;
import com.bixian365.dzc.activity.member.LoginNameActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 与H5交互工具类
 * 定义相关接口提供给H5调用
 * @author mfx
 */
public class WebViewJavaScriptInterface {
    //    1、H5获取用户手机号（无登录，返回空）
//    {"action": "getPhoneNo","callbackMethod":"非空需回调"}
//2、H5关闭 WebView 窗口
//    {"action": "closeWebView"}
//3、H5打开 WebView 窗口
//    {"action": "openWebView","data":{"url":"URL地址"}}
//4、H5跳原生页面
//4.1、商品详情页（参数：商品编号）
//    {"action": "gotoNativeView","view":"6","data":{"goodsId":"商品编号"}}
//4.2、购物车
//    {"action": "gotoNativeView","view":"4"}
//5、H5获取购物车详情
//    {"action": "getShoppingCart"}
//6、H5加入购物车
//    {"action": "addShoppingCart","data":{"skuBarcode":"SKU条码","quantity":"数量"}}
//7、H5获取APP版本信息
//    {"action": "getAppVersion"}
//8、APP打开H5专题页（http://h5.xianhao365.com/views/topic.html?id=专题ID）
//    {"action": "setWebViewTitle","data":{"title":"我是标题"}
    private Activity context;
    private WebView webView;
    public WebViewJavaScriptInterface(WebView webview, Activity activity){
        this.context = activity;
        this.webView = webview;
    }
    //     androidInterface.androidWebViewMethod("tag","jsonString");
    @JavascriptInterface
    public void androidWebViewMethod(String json) {
//        {"action": "getPhoneNo","callbackMethod":"非空需回调"}
        String actionStr = "";
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            actionStr = jsonObject.getString("action");
            if(actionStr.equals("setWebViewTitle")){
//                {"action": "setWebViewTitle","data":{"title":"我是标题"}
                JSONObject jsn = jsonObject.getJSONObject("data");
                String title = jsn.getString("title");
                EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100019,title+""));
            }else  if(actionStr.equals("getUserInfo")){
                JSONObject jsoUserInfo = new JSONObject();
                jsoUserInfo.put("uid",AppClient.USER_ID);
                jsoUserInfo.put("sid",AppClient.USER_SESSION);
                jsoUserInfo.put("phoneNo",AppClient.USERPHONE);
                jsoUserInfo.put("tag",AppClient.USERROLETAG);
                sendJsMethod(jsonObject,jsoUserInfo.toString());
                //获取登录信息
            }else if(actionStr.equals("closeWebView")){
                //关闭
                context.finish();
            }else if(actionStr.equals("openWebView")){
                //打开新的webview窗口
//            SXUtils.getInstance(context).ToastCenter("打开新界面"+actionStr);
                JSONObject jsn = jsonObject.getJSONObject("data");
                String url = jsn.getString("url");
            }

            else if(actionStr.equals("gotoNativeView")){
//            商品详情页（参数：商品编号）
//            {"action": "gotoNativeView","view":"6","data":{"goodsId":"商品编号"}}
//            {"action": "gotoNativeView","view":"4"} 购物车
                JSONObject jsn = jsonObject.getJSONObject("data");
                String viewstr = jsn.getString("view");
                if(viewstr.equals("6")){
                    String goodsId = jsn.getString("goodsId");
                    Intent intent = new Intent(context, GoodsDetailActivity.class);
                    intent.putExtra("cno",goodsId);
                    context.startActivity(intent);
                }else if(viewstr.equals("4")){
                    AppClient.TAG4 = true;
                    context.finish();
                }else if(viewstr.equals("8")){
                    String orderno = jsn.getString("orderNo");
//                    Intent intent = new Intent(context, OrderDetailActivity.class);
//                    intent.putExtra("cno",orderno);
//                    context.startActivity(intent);
                }
                else if(viewstr.equals("11")){
                    //跳转登录
//                    {"action": "gotoNativeView","data":{"view":"11"}}
                    Intent intent = new Intent(context, LoginNameActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("isEnter","12");
                    context.startActivity(intent);
                }
                else if(viewstr.equals("12")){
                    //活动商品直接立即购买
//                    {"action":"gotoNativeView","data":{"view”:“12”,”goodsId”:”商品id”}}
                }
            }
            else if(actionStr.equals("gotoSettlement")){
                //                {"action":"gotoSettlement","data":{”skuBarcode”:”15646544”,"quantity":"数量"}}
                JSONObject jsn = jsonObject.getJSONObject("data");
                String skuGoods = jsn.getString("skuBarcode");
                String numStr = jsn.getString("quantity");
                SXUtils.getInstance(context).AddOrUpdateCar(skuGoods,numStr);


            }
            else if(actionStr.equals("getShoppingCart")){
                if(!TextUtils.isEmpty(AppClient.CarJSONInfo)){
                    //购物车详情 购物车信息
                    sendJsMethod(jsonObject,AppClient.CarJSONInfo);
                }
            }

            else if(actionStr.equals("addShoppingCart")){
                //加入购物车
                JSONObject jsn = jsonObject.getJSONObject("data");
                String skuGoods = jsn.getString("skuBarcode");
                String numStr = jsn.getString("quantity");
                SXUtils.getInstance(context).AddOrUpdateCar(skuGoods,numStr);
            }else if(actionStr.equals("getAppVersion")){
                //获取客户端版本信息
//                String callBack = jsonObject.getString("callbackMethod");
                sendJsMethod(jsonObject,SXUtils.getInstance(context).getVersionName());
//                webView.loadUrl("javascript:doInVue(1.0.0)");
//                webView.loadUrl("javascript:"+callBack+"('"+SXUtils.getInstance(context).getClientDeviceInfo()+"')");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        SXUtils.getInstance(context).ToastCenter("返回值"+json.toString());
        Logs.i("js调用返回参数值==============",json.toString());
    }

    /**
     * 调用js 函数进行传参数
     * @param jsonObject  js返回function 函数
     * @param value   需要传递给js函数 参数
     */
    private void sendJsMethod(JSONObject jsonObject,String value){
        String callBack = null;
        try {
            callBack = jsonObject.getString("callbackMethod");
            String  messageLoad = "javascript:"+callBack+"('"+value+"')";
            EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100020,messageLoad+""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @JavascriptInterface
    public void  intentWebBrowser(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
