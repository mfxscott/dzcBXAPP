package com.bixian365.dzc.utils.zxing.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtils {

	/**
	 * 获取根据屏幕获取实际大小 在自定义控件中，根据屏幕的大小来获取实际的大小
	 * 
	 * @param ctx
	 * @param orgSize
	 * @return
	 */
	public static int getActualSize(Context ctx, int orgSize) {
		WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		float density = (float) displayMetrics.density;

		return (int) (orgSize * density);
	}
	
	
	/**
	 * 获得屏幕的高度
	 * 
	 * @return
	 */
	public static int getHeight(Activity ac) {
		int width = 0;
		WindowManager wm = (WindowManager) ac.getWindowManager();

		int height = wm.getDefaultDisplay().getHeight();
		width = wm.getDefaultDisplay().getWidth();
		if (height == 0) {
			return 0;
		}
		return height;
	}
}
