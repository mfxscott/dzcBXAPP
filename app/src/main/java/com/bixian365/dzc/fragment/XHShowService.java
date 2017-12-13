package com.bixian365.dzc.fragment;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bixian365.dzc.R;

public class XHShowService extends Service 
{
	private static final String TAG = "XHShowService";

	 public final static String HDMI_PATH = "/mnt/external_sd/hdmi"; //副屏幕存放视频路径


    private XHPresentation myPresentation;

    private int nowHdmiPosition=0;

    MsgReceiver receiver;

    private final static String SERVICE_ACTION = "com.xh.dualscreen.actions";
    
    class MsgReceiver extends BroadcastReceiver 
    {
        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) 
        {
            if (intent != null) 
            {
                if (intent.getIntExtra("receiver_key", -1) == 0) 
                {
                    myPresentation.dismiss();
                    unregisterReceiver(receiver);
                    stopSelf();
                }
            }
        }
    }
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	@SuppressLint("NewApi") @Override
	public void onCreate() 
	{
		super.onCreate();
			

			 initViewSurface();
	}

    @SuppressLint("InflateParams") 
    private void initViewSurface() 
    {

        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = 0;
        wmParams.height = 0;
        
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout presentationLayout = (LinearLayout) inflater.inflate(R.layout.screen_second, null);
        presentationLayout.setFocusable(false);
        mWindowManager.addView(presentationLayout, wmParams);
        
    }
    


}
