package com.rdm.anim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.rdm.common.ILog;
import com.rdm.common.ui.UIUtils;
import com.rdm.common.util.DeviceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * 红包雨视图, DELAY时间重绘, 绘制NUM_SNOWFLAKES个红包
 * <p/>
 * Created by wangchenlong on 16/1/24.
 */
public class RedPacketRainView extends View /*implements View.OnClickListener*/{

    //private static final int NUM_SNOWFLAKES = 150; // 红包数量
    private static final int DELAY = 5; // 延迟
    private LinkedList<RedPacketFlake> mSnowFlakes = new LinkedList<>(); // 红包
    private LinkedList<GiftItem> mRewarList = new LinkedList<>(); // 抽中奖的礼包列表
    //private LinkedList<RedPacketFlake> mRewarRecPacketList = new LinkedList<>(); // 抽中奖的礼包列表

    private static final long CLICK_SPACING_TIME = 300;
    private static final int MIN_MOVE_SZIE_DP = 5;

   // private static final int MIN_SIZE = 7; //飘落的最少个数
    private static final int MAX_SIZE = 5; //飘落的最大个数
    private static final long MIN_ADD_SPACE_TIME = 350; //每个飘落的间隔的最少间隔时间


    private static final int FRAME_SIZE = 28;
    private static final int FRAME_DUACTION = 33;

    private static final int BAG_POSIDTION_MARGIN_RIGHT = 25;
    private static final int BAG_POSIDTION_MARGIN_BOTTOM = 40;

    public static interface OnListener{

        /**
         *
         * @param flake
         * @param rawX  相对于屏幕左上角x值
         * @param rawY 相对于屏幕左上角y值
         */
        void onClick(RedPacketFlake flake, int rawX, int rawY);

        /**
         * 点击完之后，爆的点击动画也完成。
         * @param flake
         */
        void onBaoFinish(RedPacketFlake flake);

        /**
         * 开始飘落
         * @param flake
         */
        void onBegin(RedPacketFlake flake);

        /**
         * 结束飘落
         * @param flake
         */
        void onEnd(RedPacketFlake flake);
    }

    private static class GiftItem{
        public String name;
        public int recouseId;
    }


    /**
     * 触摸点按下时的相对于屏幕的坐标
     */
    private int mDownInScreenX;
    private int mDownInScreenY;

    /**
     * 点击次数
     */
    private int mClickCount = 0;
    private int minMoveSize = 0;

    /**
     * 当前点击时间
     */
    private long mCurrentClickTime;
    private OnListener onListener;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int mWidth  = 0;
    private int mHeight = 0;

    private boolean mRun = false;

    private RandomGenerator mRandomGenerator = new RandomGenerator();

    public RedPacketRainView(Context context) {
        super(context);
        intView(context);
    }

