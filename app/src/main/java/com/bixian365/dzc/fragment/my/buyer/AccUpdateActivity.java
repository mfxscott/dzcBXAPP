package com.bixian365.dzc.fragment.my.buyer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.entity.address.AddressProvinceEntity;
import com.bixian365.dzc.fragment.my.buyer.purchase.GetPicPopupWindow;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;
import com.bixian365.dzc.utils.view.GlideRoundTransform;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 合伙人 ，联创中心，供应商管理 三个角色账号修改
 */
public class AccUpdateActivity extends BaseActivity {
    @BindView(R.id.other_info_headimg)
    ImageView headImg;
    @BindView(R.id.other_username_tv)
    EditText username;
    @BindView(R.id.other_acc_tv)
    TextView accTv;
    @BindView(R.id.other_info_update_btn)
    Button btn;
    @BindView(R.id.partner_acc_update_city)
    TextView storeAddTv;
    @BindView(R.id.partner_acc_update_detail_edt)
    EditText  addressInfoEdt;
    @BindView(R.id.parent_address_liny)
    LinearLayout  addressLiny;
    private Activity activity;
    private List<File> imgList = new ArrayList<>();
    private Handler hand;
    UserInfoEntity    userinfo;
    private OptionsPickerView pvOptions;
    private String province="广东省",city="深圳市",district="罗湖区",addr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_update);
        Bundle bundle = this.getIntent().getExtras();
        userinfo = bundle.getParcelable("userinfo");
        ButterKnife.bind(this);
        activity = this;
        initView();
        PopViewPrick();
        SXUtils.showMyProgressDialog(activity,true);
        getUserInfoHttp();
    }
    private void initView(){
        registerBack();
        setTitle("账户信息");
        if(AppClient.USERROLETAG.equals("16")){
            addressLiny.setVisibility(View.VISIBLE);
        }
        storeAddTv.setText(userinfo.getProvince()+userinfo.getCity()+userinfo.getDistrict()+"");
        addressInfoEdt.setText(userinfo.getAddr()+"");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernamestr = username.getText().toString().trim();
                String addressInfo = addressInfoEdt.getText().toString().trim();
                if(TextUtils.isEmpty(addressInfo))
                {
                    SXUtils.getInstance(activity).ToastCenter("请输入详细收货地址");
                }
                HttpParams httpParams = new HttpParams();
                httpParams.put("username",usernamestr);
                List<File> files = new ArrayList<>();
                //去除手动添加的图片 最后一张
                for(int i=0;i<imgList.size();i++){
                    files.add(imgList.get(i));
                }
                httpParams.putFileParams("iconFile", files);
                httpParams.put("province",province);
                httpParams.put("city",city);
                httpParams.put("district",district);
                httpParams.put("addr",addressInfo);
                SXUtils.showMyProgressDialog(activity,true);
                updateOtherAcc(httpParams);
            }
        });
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpicpop = new GetPicPopupWindow(activity, ShareOnclick, false);
                //显示窗口
                getpicpop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        userinfo = (UserInfoEntity) msg.obj;
                        Glide.with(activity).load(userinfo.getIcon()).placeholder(R.mipmap.loading_img)
                                .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headImg);
                        username.setText(userinfo.getUsername());
                        accTv.setText(userinfo.getAcount());
                        break;
                    case 1001:
                        SXUtils.getInstance(activity).ToastCenter("修改信息成功");
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"my"));
                        finish();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg + "");
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
        storeAddTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SXUtils.getInstance(activity).addressPickerPopView(pvOptions);
            }
        });

    }
    private GetPicPopupWindow getpicpop;
    View.OnClickListener ShareOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getpicpop != null)
                getpicpop.dismiss();
            switch (v.getId()) {
                case R.id.get_pic_photo:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, 100);
                    break;
                case R.id.get_pic_take:
                    if (ContextCompat.checkSelfPermission(activity,Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                                1000);
                    } else {
                        filenName = SXUtils.getInstance(activity).GetNowDateTime();
                        Intent intent = null;
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra("return-data", false);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, SXUtils.getInstance(activity).getUriTakePhoto(filenName));
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        intent.putExtra("noFaceDetection", true);
                        startActivityForResult(intent, 101);
                    }
                    break;
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                filenName = SXUtils.getInstance(activity).GetNowDateTime();
                Intent intent = null;
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, SXUtils.getInstance(activity).getUriTakePhoto(filenName));
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true);
                startActivityForResult(intent, 101);
            } else
            {
                // Permission Denied
                Toast.makeText(activity, "相机权限被拒绝,请在设置-权限管理中开启.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
    }

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
    private String imgStr;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        SXUtils.getInstance(activity).deleteDir(GETPICPATH);
        switch (requestCode) {
            //相册
            case 100:
                if (data == null) {
                    break;
                }
                try {
//                Bitmap bitmap12 = ImageUtils.setPic(Utils.getRealFilePath(activity,data.getData()));
//                imgStr =   getImageString(uploadImg,getRealFilePath(this,data.getData()),2);
                    imgStr = getRealFilePath(this, data.getData());
                    Logs.i("相册=======图片路径" + imgStr);
                    if (TextUtils.isEmpty(imgStr)) {
                        return;
                    }
                    File file = new File(imgStr);
                    imgList.add(0, file);
                    Glide.with(activity).load(imgStr).placeholder(R.mipmap.default_head)
                            .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headImg);
                }  catch (Exception e){
                    SXUtils.getInstance(activity).ToastCenter("获取照片发生异常");
                }
                break;
            //拍照
            case 101:
                try {

                    Bitmap bitmap = BitmapFactory.decodeFile(SXUtils.getInstance(activity).getPathTakePhoto(filenName));
//                //利用Bitmap对象创建缩略图
                    Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, AppClient.fullWidth, AppClient.fullHigh);
                    try {
                        SXUtils.getInstance(activity).saveFile(showbitmap,SXUtils.getInstance(activity).getPathTakePhoto(filenName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imgStr = SXUtils.getInstance(activity).getPathTakePhoto(filenName);
                    Logs.i("拍照=======图片路径" + imgStr);
                    if (TextUtils.isEmpty(imgStr)) {
                        return;
                    }
                    File files = new File(imgStr) ;
                    imgList.add(0,files);
                    Glide.with(activity).load(imgStr).skipMemoryCache(true).placeholder(R.mipmap.default_head)
                            .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headImg);
                }catch (Exception e){
                    SXUtils.getInstance(activity).ToastCenter("获取照片发生异常");
                }
//                Bitmap bitmap31 = ImageUtils.setPic(Utils.getInstance(activity).getPathTakePhoto());
//                Bitmap bitmap = BitmapFactory.decodeFile(getPathTakePhoto());
//                //利用Bitmap对象创建缩略图
//                Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 250, 250);
//                try {
//                    saveFile(showbitmap,getPathTakePhoto());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                break;
        }
    }
    private String filenName="";

    public void updateOtherAcc(HttpParams httpParams) {
        HttpUtils.getInstance(activity).requestUploadImgPost(false, AppClient.UPDATE_USER_INFO, httpParams, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Log.i("上传成功=========", jsonObject.toString());
                Message msg = new Message();
                msg.what = 1001;
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
    public void getUserInfoHttp() {
        HttpUtils.getInstance(activity).requestPost(false, AppClient.USER_INFO, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                UserInfoEntity gde = null;
                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),UserInfoEntity.class);
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
     * 用户选择地址
     */
    private void  PopViewPrick(){
        pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                ArrayList<AddressProvinceEntity>   jsonBean = SXUtils.getInstance(activity).getAddress();
                //返回的分别是三个级别的选中位置
                String tx="";
                if(jsonBean.get(options1).getChildren().get(option2).getChildren() != null){
                    tx = jsonBean.get(options1).getLabel()
                            + jsonBean.get(options1).getChildren().get(option2).getLabel()
                            + jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel();
                    province =jsonBean.get(options1).getLabel();
                    city = jsonBean.get(options1).getChildren().get(option2).getLabel();
                    district =   jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel();;

                }else{
                    tx = jsonBean.get(options1).getLabel()
                            + jsonBean.get(options1).getChildren().get(option2).getLabel();
                    province =jsonBean.get(options1).getLabel();
                    city = jsonBean.get(options1).getChildren().get(option2).getLabel();
                    district ="";
                }

//              tvOptions.setText(tx);
                storeAddTv.setText(tx+"");
//                addressInfoEdt.setText(tx+"");
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
//                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.qblue))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.qblue))//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setLinkage(true)//设置是否联动，默认true
//                .setLabels("省", "市", "区")//设置选择的三级单位
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();
    }
}
