package com.bixian365.dzc.utils;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by mfx-t224 on 2017/6/28.
 */

class MyCountDownTimer extends CountDownTimer {
    /**
          *
          * @param millisInFuture
          *      表示以毫秒为单位 倒计时的总数
          *
          *      例如 millisInFuture=1000 表示1秒
          *
          * @param countDownInterval
          *      表示 间隔 多少微秒 调用一次 onTick 方法
          *
          *      例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
          *
          */
    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }
    @Override
    public void onFinish() {
    }
    @Override
    public void onTick(long millisUntilFinished) {
        Log.i("MainActivity", millisUntilFinished + "");
//        tv.setText("倒计时(" + millisUntilFinished / 1000 + ")...");
    }
}