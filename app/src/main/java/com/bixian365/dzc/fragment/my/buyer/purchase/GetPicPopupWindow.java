package com.bixian365.dzc.fragment.my.buyer.purchase;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.bixian365.dzc.R;


/**
 * ***************************
 * 选择照片方式弹窗
 * @author mfx
 * ***************************
 */
public class GetPicPopupWindow extends PopupWindow {
  
    private Button photoBtn,takeingBtn,cancelBtn;
    private View mMenuView;
    /**
     * @param context
     * @param itemsOnClick
     * @param isshow  是否显示 相册选取图片  上传头像需要
     */
    public GetPicPopupWindow(Activity context, OnClickListener itemsOnClick, boolean isshow) {
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.get_pic_popu_layout, null);
        photoBtn = (Button) mMenuView.findViewById(R.id.get_pic_photo);
        takeingBtn = (Button) mMenuView.findViewById(R.id.get_pic_take);
        cancelBtn = (Button) mMenuView.findViewById(R.id.get_pic_cancel);
        //取消按钮
        cancelBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {  
                //销毁弹出框  
                dismiss();  
            }  
        });
        if(isshow){
            photoBtn.setVisibility(View.VISIBLE);
        }
        //设置按钮监听  
        photoBtn.setOnClickListener(itemsOnClick);
        takeingBtn.setOnClickListener(itemsOnClick);
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
                int height = mMenuView.findViewById(R.id.popu_lay).getTop();  
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
  
}  