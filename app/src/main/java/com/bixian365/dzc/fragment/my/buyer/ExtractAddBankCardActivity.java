package com.bixian365.dzc.fragment.my.buyer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lzy.okhttputils.model.HttpParams;
import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.entity.address.AddressProvinceEntity;
import com.bixian365.dzc.entity.wallet.WalletInfoEntity;
import com.bixian365.dzc.entity.wallet.bank.OpenBankEntity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;
import com.bixian365.dzc.utils.httpClient.HttpUtils;
import com.bixian365.dzc.utils.httpClient.ResponseData;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExtractAddBankCardActivity extends BaseActivity implements View.OnClickListener{
    private Activity activity;
    private Handler hand;
    private List<OpenBankEntity> openBank;//得到开户行名称
    private OptionsPickerView OpenAddressOptions;
    private OptionsPickerView OpenBankOptions;
    private String  pCode,cCode,aCode;//地址编号
    private String  provinceStr,cityStr,districtStr;
    @BindView(R.id.extract_add_card_detail_btn)
    Button btn;
    @BindView(R.id.addbank_get_openaddress_rely)
    RelativeLayout openAddressRel;
    @BindView(R.id.addbank_get_openbank_rely)
    RelativeLayout openBanekRel;
    @BindView(R.id.extract_open_bank_tv)
    TextView  openBankTv;
    @BindView(R.id.extract_open_address_tv)
    TextView openAddressTv;
    @BindView(R.id.extract_name_edit)
    TextView  nameTv;
    @BindView(R.id.extract_idno_edit)
    TextView  idNoTv;
    @BindView(R.id.extract_accno_edit)
    TextView  accNoTv;
    @BindView(R.id.extract_zhbank_name_edit)
    TextView  zhBankNameTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract_add_bank_card);
        activity = this;
        ButterKnife.bind(activity);

        initView();
        OpenBankPopViewPrick();
        OpenAddressPopViewPrick();