    public RedPacketRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intView(context);
    }

    public RedPacketRainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView(context);

    }

    private void intView(Context context){
        minMoveSize = DeviceUtils.dip2px(context, MIN_MOVE_SZIE_DP);
    }

    public OnListener getOnListener() {
        return onListener;
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    /**
     * 处理点击事件。
     * @param rawX
     * @param rawY
     * @param x
     * @param y
     */
    private void doClick(int rawX,int rawY, int x, int y) {

        //x,y稍微

        for (RedPacketFlake flake : mSnowFlakes) {
            if( !flake.isStop() && flake.contains(x,y)){
                if(onListener!= null){
                    onListener.onClick(flake,rawX,rawY);
                }

                flake.setStop(true);
                flake.doBaoAnimation(getContext());

                final RedPacketFlake item = flake;
                flake.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean needRemove =  onBaoFinish(item);
                        if(needRemove){
                            removeItem(item);
                        }

                        if(onListener!= null){
                            //爆的的动作完成
                            onListener.onBaoFinish(item);
                        }
                        //UIUtils.showToast(getContext(),"暴击完成",false);
                    }


                }, FRAME_DUACTION * FRAME_SIZE - 400);

                return;
            }
        }

    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            initSnow(w, h);
        }
    }

    public void setRun(boolean isRun){
        if(isRun == mRun){
            return;
        }
        mRun = isRun;
        if (mRun) {
            mHandler.postDelayed(runnable, DELAY);
        }else{
            mHandler.removeCallbacks(runnable);
            for(RedPacketFlake flake : mSnowFlakes){
                flake.destroy();
            }
            mSnowFlakes.clear();
            mRewarList.clear();
        }
    }


    /**
     * 通知抽中奖了。
     */
    public void notifyReward(String giftName,int giftResouceId){
        GiftItem giftItem = new GiftItem();
        giftItem.name = giftName;
        giftItem.recouseId = giftResouceId;
        mRewarList.add(giftItem);
    }

    /**
     * 爆红包的动作完成
     * @param item
     */
    private boolean onBaoFinish(final RedPacketFlake item) {

        //查看是否有礼包
        GiftItem rewardGift = mRewarList.poll();
        if(rewardGift == null){
            //没有礼品获得
            return true;
        }
        final float dx = getWidth() - DeviceUtils.dip2px(getContext(), BAG_POSIDTION_MARGIN_RIGHT) - item.mPosX;
        final float dy = getHeight() - DeviceUtils.dip2px(getContext(), BAG_POSIDTION_MARGIN_BOTTOM) - item.mPoxY;
        final float itemX = item.mPosX;
        final float itemY = item.mPoxY;

        final ImageView imageView =  item.getImageView();
        imageView.setImageResource(rewardGift.recouseId);

        ValueAnimator animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(800);
        AccelerateInterpolator interpolator = new AccelerateInterpolator();
        animation.setInterpolator(interpolator);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                float x = itemX + dx * value;
                float y = itemY + dy * value;
                //ILog.i("xxxx", " value:"+value +", x = " + x +",y=" + y );
                item.setPosition(x,y);
                float scale = 1f - value;
                if(scale < 0.01f){
                    scale = 0.01f;
                }
                try {
                    /*Matrix matrix = new Matrix();
                    matrix.postScale(scale,scale);
                    matrix.postTranslate(dx* value, dy*value);*/
                   /* imageView.setScaleX(dx* value);
                    imageView.setScaleY(dy* value);*/
                    item.setScale(scale,scale);
                    imageView.setImageAlpha((int) ((1f - value) * 255));
                }catch (Exception ex){
                    //ignoe
                }
               // imageView.setScaleY(1f - value);
              //  imageView.setScaleY(1f - value);
                if (value >= 1.0f) {
                    removeItem(item);
                    UIUtils.showToast(getContext(), "抽中动作结束！", false);
                }
            }
        });

        animation.start();

        return false;
    }

    //private Paint mPaint;// = new Paint(Paint.ANTI_ALIAS_FLAG);

    private void initSnow(int width, int height) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        paint.setColor(Color.WHITE); // 红包
        paint.setStyle(Paint.Style.FILL); // 填充;
        mWidth = width;
        mHeight = height;
       // mPaint = paint;
    }


    private RedPacketFlake addItem(){
        RedPacketFlake snowFlake = RedPacketFlake.create(getContext(),mWidth, mHeight);
        mSnowFlakes.add(snowFlake);
        if(onListener!=null){
            onListener.onBegin(snowFlake);
        }

        return snowFlake;
    }

    private void removeItem(RedPacketFlake flake){
        boolean ok = mSnowFlakes.remove(flake);
        if (ok && onListener != null) {
            onListener.onEnd(flake);
        }
        flake.destroy();
    }


    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        ArrayList<RedPacketFlake> linkedList = new ArrayList<>(mSnowFlakes);
        Collections.reverse(linkedList);

        for (RedPacketFlake s : linkedList) {
            s.draw(canvas);
        }

        // 隔一段时间重绘一次, 动画效果
        mHandler.postDelayed(runnable, DELAY);
    }


    private long lastAddNewTime = 0;

    // 重绘线程
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
           /* while(mSnowFlakes.size() < MIN_SIZE){
                addItem();
            }*/
            if(lastAddNewTime == 0){
                addItem();
                lastAddNewTime = System.currentTimeMillis();

            }else if(System.currentTimeMillis() - lastAddNewTime > MIN_ADD_SPACE_TIME){
                //有概率的生成飘落
                if(mSnowFlakes.size() < MAX_SIZE) {
                    boolean addNew = mRandomGenerator.nextBoolean();
                    if(addNew){
                        addItem();
                        lastAddNewTime = System.currentTimeMillis();
                    }
                }
            }


            ArrayList<RedPacketFlake> list = new ArrayList<>(mSnowFlakes);
            for (RedPacketFlake s : list) {
                if (!s.isInside(mWidth, mHeight)) {
                    removeItem(s);
                }else{
                    s.move();
                }
            }
            invalidate();
        }
    };



    public boolean onTouchEvent(MotionEvent event) {
        //获取相对屏幕的坐标，即以屏幕左上角为原点

        final int x = (int)event.getX();
        final int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //记录Down下时的坐标
                mDownInScreenX = (int)event.getRawX();
                mDownInScreenY = (int)event.getRawY();
                //记录当前点击的时间
                mCurrentClickTime = System.currentTimeMillis();
                //点击次数加1
                mClickCount++;
                break;

            case MotionEvent.ACTION_MOVE: {
                int movX = (int)event.getRawX();
                int movY = (int)event.getRawY();

                if(Math.abs(movX - mDownInScreenX) > minMoveSize
                        || Math.abs(movY - mDownInScreenY) > minMoveSize) {
                    mClickCount = 0; // 只要过于移动了 就没有点击事件了
                }

                break;
            }
            default : {
                if (mClickCount == 1 && (System.currentTimeMillis() - mCurrentClickTime < CLICK_SPACING_TIME)) {
                    //点击事件
                    final int rawX = mDownInScreenX;
                    final int rawY = mDownInScreenY;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            doClick(rawX,rawY,x,y);
                        }
                    });
                    mClickCount = 0;
                }
            }

        }

        return true;
    }

}