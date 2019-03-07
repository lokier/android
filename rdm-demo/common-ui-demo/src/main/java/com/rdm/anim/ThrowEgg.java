package com.rdm.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import com.rdm.common.ui.demo.R;

import java.util.Random;


/**
 * <p/>
 * Created by Lokiearo on 17/4/6.
 */
public class ThrowEgg {

    //第一个阶段：0.2秒，鸡蛋从初始位置出来并且放大；
    //第二阶段：0.5秒，鸡蛋砸到任意一个位置；
    //第三阶段：0.2秒，蛋清出现并停留
    //第三阶段：0.2秒, 蛋清放大并逐渐透明化；

    private static final long PERIOD_1 = 500;//
    private static final long PERIOD_2 = 1000;//
    private static final long PERIOD_3 = 200;//
    private static final long PERIOD_4 = 400;//
    private static final long ANIMATION_SPAN_TIME = 50;//动画间隔时间


    private int mWidth;// 宽度
    private int mHeight; // 高度
    private PointF mStartPointF;
    private PointF mEndPointF;
    private Drawable mEggDrawable = null;
    private Drawable mEggWaterDrawaable = null;


    //随机属性
    private final float mScalePeriodMaxSize; //第一阶段鸡蛋缩放的最大倍数
    private final float mScaleMinSize; //第二阶段鸡蛋砸下去之后的倍数
    private  float mRotateDegreeScale; //旋转
    private  float mRoateDegree = 0; //旋转

    private Long mAnimationStartTime = null;// 动画开始时间；

    private static final Random gRandom = new Random(System.currentTimeMillis());
    /**
     *
     * @param width
     * @param hegiht
     * @param startPosition
     * @param endPostion
     */
    public ThrowEgg(Context context, int width, int hegiht, PointF startPosition, PointF endPostion) {
        mWidth = width;
        mHeight = hegiht;
        mStartPointF = startPosition;
        mEndPointF = endPostion;
        mEggDrawable = context.getResources().getDrawable(R.drawable.smoba_icon_battle_egg_small);
        mEggWaterDrawaable = context.getResources().getDrawable(R.drawable.smoba_icon_egg_whater);
        mEggDrawable.setBounds(0,0, mEggDrawable.getIntrinsicWidth(), mEggDrawable.getIntrinsicHeight());
        mEggWaterDrawaable.setBounds(0,0, mEggWaterDrawaable.getIntrinsicWidth(), mEggWaterDrawaable.getIntrinsicHeight());

        mScalePeriodMaxSize =  3 * gRandom.nextFloat();
        mRotateDegreeScale = 5 + gRandom.nextInt(10);

        if(gRandom.nextInt() % 2 == 0){
            mRotateDegreeScale = - mRotateDegreeScale;
        }
        mRoateDegree = - mRotateDegreeScale;

        mScaleMinSize =  1;//(mScalePeriodMaxSize - gRandom.nextFloat()) / 2 ;
    }

    /**
     * 开始砸鸡蛋
     */
    public void start(){
        mAnimationStartTime = System.currentTimeMillis();
    }

    public boolean isEnd(){
        return (System.currentTimeMillis() - mAnimationStartTime)  * 1.0f  /  (PERIOD_1 + PERIOD_2 + PERIOD_3 + PERIOD_4) > 1.0f;
    }


  /*  *//**
     * 当前动画进度
     * @return   0到1之间
     *//*
    private float getCurrentProgress(){
        float progress = (System.currentTimeMillis() - mAnimationStartTime)  * 1.0f  /  (PERIOD_1 + PERIOD_2 + PERIOD_3 + PERIOD_4);
        return progress;
    }*/

    /**
     * 画鸡蛋
     * @param canvas
     */
    public void drawEgg(Canvas canvas) {
        if(isEnd()){
            return;
        }
        canvas.save();

        //计算当前位置；
        float pregress = (System.currentTimeMillis() - mAnimationStartTime)  * 1.0f  /  (PERIOD_1 + PERIOD_2 );
        long spendTime = System.currentTimeMillis() - mAnimationStartTime;
        float x = mStartPointF.x + (mEndPointF.x - mStartPointF.x) * pregress;
        float y = mStartPointF.y + (mEndPointF.y - mStartPointF.y) * pregress;
        mRoateDegree += mRotateDegreeScale;
        if(spendTime <= PERIOD_1){
            float scale = 1 +  mScalePeriodMaxSize * (spendTime * 1.0f  / PERIOD_1 );
            float dx = x - mEggDrawable.getIntrinsicWidth() * scale / 2.0f;
            float dy = y - mEggDrawable.getIntrinsicHeight()* scale / 2.0f;
            canvas.rotate(mRoateDegree,x,y);
            canvas.translate(dx,dy);
            canvas.scale(scale,scale);
            mEggDrawable.draw(canvas);
        }else if(spendTime <=  (PERIOD_1 + PERIOD_2)){
            float initSale = 1 + mScalePeriodMaxSize;
            float p =  (spendTime - PERIOD_1)  * 1.0f/ PERIOD_2;
            float scale = initSale -  (initSale - mScaleMinSize) * p;
            float dx = x - mEggDrawable.getIntrinsicWidth() * scale / 2.0f;
            float dy = y - mEggDrawable.getIntrinsicHeight() * scale / 2.0f;
            canvas.rotate(mRoateDegree,x,y);
            canvas.translate(dx,dy);
            canvas.scale(scale,scale);
            mEggDrawable.draw(canvas);
        }

        canvas.restore();

    }

    /**
     * 画蛋清
     * @param canvas
     */
    public void drawEggWater(Canvas canvas) {
        if(isEnd()){
            return;
        }
        canvas.save();

        long spendTime = System.currentTimeMillis() - mAnimationStartTime;
        if (spendTime > (PERIOD_1 + PERIOD_2)) {
            float x = mEndPointF.x;// - mStartPointF.x) * pregress;
            float y = mEndPointF.y;// + (mEndPointF.y - mStartPointF.y) * pregress;
            float initSale = 1 + mScalePeriodMaxSize / 2.5f;

           if(spendTime <=  (PERIOD_1 + PERIOD_2 + PERIOD_3)) {
               float dx = x - mEggWaterDrawaable.getIntrinsicWidth() * initSale / 2.0f;
               float dy = y - mEggWaterDrawaable.getIntrinsicHeight() * initSale / 2.0f;
               canvas.translate(dx, dy);
               canvas.scale(initSale, initSale);
               mEggWaterDrawaable.draw(canvas);
           }else {
               //放大1.2倍
               float progress =  (spendTime - PERIOD_1 - PERIOD_2 - PERIOD_3) * 1.0f/ PERIOD_4 ;
               if(progress > 1.0f){
                   progress = 1.0f;
               }
               initSale = initSale *(1 + 0.3f * progress);
               float dx = x - mEggWaterDrawaable.getIntrinsicWidth() * initSale / 2.0f;
               float dy = y - mEggWaterDrawaable.getIntrinsicHeight() * initSale / 2.0f;
               canvas.translate(dx, dy);
               canvas.scale(initSale, initSale);
               mEggWaterDrawaable.setAlpha((int)(255 * (1- progress)));
               mEggWaterDrawaable.draw(canvas);
           }
        }

        canvas.restore();

    }

}
