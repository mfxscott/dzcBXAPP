package com.bixian365.dzc.activity;

import android.Manifest;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.fragment.my.buyer.purchase.GetPicPopupWindow;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.PicHelper;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.view.MyGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsFeedbackActivity extends BaseActivity implements View.OnClickListener{
    private Activity activity;
    private Handler hand;
    @BindView(R.id.feedback_gridv)
    MyGridView feedbackGridv;
    @BindView(R.id.feedback_submit_btn)
    Button btn;
    @BindView(R.id.feedback_goods_img)
    ImageView bigImgIv;
    @BindView(R.id.feedback_goods_rely)
    RelativeLayout  imgRel;
    @BindView(R.id.feedback_goods_close_tv)
    TextView closeImgTv;
    @BindView(R.id.feedback_content_edt)
    EditText contentEdt;
    @BindView(R.id.feedback_phone_edt)
    EditText phoneEdt;
    @BindView(R.id.feedback_goods_iv)
    ImageView goodsImg;
    @BindView(R.id.feedback_goodsname_tv)
    TextView goodsNameTv;
    @BindView(R.id.feedback_marketprice_tv)
    TextView marketPriceTv;
    @BindView(R.id.feedback_shopprice_tv)
    TextView shopPriceTv;
    @BindView(R.id.goods_detail_feedback_shoname)
    TextView shopName;
    @BindView(R.id.feedback_del_goback_linlay)
    LinearLayout feedBackLin;
    @BindView(R.id.feedback_modle_tv)
    TextView  feedbackModleTv;
    private ImageGridViewAdapter imgGridAdapter;
    private List<File> imgList = new ArrayList<>();
    private int checkPosition;//当前选中查看的图片索引
    private GetPicPopupWindow getpicpop;
    private String goodsCode,goodsName,marketPrice,shopPrice,model,goodsurl,shopNameStr;
    private boolean  IstakePhoto;//ture 拍照，是否是拍照，还是相册 区分申请不同权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_feedback);
        activity = this;
        goodsCode = this.getIntent().getStringExtra("goodsCode");
        goodsName = this.getIntent().getStringExtra("goodsName");
        marketPrice = this.getIntent().getStringExtra("marketPrice");
        shopPrice = this.getIntent().getStringExtra("shopPrice");
        model = this.getIntent().getStringExtra("model");
        goodsurl = this.getIntent().getStringExtra("goodsImg");
        shopNameStr = this.getIntent().getStringExtra("shopName");
        ButterKnife.bind(activity);
        initView();
    }
    private void initView(){
        registerBack();
        setTitle("快速反馈");
        feedBackLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRel.setVisibility(View.GONE);
            }
        });
        phoneEdt.setText(TextUtils.isEmpty(AppClient.USERPHONE)?"":AppClient.USERPHONE);
        goodsNameTv.setText(goodsName);
        marketPriceTv.setText("¥"+marketPrice);
        shopPriceTv.setText("¥"+shopPrice+"/"+model);
        feedbackModleTv.setText("/"+model);
//        marketPriceTv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
        shopName.setText(shopNameStr+"");
        Glide.with(activity)
                .load(goodsurl)
                .fitCenter()
                .into(goodsImg);


        //默认添加一个增加按钮图片
        imgList.add(new File("123456"));
        imgGridAdapter = new ImageGridViewAdapter(imgList);
        feedbackGridv.setAdapter(imgGridAdapter);
        feedbackGridv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    imgRel.setVisibility(View.VISIBLE);
                    bigImgIv.setImageBitmap(img);
//                    imgList.remove(position);
//                    imgGridAdapter.notifyDataSetChanged();
                }
            }
        });
        closeImgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRel.setVisibility(View.GONE);
                imgList.remove(checkPosition);
                imgGridAdapter.notifyDataSetChanged();
            }
        });
        imgRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRel.setVisibility(View.GONE);
            }
        });
        btn.setOnClickListener(this);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        SXUtils.getInstance(activity).ToastCenter("感谢您的反馈");
                        finish();
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.feedback_submit_btn:
                String  contentStr = contentEdt.getText().toString().trim();
                String  phoneStr = phoneEdt.getText().toString().trim();
                if(TextUtils.isEmpty(contentStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入反馈内容");
                    return;
                }
                if(TextUtils.isEmpty(phoneStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入联系电话");
                    return;
                }
                SXUtils.showMyProgressDialog(activity,true);
                feedbackHttp(phoneStr,contentStr);
                break;
        }
    }
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

//                        Intent intent = null;
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra("return-data", false);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriTakePhoto());
//                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                        intent.putExtra("noFaceDetection", true);
//                        startActivityForResult(intent, 101);
                    }
                    break;
            }
        }
    };
    private String filenName="";//图片名称
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
//                        Intent intent = null;
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        intent.putExtra("return-data", false);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriTakePhoto());
//                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//                        intent.putExtra("noFaceDetection", true);
//                        startActivityForResult(intent, 101);


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
//                imgStr = getRealFilePath(this, data.getData());
//                Logs.i("相册=======图片路径"+imgStr);
//                if (TextUtils.isEmpty(imgStr)) {
//                    return;
//                }
//                File file = new File( imgStr ) ;
//                imgList.add(0,file);
//                imgGridAdapter.notifyDataSetChanged();


                try {
                    imgStr = getRealFilePath(this, data.getData());
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
//                imgStr = getPathTakePhoto(filenName);
//                Logs.i("拍照=======图片路径"+imgStr);
////                if(bitmap31 == null){
////                    return;
////                }
//                if (TextUtils.isEmpty(imgStr)) {
//                    return;
//                }
//                File files = new File(imgStr) ;
//                imgList.add(0,files);
//                imgGridAdapter.notifyDataSetChanged();





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






    public void feedbackHttp(String phone,String contents) {
        HttpParams params = new HttpParams();
        params.put("contents", contents);
        params.put("phoneNo", phone);
        params.put("goodsCode", goodsCode);
        List<File> files = new ArrayList<>();
        //去除手动添加的图片 最后一张
        for(int i=0;i<imgList.size();i++){
            if(i!=imgList.size()-1){
                files.add(imgList.get(i));
                Logs.i("上次file图片张数============"+files.get(i).getPath());
            }
            Logs.i("上次imgList图片张数============"+imgList.get(i).getPath());

        }

        params.putFileParams("feedbackFile", files);
        HttpUtils.getInstance(activity).requestUploadImgPost(false, AppClient.GOODS_FEEDBACK, params, new HttpUtils.requestCallBack() {
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
    class ImageGridViewAdapter extends BaseAdapter {
        private final LayoutInflater mLayoutInflater;

        public ImageGridViewAdapter(List<File> result) {
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
            if (imgList.size() - 1 == position) {
                Glide.with(activity).load("android.resource://com.xianhao365.o2o/mipmap/" + R.mipmap.addphoto).into(vh.imgv);
            } else {
                Glide.with(activity).load(imgList.get(position)).asBitmap().into(vh.imgv);
            }

            return convertView;
        }

        class LifeViewHolder {
            ImageView imgv;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if(imgRel.getVisibility() == View.VISIBLE) {
                imgRel.setVisibility(View.GONE);
                return true;
            }else{
                finish();
            }
        }
        return false;
    }
}