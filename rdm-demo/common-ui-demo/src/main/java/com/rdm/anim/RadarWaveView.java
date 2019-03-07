package com.rdm.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 *   雷达波纹组件
 * Created by lokierao on 2017/9/6.
 */
public class RadarWaveView extends View /*implements View.OnClickListener*/{

    //private static final int NUM_SNOWFLAKES = 150; // 红包数量
    private static final int DELAY = 5; // 延迟
    private LinkedList<ThrowEgg> mDrawableList = new LinkedList<>(); // 红包


    public static interface OnListener{

        void onThrowEggFnihshend(ThrowEgg egg);
    }

    private static class GiftItem{
        public String name;
        public int recouseId;
    }

    private OnListener onListener;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int mWidth  = 0;
    private int mHeight = 0;


    public RadarWaveView(Context context) {
        super(context);
        intView(context);
    }

    public RadarWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intView(context);
    }

    public RadarWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView(context);

    }

    private void intView(Context context){

        final Handler h = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                throwEgg();
                h.postDelayed(this,300);
            }
        };
        h.postDelayed(runnable,300);

    }

    public OnListener getOnListener() {
        return onListener;
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }
    }

    private void initSnow(int w, int h) {
        mWidth = w;
        mHeight = h;
    }

    private Random random = new Random(System.currentTimeMillis());

    /***
     * 砸一个鸡蛋
     * @param egg
     */
    public void throwEgg() {
        if(mWidth < 1 || mHeight < 1) {
            return;
        }

        PointF start = new PointF(100,mHeight - 100);
        float endY = mHeight * 0.8f * (random.nextInt(100) / 100f);
        float endX = mWidth * 0.2f + mWidth * 0.8f * (random.nextInt(100)/100f);

       // PointF end = new PointF(endX,endY);
        PointF end = new PointF(endX,endY);

        ThrowEgg egg = new ThrowEgg(getContext(),mWidth,mHeight,start,end);
        mDrawableList.add(egg);
        egg.start();
        onAddEgg(egg);
        invalidate();
    }

    private void onAddEgg(ThrowEgg egg){

    }

    private void onRemove(ThrowEgg egg){
        if(onListener != null){
            onListener.onThrowEggFnihshend(egg);
        }
    }



    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ArrayList<ThrowEgg> linkedList = new ArrayList<ThrowEgg>(mDrawableList);
        if(!linkedList.isEmpty()) {
       //     Collections.reverse(linkedList);
            for (ThrowEgg s : linkedList) {
                s.drawEggWater(canvas);
            }
            for (ThrowEgg s : linkedList) {
                s.drawEgg(canvas);
            }
            // 隔一段时间重绘一次, 动画效果
            mHandler.postDelayed(runnable, DELAY);
        }
    }



    // 重绘线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LinkedList<ThrowEgg> linkedList = new LinkedList<ThrowEgg>(mDrawableList);

            //除掉已经完成的egg
            for(ThrowEgg egg: mDrawableList) {
                if(egg.isEnd()){
                    linkedList.remove(egg);
                    onRemove(egg);
                }
            }
            mDrawableList = linkedList;
            invalidate();
        }
    };


}