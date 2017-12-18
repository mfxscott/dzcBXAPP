package com.bixian365.dzc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bixian365.dzc.activity.SearchActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.MyApplication;
import com.bixian365.dzc.activity.member.LoginNameActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.entity.UserRenderInfoEntity;
import com.bixian365.dzc.entity.address.AddressProvinceEntity;
import com.bixian365.dzc.entity.bill.BillDataSetEntity;
import com.bixian365.dzc.fragment.MainFragmentActivity;
import com.bixian365.dzc.utils.dncry.wsc.AESEDncryption;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.SwipyRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.bixian365.dzc.utils.httpClient.RequestReqMsgData.getReqsn;

public class SXUtils {
    public String GETPICPATH = getSDPath() + "/cacheImg";//图片上传，保存图片地址
    // 定义私有构造方法（防止通过 new SingletonTest()去实例化）
    // 定义一个SingletonTest类型的变量（不初始化，注意这里没有使用final关键字）
    // 定义一个静态的方法（调用时再初始化SingletonTest，但是多线程访问时，可能造成重复初始化问题）
    private static SXUtils mInstance;
    private Context mContext;

    private SXUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static SXUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SXUtils.class) {
                if (mInstance == null) {
                    mInstance = new SXUtils(context);
                }
            }
        }
        return mInstance;
    }
    // 获取根目录路径
    public  String getSDPath() {
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        // 如果有sd卡，则返回sd卡的目录
        if (hasSDCard){
            return Environment.getExternalStorageDirectory().getPath()+"/sx";
        } else
            // 如果没有sd卡，则返回存储目录
            return Environment.getDownloadCacheDirectory().getPath()+"/sx";
    }
    public ArrayList<Activity> list = new ArrayList<Activity>();
    /**
     * Activity关闭时，删除Activity列表中的Activity对象*/
    public void removeActivity(Activity a){
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象*/
    public void addActivity(Activity a){
        list.add(a);
        Logs.i("baseActivity=====",list.size()+"=====");
    }
    /**
     * 关闭Activity列表中的所有Activity*/
    public void finishActivity(){
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
    }
    /**
     * 创建文件夹
     * @return
     */
//    public void CreateText(String path) {
//        File f = new File(path);
//        System.out.println(f.exists());
//
//    }
    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    public  long getFolderSize(File file){
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++)
            {
                if (fileList[i].isDirectory())
                {
                    size = size + getFolderSize(fileList[i]);
                }else{
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size/1024;
//        return size;
    }
    //删除文件夹和文件夹里面的文件
    public  void deleteDir(final String pPath) {
        File dir = new File(pPath);
        if(dir.exists()){
            deleteDirWihtFile(dir);
        }

    }

    public  void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    /**
     * 判断当前摄像头能否被使用（摄像头权限是否开启）
     *
     * @return
     */
    public boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }
    /**
     * @param context
     * @param str   提示文字
     */
//    public  void ToastCenter(Context context, String str) {
//                try {
////                    Utils.getInstance(.ToastshowDialogView((Activity) context, "温馨提示", str + "");
//                }catch (Exception e){
//                    Logs.i("弹出提示异常信息","==="+e.toString());
//                }
//            }

    /**
     * 提示信息
     */
    public void ToastCenter(String str) {
        Toast toast = Toast.makeText(mContext, TextUtils.isEmpty(str) ? "连接服务器异常" : str, Toast.LENGTH_SHORT);
        //第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶
        //第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移
        //第三个参数：同的第二个参数道理一样
        //如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示
        toast.setGravity(Gravity.CENTER, 0, 0);
        //屏幕居中显示，X轴和Y轴偏移量都是0
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static Dialog dialog;
    public static LayoutInflater inflater;
    public static LinearLayout.LayoutParams params;

    /**
     * @param str
     * @param isBack true可按返回取消
     */
    public static void showMyProgressDialog(Activity activity, String str, boolean isBack) {
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.common_loading, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.progressdialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_dialog_text);// 提示文字
        tipTextView.setText(str + "...");// 设置加载信息
        dialog = new Dialog(activity, R.style.common_progressloading_dialog);// 创建自定义样式dialog
        dialog.setCancelable(isBack);// 不可以用“返回键”取消
        dialog.setContentView(layout, params);// 设置布局
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * @param isBack true可按返回取消
     */
    public static void showMyProgressDialog(Context activity, boolean isBack) {
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.common_loading, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.progressdialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_dialog_text);// 提示文字
        tipTextView.setText("");// 设置加载信息
        dialog = new Dialog(activity, R.style.common_progressloading_dialog);// 创建自定义样式dialog
        dialog.setCancelable(isBack);// 不可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(layout, params);// 设置布局
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }
    public static void showMyProgressDialog(Context activity, boolean isBack,int tag) {
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.common_loading, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.progressdialog_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tv_dialog_text);// 提示文字
        tipTextView.setText("");// 设置加载信息
        dialog = new Dialog(activity, R.style.common_progressloading_dialog);// 创建自定义样式dialog
        dialog.setCancelable(isBack);// 不可以用“返回键”取消
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout, params);// 设置布局
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 取消加载匡
     *
     * @param
     * @author mfx
     */
    public static void DialogDismiss() {
        if (dialog == null) {
            return;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 弹出提示 确定
     *
     * @param title
     * @param contentStr
     */
    public void ToastshowDialogView(Context activity, String title, String contentStr) {
        final Dialog tipDialog = new AlertDialog.Builder(activity).create();
        tipDialog.show();
        tipDialog.setCancelable(true);
        tipDialog.setCanceledOnTouchOutside(false);
        Window window = tipDialog.getWindow();
        window.setContentView(R.layout.common_dialog);
        LinearLayout cancel = (LinearLayout) window.findViewById(R.id.dialog_liny);
        TextView content = (TextView) window.findViewById(R.id.dialog_message_tv);
        TextView titletv = (TextView) window.findViewById(R.id.dialog_title_tv);
        TextView rightBtn = (TextView) window.findViewById(R.id.dialog_right_btn);
        TextView leftBtn = (TextView) window.findViewById(R.id.dialog_right_btn);
        View vi = window.findViewById(R.id.add_bank_dialog_view);
        vi.setVisibility(View.GONE);
        vi.setPadding(0, 2, 0, 2);
        leftBtn.setText("确定");
        titletv.setText(title);
        ImageView iv = (ImageView) window.findViewById(R.id.dialog_close_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        content.setText(contentStr);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog.dismiss();
            }
        });
    }

