package com.android.scale.uart;


import com.bixian365.dzc.utils.Logs;

public class UartReadThread extends Thread {
    private static final String TAG = "UartReadThread";
    private int fb;
    private boolean isStart;
    private IReadDataListener listener;
    private StringBuilder builder = new StringBuilder();

    public UartReadThread(int fb) {
        this.fb = fb;
    }

    public void setListener(IReadDataListener listener) {
        this.listener = listener;
    }

    public void stopThread() {
        isStart = false;
        interrupt();
    }

    @Override
    public void run() {
        isStart = true;
        while (isStart) {
            byte[] data = UartOptNative.uartReadMessageNative(fb);
            if (data != null && data.length > 0) {
                builder.append(Utils.bytesToHexString(data));
            }
            if (builder.length() > 0) {
                if(builder.toString().indexOf("4d3e505441010001")>=0){
                    Logs.i("+++++++++去皮======"+builder.toString());
                    //去皮
                    listener.onReadData(builder.toString());
                    builder.replace(0, builder.toString().length(), "");
                }else
                if(builder.toString().indexOf("4d3e505a4f010001")>=0){
                    //置零
                    Logs.i("+++++++++置零======"+builder.toString());
                    listener.onReadData(builder.toString());
                    builder.replace(0, builder.toString().length(), "");
                }else if(builder.toString().indexOf("4d3e504244010001")>=0){
                    //定标
                    Logs.i("+++++++++标定成功======"+builder.toString());
                    listener.onReadData(builder.toString());
                    builder.replace(0, builder.toString().length(), "");
                }
                while (builder.toString().indexOf("4d3e505754") >= 0
//                     || builder.toString().indexOf("4d3e505441")>=0//去皮
//                     || builder.toString().indexOf("4d3e505a4f")>=0//置零协议
//                      ||builder.toString().indexOf("4d3e504244")>=0 //定标
                        ) {
                    int a = builder.toString().indexOf("4d3e505754");
                    if (builder.toString().length() >= a + 32) {
                        String cmd = builder.toString().substring(a + 10, a + 30);
                        listener.onReadData(cmd);
                        builder.replace(0, a + 32, "");
                    } else {
                        break;
                    }
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
