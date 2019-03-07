package com.rdm.base.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rdm.common.ui.R;
import com.rdm.common.util.DeviceUtils;


public class TitleButton extends RelativeLayout
{
	//private static final int IMAGE_ID = 100000;

	public enum Mode
	{
		left, right, center
	}

	ImageView mImageView;
	TextView mTextView;
	ImageView mRedView;

	public TitleButton(Context context)
	{
		this(context, null, 0);
	}

	public TitleButton(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public TitleButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mImageView = new ImageView(context);
		mImageView.setLayoutParams(params);
		mImageView.setId(R.id.imageButton_imageview_id);
		addView(mImageView);

		mRedView = new ImageView(context);
		mRedView.setBackgroundResource(R.drawable.red_point);
		params = new LayoutParams(DeviceUtils.dip2px(context, 8),DeviceUtils.dip2px(context, 8));
		params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.imageButton_imageview_id);
		params.addRule(RelativeLayout.ALIGN_TOP, R.id.imageButton_imageview_id);
		mRedView.setVisibility(View.GONE);
		addView(mRedView, params);

		params = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		// params.gravity = Gravity.CENTER;
		mTextView = new TextView(context);
		mTextView.setPadding(DeviceUtils.dip2px(context, 8), 0,
				DeviceUtils.dip2px(context, 8), 0);
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		mTextView.setLayoutParams(params);
		mTextView.setClickable(false);
		mTextView.setFocusable(false);
		mTextView.setFocusableInTouchMode(false);
		mTextView.setTextColor(context.getResources().getColor(R.color.default_title_bar_text_color));
		addView(mTextView, params);
	}

	public final void setTextColorByResId(int resId)
	{
		if (mTextView != null)
		{
			mTextView.setTextColor(getResources().getColor(resId));
		}
	}

	public final void setTextColor(int color)
	{
		if (mTextView != null)
		{
			mTextView.setTextColor(color);
		}
	}

	public final void setMode (Mode mode)
	{
		if (mode == Mode.left)
		{
			LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
			// params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
			mImageView.setLayoutParams(params);
		}
		else if (mode == Mode.center)
		{
			LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			// params.gravity = Gravity.CENTER | Gravity.CENTER_VERTICAL;
			mImageView.setLayoutParams(params);
		}
		else
		{
			LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
			// params.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
			mImageView.setLayoutParams(params);
		}
	}

	public final void setImage (int resid)
	{
		mImageView.setImageResource(resid);
		mImageView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.GONE);
	}

	public final void setImage (int resid, int bgResid)
	{
		mImageView.setImageResource(resid);
		setBackgroundResource(bgResid);
		mImageView.setVisibility(View.VISIBLE);
		mTextView.setVisibility(View.GONE);
	}
	
	public final void setImageDrawable(Drawable drawable)
	{
		mImageView.setImageDrawable(drawable);
	}

	public void setEnabled (boolean paramBoolean)
	{
		super.setEnabled(paramBoolean);
		mTextView.setEnabled(paramBoolean);
		mImageView.setEnabled(paramBoolean);
	}

	public final void setText (int resid)
	{
		mTextView.setText(resid);
		mTextView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.GONE);
	}

	public final void setText (String content)
	{
		mTextView.setText(content);
		mTextView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.GONE);
	}

	public final void setText (String content, int bgResid)
	{
		mTextView.setText(content);
		setBackgroundResource(bgResid);
		mTextView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.GONE);
	}

	public final void showRedPoint ()
	{
		mRedView.setVisibility(View.VISIBLE);
	}

	public final void hideRedPoint ()
	{
		mRedView.setVisibility(View.GONE);
	}
}