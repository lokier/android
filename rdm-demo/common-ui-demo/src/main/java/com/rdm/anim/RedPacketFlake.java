package com.rdm.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.rdm.common.ui.demo.R;


/**
 * 红包的类, 移动, 移出屏幕会重新设置位置.
 * <p/>
 * Created by Lokiearo on 16/12/2.
 */
public class RedPacketFlake extends Handler {

    private static final int ICON_RED_PACKET_RESOUCE = R.drawable.icon_red_packet_rain;
    // 红包的角度
    private static final float ANGE_RANGE = 0.1f; // 角度范围
    private static final float HALF_ANGLE_RANGE = ANGE_RANGE / 2f; // 一般的角度
    private static final float HALF_PI = (float) Math.PI / 2f; // 半PI
    private static final float ANGLE_SEED = 25f; // 角度随机种子
    private static final float ANGLE_DIVISOR = 10000f; // 角度的分母

    // 红包的移动速度
    private static final float INCREMENT_LOWER = 27f;
    private static final float INCREMENT_UPPER = 33F;


    private final RandomGenerator mRandom; // 随机控制器
    //private  Point mPosition; // 红包位置
    public float mPosX = 0;
    public float mPoxY = 0;
    private float mAngle; // 角度
    private final float mIncrement; // 红包的速度
   // private final Paint mPaint; // 画笔

    private  RectF mBound = new RectF();
    private ImageView mImageView;

    private AnimationDrawable mDrawableBao; //点击红包后“爆”的动作
    private boolean isStop = false;

    private RedPacketFlake(Context context, RandomGenerator random, Point position, float angle, float increment) {
        super(Looper.getMainLooper());
        mRandom = random;
        mIncrement = increment;
        mAngle = angle;
        mImageView = new ImageView(context);
        mImageView.setImageResource(ICON_RED_PACKET_RESOUCE);
        mImageView.measure(0,0);
        mImageView.layout(0,0,mImageView.getMeasuredWidth(),mImageView.getMeasuredHeight());
        Drawable drawable = mImageView.getDrawable();
        mBound.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        setPosition(position.x, position.y);
    }


    public static RedPacketFlake create(Context context,int width, int height) {
        RandomGenerator random = new RandomGenerator();
        int x = 50 + random.getRandom(width - 100);
        int y = 0;
        Point position = new Point(x, y);
        float angle = random.getRandom(ANGLE_SEED) / ANGLE_SEED * ANGE_RANGE + HALF_PI - HALF_ANGLE_RANGE;
        float increment = random.getRandom(INCREMENT_LOWER, INCREMENT_UPPER);
       // float flakeSize = random.getRandom(FLAKE_SIZE_LOWER, FLAKE_SIZE_UPPER);
        return new RedPacketFlake(context, random, position, angle, increment);
    }


    public void setPosition(float x, float y) {
     //   mPosition = position;
        mPosX = x;
        mPoxY = y;

        float left = x - mBound.width() / 2f;
        float top =  y - mBound.height() / 2;
        mBound.set(left, top, left + mBound.width(), top + mBound.height());
    }

    public void setStop(boolean isStop){
        this.isStop = isStop;
    }

    public ImageView getImageView(){
        return mImageView;
    }

    private float mSx = 1f,mSy = 1f;

    public void setScale(float sx,float sy) {
        //mSx
        mSx = sx;
        mSy = sy;
    }
    Matrix matrix = new Matrix();

    // 绘制红包
    public void draw(Canvas canvas) {
        float left = mPosX - mBound.width() * mSx / 2f;
        float top = mPoxY - mBound.height()* mSy / 2;
        canvas.save();

        matrix.setScale(mSx, mSy);
        matrix.postTranslate(left, top);
        canvas.setMatrix(matrix);

        mImageView.draw(canvas);


        canvas.restore();

    }



    // 移动红包
    public void move() {
        if(isStop){
            return;
        }
        double x = mPosX + (mIncrement * Math.cos(mAngle));
        double y = mPoxY + (mIncrement * Math.sin(mAngle));

        mAngle += mRandom.getRandom(-ANGLE_SEED, ANGLE_SEED) / ANGLE_DIVISOR; // 随机晃动

        setPosition((float)x, (float)y);
    }

    public boolean contains(int x,int y){
       // RectF rect = new RectF(mPosition.x - mFlakeSize,mPosition.y - mFlakeSize,mPosition.x + mFlakeSize, mPosition.y + mFlakeSize );
        return mBound.contains(x,y);
    }


    // 判断是否在其中
    public boolean isInside(int width, int height) {
        float x = mPosX;
        float y =  mPoxY;

        if(y < 50){
            //还没开始飘落
            return true;
        }

        float w = mBound.width() / 2f;
        float h = mBound.height() / 2f;

        return x >= -w - 1 && x - w < width /*&& y >= - h - 1*/ && y - h < height;
    }



    /**
     * 执行爆的动作；
     * @return
     */
    public void doBaoAnimation(Context context) {
        if(mDrawableBao != null){
            return;
        }
        mImageView.setImageResource(R.drawable.icon_red_pacaket_bao);
        mDrawableBao = (AnimationDrawable) mImageView.getDrawable();
        mDrawableBao.start();

    }

   /* public void onClick(Context context) {



    }*/

    // 销毁
    public void destroy(){
        mImageView = null;
        if(mDrawableBao != null){
            mDrawableBao.stop();
        }
        mDrawableBao = null;
        removeCallbacksAndMessages(null);
    }

    public boolean isStop() {
        return isStop;
    }
}
