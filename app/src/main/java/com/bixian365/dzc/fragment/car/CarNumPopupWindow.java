package com.bixian365.dzc.fragment.car;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bixian365.dzc.R;


/**
 * ***************************
 * 选择照片方式弹窗
 * @author mfx
 * ***************************
 */
public class CarNumPopupWindow extends PopupWindow implements OnClickListener {

    private View mMenuView;
    private TextView inputNumTv;
    private String saveNum="";//保存输入值
    private ImageView delImg;
    /**
     * @param context
     * @param itemsOnClick
     */
    public CarNumPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.car_pop_edit, null);
        TextView tv0 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv0);
        TextView tv1 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv1);
        TextView tv2 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv2);
        TextView tv3 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv3);
        TextView tv4 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv4);
        TextView tv5 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv5);
        TextView tv6 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv6);
        TextView tv7 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv7);
        TextView tv8 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv8);
        TextView tv9 = (TextView) mMenuView.findViewById(R.id.car_input_num_tv9);
         delImg = (ImageView) mMenuView.findViewById(R.id.car_pop_input_del_tv);
        TextView  cancelTv = (TextView) mMenuView.findViewById(R.id.car_input_num_cancel_tv);
        TextView  confirmTv = (TextView) mMenuView.findViewById(R.id.car_input_num_confrim_tv);
        inputNumTv = (TextView) mMenuView.findViewById(R.id.car_pop_input_num_tv);
        cancelTv.setOnClickListener(itemsOnClick);
        confirmTv.setOnClickListener(itemsOnClick);
        tv0.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        delImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                delImg.setVisibility(View.GONE);
                saveNum="";
                inputNumTv.setText("");
            }
        });
        //设置按钮监听
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.car_popu_lay).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    /**
     * 获得当前输入购物车数量
     * @return
     */
    public String getInputNum(){
        String carNum = inputNumTv.getText().toString().trim();
        return carNum;
    }
    @Override
    public void onClick(View view) {
        String num="" ;
        switch (view.getId()){
            case R.id.car_input_num_tv0:
                num = "0";
                break;
            case R.id.car_input_num_tv1:
                num = "1";
                break;
            case R.id.car_input_num_tv2:
                num = "2";
                break;
            case R.id.car_input_num_tv3:
                num = "3";
                break;
            case R.id.car_input_num_tv4:
                num = "4";
                break;
            case R.id.car_input_num_tv5:
                num = "5";
                break;
            case R.id.car_input_num_tv6:
                num = "6";
                break;
            case R.id.car_input_num_tv7:
                num = "7";
                break;
            case R.id.car_input_num_tv8:
                num = "8";
                break;
            case R.id.car_input_num_tv9:
                num = "9";
                break;
        }
        saveNum +=num;
        inputNumTv.setText(saveNum);
        delImg.setVisibility(View.VISIBLE);
    }
}