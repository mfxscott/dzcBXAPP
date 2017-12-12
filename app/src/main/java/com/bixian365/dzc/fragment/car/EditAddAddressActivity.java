package com.bixian365.dzc.fragment.car;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.address.AddressInfoEntity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.address.AddressProvinceEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAddAddressActivity extends BaseActivity implements View.OnClickListener{
    private AddressInfoEntity addressInfo;
    private String tag;//0 新增 1 编辑判断是编辑还是新增进入
    private Activity activity;
    private Handler hand;
    @BindView(R.id.edit_address_save_btn)
    Button button;
    @BindView(R.id.address_update_area_lin)
    LinearLayout addressLin;
    @BindView(R.id.address_update_area_tv)
    TextView  addressTv;
    @BindView(R.id.edit_address_phone_edt)
    EditText  phoneEt;
    @BindView(R.id.edit_address_name_edt)
    EditText  nameEt;
    @BindView(R.id.edit_address_info_edt)
    EditText  addreinfoEt;
    @BindView(R.id.edit_address_isdefault_cb)
    CheckBox  checkBox;
    private String  pCode,cCode,aCode;//地址编号
    private String  provinceStr="广东省",cityStr="深圳市",districtStr="罗湖区";
    private boolean  isDefault =  false;//是否为默认地址
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_add_address);
        ButterKnife.bind(this);
        activity =this;
        tag = (String) this.getIntent().getExtras().get("tag");
        if(tag.equals("1")) {
            addressInfo = (AddressInfoEntity) this.getIntent().getExtras().get("address");
            provinceStr=addressInfo.getProvinceName();
            cityStr=addressInfo.getCityName();
            districtStr=addressInfo.getDistrictName();
        }
        initView();
        PopViewPrick();
    }
    private  void initView(){
        registerBack();
        addressLin.setOnClickListener(this);
        button.setOnClickListener(this);
        if(tag.equals("1")) {
            nameEt.setText(addressInfo.getConsigneeName());
            phoneEt.setText(addressInfo.getConsigneeMobile());
            addressTv.setText(addressInfo.getProvinceName()+addressInfo.getCityName()+addressInfo.getDistrictName());
            addreinfoEt.setText(addressInfo.getAddress());
            if(addressInfo.getIsDefault().equals("1"))
                checkBox.setChecked(true);
            else
                checkBox.setChecked(false);
            setTitle("编辑地址");
        }else{
            setTitle("新增地址");
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isDefault = true;
                }else{
                    isDefault = false;
                }
            }
        });
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        EventBus.getDefault().post(new MessageEvent(555,"orderList"));
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
            case R.id.edit_address_save_btn:
                String  nameStr = nameEt.getText().toString().trim();
                String phoneStr = phoneEt.getText().toString().trim();
                String  addInfoStr = addreinfoEt.getText().toString().trim();
                String  addressStr = addressTv.getText().toString();
                if(TextUtils.isEmpty(nameStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入收货人姓名");
                }else if(TextUtils.isEmpty(phoneStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入联系电话");
                }else if(TextUtils.isEmpty(addInfoStr)){
                    SXUtils.getInstance(activity).ToastCenter("请输入详细收货地址");
                }else if(TextUtils.isEmpty(addressStr)){
                    SXUtils.getInstance(activity).ToastCenter("请选择收货地址");
                }else{
                    SXUtils.showMyProgressDialog(activity,true);
                    getUpdateAddress(nameStr,phoneStr,addInfoStr);
                }

                break;
            case  R.id.address_update_area_lin:
                SXUtils.getInstance(activity).addressPickerPopView(pvOptions);
                break;
        }
    }
    OptionsPickerView pvOptions;
    private void  PopViewPrick(){
        pvOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                ArrayList<AddressProvinceEntity> jsonBean = SXUtils.getInstance(activity).getAddress();
//                SXUtils.getInstance(activity).ToastCenter(jsonBean.get(options1).getLabel());
//                SXUtils.getInstance(activity).ToastCenter(jsonBean.get(options1).getChildren().get(option2).getLabel());
//                SXUtils.getInstance(activity).ToastCenter(jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel());
                pCode = jsonBean.get(options1).getValue();
                cCode = jsonBean.get(options1).getChildren().get(option2).getValue();
                aCode = jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getValue();
                //返回的分别是三个级别的选中位置
                provinceStr = jsonBean.get(options1).getLabel();
                cityStr = jsonBean.get(options1).getChildren().get(option2).getLabel();
                districtStr = jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel();

                String tx = jsonBean.get(options1).getLabel()
                        + jsonBean.get(options1).getChildren().get(option2).getLabel()
                        + jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel();
//              tvOptions.setText(tx);
                addressTv.setText(tx+"");
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
    /**
     * 修改收货地址
     */
    public void getUpdateAddress(String name,String phone,String addressInf) {
        HttpParams httpp = new HttpParams();
        httpp.put("consignee",name);
        httpp.put("id",addressInfo==null?"":addressInfo.getConsigneeId());
        httpp.put("mobile",phone);
        httpp.put("provinceCode",pCode);
        httpp.put("provinceName",provinceStr);
        httpp.put("cityCode",cCode);
        httpp.put("cityName",cityStr);
        httpp.put("districtCode",aCode);
        httpp.put("districtName",districtStr);
        httpp.put("address",addressInf);
        httpp.put("isDefault",isDefault);
        HttpUtils.getInstance(activity).requestPost(false,tag.equals("0") ?AppClient.ADDRESS_ADD:AppClient.ADDRESS_UPDATE, httpp, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                JSONObject jsonObject1 = null;
//                FromOrderEntity orderFrom = (FromOrderEntity) ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),FromOrderEntity.class);
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

}
