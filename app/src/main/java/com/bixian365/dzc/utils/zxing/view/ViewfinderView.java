/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bixian365.dzc.utils.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.bixian365.dzc.R;
import com.bixian365.dzc.utils.zxing.camera.CameraManager;
import com.bixian365.dzc.utils.zxing.utils.DisplayUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	
	private Collection<ResultPoint> possibleResultPoints;
	
	private Bitmap mBmpLine;
	private BitmapDrawable mBmpLineDrawable;
	
	private Bitmap mBmpLT;
	private Bitmap mBmpTR;
	private Bitmap mBmpLB;
	private Bitmap mBmpRB;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 利用反射判断setLayerType方法是否存在，存在则关闭硬件加速
		try {
			Class nowClass = Class.forName("android.view.View");
			Method[] methodss = nowClass.getMethods();
			for (int idx = 0; idx < methodss.length; idx++) {
				if (methodss[idx].getName().equals("setLayerType")) {
					try {
						// setLayerType(View.LAYER_TYPE_SOFTWARE, null); //
						// 关闭硬件加速
					} catch (Exception ex) {
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		possibleResultPoints = new HashSet<ResultPoint>(5);
		
		mBmpLine = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_scan);
		//mBmpLine = BitmapFactory.decodeResource(getResources(), R.drawable.barcode_line);
		mBmpLineDrawable = new BitmapDrawable(mBmpLine);
		
		mBmpLT = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_barcode_lt);
		mBmpTR = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_barcode_tr);
		mBmpLB = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_barcode_lb);
		mBmpRB = BitmapFactory.decodeResource(getResources(), R.mipmap.scan_barcode_br);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		
		// 绘制蒙版(注：此处不需减掉20的Top，回取的是整个屏幕高度计算的，不需减掉20的状态栏)
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		// 绘制结果图
		if (resultBitmap != null) {
//			BitmapDrawable resultBitmapDrawable = new BitmapDrawable(resultBitmap);
//			resultBitmapDrawable.setBounds(frame);
//			resultBitmapDrawable.draw(canvas);
		} else {
			if (lineTop == 0) {
				start = lineTop = frame.top;
			}
			drawLine(canvas, frame);
		}
		
		// 绘制4个角
		canvas.drawBitmap(mBmpLT, frame.left, frame.top, null);
		canvas.drawBitmap(mBmpTR, frame.right - mBmpTR.getWidth(), frame.top, null);
		canvas.drawBitmap(mBmpLB, frame.left, frame.bottom - mBmpLB.getHeight(), null);
		canvas.drawBitmap(mBmpRB, frame.right - mBmpRB.getWidth(), frame.bottom - mBmpRB.getHeight(), null);
	}
	
	private int lineTop;
	private int start = 0;
	
	private boolean dir = true;
	
	private void drawLine(Canvas canvas, Rect frame) {
		// 绘制线条
		int left = frame.left;;
		int right = frame.right;
		int top = lineTop;
		//mBmpLineDrawable.setBounds(left, top, right, top + mBmpLine.getHeight());
		mBmpLineDrawable.setBounds(left, start , right, top);
		mBmpLineDrawable.draw(canvas);
		
		if (dir) {
			lineTop += DisplayUtils.getActualSize(getContext(), 3);
			//temp += DisplayUtils.getActualSize(getContext(), 3);
		} else {
			lineTop -= DisplayUtils.getActualSize(getContext(), 3);
		}
		
//		if(top > frame.bottom - 3  || top < frame.top){
//			dir = !dir;
//		}
		
		if (lineTop +  3 >= frame.bottom || lineTop <= start + 2) {
			dir = !dir;
		}
		
		postInvalidateDelayed(10, frame.left, frame.top, frame.right, frame.bottom);
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}
}
