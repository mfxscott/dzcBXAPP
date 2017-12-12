package com.bixian365.dzc.fragment.my.buyer.purchase;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.adapter.CGOrderDeliveRecyclerViewAdapter;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.cgListInfo.CGListInfoEntity;
import com.bixian365.dzc.entity.cgListInfo.CGPurchaseLinesEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.PicHelper;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.view.MyGridView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bixian365.dzc.R.id.cg_delive_name_edt;

/**
 * 供应商采购订单发货流程及相关参数填写
 */
public class CGOrderDeliveActivity extends BaseActivity {
    @BindView(R.id.cg_order_detail_take_btn)
    TextView cgOrderDetailTakeBtn;
    @BindView(R.id.delive_upload_img)
    ImageView deliveUploadImg;
    @BindView(R.id.cg_delive_gridv)
    MyGridView imgGirdv;
    @BindView(R.id.delive_pp_img_view)
    ImageView  ppImgV;
    @BindView(R.id.delive_pp_rely_view)
    RelativeLayout ppRely;
    @BindView(R.id.cg_delive_orderno_tv)
    TextView  orderNoTv;
    @BindView(R.id.cg_delive_carno_edt)
    EditText carNoEdt;
    @BindView(R.id.cg_delive_driver_name_edt)
    EditText driverNameEdt;
    @BindView(R.id.cg_delive_driver_phone_edt)
    EditText driverPhoneEdt;
    @BindView(R.id.cg_delive_address_edt)
    EditText deliveAddressEdt;
    @BindView(cg_delive_name_edt)
    EditText deliveNameEdt;
    @BindView(R.id.cg_delive_phone_edt)
    EditText delivePhoneEdt;
    @BindView(R.id.cg_delive_number_edt)
    EditText deliveNumberEdt;
    @BindView(R.id.cg_order_delive_recycler)
    RecyclerView recycler;
    @BindView(R.id.item_img_close)
    TextView  closeImg;
    @BindView(R.id.cg_all_title_goback_linlay)
    LinearLayout  cgBackLin;
    private int checkPosition;//当前选中查看的图片索引
    private Handler hand;
    private Activity activity;
    private String purchaseCode, numberStr, skucode;
    private GetPicPopupWindow getpicpop;
    private List<File> imgList = new ArrayList<>();
    private ImageGridViewAdapter  imgGridAdapter;
    private CGListInfoEntity cgListInfo;
    private List<CGPurchaseLinesEntity> purchaseList;
    private CGOrderDeliveRecyclerViewAdapter simpAdapter;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgorder_delive);
        ButterKnife.bind(this);
        activity = this;
        //注册事件
//        purchaseCode = this.getIntent().getStringExtra("code");
//        numberStr = this.getIntent().getStringExtra("num");
//        skucode = this.getIntent().getStringExtra("skucode");

        Bundle bundle = this.getIntent().getExtras();
        ArrayList list = bundle.getParcelableArrayList("PurchaseList");
        cgListInfo = bundle.getParcelable("orderList");
        purchaseList= (List<CGPurchaseLinesEntity>) list.get(0);//强转成你自己定义的list，这样list2就是你传过来的那个list了。
        purchaseCode  = cgListInfo.getPurchaseCode();

        initView();
    }
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void initView(){
        registerBack();
        setTitle("确认发货");
        orderNoTv.setText(purchaseCode+"");
        deliveNumberEdt.setText(numberStr+"");
        deliveNameEdt.setText(AppClient.USERNAME+"");
        delivePhoneEdt.setText(AppClient.USERPHONE+"");

        //默认添加一个增加按钮图片
        imgList.add(new File("123456"));
        deliveUploadImg.setVisibility(View.GONE);
        imgGridAdapter = new ImageGridViewAdapter(imgList);
        imgGirdv.setAdapter(imgGridAdapter);
        cgBackLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ppRely.setVisibility(View.GONE);
            }
        });
        imgGirdv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(imgList.size()-1 == position){
                    if(imgList.size() < 4){
                        getpicpop = new GetPicPopupWindow(activity, ShareOnclick, false);
                        //显示窗口
                        getpicpop.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    }else{
                        SXUtils.getInstance(activity).ToastCenter("最多上传三张照片");
                    }
                }else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                    Bitmap img = BitmapFactory.decodeFile(imgList.get(position).getPath(),options);
                    checkPosition =position;
                    ppRely.setVisibility(View.VISIBLE);
                    ppImgV.setImageBitmap(img);
