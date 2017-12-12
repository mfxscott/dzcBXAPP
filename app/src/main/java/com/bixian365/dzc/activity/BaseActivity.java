package com.bixian365.dzc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.lzy.okhttputils.OkHttpUtils;
import com.bixian365.dzc.R;
import com.bixian365.dzc.utils.SXUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by mfx-t224 on 2017/6/29.
 */

public class BaseActivity extends AppCompatActivity {
    public MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) getApplicationContext();
        SXUtils.getInstance(this).addActivity(this);
        StatService.start(this);
//注册事件
    }
    /**
     * 设置点击左上角的返回事件.默认是finish界面
     */
    protected void registerBack() {
        LinearLayout   allTitleGobackLinlay = (LinearLayout) findViewById(R.id.all_title_goback_linlay);
        allTitleGobackLinlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.this.finish();
            }
        });
    }
    protected  void setTitle(String title){
        TextView allTitleName = (TextView) findViewById(R.id.all_title_name);
        allTitleName.setText(title+"");
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OkHttpUtils.getInstance().cancelTag(this);//取消以Activity.this作为tag的请求
    }
//    }
}
