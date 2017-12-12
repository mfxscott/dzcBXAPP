package com.bixian365.dzc.fragment.my;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.bixian365.dzc.R;
import com.bixian365.dzc.activity.BaseActivity;
import com.bixian365.dzc.utils.SXUtils;

/**
 * 公共跳转fragment和 activity
 * 相连接
 * 调用方式
 *
 Intent setfragment = new Intent(activity,MyCommonFragment.class);
 bundle = new Bundle();
 bundle.putInt("flag", 1);
 setfragment.putExtras(bundle);
 startActivity(setfragment);
 */
public class MyCommonFragment extends BaseActivity {

	private Bundle bundle;
	private Activity activity;
    private int flag;//标识跳转不同位置
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_my_common);
		SXUtils.getInstance(activity).addActivity(this);
		bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		activity = this;
        flag = bundle.getInt("flag");
       switch (flag) {
           //设置
           case 1:
//               Fragment setFrag = new SetActivity();
//               SXUtils.getInstance(activity).CommonFragment(activity.getFragmentManager(), setFrag, "set", null);
               break;
           //我的银行卡
       }
       }
	/**
     * 点击返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听按下返回键
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            SXUtils.getInstance(activity).FragmentGoBack(activity);
        }
        return true;
    }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 100:
//                Utils.getInstance(activity).ToastCenter(activity,"100");
				// 直接从相册获取
				try {
//					startPhotoZoom(data.getData());
				} catch (NullPointerException e) {
					e.printStackTrace();// 用户点击取消操作
				}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
