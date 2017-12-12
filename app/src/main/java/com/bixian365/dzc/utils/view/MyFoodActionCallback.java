package com.bixian365.dzc.utils.view;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

import com.bixian365.dzc.entity.FoodActionCallback;
import com.bixian365.dzc.utils.SXUtils;

import static com.bixian365.dzc.fragment.MainFragmentActivity.badge1;

/**
 * 自定义贝赛尔添加购物车效果
 */
public class MyFoodActionCallback implements FoodActionCallback {
    private String skuBarcode;
    private Activity activity;
    private View  badgeView=null;//商品详情点击加入购物车
    public MyFoodActionCallback (Activity activity,String skuBarcode){
        this.skuBarcode = skuBarcode;
        this.activity = activity;
    }
    public MyFoodActionCallback (Activity activity,View view,String skuBarcode){
        this.skuBarcode = skuBarcode;
        this.activity = activity;
        this.badgeView = view;
    }
    @Override
    public void addAction(View view) {
        if(!SXUtils.getInstance(activity).IsLogin())
            return ;
        NXHooldeView nxHooldeView = new NXHooldeView(activity);
        int position[] = new int[2];
        view.getLocationInWindow(position);
        nxHooldeView.setStartPosition(new Point(position[0], position[1]));
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
        rootView.addView(nxHooldeView);
        int endPosition[] = new int[2];
        if(badgeView!=null)
            badgeView.getLocationInWindow(endPosition);
        else
            badge1.getLocationInWindow(endPosition);
        nxHooldeView.setEndPosition(new Point(endPosition[0], endPosition[1]));
        nxHooldeView.startBeizerAnimation();
        SXUtils.getInstance(activity).AddOrUpdateCar(skuBarcode,"1");
    }
}