//                    imgList.remove(position);
//                    imgGridAdapter.notifyDataSetChanged();
                }
            }
        });
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ppRely.setVisibility(View.GONE);
                imgList.remove(checkPosition);
                imgGridAdapter.notifyDataSetChanged();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recycler.setLayoutManager(linearLayoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        simpAdapter = new CGOrderDeliveRecyclerViewAdapter(this,purchaseList);
        recycler.setAdapter(simpAdapter);

        ppRely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ppRely.setVisibility(View.GONE);
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("发货成功");
//                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT10001,"cgList"));
//                        EventBus.getDefault().post(new MessageEvent(444,"cgDetail"));
//                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100018,"1"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100016,"get"));
                        EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100015,"send"));
                        finish();
                        break;
                    case 1001:
                        break;
                    case AppClient.ERRORCODE:
                        String msgs = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(msgs);
                        break;
                }
                SXUtils.DialogDismiss();
                return true;
            }
        });
    }
    View.OnClickListener ShareOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getpicpop != null)
                getpicpop.dismiss();
            switch (v.getId()) {
                case R.id.get_pic_photo:
                    IstakePhoto = false;
                    if (ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
    private boolean  IstakePhoto;//ture 拍照，是否是拍照，还是相册 区分申请不同权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if(IstakePhoto){
                    if (ContextCompat.checkSelfPermission(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
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
            }else {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
//                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如：image/jpeg 、 image/png等的类型
//                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                    startActivityForResult(pickIntent, 100);

                    Intent intent= new Intent(Intent.ACTION_GET_CONTENT);// ACTION_OPEN_DOCUMENT
                    // intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //4.4推荐用此方式，4.4以下的API需要再兼容
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, 100);//4.4版本
                } else {
                    Toast.makeText(activity, "文件权限被拒绝,将无法选择相册图片", Toast.LENGTH_SHORT).show();
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private String filenName="";//图片名称
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
                    imgStr = getRealFilePath(CGOrderDeliveActivity.this, data.getData());
                    Logs.i("相册=======图片路径"+imgStr);
                    if (TextUtils.isEmpty(imgStr)) {
                        return;
                    }
                    File file = new File(imgStr) ;
                    imgList.add(0,file);
                    imgGridAdapter.notifyDataSetChanged();
                }catch (NullPointerException e)
                {
                    try {
                        Uri selectedImage = data.getData();
                        imgStr = PicHelper.getPath(activity, selectedImage);
                        if (TextUtils.isEmpty(imgStr)) {
                            return;
                        }
                        File file = new File(imgStr);
                        imgList.add(0, file);
                        imgGridAdapter.notifyDataSetChanged();
                    }catch (Exception e1){
                        SXUtils.getInstance(activity).ToastCenter("获取照片发生异常");
                    }
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
                    File files = new File(imgStr);
                    imgList.add(0, files);
                    imgGridAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    SXUtils.getInstance(activity).ToastCenter("获取照片发生异常");
                }
                break;
            case 102:
                break;
            //相册裁剪完成后返回
            case 103:
                break;
        }
    }

    /**
     * uri转 path
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

    /**
     * 获取供应商采购列表
     */
    public void GetGYSBillListHttp() {
        String  carNoStr = carNoEdt.getText().toString().trim();
        String  driverNameStr = driverNameEdt.getText().toString().trim();
        String  driverPhoneStr = driverPhoneEdt.getText().toString().trim();
        String  deliveAddressStr = deliveAddressEdt.getText().toString().trim();
        String deliveNameStr = deliveNameEdt.getText().toString().trim();
        String  delivePhone = delivePhoneEdt.getText().toString().trim();
        String  deliverNumStr = deliveNumberEdt.getText().toString().trim();
        if(TextUtils.isEmpty(carNoStr)){
            SXUtils.getInstance(activity).ToastCenter("请输入车牌号");
            return;
        }
        if(TextUtils.isEmpty(driverNameStr)){
            SXUtils.getInstance(activity).ToastCenter("请输入司机名称");
            return;
        }
        if(TextUtils.isEmpty(driverPhoneStr)){
            SXUtils.getInstance(activity).ToastCenter("请输入司机电话号码");
            return;
        }
        if(TextUtils.isEmpty(deliveAddressStr)){
            SXUtils.getInstance(activity).ToastCenter("请输入发货地址");
            return;
        }
        if(TextUtils.isEmpty(deliveNameStr)){
            SXUtils.getInstance(activity).ToastCenter("请输入发货人名称");
            return;
        }
        if(TextUtils.isEmpty(delivePhone)){
            SXUtils.getInstance(activity).ToastCenter("请输入发货人电话");
            return;
        }

        if(imgList ==null || imgList.size()<2){
            Message msg = new Message();
            msg.what = AppClient.ERRORCODE;
            msg.obj = "请上传司机与货车照片";
            hand.sendMessage(msg);
            return;
        }
        SXUtils.getInstance(activity).showMyProgressDialog(activity,true);
        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDateSuffix = dateformat.format(date);
        HttpParams params = new HttpParams();
        params.put("purchaseCode", purchaseCode);
        params.put("vehicleNo", carNoStr);
        params.put("driverName", driverNameStr);
        params.put("driverPhone", driverPhoneStr);
        params.put("sendTime", sDateSuffix);
        params.put("sendAddr", deliveAddressStr);//商品
        params.put("senderPhone", delivePhone);
        params.put("senderName", deliveNameStr);

        params.put("linestr",simpAdapter.getInputNum().toString());
        List<File> files = new ArrayList<>();
        //去除手动添加的图片 最后一张
        for(int i=0;i<imgList.size();i++){
            if(i!=imgList.size()-1){
                files.add(imgList.get(i));
            }
        }
        params.putFileParams("attachFiles", files);
        HttpUtils.getInstance(activity).requestUploadImgPost(false, AppClient.GYS_CPURCHASE_DELIVER, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                Log.i("上传成功=========", jsonObject.toString());
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
    @OnClick({R.id.delive_upload_img, R.id.cg_order_detail_take_btn,R.id.delive_pp_rely_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.delive_upload_img:
                getpicpop = new GetPicPopupWindow(this, ShareOnclick, false);
                //显示窗口
                getpicpop.showAtLocation(deliveUploadImg, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.cg_order_detail_take_btn:

                GetGYSBillListHttp();
                break;
            case R.id.delive_pp_rely_view:
                break;
        }
    }
    class ImageGridViewAdapter extends BaseAdapter {
        private final LayoutInflater mLayoutInflater;
        public ImageGridViewAdapter( List<File> result) {
            mLayoutInflater = LayoutInflater.from(activity);
        }
        public int getCount() {
            return imgList.size();
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            LifeViewHolder vh;
            if (convertView == null) {
                vh = new LifeViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.img_item_layout, null);
                vh.imgv = (ImageView) convertView.findViewById(R.id.item_img);
                convertView.setTag(vh);
            } else {
                vh = (LifeViewHolder) convertView.getTag();
            }
            vh.imgv.setBackgroundResource(R.color.transparent);
            if(imgList.size()-1 == position){
                Glide.with(activity).load("android.resource://com.xianhao365.o2o/mipmap/"+R.mipmap.addphoto).into(vh.imgv);
            }else{
                Glide.with(activity).load(imgList.get(position)).into(vh.imgv);
            }
            return convertView;
        }
        class LifeViewHolder{
            ImageView imgv;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if(ppRely.getVisibility() == View.VISIBLE) {
                ppRely.setVisibility(View.GONE);
                return true;
            }else{
                finish();
            }
        }
        return false;
    }

}