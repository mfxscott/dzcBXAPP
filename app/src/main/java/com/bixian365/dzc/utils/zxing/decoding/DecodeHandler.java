/*
 * Copyright (C) 2010 ZXing authors
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

package com.bixian365.dzc.utils.zxing.decoding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.bixian365.dzc.R;
import com.bixian365.dzc.utils.zxing.BaseQRScanActivity;
import com.bixian365.dzc.utils.zxing.camera.CameraManager;
import com.bixian365.dzc.utils.zxing.camera.PlanarYUVLuminanceSource;

import java.util.Hashtable;


public final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final BaseQRScanActivity activity;
	private final MultiFormatReader multiFormatReader;

	public DecodeHandler(BaseQRScanActivity activity, Hashtable<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case R.id.decode:
			// Log.d(TAG, "Got decode message");
			decode((byte[]) message.obj, message.arg1, message.arg2);
			break;
		case R.id.decode_img:
	    	decodeImage((byte[]) message.obj, message.arg1, message.arg2);
	    	break;
		case R.id.quit:
			Looper.myLooper().quit();
			break;
		}
	}

	/**
	 * 解析图像中的二维码
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * 
	 * @param data 图像的原始帧
	 *            The YUV preview frame.
	 * @param width 图像的宽
	 *            The width of the preview frame.
	 * @param height 图像的高
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;
		
		// add 
		PlanarYUVLuminanceSource source = null;
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		if (display.getWidth() > display.getHeight()) {
			source = CameraManager.get().buildLuminanceSource(data, width, height);
		} else {
			// add 
			byte[] rotatedData = new byte[data.length];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++)
					rotatedData[x * height + height - y - 1] = data[x + y * width];
			}
			source = CameraManager.get().buildLuminanceSource(rotatedData, height, width);
		}
		
		// end
		
		
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			// 解析图像中的二维码
			rawResult = multiFormatReader.decodeWithState(bitmap);
		} catch (ReaderException re) {
			// continue
		} finally {
			multiFormatReader.reset();
		}

		if (rawResult != null) {
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
			Message message = Message.obtain(activity.getHandler(), R.id.decode_succeeded, rawResult);
			Bundle bundle = new Bundle();
			bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
			message.setData(bundle);
			// Log.d(TAG, "Sending decode succeeded message...");
			message.sendToTarget();
		} else {
			Message message = Message.obtain(activity.getHandler(), R.id.decode_failed);
			message.sendToTarget();
		}
	}

	private void decodeImage(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;
		PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSourceWithOrgSize(data, width, height);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		try {
			rawResult = multiFormatReader.decodeWithState(bitmap);
		} catch (ReaderException re) {
			// continue
		} finally {
			multiFormatReader.reset();
		}

		if (rawResult != null) {
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode (" + (end - start) + " ms):\n"
					+ rawResult.toString());
			Message message = Message.obtain(activity.getHandler(),
					R.id.decode_succeeded, rawResult);
			Bundle bundle = new Bundle();
			bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
					source.renderCroppedGreyscaleBitmap());
			message.setData(bundle);
			// Log.d(TAG, "Sending decode succeeded message...");
			message.sendToTarget();
		} else {
			Toast.makeText(activity, "图片扫描失败，请确认选择的图片中包含有效的二维码图案",
					Toast.LENGTH_SHORT).show();
			if (activity.getHandler() != null) {
				Message message = Message.obtain(activity.getHandler(),
						R.id.decode_failed);
				message.sendToTarget();
			}
		}
	}

}

