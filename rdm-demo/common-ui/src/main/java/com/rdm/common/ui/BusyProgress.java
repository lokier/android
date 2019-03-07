package com.rdm.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.BaseApp;

import java.lang.ref.WeakReference;

/**
 * Created by rao
 */
public class BusyProgress {

    private static final String TAG = "SmartProgress";

    public static void delayFinish(final Activity activity) {
        BaseApp.get().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        }, DELAY_DISMISS_DURING);
    }

    public static final int DELAY_DISMISS_DURING = 1600;

    private static final int SHOW = 0;
    private static final int DISMISS = 1;

    private volatile boolean lazyShow = true;

    private boolean released;

    private Context context;
    private int animationResId;

    private CommonProgressDialog progressDialog;

    private OnCancelListener onCancelListener;

    public BusyProgress(Context context) {
        this.context = context;
    }

    public void show(int msg) {
        show(context.getString(msg));
    }

    public void show(String msg) {
        int delay = lazyShow ? 400 : 0;
        lazyShow = false;
        show(msg, delay);
    }

    public void showNow(int msg) {
        showNow(context.getString(msg));
    }

    public void showNow(String msg) {
        lastShowTime = System.currentTimeMillis();
        show(msg, 0);
    }

    public void show(String msg, long delay) {
        handler.removeMessages(SHOW);
        handler.sendMessageDelayed(handler.obtainMessage(SHOW, msg), delay);
    }

    public void update(int msg) {
        update(context.getString(msg));
    }

    public void update(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.show(msg);
                } else {
                    showNow(msg);
                }
            }
        });
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void dismissNow(){
        handler.removeMessages(SHOW);
        innerDismiss();
    }

    public void dismiss() {
        handler.removeMessages(SHOW);

        long offset = System.currentTimeMillis() - lastShowTime;

        if (offset < 1000) {
            handler.sendEmptyMessageDelayed(DISMISS, Math.max(0, 1000 - offset));
        } else {
            innerDismiss();
        }
    }

    public void delayDismiss() {
        delayDismiss(DELAY_DISMISS_DURING);
    }

    public void delayDismiss(long time) {
        BaseApp.get().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, time);
    }

    public boolean isShowing() {
        return handler.hasMessages(SHOW) || (progressDialog != null && progressDialog.isShowing());
    }

    public void release() {
        if (!released) {
            released = true;

            doRelease();
        }
    }

    protected void setAnimationDrawableRes(int resId) {
        animationResId = resId;
        if (progressDialog != null) {
            progressDialog.setAnimationDrawableRes(resId);
        }
    }

    private void doRelease() {
        handler.removeMessages(SHOW);

        innerDismiss();

        context = null;
    }

    private long lastShowTime;

    private void innerDismiss() {
        lastShowTime = 0;
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (released)
                return;

            if (msg.what == SHOW) {
                if (context instanceof BaseActivity && ((BaseActivity) context).isDestroyed_()) {
                    //ILog.w(TAG, "Request show while page not visible !");
                    return;
                }

                String text = msg.obj == null ? "" : msg.obj.toString();

                if (progressDialog == null) {
                    progressDialog = CommonProgressDialog.generate(context, text, true, null, false);
                    progressDialog.setAnimationDrawableRes(animationResId);
                    progressDialog.setOnCancelListener(new InnerCancelHandler(handler, onCancelListener));
                }

                progressDialog.show();

                lastShowTime = System.currentTimeMillis();
            } else if (msg.what == DISMISS) {
                lazyShow = true;
                innerDismiss();
            }

        }
    };

    private static class InnerCancelHandler implements OnCancelListener {
        private WeakReference<Handler> mHandlerRef;
        private WeakReference<OnCancelListener> mOnCancelListenerRef;

        public InnerCancelHandler(Handler handler, OnCancelListener onCancelListener) {
            mHandlerRef = new WeakReference<Handler>(handler);
            mOnCancelListenerRef = new WeakReference<OnCancelListener>(onCancelListener);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            Handler handler = mHandlerRef.get();
            if (handler == null) {
                return;
            }
            handler.removeMessages(SHOW);
            handler.sendEmptyMessage(DISMISS);
            OnCancelListener onCancelListener = mOnCancelListenerRef.get();
            if (onCancelListener != null) {
                onCancelListener.onCancel(dialog);
            }
        }
    }

}
