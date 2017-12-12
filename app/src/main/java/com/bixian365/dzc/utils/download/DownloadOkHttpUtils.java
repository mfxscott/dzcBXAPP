package com.bixian365.dzc.utils.download;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.math.BigDecimal;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by mfx-t224 on 2017/9/13.
 */
public class DownloadOkHttpUtils {
    public static void DownFile(final Context context, String Url, final ProgressBar progressBar, final TextView numPro){
        OkHttpUtils.get(Url)
                .tag("downapk")
                .execute(new FileCallback(SXUtils.getInstance(context).getSDPath()+"/apk","bx.apk") {
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        Logs.i(file.getPath()+"========+++"+response.toString());
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logs.i("======222==="+e.toString());
                    }
                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        BigDecimal bigdec = new BigDecimal(progress).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP);
//                        numPro.setText(bigdec + "%");
//                        progressBar.setProgress(bigdec.intValue());
                        Logs.i("==="+currentSize+"======="+totalSize+"====="+progress+"==="+networkSpeed);
                        EventBus.getDefault().post(new MessageEvent(6666, bigdec.intValue() + ""));
                        if(currentSize == totalSize){
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(SXUtils.getInstance(context).getSDPath()+"/apk/bx.apk")),
                                    "application/vnd.android.package-archive");
                            context.startActivity(intent);
                        }
                    }
                });
//        OkHttpUtils.get("") // 请求方式和请求url, get请求不需要拼接参数，支持get，post，put，delete，head，options请求
//                .tag(this)               // 请求的 tag, 主要用于取消对应的请求
//                .connTimeOut(5*60*1000)      // 设置当前请求的连接超时时间
//                .readTimeOut(5*60*1000)      // 设置当前请求的读取超时时间
//                .writeTimeOut(5*60*1000)     // 设置当前请求的写入超时时间
////                .cacheKey("cacheKey")    // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
////                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST) // 缓存模式，详细请看第四部分，缓存介绍
////                .setCertificates(getAssets().open("srca.cer")) // 自签名https的证书，可变参数，可以设置多个
////                .addInterceptor(interceptor)            // 添加自定义拦截器
////                .headers("header1", "headerValue1")     // 添加请求头参数
////                .headers("header2", "headerValue2")     // 支持多请求头参数同时添加
////                .params("param1", "paramValue1")        // 添加请求参数
////                .params("param2", "paramValue2")        // 支持多请求参数同时添加
////                .params("file1", new File("filepath1")) // 可以添加文件上传
////                .params("file2", new File("filepath2")) // 支持多文件同时添加上传
////                .addUrlParams("key", List<String> values)  //这里支持一个key传多个参数
////                .addFileParams("key", List<File> files)    //这里支持一个key传多个文件
////                .addFileWrapperParams("key", List< HttpParams.FileWrapper> fileWrappers) //这里支持一个key传多个文件
////                .addCookie("aaa", "bbb")                // 这里可以传递自己想传的Cookie
////                .addCookie(cookie)                      // 可以自己构建cookie
////                .addCookies(cookies)                    // 可以一次传递批量的cookie
//                //这里给出的泛型为 RequestInfo，同时传递一个泛型的 class对象，即可自动将数据结果转成对象返回
//                .execute(new DialogCallback<RequestInfo>(this, RequestInfo.class) {
//                    @Override
//                    public void onBefore(BaseRequest request) {
//                        // UI线程 请求网络之前调用
//                        // 可以显示对话框，添加/修改/移除 请求参数
//                    }
//
//                    @Override
//                    public RequestInfo parseNetworkResponse(Response response) throws Exception{
//                        // 子线程，可以做耗时操作
//                        // 根据传递进来的 response 对象，把数据解析成需要的 RequestInfo 类型并返回
//                        // 可以根据自己的需要，抛出异常，在onError中处理
//                        return null;
//                    }
//
//                    @Override
//                    public void onResponse(boolean isFromCache, RequestInfo requestInfo, Request request, @Nullable Response response) {
//                        // UI 线程，请求成功后回调
//                        // isFromCache 表示当前回调是否来自于缓存
//                        // requestInfo 返回泛型约定的实体类型参数
//                        // request     本次网络的请求信息，如果需要查看请求头或请求参数可以从此对象获取
//                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果数据来自于缓存，该对象为null
//                    }
//
//                    @Override
//                    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
//                        // UI 线程，请求失败后回调
//                        // isFromCache 表示当前回调是否来自于缓存
//                        // call        本次网络的请求对象，可以根据该对象拿到 request
//                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果网络异常 或者数据来自于缓存，该对象为null
//                        // e           本次网络访问的异常信息，如果服务器内部发生了错误，响应码为 400~599之间，该异常为 null
//                    }
//
//                    @Override
//                    public void onAfter(boolean isFromCache, @Nullable RequestInfo requestInfo, Call call, @Nullable Response response, @Nullable Exception e) {
//                        // UI 线程，请求结束后回调，无论网络请求成功还是失败，都会调用，可以用于关闭显示对话框
//                        // isFromCache 表示当前回调是否来自于缓存
//                        // requestInfo 返回泛型约定的实体类型参数，如果网络请求失败，该对象为　null
//                        // call        本次网络的请求对象，可以根据该对象拿到 request
//                        // response    本次网络访问的结果对象，包含了响应头，响应码等，如果网络异常 或者数据来自于缓存，该对象为null
//                        // e           本次网络访问的异常信息，如果服务器内部发生了错误，响应码为 400~599之间，该异常为 null
//                    }
//
//                    @Override
//                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                        // UI 线程，文件上传过程中回调，只有请求方式包含请求体才回调（GET,HEAD不会回调）
//                        // currentSize  当前上传的大小（单位字节）
//                        // totalSize 　 需要上传的总大小（单位字节）
//                        // progress     当前上传的进度，范围　0.0f ~ 1.0f
//                        // networkSpeed 当前上传的网速（单位秒）
//                    }
//
//                    @Override
//                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
//                        // UI 线程，文件下载过程中回调
//                        //参数含义同　上传相同
//                    }
//                });
    }
}
