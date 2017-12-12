package com.bixian365.dzc.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.fragment.my.buyer.purchase.GetPicPopupWindow;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;


public class CommonWebViewMainActivity extends Activity {
    private WebView myWebView;
    private SwipeRefreshLayout SWwebv;
    public String postUrl = ""; // 第一次不同变量url
    private String LoginUrl = "";
    private String webTitleStr = "";
    private String tag = "";
    private Activity activity;
    private  ValueCallback<Uri> mUploadMessage;
    private  ValueCallback<Uri[]> mUploadMessageSZ;
    private  TextView allTitleName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_main);
        activity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EventBus.getDefault().register(this);
        /**
         * 活动url 点击图片是传入URL
         */
        postUrl = this.getIntent().getStringExtra("postUrl");
        tag = this.getIntent().getStringExtra("tag");
        /**
         * 活动入口地址 测试环境传
         */
        init();
        myWebView = (WebView) findViewById(R.id.common_webview);
        SWwebv = (SwipeRefreshLayout) findViewById(R.id.common_swipe_container);
         allTitleName = (TextView) findViewById(R.id.all_title_name);
        allTitleName.setText("");
//		Utils.getInstance(activity).setColorSchemeResources(SWwebv);
//		SWwebv.setColorSchemeResources( R.color.blue, R.color.green, R.color.gray,R.color.red);
        SWwebv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新刷新页面
                myWebView.reload();
            }
        });
//		myWebView.setSaveEnabled(true);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		webettings.setBuiltInZoomControls(false);// support zoom
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportMultipleWindows(true);
//		// 开启 DOM storage API 功能
//		webSettings.setDomStorageEnabled(true);
//		manager = OKManager.getInstance(activity);
        //获取密码因子注册设置密码，和支付密码有需要
//		GETPassWordValue(activity,manager,1);
//		GETPassWordValue(activity,manager,2);
        Logs.i("weiview请求链接====",postUrl+"");
        if(tag.equals("2")){
//          myWebView.postUrl(postUrl,null);
            myWebView.loadUrl(postUrl);
        }else {
            myWebView.postUrl(SXUtils.getInstance(activity).getApp().getHttpUrl(), SXUtils.getInstance(activity).WebViewPostJSONObject(activity,postUrl).getBytes());
        }
//        myWebView.addJavascriptInterface(new IntentWebJavaScriptInterface(),
//                "intent");
        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //触摸焦点起作用
        myWebView.requestFocus();
        //提供暴露接口给js调用
        myWebView.addJavascriptInterface(new WebViewJavaScriptInterface(myWebView,activity), "androidInterface");
        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                webTitleStr = title;
            }
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    SWwebv.setRefreshing(false);
                    SWwebv.setEnabled(false);
                } else {
                    if (!SWwebv.isRefreshing())
                        SWwebv.setRefreshing(true);
                }
                setTitle(view.getTitle()+"");
            }

        });
        myWebView.setWebChromeClient(new MyWebChromeClient());
//        myWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
//            }
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                webTitleStr = title;
//            }
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress == 100) {
//                    SWwebv.setRefreshing(false);
//                    SWwebv.setEnabled(false);
//                } else {
//                    if (!SWwebv.isRefreshing())
//                        SWwebv.setRefreshing(true);
//                }
//                setTitle(view.getTitle()+"");
//            }
//
//        });
        myWebView.setWebViewClient(new myWebViewClient());
    }
    private class MyWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), 1000);

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), 1000);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), 1000);

        }
        // For Android 5.0+
        public boolean onShowFileChooser (WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadMessageSZ = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    1000);
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            webTitleStr = title;
        }
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) {
                SWwebv.setRefreshing(false);
                SWwebv.setEnabled(false);
            } else {
                if (!SWwebv.isRefreshing())
                    SWwebv.setRefreshing(true);
            }
            allTitleName.setText(view.getTitle()+"");
        }
    }


    //     class ReWebChomeClient extends WebChromeClient {