//    public void  setSysStatusBar(Activity activity,int color){
////        setTranslucentStatus(activity);
//
//        SystemBarTintManager tintManager=new SystemBarTintManager(activity);
//        tintManager.setStatusBarTintResource(color);
//        tintManager.setStatusBarTintEnabled(true);
//    }

    /**
     * webview 请求post参数进行加密传输
     *
     * @param context
     * @param url     跳转url
     * @return
     */
    public String WebViewPostJSONObject(Context context, String url) {
        JSONObject jsonObject = new JSONObject();
        String encryptKey = "";
        try {
//            jsonObject.put("latitude", AppClient.LATITUDE);
//            jsonObject.put("longitude", AppClient.LONGITUDE);
//            jsonObject.put("memberno", AppClient.MEMBERNO);
            jsonObject.put("channel", "APP");
//            jsonObject.put("tokenstr", AppClient.TOKENSTR);
//            jsonObject.put("loginname", AppClient.PHONENO);
            jsonObject.put("gourl", url);
//            jsonObject.put("customerid",AppClient.CUSTOMERSID);
//            jsonObject.put("user_id",AppClient.USERID);
            jsonObject.put("mobileuuid", getClientDeviceInfo());
            String json = jsonObject.toString();
            Logs.i("web封装参数====" + json);
            AESEDncryption mAes = new AESEDncryption();

            try {
                encryptKey = mAes.encrypt(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "sxUrl=" + encryptKey;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return encryptKey;
    }

    /**
     * 拨打客服电话
     */
    public void CallCustPhone(){
        //拨打客服电话
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data = Uri.parse("tel:" + AppClient.CUST_TELEPHONE);
        intent.setData(data);
        mContext.startActivity(intent);
    }
    /**
     * 提示信息
     *
     * @param intstr 文字
     */
    public void ToastCenter(int intstr) {
        Toast toast = Toast.makeText(mContext, intstr, Toast.LENGTH_SHORT);
        //第一个参数：设置toast在屏幕中显示的位置。我现在的设置是居中靠顶
        //第二个参数：相对于第一个参数设置toast位置的横向X轴的偏移量，正数向右偏移，负数向左偏移
        //第三个参数：同的第二个参数道理一样
        //如果你设置的偏移量超过了屏幕的范围，toast将在屏幕内靠近超出的那个边界显示
        toast.setGravity(Gravity.CENTER, 0, 0);
        //屏幕居中显示，X轴和Y轴偏移量都是0
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 公共设置 界面 购物车数量，搜索商品，商品详情
     * @param carNum
     */
    public void setGoodsBadeNum(TextView  carNum){
        if(MainFragmentActivity.totalCarNum == 0){
            carNum.setVisibility(View.GONE);
        }else{
            carNum.setTextColor(mContext.getResources().getColor(R.color.white));
            carNum.setBackground(mContext.getResources().getDrawable(R.drawable.my_lc_msg_shap));
            carNum.setVisibility(View.VISIBLE);
            if(MainFragmentActivity.totalCarNum >99){
                carNum.setText(99+"+");
            }else{
                carNum.setText(MainFragmentActivity.totalCarNum+"");
            }
        }
    }
    /**
     * 保持状态值
     * 得到application对象
     *
     * @return
     */
    public MyApplication getApp() {
        return ((MyApplication) mContext.getApplicationContext());
    }

    public void setSharePreferences(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sxsc",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString(key, value);
        //提交当前数据
        editor.apply();
    }

    public String getSharePreferences(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sxsc",
                Activity.MODE_PRIVATE);
        String userId = sharedPreferences.getString(key, "0");
        return userId;
    }

    public void removeSharePreferences(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("sxsc",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 判断密码输入是否包含数字和密码
     *
     * @param str
     * @return true  包含
     */
    public boolean testPsd(String str) {
        boolean isDigit = false;
        boolean isLetter = false;
        for (int i = 0; i < str.length(); i++) { //循环遍历字符串
            if (Character.isDigit(str.charAt(i))) {     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        if (isDigit == true && isLetter == true) {
            return true;
        }
        return false;
    }

    /**
     * 得到设备信息
     *
     * @return
     */
    public String getClientDeviceInfo() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
        } catch (Exception e) {
            Logs.e("TAG", "get the system sn ERROR!", e);
        }
        String buildVersion = android.os.Build.VERSION.RELEASE;
        return "" + buildVersion + "";
        //		return deviceID + "|android" + "|android|" + buildVersion + "|android";
    }

    /**
     * 渠道标志为： 1，andriod（a）
     *
     * @return
     * @Description
     */
    public String getDeviceId() {
        String imei = "";
        // 渠道标志 a
        try {
            // IMEI（imei）
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            imei = "imei" + tm.getDeviceId();
            return "" + imei;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logs.i("getDeviceId : i++++++++++++++++++++++++", imei.toString());
        return imei.toString();

    }

    /**
     * 获取系统版本号
     *
     * @param
     * @throws Exception
     * @author mfx
     */
    public String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        String version = "1.0.0";
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取短信验证码
     *
     * @param activity 上下文
     * @param mobile   手机号
     * @param type     (1=登录,2=注册,3=忘记密码,4=安全密码)
     * @param handler  回调
     */
//    public void getCodeMsgHttp(Activity activity, String mobile, String type, final Handler handler) {
//        RequestBody requestBody = new FormBody.Builder()
//                .add("mobile", mobile)
//                .add("type", type)//拉取类型(1=登录,2=注册,3=忘记密码,4=安全密码)
//                .build();
//        new OKManager(activity).sendStringByPostMethod(requestBody, AppClient.GET_CODEMSG, new OKManager.Func4() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                String secs="";
//                try {
//                    JSONObject jsonO = new JSONObject(jsonObject.toString());
//                    secs = jsonO.getString("secs");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.what = AppClient.GETCODEMSG;
//                msg.obj = secs+ "";
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                handler.sendMessage(msg);
//            }
//        });

    /**
     * 获取短信验证码
     *
     * @param activity 上下文
     * @param mobile   手机号
     * @param type     (1=登录,2=注册,3=忘记密码,4=安全密码)
     * @param handler  回调
     */
    public void getCodeMsgHttp(Activity activity,String mobile, String type, final Handler handler) {
        HttpParams params = new HttpParams();
        params.put("mobile",mobile);
        params.put("type",type);//拉取类型(1=登录,2=注册,3=忘记密码,4=安全密码)
        HttpUtils.getInstance(activity).requestPost(false,AppClient.GET_CODEMSG, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                String secs="";
                try {
                    JSONObject jsonO = new JSONObject(jsonObject.toString());
                    secs = jsonO.getString("secs");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = AppClient.GETCODEMSG;
                msg.obj = secs+ "";
                handler.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError;
                handler.sendMessage(msg);

            }
        });
    }
//        HttpParams params = new HttpParams();
//        params.put("mobile", mobile);
//        params.put("type", type);//拉取类型(1=登录,2=注册,3=忘记密码,4=安全密码)
//        HttpUtils.getInstance(activity).requestPost(false,AppClient.GET_CODEMSG, params, new HttpUtils.requestCallBack() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                Logs.i("验证码发送成功返回参数=======", jsonObject.toString());
//                Message msg = new Message();
//                msg.what = AppClient.GETCODEMSG;
//                msg.obj = jsonObject + "";
//                handler.sendMessage(msg);
//            }
//            @Override
//            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                handler.sendMessage(msg);
//
//            }
//        });



//}

    /**
     * 公共fragment 跳转
     *
     * @param fm       FragmentManager
     * @param fragment 跳转fragment
     * @param tag      标签
     * @param bundle   传递数据
     */
    public void CommonFragment(FragmentManager fm, Fragment fragment, String tag, Bundle bundle) {
        if (bundle != null)
            fragment.setArguments(bundle);
        if (fm != null) {
            fm.beginTransaction()
                    .add(R.id.my_content, fragment, tag)
                    .addToBackStack(null).commit();
//            utils.addFragmentTag(tag);
        }
    }

    /**
     * 已经是最后一个fragmentgetSupportFragmentManager()或者getFragmentManager()
     * 具体要看你add to back stack 是用哪个
     * fragmeng 返回一级级返回
     *
     * @param
     * @param activity
     * @author mfx
     */
    public void FragmentGoBack(Activity activity) {
        int num = activity.getFragmentManager().getBackStackEntryCount();
        //实名认证成功
        Fragment realAuthfinsh = activity.getFragmentManager().findFragmentByTag("realAuthfinsh");
        //上传银行成功
        Fragment uploadCardimgfinish = activity.getFragmentManager().findFragmentByTag("realNameUploadFinish");
        //支付成功点击返回关闭所有回到个人中心
        Fragment payresult = activity.getFragmentManager().findFragmentByTag("payresult");

//添加银行卡成功
        Fragment addbanksuc = activity.getFragmentManager().findFragmentByTag("addbankcardsucceed");

        Fragment realauthone = activity.getFragmentManager().findFragmentByTag("real1");
        Fragment realauthTwo = activity.getFragmentManager().findFragmentByTag("realAuthTwo");
        Fragment realNameBindCard = activity.getFragmentManager().findFragmentByTag("realNameBindCard");
        Fragment uploadcard = activity.getFragmentManager().findFragmentByTag("uploadcard");

        if (realauthTwo != null || realauthone != null || realNameBindCard != null || uploadcard != null) {
            activity.finish();
            return;
        }
        //等于1为第一个界面
        if (num == 1) {
            activity.finish();
        } else {
            activity.getFragmentManager().popBackStack();
        }
//        }

    }

    /**
     * 获取系统时间
     *
     * @return
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String GetNowDateTime() {
        return sdf.format(new Date());
    }


    public void setColorSchemeResources(SwipyRefreshLayout mSwipyRefreshLayout) {
        mSwipyRefreshLayout.setColorSchemeResources(R.color.qblue, R.color.blue, R.color.red);
    }

    /**
     * 获取header请求参数
     *
     * @param rspMsgName 方法名
     * @return
     */
    public HttpHeaders GetheadData(String rspMsgName) {
        HttpHeaders headers = new HttpHeaders();
        headers.put("X-App-Key", "XIANHAO365");
        headers.put("X-Method", rspMsgName);
        headers.put("X-Timestamp", SXUtils.getInstance(mContext).GetNowDateTime());
        headers.put("X-Version", SXUtils.getInstance(mContext).getVersionName());
        headers.put("X-User-ID", TextUtils.isEmpty(AppClient.USER_ID) ? "" : AppClient.USER_ID);
        headers.put("X-User-Session", TextUtils.isEmpty(AppClient.USER_SESSION) ? "" : AppClient.USER_SESSION);
        headers.put("X-OS", "Android");
        headers.put("X-OS-Version", SXUtils.getInstance(mContext).getClientDeviceInfo());
        headers.put("X-App-Version", SXUtils.getInstance(mContext).getVersionName());
        headers.put("X-UDID", SXUtils.getInstance(mContext).getDeviceId()+"");
        headers.put("X-Nonce", getReqsn());
        Logs.i("请求头数据+++++++++",headers.toString()+"=======");
        return headers;
    }

    /**
     * 公共调用图片装置
     * @param imgUrl
     * @param view
     */
    public void GlideSetImg(String imgUrl, ImageView view) {
        Glide.with(mContext)
                .load(imgUrl)
                .placeholder(R.mipmap.loading_img)
                .error(R.mipmap.loading_img)
                .fitCenter()
                .into(view);
    }

    /**
     * Glide加载时等比例缩放图片至屏幕宽度
     * 等比缩放全屏屏幕
     * @param url
     * @param imageView
     */
    public void GlidFullSetImg(String url,final ImageView imageView){

//        load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.default_big_load_img)

        Glide.with(mContext).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.default_big_load_img).asBitmap().error(R.mipmap.default_big_load_img).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = AppClient.fullWidth * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.height = height;
                para.width = AppClient.fullWidth;
                imageView.setImageBitmap(resource);
            }
        });
        Glide.with(mContext).load(url).asBitmap().error(R.mipmap.default_big_load_img).into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = AppClient.fullWidth * imageHeight / imageWidth;
                ViewGroup.LayoutParams para = imageView.getLayoutParams();
                para.height = height;
                para.width = AppClient.fullWidth;
                imageView.setImageBitmap(resource);
            }
        });
    }
    /**
     * 判断用户是否已经登录
     * @return
     */
    public boolean IsLogin(){
        if(TextUtils.isEmpty(AppClient.USER_ID) || TextUtils.isEmpty(AppClient.USER_SESSION)){
            Intent intent = new Intent(mContext, LoginNameActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("isEnter","12");
            mContext.startActivity(intent);
            return  false;
        }
        return true;
    }
    /**
     * 获取拍照上传图片地址
     *
     * @return
     */
    public Uri getUriTakePhoto(String name) {
        File file = CreateText(GETPICPATH, "/"+name+".jpg");
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    public String getPathTakePhoto(String name) {
        File file = CreateText(GETPICPATH, "/"+name+".jpg");
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
    /**
     * 价格保留两位小数点
     * @param price
     * @return
     */
    public String priceTwoNum(String price){
        if(price.indexOf(".") == -1){
            return "";
        }
        try {
            if(!TextUtils.isEmpty(price)){
                DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                return decimalFormat.format(price);//format 返回的是字符串
            }
        }catch (Exception e){
            return "";
        }

        return "";
    }
    /**
     * 从asset路径下读取对应文件转String输出
     * @return
     */
    public  String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader( mContext.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 主要用于判断用户角色，合伙人，供应商，联创中心用户是指显示我的界面
     * 摊主和用户才会显示购买查看相关产品界面
     * tag ==1 标识普通用户
     */
    public void IntentMain(Activity activity){
        if(TextUtils.isEmpty(AppClient.USERROLETAG) ||AppClient.USERROLETAG.equals("32")||AppClient.USERROLETAG.equals("64")){
            Intent mainintent = new Intent(activity, MainFragmentActivity.class);
            mainintent.putExtra("tag","1");
            mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(mainintent);
        }else{
            if(AppClient.LOGOUT && TextUtils.isEmpty(AppClient.USER_SESSION)){
                //在合伙人及供应商角色退出后 跳转登录点击关闭按钮进入 用户主页
                Intent mainintent = new Intent(activity, MainFragmentActivity.class);
                mainintent.putExtra("tag","1");
                mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(mainintent);
            }else{
                Intent mainintent = new Intent(activity, MainFragmentActivity.class);
                mainintent.putExtra("tag","2");
                mainintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(mainintent);
            }
        }
//        activity.finish();
        SXUtils.getInstance(activity).finishActivity();
        SXUtils.getInstance(activity).list.clear();
    }

    /**
     * 保留两位小数价格参数
     * @param scale
     * @return
     */
    public String getFloatPrice(float scale){
        if(TextUtils.isEmpty(scale+""))
            return "00.00";
        DecimalFormat   fnum   =   new   DecimalFormat("##0.00");
        String   dd=fnum.format(scale);
        return dd;
    }
    /**
     * 删除原图  保存缩小图
     * @param bm
     * @param fileName
     * @throws Exception
     */
    public void saveFile(Bitmap bm, String fileName) throws Exception {
        File dirFile = new File(fileName);
        //检测图片是否存在
        if(dirFile.exists()){
            dirFile.delete();  //删除原图片
        }
        File myCaptureFile = new File(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        //100表示不进行压缩，70表示压缩率为30%
        bm.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        bos.flush();
        bos.close();
    }
    /**
     * 获得购物车商品减去一个
     * @param nowNum  当前购物车数量
     * @param subNum  当前减去购物车数量
     * @return
     */
    public String getCarNum(String nowNum,int subNum){
        int setNum=0;
        if(TextUtils.isEmpty(nowNum)){
            return setNum+"";
        }
        int  nowNumInt = Integer.parseInt(nowNum);
        setNum = nowNumInt-subNum;
        return setNum+"";
    }
    public Dialog tipDialog;
    public void MyDialogView(Context context,String title, String contentStr, View.OnClickListener onClickListener) {
        tipDialog = new AlertDialog.Builder(context).create();
        tipDialog.show();
        tipDialog.setCancelable(true);
        tipDialog.setCanceledOnTouchOutside(false);
        Window window = tipDialog.getWindow();
        window.setContentView(R.layout.common_dialog);
        LinearLayout cancel = (LinearLayout) window.findViewById(R.id.dialog_liny);
        TextView content = (TextView) window.findViewById(R.id.dialog_message_tv);
        TextView titletv = (TextView) window.findViewById(R.id.dialog_title_tv);
        TextView rightBtn = (TextView) window.findViewById(R.id.dialog_right_btn);
        TextView leftBtn = (TextView) window.findViewById(R.id.dialog_left_btn);
        leftBtn.setVisibility(View.VISIBLE);
        View vi = window.findViewById(R.id.add_bank_dialog_view);
        vi.setVisibility(View.GONE);
        vi.setPadding(0, 2, 0, 2);
        leftBtn.setText("取消");
        titletv.setText(title);
        ImageView iv = (ImageView) window.findViewById(R.id.dialog_close_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        content.setText(contentStr);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(onClickListener);
    }
    public EditText setip;
    public Dialog testIPDialog;
    public void setIPDialogView(Activity act,View.OnClickListener onClickListener) {
        testIPDialog = new AlertDialog.Builder(act).create();
        testIPDialog.show();
        testIPDialog.setCancelable(true);
        testIPDialog.setCanceledOnTouchOutside(false);
        Window window = testIPDialog.getWindow();
        window.setContentView(R.layout.set_ip_dialog);
        setip = (EditText) window.findViewById(R.id.test_set_ip_edt);
        TextView confirm = (TextView) window.findViewById(R.id.test_set_ip_tv);
        confirm.setOnClickListener(onClickListener);
        showKeyboard(setip);
    }
    public void showKeyboard(EditText editText) {
        if(editText!=null){
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }
    /**
     * 显示确定按钮事件
     * @param context
     * @param title
     * @param contentStr
     * @param onClickListener
     */
    public void ConfrimDialogView(Context context,boolean cancelable,String title, String contentStr, View.OnClickListener onClickListener) {
        tipDialog = new AlertDialog.Builder(context).create();
        tipDialog.show();
        tipDialog.setCancelable(cancelable);
        tipDialog.setCanceledOnTouchOutside(false);
        Window window = tipDialog.getWindow();
        window.setContentView(R.layout.common_dialog);
        LinearLayout cancel = (LinearLayout) window.findViewById(R.id.dialog_liny);
        TextView content = (TextView) window.findViewById(R.id.dialog_message_tv);
        TextView titletv = (TextView) window.findViewById(R.id.dialog_title_tv);
        TextView rightBtn = (TextView) window.findViewById(R.id.dialog_right_btn);
        TextView leftBtn = (TextView) window.findViewById(R.id.dialog_left_btn);
        leftBtn.setVisibility(View.GONE);
        View vi = window.findViewById(R.id.add_bank_dialog_view);
        vi.setVisibility(View.GONE);
        vi.setPadding(0, 2, 0, 2);
        leftBtn.setText("取消");
        titletv.setText(title);
        ImageView iv = (ImageView) window.findViewById(R.id.dialog_close_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        content.setText(contentStr);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(onClickListener);
    }
    /**
     * 版本升级显示弹窗
     * @param context
     * @param title
     * @param contentStr
     * @param onClickListener
     */
    public void UpdateConfrimDialogView(Context context,boolean cancelable,String title, String contentStr, View.OnClickListener onClickListener) {
        tipDialog = new AlertDialog.Builder(context).create();
        tipDialog.show();
        tipDialog.setCancelable(cancelable);
        tipDialog.setCanceledOnTouchOutside(false);
        Window window = tipDialog.getWindow();
        window.setContentView(R.layout.common_dialog);
        LinearLayout cancel = (LinearLayout) window.findViewById(R.id.dialog_liny);
        TextView content = (TextView) window.findViewById(R.id.dialog_message_tv);
        content.setTextSize(16);
        TextView titletv = (TextView) window.findViewById(R.id.dialog_title_tv);
        TextView rightBtn = (TextView) window.findViewById(R.id.dialog_right_btn);
        TextView leftBtn = (TextView) window.findViewById(R.id.dialog_left_btn);
        if(cancelable){
            leftBtn.setVisibility(View.VISIBLE);
        }else{
            leftBtn.setVisibility(View.GONE);
        }
        rightBtn.setText("下载更新");
        leftBtn.setText("暂不更新");

        View vi = window.findViewById(R.id.add_bank_dialog_view);
        vi.setVisibility(View.GONE);
        vi.setPadding(0, 2, 0, 2);
        titletv.setText(title);
        ImageView iv = (ImageView) window.findViewById(R.id.dialog_close_iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        content.setText(contentStr);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(onClickListener);
    }


    /**
     * 获取本地省份城市区域地址
     * @return
     */
    public ArrayList<AddressProvinceEntity>  getAddress(){
        String json = SXUtils.getInstance(mContext).getFromAssets("areas.json");
        ArrayList<AddressProvinceEntity>   jsonBean = (ArrayList<AddressProvinceEntity>) ResponseData.getInstance(mContext).parseJsonArray(json.toString(), AddressProvinceEntity.class);
        return jsonBean;
    }
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    /**
     * 弹出选择地址pop
     * @param pvOptions
     */
    public void addressPickerPopView(OptionsPickerView pvOptions) {//解析数据
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        ArrayList<AddressProvinceEntity>   jsonBean = getAddress();
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            options1Items.add(jsonBean.get(i).getLabel());
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            if(jsonBean.get(i).getChildren() != null) {
                for (int c = 0; c < jsonBean.get(i).getChildren().size(); c++) {//遍历该省份的所有城市

                    String CityName = jsonBean.get(i).getChildren().get(c).getLabel();
                    CityList.add(CityName);//添加城市

                    ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (jsonBean.get(i).getChildren().get(c).getChildren() != null) {
                        if (jsonBean.get(i).getChildren().get(c).getLabel() == null
                                || jsonBean.get(i).getChildren().get(c).getChildren().size() == 0) {
                            City_AreaList.add("");
                        } else {
                            for (int d = 0; d < jsonBean.get(i).getChildren().get(c).getChildren().size(); d++) {//该城市对应地区所有数据
                                String AreaName = jsonBean.get(i).getChildren().get(c).getChildren().get(d).getLabel();

                                City_AreaList.add(AreaName);//添加该城市所有地区数据
                            }
                        }
                    }
                    Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                }
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        pvOptions.setPicker(options1Items, options2Items, options3Items);//添加数据源
        pvOptions.show();
    }

    /**
     * 获取当前商品列表的sku是否已经存在购物车，
     * 存在  返回当前购物车的数量
     * 不存在  返回空字符串
     * @param sku  商品sku
     * @return
     */
     public String CheckExistCar(String sku){
        //当前sku的购物车数量
         if(AppClient.carSKUNumMap == null || AppClient.carSKUNumMap.size()<=0){
             return "";
         }
        if(AppClient.carSKUNumMap.containsKey(sku)){
            return   AppClient.carSKUNumMap.get(sku);
        }
         return "";
     }
    /**
     * 添加或者删除购物车
     *
     * @param skuBarcode SKU条码
     * @param quantity   更新数量（0=删除，X=设值）
     * @param
     */
    public void AddOrUpdateCar(String skuBarcode, final String quantity) {
        HttpParams params = new HttpParams();
        params.put("skuBarcode", skuBarcode + "");
        params.put("quantity", quantity + "");
        Logs.i("加入购物车数量========"+quantity);
        HttpUtils.getInstance(mContext).requestPost(false, AppClient.CARADDUPDATA, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("购物车成功返回参数=======", jsonObject.toString());
//                if(quantity.equals(0)){
//                    //减少购物车数量
//                    MainFragmentActivity.getInstance().setBadge(false,1);
//                }else{
//                    //增加购物车数量
//                    MainFragmentActivity.getInstance().setBadge(true,1);
//                }

                if(!TextUtils.isEmpty(quantity) && Integer.parseInt(quantity)>0)
                    AppClient.isDelCarGoods= false;
                SXUtils.DialogDismiss();
                //每次增加或减除 都刷新购物车
                EventBus.getDefault().post(new MessageEvent(2,"addsub"));

            }
            @Override
            public void onResponseError(String strError) {
                Logs.i("添加购物车错误====="+strError.toString());
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);
                SXUtils.DialogDismiss();
            }
        });
    }

    /**
     * 获取常用清单列表
     * @param hand
     */
    public void getBill(final Handler hand,int pageIndex) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("pageSize","10");
        httpParams.put("pageIndex",pageIndex+"");
        HttpUtils.getInstance(mContext).requestPost(false, AppClient.COMMONBILL, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("常用发送成功返回参数=======",jsonObject.toString());
                List<BillDataSetEntity> goodsTypeList = ResponseData.getInstance(mContext).parseJsonArray(jsonObject.toString(), BillDataSetEntity.class);
                Message msg = new Message();
                msg.what = 1009;
                msg.obj = goodsTypeList;
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
    //    public void psdLoginHttp(final String mobile,final String psdStr,final Handler hand){
//        RequestBody requestBody = new FormBody.Builder()
//                .add("mobile", mobile)
//                .add("password",psdStr)
//                .add("loginType","1")//0=验证码登录,1=密码登录
//                .build();
//        new OKManager(mContext).sendStringByPostMethod(requestBody, AppClient.USER_LOGIN, new OKManager.Func4() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                Logs.i("密码登录发送成功返回参数=======",jsonObject.toString());
//                try {
//                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
//                    AppClient.USER_ID = jsonObject1.getString("uid");
//                    AppClient.USER_SESSION = jsonObject1.getString("sid");
//                    AppClient.USERROLETAG = jsonObject1.getString("tag");
//                    SXUtils.getInstance(mContext).setSharePreferences("username",mobile);
//                    SXUtils.getInstance(mContext).setSharePreferences("psd",psdStr);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Message msg = new Message();
//                msg.what = 1000;
//                msg.obj = "";
//                hand.sendMessage(msg);
//            }
//            @Override
//            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);
//            }
//        });
//    }
    public void psdLoginHttp(final Handler hand,final String mobile,final String psdStr) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("mobile", mobile);
        httpParams.put("password",psdStr);
        httpParams.put("loginType","1");//0=验证码登录,1=密码登录
        HttpUtils.getInstance(mContext).requestPost(false, AppClient.USER_LOGIN, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("密码登录发送成功返回参数=======",jsonObject.toString());
                try {
                    JSONObject jsonObject1 = new JSONObject(jsonObject.toString());
                    AppClient.USER_ID = jsonObject1.getString("uid");
                    AppClient.USER_SESSION = jsonObject1.getString("sid");
                    String userTag = jsonObject1.getString("tag");
                    if(!AppClient.USERROLETAG.equals(userTag)){
                        AppClient.LOGOUT = true;
                    }
                    AppClient.USERROLETAG = jsonObject1.getString("tag");
                    SXUtils.getInstance(mContext).setSharePreferences("username",mobile);
                    SXUtils.getInstance(mContext).setSharePreferences("psd",psdStr);
                    AppClient.USERPHONE = mobile;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = "";
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
    /**
     * 获取不同用户信息
     */
    public void getUserInfoHttp(final Handler hand) {
        HttpUtils.getInstance(mContext).requestPost(false,AppClient.USER_INFO, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                UserInfoEntity gde = null;
                gde = ResponseData.getInstance(mContext).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = gde;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what =AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);

            }
        });
    }

    /**
     * 获取用户我的里面相关订单等数量
     * @param hand
     */
    public void getUserNumberHttp(final Handler hand) {
        HttpUtils.getInstance(mContext).requestPost(false,AppClient.USER_NUM_NFO, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("获取用户订单数量========"+jsonObject.toString());
                UserRenderInfoEntity gde = null;
                gde = ResponseData.getInstance(mContext).parseJsonWithGson(jsonObject.toString(),UserRenderInfoEntity.class);
                Message msg = new Message();
                msg.what = 1001;
                msg.obj = gde;
                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what =AppClient.ERRORCODE;
                msg.obj = strError;
                hand.sendMessage(msg);

            }
        });
    }
    /**
     * 删除我的地址
     */
    public void delAddress(String id) {
        HttpParams httpp = new HttpParams();
        httpp.put("id",id);
        HttpUtils.getInstance(mContext).requestPost(false, AppClient.ADDRESS_DEL, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("删除地址=====",jsonObject.toString());
                EventBus.getDefault().post(new MessageEvent(555,"orderList"));
//                JSONObject jsonObject1 = null;
//                List<AddressInfoEntity> orderFrom = ResponseData.getInstance(mContext).parseJsonArray(jsonObject.toString(),AddressInfoEntity.class);
//                Message msg = new Message();
//                msg.what = 1000;
//                msg.obj = orderFrom;
//                hand.sendMessage(msg);
            }
            @Override
            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);

            }
        });
    }
    /**
     * 添加删除常用清单
     * @param tag  1 添加 0 删除
     * @param goods  商品ID 和商品code
     */
    public void AddDelBill(int tag,String goods,final Handler hand){
        HttpParams params = new HttpParams();
        String appUrl = "";
        if(tag ==1){
            params.put("goodsCode", goods);
            appUrl = AppClient.GOODS_ADDCOMMON;
        }else{
            params.put("goodsCode", goods);
            appUrl = AppClient.GOODS_DELDCOMMON;
        }
        HttpUtils.getInstance(mContext).requestPost(false,appUrl, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
//                String jsonstr="";
//                try {
//                    JSONObject jso = new JSONObject(jsonObject.toString());
//                    jsonstr = jso.getString("dataset");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                GoodsDetailInfoEntity gde = (GoodsDetailInfoEntity) ResponseData.getInstance(mContext).parseJsonWithGson(jsonstr.toString(),GoodsDetailInfoEntity.class);
                if(hand != null) {
                    Message msg = new Message();
                    msg.what = 1006;
                    msg.obj = "";
                    hand.sendMessage(msg);
                }else{
                    EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10002,"bill"));
                    EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10003,"home"));
                }
            }
            @Override
            public void onResponseError(String strError) {
                if(hand != null) {
                    Message msg = new Message();
                    msg.what = AppClient.ERRORCODE;
                    msg.obj = strError;
                    hand.sendMessage(msg);
                }else{
                    ToastCenter("请求失败");
                }
            }
        });

    }
    /**
     * 选中或者取消购物车调用
     * skucode  选中字段sku  和状态拼接   12322122:1
     */
    public void CheckBoxCar(String skucode) {
        HttpParams httpp = new HttpParams();
        httpp.put("skuBarcodeAndState",skucode);
        HttpUtils.getInstance(mContext).requestPost(false, AppClient.CHECKBOXCAR, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Logs.i("选中字段购物车商品=====",jsonObject.toString());
                EventBus.getDefault().post(new MessageEvent(2,"car"));
//                JSONObject jsonObject1 = null;
//                List<AddressInfoEntity> orderFrom = ResponseData.getInstance(mContext).parseJsonArray(jsonObject.toString(),AddressInfoEntity.class);
//                Message msg = new Message();
//                msg.what = 1000;
//                msg.obj = orderFrom;
//                hand.sendMessage(msg);
                SXUtils.DialogDismiss();
            }
            @Override
            public void onResponseError(String strError) {
//                Message msg = new Message();
//                msg.what = AppClient.ERRORCODE;
//                msg.obj = strError;
//                hand.sendMessage(msg);
                SXUtils.DialogDismiss();
            }
        });
    }


}