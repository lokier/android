package com.cn;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ImageButton;

public class ImageResourseList {


	static int[] imagereResourse;//图片ID

	//constructor
	public ImageResourseList(int[] imagereResourse){
		ImageResourseList.imagereResourse = imagereResourse;
	}
	//返回图片数组
	public List<ImageButton> initImageList(Context context){

		List<ImageButton> imageresourse = new ArrayList<ImageButton>();
		for(int i=0;i<imagereResourse.length;i++){
			ImageButton image = new ImageButton(context);
			image.setBackgroundResource((Integer) imagereResourse[i]);
			imageresourse.add(image);
		}
		return imageresourse;
	}
}
