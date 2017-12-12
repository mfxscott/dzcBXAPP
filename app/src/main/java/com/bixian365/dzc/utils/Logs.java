package com.bixian365.dzc.utils;

/**
 * ***************************
 * 日志管理
 * @author mfx
 * ***************************
 */
public class Logs {
	//控制所有日志是否开启
	public static boolean ISDEBUG = true;//false 为 关闭日志

	private static final boolean VERBOSE = true;
	private static final boolean DEBUG = true;
	private static final boolean INFO = true;
	private static final boolean WARN = true;
	private static final boolean ERROR = true;

	// public static final String TAG_PREFIX = "elife";
	public static void v(String tag, String msg) {
		if (VERBOSE && ISDEBUG) {
			android.util.Log.v(tag, msg);
		}
	}
	public static void v(String tag, String msg, Throwable tr) {
		if (VERBOSE && ISDEBUG) {
			android.util.Log.v(tag, msg, tr);
		}
	}
	public static void d(String tag, String msg) {
		if (DEBUG && ISDEBUG) {
			android.util.Log.d(tag, msg);
		}
	}
	public static void d(String tag, String msg, Throwable tr) {
		if (DEBUG && ISDEBUG) {
			android.util.Log.d(tag, msg, tr);
		}
	}
	public static void i(String tag, String msg) {
		if (INFO && ISDEBUG) {
			android.util.Log.i(tag, msg);
		}
	}
	public static void i(String msg) {
		if (INFO && ISDEBUG) {
			android.util.Log.i("log==========", msg);
		}
	}
	public static void i(String tag, String msg, Throwable tr) {
		if (INFO && ISDEBUG) {
			android.util.Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (WARN && ISDEBUG) {
			android.util.Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (WARN && ISDEBUG) {
			android.util.Log.w(tag, msg, tr);
		}
	}

	public static void w(String tag, Throwable tr) {
		if (WARN && ISDEBUG) {
			android.util.Log.w(tag, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (ERROR && ISDEBUG) {
			android.util.Log.e(tag, msg);
		}
	}
	public static void e(String tag, String msg, Throwable tr) {
		if (ERROR && ISDEBUG) {
			android.util.Log.e(tag, msg, tr);
		}
	}
}
