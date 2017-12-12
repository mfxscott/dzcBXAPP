package com.bixian365.dzc.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.bixian365.dzc.R;
import com.bixian365.dzc.entity.MessageEvent;
import com.bixian365.dzc.fragment.my.store.order.MyOrderActivity;
import com.bixian365.dzc.utils.SXUtils;
import com.bixian365.dzc.utils.httpClient.AppClient;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wx_result_layout);

		api = WXAPIFactory.createWXAPI(this, "wx3720c579ea25cf77");
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("微信=========", "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
				case 0:// 支付成功
					SXUtils.getInstance(WXPayEntryActivity.this).ToastCenter("微信完成");
					if(AppClient.PayTag.equals("12")){
						Intent payorder = new Intent(WXPayEntryActivity.this,MyOrderActivity.class);
						payorder.putExtra("orderTag","2");
						startActivity(payorder);
					}
					EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
					EventBus.getDefault().post(new MessageEvent(1003,"store"));
					WXPayEntryActivity.this.finish();
					EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100025,"toppay"));
					break;
				case -1: // 支付失败
					//-1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
					Toast.makeText(WXPayEntryActivity.this,"错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等",Toast.LENGTH_LONG).show();
					if(AppClient.PayTag.equals("12")){
						Intent payorder = new Intent(WXPayEntryActivity.this,MyOrderActivity.class);
						payorder.putExtra("orderTag","1");
						startActivity(payorder);
					}
					EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
					EventBus.getDefault().post(new MessageEvent(1003,"store"));
					EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100025,"toppay"));
					WXPayEntryActivity.this.finish();
					break;
				case -2:// 取消支付

					SXUtils.getInstance(WXPayEntryActivity.this).ToastCenter("您取消了支付");

					if(AppClient.PayTag.equals("0")){
						WXPayEntryActivity.this.finish();
						return;
					}

					if(AppClient.PayTag.equals("12")){
						Intent payorder = new Intent(WXPayEntryActivity.this,MyOrderActivity.class);
						payorder.putExtra("orderTag","1");
						startActivity(payorder);
					}
					EventBus.getDefault().post(new MessageEvent(AppClient.EVENT1,"store"));
					EventBus.getDefault().post(new MessageEvent(1003,"store"));
					EventBus.getDefault().post(new MessageEvent(AppClient.EVENT100025,"toppay"));
					WXPayEntryActivity.this.finish();
					break;
			}
		}
	}
}