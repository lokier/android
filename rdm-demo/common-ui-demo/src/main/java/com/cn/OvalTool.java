package com.cn;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageButton;

import java.util.List;

@SuppressWarnings("deprecation")

public class OvalTool {

	public static final int MAX_SIZE = 14;

	AbsoluteLayout absolutelayout;//椭圆布局
	double dushu=0;
	int imageIndex=0;//图片下标
	List<ImageButton> imageresourseList;//图片数组
	Activity context;
	Handler handler;
	int[] imageID;
	int setImageID = 0x7f022000;
	public OvalTool(Context context,List<ImageButton> imageresourseList){
		this.context = (Activity) context;
		absolutelayout = new AbsoluteLayout(context);
		this.imageresourseList = imageresourseList;
		imageID = new int[imageresourseList.size()];
		for(int i=0;i<imageID.length;i++){
			imageresourseList.get(i).setId(setImageID+i);
		}
		initOnclicListener();
	}

	//初始化旋转View
	public View initAbsoluteLayout(){
		xianshi();//调用加载方法
		View absolutelayoutView = (View)absolutelayout;
		return absolutelayoutView;

	}


	public void xianshi() {

		absolutelayout.removeAllViews();//清空absolutelayout中的所有子元素
		int imagewidth = Constant.IMAGE_WHITH;
		int imageHeight = Constant.IMAGE_HEIGHT;

		double minSizePercent = Constant.IMAGE_SCALE;
		int maxSzie = MAX_SIZE;

		int offset = 0;
		//if(imageresourseList.size() <= 5){
			offset = imageresourseList.size() / 2;
		//}

		for (int i = 0; i < imageresourseList.size(); i++) {

			double iconRadian = Math.PI * 2 / (double) maxSzie * (i -offset) + (double) dushu / 360 * 2 * Math.PI;

			while (iconRadian >= Math.PI * 2) {
				iconRadian -= (Math.PI * 2);
			}
			while (iconRadian < 0) {
				iconRadian += (Math.PI * 2);
			}
			double iconSizePercent = Math.abs(iconRadian - Math.PI) / Math.PI * (1 - minSizePercent) + minSizePercent;
			int iconX = (int) (Constant.OVAL_WHITH * Math.sin(iconRadian));
			int iconY = (int) (Constant.OVAL_HEIGHT * Math.cos(iconRadian));
			//Color.BLACK

			AbsoluteLayout.LayoutParams params = new LayoutParams((int) (imagewidth * iconSizePercent), (int) (imageHeight * iconSizePercent), Constant.SCREEN_CORE_X + (int) iconX, Constant.SCREEN_CORE_Y + (int) iconY);
			View view = (View) imageresourseList.get(i);
			///view.getBackground().setAlpha();

			absolutelayout.addView(view, params);
			if ((180 / Math.PI * iconRadian >= 0 && 180 / Math.PI * iconRadian < 90) || (180 / Math.PI * iconRadian >= 270 && 180 / Math.PI * iconRadian < 360)) {
				imageresourseList.get(i).setClickable(true);
			} else imageresourseList.get(i).setClickable(false);
		}
		Log.i("System", "被执行");
	}
	public void move(float moveRate){
		dushu -= moveRate;
		if(dushu<=0){
			dushu += 360;
		}
	}

	public double getDushu(){
		return dushu;
	}

	public void setDushu(double dushu){
		this.dushu = dushu;
		this.dushu += 360;
	}

	public void initOnclicListener() {
		for (int i = 0; i < imageresourseList.size(); i++) {

			imageresourseList.get(i).setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					//Log.i("System", "--------bbbbb"+v.getId());
					if(v.getId() == imageID[0]){
						//Toast.makeText(context, "---------"+imageID[0], 1).show();
						//Log.i("System", "---------------");
						//Intent intent = new Intent(context,Notice_First.class);
						//context.startActivity(intent);
						//context.finish();
					}else if(v.getId() ==imageID[1]){
						//Toast.makeText(context, "---------"+imageID[1], 1).show();
						//Log.i("System", "---------------");
						//Intent intent = new Intent(context,Game.class);
						//context.startActivity(intent);
						//context.finish();
					}
					else if(v.getId() ==imageID[2]){
						//Toast.makeText(context, "---------"+imageID[2], 1).show();
						Log.i("System", "---------------");
						///Intent intent = new Intent(context,Video.class);
						//context.startActivity(intent);
						//context.finish();
					}
					else if(v.getId() ==imageID[3]){
						//Toast.makeText(context, "---------"+imageID[3], 1).show();
						//Log.i("System", "---------------");
						//Intent intent = new Intent(context,Home.class);
						//context.startActivity(intent);
						//context.finish();
					}
					else if(v.getId() ==imageID[4]){
						//Toast.makeText(context, "---------"+imageID[4], 1).show();
						//Log.i("System", "---------------");
						//Intent intent = new Intent(context,Home.class);
						//context.startActivity(intent);
						//context.finish();
					}
					if(v.getId() ==imageID[5]){
						// Toast.makeText(context, "---------"+imageID[5], 1).show();
						// Log.i("System", "---------------");
						//Intent intent = new Intent(context,Sale.class);
						//context.startActivity(intent);
						//context.finish();
					}
				}
			});
		}
	}
}
