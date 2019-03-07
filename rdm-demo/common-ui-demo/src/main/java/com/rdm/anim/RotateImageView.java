package com.rdm.anim;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 *
 *   图片翻转效果组件
 * Created by lokierao on 2017/9/26.
 */
public class RotateImageView extends RelativeLayout /*implements View.OnClickListener*/ {


    private ImageView mImgFront;
    private ImageView mImgBack;
    private boolean turnOverIng = false;
    private String mFrontUrl = null;
    private String mBackUrl = null;


    public RotateImageView(Context context) {
        super(context);
        intView(context);
    }

    public RotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intView(context);
    }

    public RotateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intView(context);

    }

    private void intView(Context context){
        mImgFront = new ImageView(context);
        mImgBack = new ImageView(context);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mImgBack,params1);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(mImgFront,params2);

       mImgBack.setVisibility(View.INVISIBLE);
       mImgFront.setVisibility(View.VISIBLE);
    }



    /***
     *显示并翻转。（图片加载完之后才翻转）
     */
    public void showAndTrunOver(String frontUrl, String backUrl) {

        if(frontUrl.equals(mBackUrl) || backUrl.equals(mFrontUrl)){
            String tmp = frontUrl;
            frontUrl = backUrl;
            backUrl = tmp;
        }


        mHandler.removeMessages(1);
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = new String[]{frontUrl,backUrl};
        mHandler.sendMessage(msg);
    }


    private void onStartTurnover(String frontUrl, String backUrl){
        turnOverIng = true;
        mFrontUrl = frontUrl;
        mBackUrl = backUrl;
        ImageView fonrtImg ,bakcImg;
        fonrtImg = mImgFront;
        bakcImg = mImgBack;

        mImgListener.resetCount(2);
        ImageLoader.getInstance().displayImage(frontUrl, fonrtImg, mImgListener);
        ImageLoader.getInstance().displayImage(backUrl, bakcImg, mImgListener);
    }

    private void onLoadImageFinished(){
        turnOver();
    }

    private void onTurnoverFinished() {
        turnOverIng = false;
    }

    private int degreeIndex = 0;
    private static final float[] DEGREE_LIST = new float[]{-180f,0f};

    public void turnOver() {
        degreeIndex = degreeIndex % DEGREE_LIST.length;
       float degree = DEGREE_LIST[degreeIndex++];
        Rotatable rotatable = new Rotatable.Builder(this)
                .sides(mImgFront, mImgBack)
                .direction(Rotatable.ROTATE_Y)
                .rotationCount(1)
                .build();
        rotatable.setTouchEnable(false);

        rotatable.rotate(Rotatable.ROTATE_Y, degree, 1500, aLinstern);

    }



    private Handler mHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                if (turnOverIng) {
                    Message m = Message.obtain();
                    m.what = msg.what;
                    m.obj = msg.obj;
                    sendMessageDelayed(m,100);
                    //当前翻转还没完成，延迟
                    return;
                }
                String[] urls = (String[])msg.obj;
                onStartTurnover(urls[0],urls[1]);

            }
        }

    };


    private ImgListener mImgListener = new ImgListener();

    private Animator.AnimatorListener aLinstern =  new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            onTurnoverFinished();
        }



        @Override
        public void onAnimationCancel(Animator animation) {
            onTurnoverFinished();
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private class ImgListener implements ImageLoadingListener {

        public int countDown = 0;

        private void resetCount(int countdonw){
            this.countDown = countdonw;
        }


        @Override
        public void onLoadingStarted(String s, View view) {

        }

        @Override
        public void onLoadingFailed(String s, View view, FailReason failReason) {
            countDown--;
            if(countDown == 0){
                onLoadImageFinished();
            }
        }

        @Override
        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            countDown--;
            if(countDown == 0){
                onLoadImageFinished();
            }
        }

        @Override
        public void onLoadingCancelled(String s, View view) {
            countDown--;
            if(countDown == 0){
                onLoadImageFinished();
            }
        }
    }



}