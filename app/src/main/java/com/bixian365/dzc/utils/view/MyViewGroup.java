package com.bixian365.dzc.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mfx-t224 on 2017/7/26.
 */

public class MyViewGroup  extends ViewGroup {
    private int horinzontalMargin;
    private int verticalMargin;

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHorinzontalMargin(int w) {
        horinzontalMargin = w;
        requestLayout();
    }

    public void setVerticalMargin(int h) {
        verticalMargin = h;
        requestLayout();
    }

    /**控制子控件换行*/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int marginWidth = horinzontalMargin;
        int marginHeight = verticalMargin;
        int count = getChildCount();
        int row = 0;
        /**right postion of childView*/
        int right = l;
        /**top position of childView*/
        int top;

        for (int j = 0;j< count;j++){
            View childView = getChildAt(j);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            right += childWidth + marginWidth;

            if (right > r){
                right = marginWidth + l + childWidth;
                row++;
            }

            top = row*(childHeight + marginHeight) + t + marginHeight;
            childView.layout(right -childWidth, top, right, top + childHeight);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父控件宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = 0;
        // 父控件高度--方法：累计每个子控件的宽度+左右间隔，当数值超过父控件的宽度时，换行。记录行数。
        // 记录ViewGroup中Child的总个数
        int count = getChildCount();
        int lines = 1;
        int currWidth = 0; // 每行当前宽度总和
        for (int index = 0; index < count; index++) {
            final View child = getChildAt(index);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            currWidth += (horinzontalMargin + childWidth);//左侧间隔+子控件宽度
            // 另起一行
            if (currWidth > widthSize) {
                currWidth = 0;
                currWidth += (horinzontalMargin + childWidth);//左侧间隔+子控件宽度
                lines++;
                heightSize += childHeight;
            }
            Log.e("lines",lines+"");
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

//            child.measure(childWidth,childHeight);
        }
        //  子控件高度+间隔
        heightSize += lines * verticalMargin;
        setMeasuredDimension(widthSize, heightSize);

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
}