package com.cn;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.rdm.common.ILog;

import java.util.ArrayList;
import java.util.List;

public class ControOvalTool {

	View absolutelayoutView;
	OvalTool ovalView;
	List<ImageButton> imageresourseList = new ArrayList<ImageButton>();
	
	Handler handler;
	//Runnable r;
	//Runnable r1;
	static int MOVE_RATE= 1;
	private LinearLayout relativerayout;
	
	
	public ControOvalTool( LinearLayout relativerayout,Context context,int[] imageResourseID,int mogeRate){
		
		ImageResourseList imageresourse = new ImageResourseList(imageResourseID);
		imageresourseList = imageresourse.initImageList(context);
        ovalView = new OvalTool(context,imageresourseList);
        absolutelayoutView = ovalView.initAbsoluteLayout();
        relativerayout.addView(absolutelayoutView);
        MOVE_RATE = mogeRate;
		this.relativerayout = relativerayout;
        handler = new Handler();

		move(0);
	}
	
	public void move(float dushu){
		dushu = dushu / 5;
		dushu = Math.min(3,dushu);
		handler.post(new MoveRun(dushu,false));
	}

	public void setDushu(float dushu){
		handler.post(new MoveRun(dushu,true));

	}

	public void resetPositon(){

		float unit = 360f / OvalTool.MAX_SIZE;

		float currentDush =(float) ovalView.getDushu();

		float extraDush = currentDush % unit;
		float moreDush = unit - extraDush;
		if(extraDush > moreDush){
			doAnimotorMove(moreDush,null);
		}else{
			doAnimotorMove(-extraDush,null);

		}



	}

	public void doAnimotorMove(float moveDushu, final Runnable finishedRunnable) {

		float currentDush =(float) ovalView.getDushu();
		float targetDush = currentDush + moveDushu;
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentDush,targetDush);
		valueAnimator.setDuration(500).start();
		valueAnimator.setInterpolator(new DecelerateInterpolator());
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float value = (Float) animation.getAnimatedValue();
				//TODO use the value
				setDushu(value);
			}
		});
		valueAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if(finishedRunnable != null){
					finishedRunnable .run();
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				if(finishedRunnable != null){
					finishedRunnable .run();
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

	}

	private class MoveRun implements Runnable{

		private float dushu;
		private boolean isSet = false;
		public MoveRun(float dushu,boolean isSet){
			this.dushu = dushu;
			this.isSet = isSet;
		}

		@Override
		public void run() {
			if(isSet){
				ovalView.setDushu(dushu);
			}else{
				ovalView.move(dushu);
			}
			relativerayout.removeView(absolutelayoutView);
			absolutelayoutView = ovalView.initAbsoluteLayout();
			relativerayout.addView(absolutelayoutView);
		}
	}

}
