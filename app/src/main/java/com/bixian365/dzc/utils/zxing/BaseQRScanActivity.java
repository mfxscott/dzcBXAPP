package com.bixian365.dzc.utils.zxing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.bixian365.dzc.R;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.zxing.camera.CameraManager;
import com.bixian365.dzc.utils.zxing.decoding.CaptureActivityHandler;
import com.bixian365.dzc.utils.zxing.decoding.DecodeFormatManager;
import com.bixian365.dzc.utils.zxing.decoding.DecodeHandler;
import com.bixian365.dzc.utils.zxing.decoding.InactivityTimer;
import com.bixian365.dzc.utils.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


/**
 * 二维码扫描界面
 * @author mfx
 */
public class BaseQRScanActivity extends Activity
		implements SurfaceHolder.Callback, OnClickListener{

	static final String TAG = BaseQRScanActivity.class.getSimpleName();

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;

	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;

	//	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;

	private boolean playBeep;

	private static final float BEEP_VOLUME = 0.10f;

	private boolean vibrate;

	public static final int PHOTO_RESOULT = 3;

	public static final int PHOTO_WITH_DATA = 4;

	public static final String IMAGE_UNSPECIFIED = "image/*";

	private DecodeHandler mDecodeHandler ;

	/**
	 * 进入该界面时，是否是从选择图片界面返回
	 * 如果是用户从图片选择界面跳转过来，则扫描用户选择的图片
	 */
	private boolean hasUserSelectImg;

	private Bitmap mSelectImg;
	private boolean isCropImg;

	// add new
	protected View leftTitleButton;
	protected View rightTitleButton;
	protected TextView tvTitle;
	protected ImageView btnGallery;
	protected ImageView btnFlashLight;
//	private RelativeLayout relLay;
    private TextView scanInput;
	// 扫描记录

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_qcode);
		CameraManager.init(getApplication());
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//		UXUtils.getInstance(BaseQRScanActivity.this).addActivity(this);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//		relLay = (RelativeLayout) findViewById(R.id.rel_lay);
//		txtResult = (TextView) findViewById(R.id.txtResult);
         scanInput = (TextView) findViewById(R.id.scan_label);
		scanInput.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
//		FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) relLay.getLayoutParams();
//		params.height = DisplayUtils.getHeight(this);
//		params.width = LayoutParams.MATCH_PARENT;
//		params.setMargins(0, -20, 0, 0);
//		relLay.setLayoutParams(params);
		LinearLayout backLin  = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
		backLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		TextView titleTv = (TextView) findViewById(R.id.all_title_name);
		titleTv.setText("二维码/条形码");

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(3);
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			decodeFormats = new Vector<BarcodeFormat>();

			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);

		}

		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		mDecodeHandler = new DecodeHandler(this, hints);

		btnGallery = (ImageView) findViewById(R.id.btn_gallery);
		btnFlashLight = (ImageView) findViewById(R.id.btn_flash_light);


		btnGallery.setOnClickListener(this);
		btnFlashLight.setOnClickListener(this);
		initTitle();

	}

	private void initTitle() {
//		leftTitleButton = findViewById(R.id.btn_back);
//		leftTitleButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//
//		rightTitleButton = findViewById(R.id.btn_menu);
//		rightTitleButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO
//			}
//		});
//
//		tvTitle = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();

		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	private boolean isFlashLightOn;

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_gallery:
				Intent intentGetPhoto = new Intent(Intent.ACTION_GET_CONTENT);
				intentGetPhoto.setType(IMAGE_UNSPECIFIED);
				startActivityForResult(intentGetPhoto, PHOTO_WITH_DATA);
				break;
			case R.id.btn_flash_light:
				if (!isFlashLightOn) {
					CameraManager.get().enableFlashlight();
					btnFlashLight.setImageResource(R.mipmap.scan_ic_flash_light_on);
					isFlashLightOn = true;
				} else {
					CameraManager.get().disableFlashlight();
					btnFlashLight.setImageResource(R.mipmap.scan_ic_flash_light_off);
					isFlashLightOn = false;
				}
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PHOTO_WITH_DATA) {
			isCropImg = true;
			if (data != null) {
				Uri uri = data.getData();
				startPhotoZoom(uri);
				return ;
			}
		}

		isCropImg = false;

		if (requestCode == PHOTO_RESOULT) {
			if (data != null) {
				final Bundle extras = data.getExtras();
				if (extras != null) {
					hasUserSelectImg = true;
					mSelectImg = extras.getParcelable("data");
					Log.i(TAG, "select img size:" + mSelectImg.getWidth() + "x" + mSelectImg.getHeight());
				}
			}
		}
	}

	/**
	 * 裁剪选择的图片
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_RESOULT);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 打开视频预览
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
			CameraManager.get().setCameraDisplayOrientation(this, 0);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}

		// 开启预览,并启动解析器
		if (handler == null) {
			handler = new CaptureActivityHandler(BaseQRScanActivity.this, decodeFormats, characterSet);
		}

		if (hasUserSelectImg) {
			hasUserSelectImg = false;
			Log.i(TAG, "aainit camera has selectImg:");
			Message.obtain(mDecodeHandler, R.id.decode_img, mSelectImg.getWidth(), mSelectImg.getHeight(), getRGBLuminanceSource(mSelectImg)).sendToTarget();
		}
	}



	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!isCropImg) {
			if (!hasSurface) {
				hasSurface = true;
				initCamera(holder);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	public void handleDecode(Object obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
//		obj.toString();
		String barcodeStr = obj.toString();

		onScanSuccess(barcodeStr);
	}
	/**
	 * 扫码成功后的回调
	 * @param qrCode
	 */
	protected void onScanSuccess(String qrCode){
		Logs.i("扫码返回值============"+qrCode);
//		SXUtils.getInstance(this).ToastCenter("扫码获得"+qrCode);

		finish();
//		if (!qrCode.startsWith("https://www.sanxiapay.com/")) {
////			Toast.makeText(BaseQRScanActivity.this, "扫码结果"+qrCode, Toast.LENGTH_SHORT).show();
////			UXUtils.getInstance(BaseQRScanActivity.this).ToastCenter(BaseQRScanActivity.this,"无法识别此"+qrCode);
//			return;
//		}
//		Intent intent = new Intent();
//		intent.setAction("android.intent.action.VIEW");
//		intent.addCategory(Intent.CATEGORY_BROWSABLE);
//		Uri content_url = Uri.parse(qrCode);
//		intent.setData(content_url);
//		startActivity(intent);

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private static byte[] getRGBLuminanceSource(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

		// In order to measure pure decoding speed, we convert the entire image to a greyscale array
		// up front, which is the same as the Y channel of the YUVLuminanceSource in the real app.
		byte[] luminances = new byte[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				int pixel = pixels[offset + x];
				int r = (pixel >> 16) & 0xff;
				int g = (pixel >> 8) & 0xff;
				int b = pixel & 0xff;

				if (r == g && g == b) {
					// Image is already greyscale, so pick any channel.
					luminances[offset + x] = (byte) r;
				} else {
					// Calculate luminance cheaply, favoring green.
					luminances[offset + x] = (byte) ((r + g + g + b) >> 2);
				}
			}
		}
		return luminances;
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};


}