package com.android.scale.uart;


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
    public boolean checkData(String data) {
        byte[] tmp = Utils.hexStringToBytes(data);
        int sum = 0;
        for (int i = 0; i < tmp.length; i++) {
            sum += tmp[i];
        }
        String crc = Integer.toHexString(sum);
        if (crc.contains(data.substring(data.length() -2, data.length()))) {
            return true;
        }
        return false;
    }
    @Override
    public void run() {
        isStart = true;


        while (isStart) {
            byte[] data = UartOptNative.uartReadMessageNative(fb);
            if (data != null && data.length > 0) {
                builder.append(Utils.bytesToHexString(data));
//                Log.d(TAG, "read data =" + Utils.bytesToHexString(data));
            }

            if (builder.length() > 0) {
                while (builder.toString().indexOf("4d3e505754") >= 0) {
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





//        while (isStart) {
//            byte[] data = UartOptNative.uartReadMessageNative(fb);
//            if (data != null && data.length > 0) {
//                builder.append(Utils.bytesToHexString(data));
//                // Log.d(TAG, "read data =" + Utils.bytesToHexString(data));
//            }
//
//            if (builder.length() > 0) {
//                while (true) {
//                    int a = builder.toString().indexOf("4d3e50");
//                    if (a == 0) {
//                        if (builder.toString().length() >= 10) {
//                            String fun = builder.toString().substring(6, 10);
//                            if (fun.equals("5754")) {
//                                if (builder.toString().length() >= 32) {
//                                    String cmd = builder.toString().substring(0, 32);
//                                    if (checkData(cmd)) {
//                                        listener.onReadData(cmd);
//                                    }
//                                    builder.replace(0, 32, "");
//                                } else {
//                                    break;
//                                }
//                            } else if (fun.equals("5441")) {
//                                if (builder.toString().length() >= 16) {
//                                    String cmd = builder.toString().substring(
//                                            0, 16);
//                                    listener.onReadData(cmd);
//                                    builder.replace(0, 16, "");
//                                } else {
//                                    break;
//                                }
//                            } else if (fun.equals("5a30")) {
//                                if (builder.toString().length() >= 16) {
//                                    String cmd = builder.toString().substring(
//                                            0, 16);
//                                    listener.onReadData(cmd);
//                                    builder.replace(0, 16, "");
//                                } else {
//                                    break;
//                                }
//                            } else if (fun.equals("4244")) {
//                                if (builder.toString().length() >= 16) {
//                                    String cmd = builder.toString().substring(
//                                            0, 16);
//                                    listener.onReadData(cmd);
//                                    builder.replace(0, 16, "");
//                                } else {
//                                    break;
//                                }
//                            }
//                        } else {
//                            break;
//                        }
//                    } else if (a > 0) {
//                        builder.replace(0, a, "");
//                    } else {
//                        break;
//                    }
//                }
//            }
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
