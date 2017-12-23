package com.android.scale.uart;


import android.util.Log;

public class UartOptNative {
    private static final String TAG = "UartOptNative";

    static {
        try {
            System.loadLibrary("UartOpt");
            Log.d(TAG, "Load LibUartOpt succeed!");
        } catch (Exception e) {
            Log.e(TAG, "Load LibUartOpt fail!");
            e.printStackTrace();
        }
    }

    /**
     * 初始化串口;
     *
     * @param dev      串口设备文件路径 /dev/ttyS1 注意在adb中查看文件是否有读写权限
     * @param bandRate 波特率
     * @return 串口的fb；
     */
    public static native int openUartNative(String dev, int bandRate);

    /**
     * 关闭串口;
     *
     * @param fb 串口的fb
     * @return 0 关闭成功 -1 失败；
     */
    public static native int closeUartNative(int fb);

    /**
     * 串口发送消息;
     *
     * @param fb 串口的fb
     * @param message 需要发送的消息；
     * @return ；
     */
    public static native int uartSendMessageNative(int fb, byte[] message);

    public static native int uartSendMessageNative(int fb, byte message);

    /**
     * 串口读消息;
     *
     * @param fb 串口的fb
     * @return 读到的数据；
     */
    public static native byte[] uartReadMessageNative(int fb);
}
