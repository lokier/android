package com.cn;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.rdm.base.app.BaseApp;
import com.rdm.common.ILog;
import com.rdm.common.ui.demo.R;
import com.rdm.common.util.DeviceUtils;

public class ActivityActivity extends Activity {
    /** Called when the activity is first created. */
	LinearLayout linnerlayout;
	static int[] imageResourse = {
		/*	R.drawable.home_com,
			R.drawable.home_eyes,
			R.drawable.home_game,*/
			R.drawable.home_newspaper,
			R.drawable.home_serve,
			R.drawable.home_shopping};
	ControOvalTool contraOval;

	private static final int PER_MOVE_UNIT = DeviceUtils.dip2px(BaseApp.get(),5);
	private static final int PER_MOVE_UNIT_START = DeviceUtils.dip2px(BaseApp.get(),15);

	private MoveGestureHandle gestureHandle = null;// = new MoveGestureHandle()
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oval);
    	linnerlayout = (LinearLayout) findViewById(R.id.xxxx);
		contraOval = new ControOvalTool(linnerlayout, ActivityActivity.this, imageResourse, 4);
		gestureHandle = new MoveGestureHandle(contraOval);
    }


    @Override
	public boolean onTouchEvent(MotionEvent event) {
		if(gestureHandle!= null){
			gestureHandle.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	private static class MoveGestureHandle implements GestureDetector.OnGestureListener  {
		private ControOvalTool contraOval;
		GestureDetector mygesture = new GestureDetector(this);

		public MoveGestureHandle(ControOvalTool tool) {
			contraOval = tool;
		}

		boolean canMove = false;
		public boolean onTouchEvent(MotionEvent event) {
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				canMove = false;
			}
			boolean flag = mygesture.onTouchEvent(event);
			if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
				if(canMove) {
					contraOval.resetPositon();
				}
			}
			return flag;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		private float totalDistanceX = 0;

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			hanldeMove(distanceX);
			return false;
		}


		@Override
		public void onLongPress(MotionEvent e) {

		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if(velocityX > 800) {
				canMove = false;
				float newDushu = velocityX / 200;
				contraOval.doAnimotorMove(newDushu, new Runnable() {
					@Override
					public void run() {
						contraOval.resetPositon();
					}
				});
			}
			return false;
		}

		private void hanldeMove(float distanceX){
			contraOval.move((int)distanceX);
			totalDistanceX += distanceX;
			if (canMove) {
				if(Math.abs(totalDistanceX) >= PER_MOVE_UNIT){
					float dushu = (totalDistanceX) / PER_MOVE_UNIT;
					contraOval.move(dushu);
					totalDistanceX = 0;
				}
			}else {
				if (Math.abs(totalDistanceX) >= PER_MOVE_UNIT_START) {
					float dushu = (totalDistanceX) / PER_MOVE_UNIT;
					contraOval.move(dushu);
					canMove = true;
					totalDistanceX = 0;
				}
			}
		}
	}

}