//        initData();
    }
    private void  initData(){
        getOpenBank();

    }
    private void initView(){
        registerBack();
        setTitle("添加银行卡");

        btn.setOnClickListener(this);
        openBanekRel.setOnClickListener(this);
        openAddressRel.setOnClickListener(this);
        hand = new Handler(new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
//                        开户行获取
                        ArrayList<String> options1Items = new ArrayList<>();
                        openBank = (List<OpenBankEntity>) msg.obj;
                        if(openBank != null){
                            for (int i=0;i<openBank.size();i++)
                                options1Items.add(openBank.get(i).getItemLabel());
                        }
                        OpenBankOptions.setPicker(options1Items);//添加数据源
                        OpenBankOptions.show();
                        break;
                    case 1001:
//                        获取银行省市区
                        break;
                    case 1002:
                        //添加银行卡 跳转钱包刷新银行卡列表
                        EventBus.getDefault().post(new MessageEvent(1,"wallet"));
                        finish();
                        break;
                    case AppClient.ERRORCODE:
                        String errormsg = (String) msg.obj;
                        SXUtils.getInstance(activity).ToastCenter(errormsg+"");
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
            case R.id.extract_add_card_detail_btn:
                String name = nameTv.getText().toString().trim();
                String idno = idNoTv.getText().toString().trim();
                String accNo = accNoTv.getText().toString().trim();
                String zhname = zhBankNameTv.getText().toString().trim();
                String banName = openBankTv.getText().toString().trim();
                String khhaddress = openAddressTv.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    SXUtils.getInstance(activity).ToastCenter("请输入姓名");
                }
                else if(TextUtils.isEmpty(idno)){
                    SXUtils.getInstance(activity).ToastCenter("请输入身份证号");
                }else if(TextUtils.isEmpty(accNo)){
                    SXUtils.getInstance(activity).ToastCenter("请输入银行卡号");
                }else if(TextUtils.isEmpty(zhname)){
                    SXUtils.getInstance(activity).ToastCenter("请输入开户行网点名称");
                }else if(TextUtils.isEmpty(banName)){
                    SXUtils.getInstance(activity).ToastCenter("请选择开户行");
                }else if(TextUtils.isEmpty(khhaddress)){
                    SXUtils.getInstance(activity).ToastCenter("请选择开户行所在地");
                }else{
                    SXUtils.showMyProgressDialog(activity,true);
                    getAddBank(name,idno,accNo,zhname,banName);
                }
                break;
            case R.id.addbank_get_openaddress_rely:
                SXUtils.getInstance(activity).addressPickerPopView(OpenAddressOptions);
                break;
            case R.id.addbank_get_openbank_rely:
                if(openBank == null) {
                    getOpenBank();
                }else{
                    Message msg = new Message();
                    msg.what = 1000;
                    msg.obj = openBank;
                    hand.sendMessage(msg);
                }
                break;
        }
    }

    private void  OpenBankPopViewPrick(){
        OpenBankOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
//                SXUtils.getInstance(activity).ToastCenter(jsonBean.get(options1).getLabel());
//                SXUtils.getInstance(activity).ToastCenter(jsonBean.get(options1).getChildren().get(option2).getLabel());
//                SXUtils.getInstance(activity).ToastCenter(jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel());
                //返回的分别是三个级别的选中位置
                String tx = openBank.get(options1).getItemLabel().toString();
//              tvOptions.setText(tx);
                openBankTv.setText(tx+"");
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
     * 省市区 获取
     */
    private void  OpenAddressPopViewPrick(){
        OpenAddressOptions = new  OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                ArrayList<AddressProvinceEntity>   jsonBean = SXUtils.getInstance(activity).getAddress();

                pCode = jsonBean.get(options1).getValue();
                cCode = jsonBean.get(options1).getChildren().get(option2).getValue();
                if(jsonBean.get(options1).getChildren().get(option2).getChildren() != null){
                    aCode = jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getValue();
                    districtStr = jsonBean.get(options1).getChildren().get(option2).getChildren().get(options3).getLabel();
                }
                //返回的分别是三个级别的选中位置
                provinceStr = jsonBean.get(options1).getLabel();
                cityStr = jsonBean.get(options1).getChildren().get(option2).getLabel();



                //返回的分别是三个级别的选中位置
                String tx = provinceStr
                        +cityStr
                        + districtStr;
                openAddressTv.setText(tx+"");
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
     * 获取银行卡所开户银行
     */
    public void getOpenBank() {
        HttpUtils.getInstance(activity).requestPost(false, AppClient.GET_BANKLIST, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                List<OpenBankEntity>  openBank= ResponseData.getInstance(activity).parseJsonArray(jsonObject.toString(),OpenBankEntity.class);
                Message msg = new Message();
                msg.what = 1000;
                msg.obj = openBank;
                hand.sendMessage(msg);
            }

            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError + "";
                hand.sendMessage(msg);

            }
        });
    }
    /**
     * 添加银行卡
     */
    public void getAddBank(String name,String idno,String accNo,String bankZhName,String bankName) {
        HttpParams params = new HttpParams();
        params.put("bankAccountName",name+"");
        params.put("idNo",idno+"");
        params.put("accNo",accNo+"");
        params.put("bankBranchName",bankZhName+"");
        JSONObject jsob = new JSONObject();
        try {

            JSONObject province = new JSONObject();
            province.put("code",pCode);
            province.put("name",provinceStr);
            JSONObject city = new JSONObject();
            city.put("code",cCode);
            city.put("name",cityStr);
            JSONObject area = new JSONObject();
            area.put("code",aCode);
            area.put("name",districtStr);
            jsob.put("province",province);
            jsob.put("city",city);
            jsob.put("area",area);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("bankAddr",jsob.toString());
        params.put("bankName",bankName);
        HttpUtils.getInstance(activity).requestPost(false, AppClient.ADDBANKCARD, params, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                WalletInfoEntity gde = null;
//                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),WalletInfoEntity.class);
                Message msg = new Message();
                msg.what = 1002;
                msg.obj = "";
                hand.sendMessage(msg);
            }

            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError + "";
                hand.sendMessage(msg);

            }
        });
    }
    /**
     * 获取省市区
     */
    public void getBankAddress() {
        HttpUtils.getInstance(activity).requestPost(false, AppClient.GET_CITYAREA, null, new HttpUtils.requestCallBack() {
            @Override
            public void onResponse(Object jsonObject) {
                WalletInfoEntity gde = null;
//                gde = ResponseData.getInstance(activity).parseJsonWithGson(jsonObject.toString(),WalletInfoEntity.class);
                Message msg = new Message();
                msg.what = 1001;
                msg.obj = "";
                hand.sendMessage(msg);
            }

            @Override
            public void onResponseError(String strError) {
                Message msg = new Message();
                msg.what = AppClient.ERRORCODE;
                msg.obj = strError + "";
                hand.sendMessage(msg);

            }
        });
    }
}
