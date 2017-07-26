package com.express.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.express.R;

/**
 * Created by 猪 on 2017/7/26.
 */

public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    //单位dP
    private int mMenuRightPadding = 50;
    private boolean once = false;
    private boolean isOpen =false;
    private int mMenuWidth;
    /**
     * 未使用自定义属性时， 调用
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingMenu(Context context) {
        this(context,null);
    }

    /**
     * 当使用了自定义属性时，会调用此构造方法
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SlidingMenu(Context context, AttributeSet attrs ,int defStyle) {
        super(context, attrs, defStyle);
        //获取我们自定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu,defStyle,0);
        int n=a.getIndexCount();
        for(int i=0;i<n;i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr,(int) TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP,50,context.getResources().getDisplayMetrics()));
            break;
            }
        }
        a.recycle();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        //PD转换为PX
        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,mMenuRightPadding,context
                .getResources().getDisplayMetrics());
    }


    /**
     * 设置子VIEW的宽和高和自己的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!once)
        {
            mWapper = (LinearLayout) getChildAt(0);//因为Wapper=Menu+Content 所以不用再设置 getChildAt为获得控件群中的小控件0为下标
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width=mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width=mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 通过设置偏移量 将MENU隐藏
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(mMenuWidth,0);}
    }
    float downX;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = getScrollX();
                break;
            case MotionEvent.ACTION_UP://条件达到执行冒号内的操作
                int distance = -(int) (getScrollX()-downX);//为隐藏在左边的宽度
                if (distance > 0){
                    if (isOpen){
                        return false;
                    }else {
                        if (distance > mMenuWidth/6){
                            this.smoothScrollTo(0,0);
                            isOpen = true;
                            return true;
                        }else {
                            this.smoothScrollTo(mMenuWidth,0);
                            return true;
                        }
                    }
                }else {
                    if(!isOpen){
                        return false;
                    }else{
                        if(distance < -mMenuWidth/6){
                            this.smoothScrollTo(mMenuWidth,0);
                            isOpen = false;
                            return true;
                        }else {
                            this.smoothScrollTo(0,0);
                            return true;
                        }
                    }
                }
        }
        return super.onTouchEvent(ev);
    }

    public void openMenu(){
    if(isOpen)return;
        this.smoothScrollTo(0,0);
        isOpen = true;

    }

    public void closeMenu(){
       if (!isOpen)return;
        this.smoothScrollTo(mMenuWidth,0);
        isOpen = false;
    }

    public void toggle(){
        if(isOpen) {
        closeMenu();
        }else {
            openMenu();
        }
    }
}