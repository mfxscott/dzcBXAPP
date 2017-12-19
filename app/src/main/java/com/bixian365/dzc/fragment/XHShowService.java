package com.bixian365.dzc.fragment;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bixian365.dzc.R;

public class XHShowService extends Service implements View.OnClickListener
{
	private static final String TAG = "XHShowService";

	 public final static String HDMI_PATH = "/mnt/external_sd/hdmi"; //副屏幕存放视频路径

    private DisplayManager mDisplayManager;
    private XHPresentation myPresentation;
    private boolean isShowSecondScreen = false;
    private int nowHdmiPosition=0;
    private Display[] displays;
    MsgReceiver receiver;

    private final static String SERVICE_ACTION = "com.bixian365.dzc.actions";
    
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
        mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);

        displays = mDisplayManager.getDisplays();//get the number of displays

        Log.d(TAG, "xhDisplays=" + displays.length);

        if (displays.length > 1)
        {
            showPresentation(displays[1]);//displays[0]:main_screen  displays[1]:second_screen

            receiver = new MsgReceiver();
            IntentFilter intentFilter = new IntentFilter();

            intentFilter.addAction(SERVICE_ACTION);

            registerReceiver(receiver, intentFilter);

            initViewSurface();
        }
    }
    @SuppressLint("NewApi")
    private void showPresentation(Display display)
    {
        myPresentation = new XHPresentation(this, display);
        myPresentation.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {

            }
        });
        myPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        myPresentation.show();

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

    @Override
    public void onClick(View v)
    {
        if (!isShowSecondScreen)
        {
//            Intent i = new Intent(co, XHShowService.class);
//            startService(i);
            isShowSecondScreen = true;
        }
        else
        {
            Intent i = new Intent(SERVICE_ACTION);
            i.putExtra("receiver_key", 0);
            sendBroadcast(i);
            isShowSecondScreen = false;
        }
    }

}
