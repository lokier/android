package com.rdm.common.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdm.common.Debug;
import com.rdm.common.ILog;

public final class CommonProgressDialog extends ProgressDialog
{
	private static Handler mTimeoutHandler = new Handler();
	private Context mContext;
	private ImageView mAnimationView;
	private int mAnimationResId;

    public static CommonProgressDialog generate(Context context, CharSequence message,
                                                boolean canCancel, OnCancelListener listener) {
        return generate(context, message, canCancel, listener, false);
    }

    public static CommonProgressDialog generate(Context context, CharSequence message,
                                                boolean canCancel, OnCancelListener listener, boolean sysAlert) {
        CommonProgressDialog dialog = new CommonProgressDialog(context, R.style.mmalertdialog);
        dialog.setMessage(message);
        dialog.setCancelable(canCancel);
        dialog.setOnCancelListener(listener);
        dialog.setCanceledOnTouchOutside(false);

        if (sysAlert) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        return dialog;
    }

    public static CommonProgressDialog show(Context context, CharSequence message, boolean canCancel,
                                            OnCancelListener listener) {
        CommonProgressDialog dialog = generate(context, message, canCancel, listener);
        dialog.show();
        return dialog;
    }

    public static CommonProgressDialog show(Context context, CharSequence message) {
        return show(context, message, 0);
    }

    public static CommonProgressDialog show(Context context, CharSequence message, float timeout) {
        CommonProgressDialog dialog = generate(context, message, true, null);
        dialog.show();
        if (timeout > 0) {
            dialog.setTimeOut(timeout);
        }
        return dialog;
    }

    public interface TimeoutListener {
        void onDialogTimeOut(Dialog dialog);
    }

    private CharSequence message;

    private TextView msgTv;

    private AnimationDrawable animationDrawable;

    /**
     * 监控是否超时
     */
    private TimeoutListener mTimeOutListener;

    private Runnable mTimeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (mTimeOutListener != null) {
                mTimeOutListener.onDialogTimeOut(CommonProgressDialog.this);
            }
            dismiss();
        }

    };

	private CommonProgressDialog(Context context, int theme)
	{
		super(context, theme);
		mContext = context;
		setCanceledOnTouchOutside(true);
	}

    public void setTimeOut(float timeout) {
        mTimeoutHandler.removeCallbacks(mTimeOutRunnable);
        mTimeoutHandler.postDelayed(mTimeOutRunnable, (long) (timeout * 1000));
    }

	public void setAnimationDrawableRes(int resId) {
		mAnimationResId = resId;

		if (mAnimationView != null) {
			if (resId != 0) {
				animationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(resId);
				mAnimationView.setImageDrawable(animationDrawable);
			}
		}
	}

	@Override
	public final void dismiss ()
	{
		try
		{
			super.dismiss();

            if (animationDrawable != null) {
                animationDrawable.stop();
            }

            if (mTimeoutHandler != null) {
                mTimeoutHandler.removeCallbacks(mTimeOutRunnable);
            }
        } catch (Exception e) {
            ILog.w(CommonProgressDialog.class,e.getMessage(),e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_progress);

		msgTv = (TextView) findViewById(R.id.tv_progress_msg);
		if (message != "")
		{
			msgTv.setText(message);
		}
		else
		{
			msgTv.setVisibility(View.GONE);
		}

		mAnimationView = (ImageView) findViewById(R.id.progress_anim);
		animationDrawable = (AnimationDrawable) mAnimationView.getDrawable();
		setAnimationDrawableRes(mAnimationResId);

		if (animationDrawable != null) {
			animationDrawable.start();
		}
	}

    @Override
    public void show() {
        try {
            if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
                return;
            }
            super.show();
        } catch (Throwable e) {
            if (Debug.isDebug()) {
                throw new RuntimeException(e);
            }
        }

        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    public void show(String msg) {
        if (!TextUtils.isEmpty(message)) {
            msgTv.setText(msg);
        } else {
            msgTv.setVisibility(View.GONE);
        }

        show();
    }

    @Override
    public void cancel() {
        super.cancel();

        if (animationDrawable != null) {
            animationDrawable.stop();
        }

        if (mTimeoutHandler != null) {
            mTimeoutHandler.removeCallbacks(mTimeOutRunnable);
        }
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
    }

    public void setMessage(CharSequence message) {
        this.message = message;
    }

    public void setTimeOutListener(TimeoutListener listener) {
        mTimeOutListener = listener;
    }

}