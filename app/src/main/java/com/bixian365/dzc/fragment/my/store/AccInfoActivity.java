package com.bixian365.dzc.fragment.my.store;

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
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.bixian365.dzc.R.id.acc_info_update_btn;
import static com.bixian365.dzc.R.id.acc_store_detail_address_edt;

/**
 * 账号信息
 */
public class AccInfoActivity extends BaseActivity {
    @BindView(R.id.acc_info_username_edt)
    EditText accInfoUsernameEdt;
    @BindView(R.id.acc_info_address_edt)
    TextView accInfoAddressEdt;
    @BindView(R.id.acc_info_person_edt)
    EditText accInfoPersonEdt;
    @BindView(R.id.acc_info_person_phone_edt)
    EditText accInfoPersonPhoneEdt;
    @BindView(acc_info_update_btn)
    Button accInfoUpdateBtn;
    @BindView(R.id.acc_info_name_tv)
    EditText acount;
    @BindView(R.id.acc_userinfo_update_liny)
    LinearLayout userLin;
    //店铺信息
    @BindView(R.id.acc_store_add_edt)
    TextView storeAdd;
    @BindView(R.id.acc_store_fz_edt)
    EditText storefzr;
    @BindView(R.id.acc_store_id_edt)
    TextView storeId;
    @BindView(R.id.acc_store_name_edt)
    EditText storeInfo;
    @BindView(R.id.acc_storeinfo_update_liny)
    LinearLayout storeLin;
    @BindView(acc_store_detail_address_edt)
    EditText detailAddressEdt;
    @BindView(R.id.acc_info_headimg)
    ImageView  headimg;
    private Activity activity;
    private Handler hand;
    private UserInfoEntity userinfo;
    private Unbinder unbinder;
    private String province="广东省",city="深圳市",district="罗湖区",addr;
    private boolean  IstakePhoto;//ture 拍照，是否是拍照，还是相册 区分申请不同权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_info);
        unbinder = ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        userinfo = bundle.getParcelable("userinfo");
        activity = this;
        ButterKnife.bind(this);
        String json = SXUtils.getInstance(activity).getFromAssets("areas.txt");
        Logs.i("=======json===" + json);
        SXUtils.getInstance(activity).showMyProgressDialog(activity,true);
        getUserInfoHttp();
        initView();
        PopViewPrick();
    }
    private void initView() {
        registerBack();
        setTitle("账户信息");
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        userinfo = (UserInfoEntity) msg.obj;
                        initData();
                        break;
                    case 1001:
                        SXUtils.getInstance(activity).ToastCenter("修改信息成功");
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"my"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100012,"acc"));
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
        accInfoAddressEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(activity).addressPickerPopView(pvOptions);
            }
        });
        storeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SXUtils.getInstance(activity).addressPickerPopView(pvOptions);
            }
        });
        headimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getpicpop = new GetPicPopupWindow(activity, ShareOnclick, false);
                //显示窗口
                getpicpop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
    }
    private void  initData(){
        if(userinfo != null) {

            if (AppClient.USERROLETAG.equals("64")) {
                Glide.with(activity).load(userinfo.getIcon()).placeholder(R.mipmap.loading_img)
                        .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headimg);

                acount.setText(userinfo.getUsername() + "");
                userLin.setVisibility(View.VISIBLE);
                accInfoUsernameEdt.setText(userinfo.getUsername());
                accInfoPersonPhoneEdt.setText(userinfo.getMobile());
                accInfoAddressEdt.setText(TextUtils.isEmpty(userinfo.getProvince())?"广东省深圳市":userinfo.getProvince() + userinfo.getCity() + userinfo.getDistrict() + userinfo.getAddr()+"");
            } else {
                Glide.with(activity).load(userinfo.getShopLogo()).placeholder(R.mipmap.loading_img)
                        .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headimg);

                storeLin.setVisibility(View.VISIBLE);
                storeAdd.setText(TextUtils.isEmpty(userinfo.getProvince())?"广东省深圳市":userinfo.getProvince() + userinfo.getCity() + userinfo.getDistrict() + "");
                storefzr.setText(userinfo.getManager());
                storeId.setText("");
                storeInfo.setText(userinfo.getShopName());
                acount.setText(TextUtils.isEmpty(userinfo.getUsername())?userinfo.getShopName():userinfo.getUsername() + "");
                detailAddressEdt.setText(userinfo.getAddr()+"");
            }
        }
    }
    private GetPicPopupWindow getpicpop;
    View.OnClickListener ShareOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getpicpop != null)
                getpicpop.dismiss();
            switch (v.getId()) {
                case R.id.get_pic_photo:
                    IstakePhoto = false;
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
                        startActivityForResult(pickIntent, 100);
                    }
                    break;
                case R.id.get_pic_take:
                    IstakePhoto = true;
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
    private String filenName="";
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000)
        {
            if(IstakePhoto){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                } else {
                    // Permission Denied
                    Toast.makeText(activity, "相机权限被拒绝,将无法使用拍照功能.", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, 100);
                } else {
                    Toast.makeText(activity, "文件权限被拒绝,将无法选择相册图片.", Toast.LENGTH_SHORT).show();
                }
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
                            .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headimg);
                }catch (Exception e){
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
                        SXUtils.getInstance(activity).saveFile(showbitmap, SXUtils.getInstance(activity).getPathTakePhoto(filenName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    imgStr = SXUtils.getInstance(activity).getPathTakePhoto(filenName);
                    Logs.i("拍照=======图片路径" + imgStr);
                    if (TextUtils.isEmpty(imgStr)) {
                        return;
                    }
                    File files = new File(imgStr);
                    imgList.add(0, files);
                    Glide.with(activity).load(imgStr).placeholder(R.mipmap.default_head)
                            .error(R.mipmap.default_head).transform(new GlideRoundTransform(activity)).into(headimg);
                }catch (Exception e){
                    SXUtils.getInstance(activity).ToastCenter("获取照片发生异常");
                }
                break;
        }
    }
    OptionsPickerView pvOptions;
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
                storeAdd.setText(tx+"");
                accInfoAddressEdt.setText(tx+"");
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

    public void getStoreUpdateInfoHttp(HttpParams httpParams){
        HttpUtils.getInstance(activity).requestUploadImgPost(false, AppClient.UPDATE_STORE_INFO, httpParams, new HttpUtils.requestCallBack() {
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
//        HttpUtils.getInstance(activity).requestPost(false, AppClient.UPDATE_STORE_INFO, httpParams, new HttpUtils.requestCallBack() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                Logs.i("修改店铺信息成功返回参数=======",jsonObject.toString());
////                List<SearchHotWordEntity> goodsTypeList = ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(), SearchHotWordEntity.class);
//                Message msg = new Message();
//                msg.what = 1001;
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
    }
    @OnClick(acc_info_update_btn)
    public void onViewClicked() {
        String storeInfostr = storeInfo.getText().toString().trim();
        String person = storefzr.getText().toString().trim();
//        String address = accInfoAddressEdt.getText().toString().trim();
//        String username = acount.getText().toString().trim();
        String detailAddress = detailAddressEdt.getText().toString().trim();
        if(TextUtils.isEmpty(storeInfostr)){
            SXUtils.getInstance(activity).ToastCenter("请输入门店信息");
            return;
        }
        if(TextUtils.isEmpty(province)){
            SXUtils.getInstance(activity).ToastCenter("请选择门店地址");
            return;
        }
        if(TextUtils.isEmpty(detailAddress)){
            SXUtils.getInstance(activity).ToastCenter("请输入详细地址");
            return;
        }
        if(TextUtils.isEmpty(person)){
            SXUtils.getInstance(activity).ToastCenter("请输入负责人");
            return;
        }
        HttpParams httpParams = new HttpParams();
        httpParams.put("id",AppClient.USER_ID);
        httpParams.put("name",storeInfostr+"");
        httpParams.put("province",province);
        httpParams.put("city",city);
        httpParams.put("district",district);
        httpParams.put("addr",detailAddress);
        httpParams.put("manager",person);
        httpParams.put("userId",AppClient.USER_ID);
        httpParams.put("attachFiles","");
        httpParams.put("pageIndex","0");

        List<File> files = new ArrayList<>();
        //去除手动添加的图片 最后一张
        for(int i=0;i<imgList.size();i++){
            files.add(imgList.get(i));
        }

        httpParams.putFileParams("attachFiles", files);


        SXUtils.getInstance(activity).showMyProgressDialog(activity,true);
        getStoreUpdateInfoHttp(httpParams);
    }


    private List<File> imgList = new ArrayList<>();
    /**
     */
//    public void GetGYSBillListHttp(HttpParams params) {
//
//
//        params.putFileParams("attachFiles", files);
//        HttpUtils.getInstance(activity).requestUploadImgPost(false, AppClient.GYS_CPURCHASE_DELIVER, params, new HttpUtils.requestCallBack() {
//            @Override
//            public void onResponse(Object jsonObject) {
//                Log.i("上传成功=========", jsonObject.toString());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    /**
     * 获取不同用户信息
     */
    public void getUserInfoHttp() {
        HttpUtils.getInstance(activity).requestPost(false,AppClient.USER_INFO, null, new HttpUtils.requestCallBack() {
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
}