//
//        @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
//            }
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                webTitleStr = title;
//            }
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress == 100) {
//                    SWwebv.setRefreshing(false);
//                    SWwebv.setEnabled(false);
//                } else {
//                    if (!SWwebv.isRefreshing())
//                        SWwebv.setRefreshing(true);
//                }
//                setTitle(view.getTitle()+"");
//            }
//        // For Android 5.0+
//        @Override
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams
//                fileChooserParams) {
//            showOptions();
//            return true;
//        }
//    }
    private static final String TAG = "MyActivity";
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsg5Plus;
    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
//        Uri[] results = null;
//        if (resultCode == 1000) {
//            if (data == null) {
//            } else {
//                String dataString = data.getDataString();
//                ClipData clipData = data.getClipData();
//                if (clipData != null) {
//                    results = new Uri[clipData.getItemCount()];
//                    for (int i = 0; i < clipData.getItemCount(); i++) {
//                        ClipData.Item item = clipData.getItemAt(i);
//                        results[i] = item.getUri();
//                    }
//                }
//                if (dataString != null)
//                    results = new Uri[]{Uri.parse(dataString)};
//            }
//        }
//        mUploadMessageSZ.onReceiveValue(results);
//        mUploadMessageSZ = null;
//        return;
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
        switch (requestCode) {
            case 1000:
                Uri[] results = null;
                if (data == null) {
                } else {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null)
                        results = new Uri[]{Uri.parse(dataString)};
                }
                mUploadMessageSZ.onReceiveValue(results);
                mUploadMessageSZ = null;
                break;
            case REQUEST_CODE_PICK_IMAGE:
//                try {
//                    if (mUploadMsg == null && mUploadMsg5Plus == null) {
//                        return;
//                    }
//                    String sourcePath;
//                    if(REQUEST_CODE_PICK_IMAGE == 0){
//                         sourcePath  =  getRealFilePath(activity, data.getData());
//                    }else{
//                         sourcePath = getPathTakePhoto(); //ImageUtil.retrievePath(this, mSourceIntent, data);
//                    }
//                    if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
//                        Log.w(TAG, "sourcePath empty or not exists.");
//                        break;
//                    }
//                    Uri uri = Uri.fromFile(new File(sourcePath));
//                    if (mUploadMsg != null) {
//                        mUploadMsg.onReceiveValue(uri);
//                        mUploadMsg = null;
//                    } else {
//                        mUploadMsg5Plus.onReceiveValue(new Uri[]{uri});
//                        mUploadMsg5Plus = null;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
        }

    }
    private GetPicPopupWindow getpicpop;
    public void showOptions() {
        getpicpop = new GetPicPopupWindow(activity, ShareOnclick, false);
        //显示窗口
        getpicpop.showAtLocation(myWebView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;


    View.OnClickListener ShareOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getpicpop != null)
                getpicpop.dismiss();
            switch (v.getId()) {
                case R.id.get_pic_photo:
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1000);
                    } else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUEST_CODE_PICK_IMAGE);
                    }
                    break;
                case R.id.get_pic_take:
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1000);
                    } else {
                        Intent intent = null;
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("return-data", false);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriTakePhoto());
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        intent.putExtra("noFaceDetection", true);
                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
                    }
                    break;
            }
        }
    };
    /**
     * 获取拍照上传图片地址
     *
     * @return
     */
    public Uri getUriTakePhoto() {
        File file = CreateText(GETPICPATH, "/temp2.jpg");
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    public Uri getUriPhoto() {
        File file = CreateText(GETPICPATH, "/temp1.jpg");
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    public String getPathTakePhoto() {
        File file = CreateText(GETPICPATH, "/temp2.jpg");
        return file.getPath();
    }
    /**
     * 首页缓存写入数据
     *
     * @param path     创建目录
     * @param fielName 文件名
     */
    public File CreateText(String path, String fielName) {
        File file = new File(path);
        File f;
        if (!file.exists()) {
            // 若不存在，创建目录
            file.mkdirs();
        }
        //创建文件
        f = new File(path + fielName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;

    }

    public String GETPICPATH = SXUtils.getInstance(activity).getSDPath() + "/cacheImg";//图片上传，保存图片地址
    /**
     * uri转 path
     *
     * @param context
     * @param uri
     * @return
     */
    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    private class ReOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
                mUploadMsg = null;
            }
            if (mUploadMsg5Plus != null) {
                mUploadMsg5Plus.onReceiveValue(null);
                mUploadMsg5Plus = null;
            }
        }
    }



    /**
     * 初始化参数
     */
    private void init() {
        LinearLayout allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
    }
    /**
     * 点击返回 返回上一页
     */
    private void returnBack() {
        //判断是生活订单 按系统返回键 直接退出
        if (myWebView.canGoBack()) {
            myWebView.goBackOrForward(-1);
        } else {
            finish();
        }
    }
    // 此按键监听的是返回键，能够返回到上一个网页（通过网页的hostlistery）
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            returnBack();
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    class myWebViewClient extends WebViewClient {
        @Override
        public void onFormResubmission(WebView view, Message dontResend,
                                       Message resend) {
            super.onFormResubmission(view, dontResend, resend);
            resend.getCallback();
        }
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            view.postUrl("file:///android_asset/networkerror.html",null);
        }
        /**
         * 信任https网页的证书
         */
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // 接受所有网站的证书
//			handler.cancel();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webTitleStr = view.getTitle();
            Logs.i("pagefinish========",webTitleStr+"");
        }
        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//			if(webmycount != null){
//				webmycount.cancel();
//			}
//			webmycount = new MyCount(1000*3,1000);
//			webmycount.start();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logs.i("webview跳转链接=====",url);
            view.loadUrl(url);
            return false;
        }

    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (resultCode) {
//            // 登录之后 传递参数值
//            case 10:
//                Logs.i("webview登录返回=======","======");
//                //把请求的URL参数 &转换,连接符号  避免jsp取值失败
////				String str1 = LoginUrl.replace("&", ",");
//                if (tag.equals("2")) {
//                    myWebView.postUrl(LoginUrl, null);
//                } else {
//                    myWebView.postUrl(SXUtils.getInstance(activity).getApp().getHttpUrl(), SXUtils.getInstance(activity).WebViewPostJSONObject(activity,LoginUrl).getBytes());
//                }
//                break;
//            case 1001:
//                Logs.i("webview=========返回1001");
//                myWebView.reload();
//                break;
//        }
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent){
        if(messageEvent.getTag()== AppClient.EVENT100019){
            allTitleName.setText(messageEvent.getMessage());
        }else if(messageEvent.getTag()== AppClient.EVENT100020){
            myWebView.loadUrl(messageEvent.getMessage());
        }else if(messageEvent.getTag() == AppClient.EVENT100011){
            finish();
        }
        else if(messageEvent.getTag() == AppClient.EVENT100021){
            myWebView.loadUrl("javascript:doInVue('" + AppClient.CarJSONInfo + "')");
        }else if(messageEvent.getTag() == AppClient.EVENT100022){
            JSONObject jsoUserInfo = new JSONObject();
            try {
                jsoUserInfo.put("uid",AppClient.USER_ID);
                jsoUserInfo.put("sid",AppClient.USER_SESSION);
                jsoUserInfo.put("phoneNo",AppClient.USERPHONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myWebView.loadUrl("javascript:getUserInfo('" + jsoUserInfo.toString() + "')");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

