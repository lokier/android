package com.rdm.common.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.BaseApp;

/**
 * Created by lokierao on 2015/1/22.
 */
public class UIUtils {

    public interface OnDialogListener {
        /**
         *
         * @param dialog
         * @param which  {@link DialogInterface#BUTTON_POSITIVE} or {@link DialogInterface#BUTTON_NEGATIVE}
         * @param inputText
         */
          void onClick(android.content.DialogInterface dialog, int which, String inputText);
    }

    private static UIDelegator delegator = new SimpleUIDelegator();

    public static void setDelegator(UIDelegator d){
        delegator = d;
    }


    public static LayoutInflater getLayoutInflater ()
    {
        return (LayoutInflater) BaseApp.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static int getScreenWidth ()
    {
        return BaseApp.get().getResources().getDisplayMetrics().widthPixels;
    }

    public static void showToast(Context context, CharSequence text, boolean longToast){
        showToastWithIcon(-1,context,text,longToast);
    }

    public static void showDumpTips(){
        showToast(BaseApp.get(),"敬请期待...",false);
    }

    public static void showNetworkErrorToast(Context context) {
        if(context == null){
            return;
        }
        showToast(context, context.getResources().getText(R.string.network_error_toast),false);
    }


    public static void showNetworkInvalidToast(Context context) {
        if(context == null){
            return;
        }
        showToast(context, "网络未连接！",false);
    }

    /**
     * 保证同一时段只有一个Toast出现。
     * @param iconId
     * @param context
     * @param text
     * @param longToast
     */
    public static void showToastWithIcon (final int iconId,final  Context context, final CharSequence text,final  boolean longToast){

      // Toast.makeText(context,text,longToast,).show();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                UIDelegator d = delegator;
                if ( d != null ){
                    d.showToastWithIcon(iconId,context,text,longToast);
                }
            }
        };
        if(Looper.getMainLooper() == Looper.myLooper()){
            run.run();
        }else{
            android.os.Handler h = new android.os.Handler(Looper.getMainLooper());
            h.post(run);
        }

    }

    public static Dialog showInputDialog(Context context, String title, String hit, String default_text,
                                         OnDialogListener listener) {
       return showInputDialog(context,title,hit,default_text,"取消","确认",listener);
    }



    public static Dialog showInputDialog(Context context,
                                         String title, String hit, String default_text,
                                         String negativeText, String positiveText, OnDialogListener listener) {
        UIDelegator d = delegator;
        if ( d != null ){
            return d.showInputDialog(context,listener,title,hit,default_text,negativeText,positiveText);
        }
        return null;
    }

    public static  Dialog showConfirmDialog(Context context, String title, String content,
                                            final DialogInterface.OnClickListener dialogOnClickListener){
        return showConfirmDialog(context,title,content,"取消","确认",dialogOnClickListener);

    }


    public static Dialog showConfirmDialog(Context context, String content) {
        return showConfirmDialog(context,null,content,null,"确认",null);

    }

    public static  Dialog showConfirmDialog(Context context, String title, String content,
                                            String negativeText, String positiveText,
                                            final DialogInterface.OnClickListener dialogOnClickListener){
        UIDelegator d = delegator;
        if ( d != null ){
            return d.showConfirmDialog(context,dialogOnClickListener,title,content,negativeText,positiveText);
        }
        return null;
    }


    public static interface UIDelegator {

        void showToastWithIcon(int iconId, Context context, CharSequence text, boolean longToast);

        Dialog showInputDialog(Context context,
                               OnDialogListener listener,
                               String title, String hit, String default_text,
                               String negativeText, String positiveText);

        Dialog showConfirmDialog(Context context,
                                 final DialogInterface.OnClickListener dialogOnClickListener,
                                 String title, String content,
                                 String negativeText, String positiveText);
    }


    public static class SimpleUIDelegator implements UIUtils.UIDelegator{

        private Toast mToast;

        @Override
        public void showToastWithIcon(int iconId, Context context, CharSequence text, boolean longToast) {
            if(mToast!= null){
                mToast.setText(text);
                mToast.setDuration(longToast ? Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
            }else{
                mToast=  Toast.makeText(context,text,longToast ? Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
            }
            mToast.show();
        }

        @Override
        public Dialog showInputDialog(final Context context,final OnDialogListener listener, String title, String hit, String default_text, String negativeText, String positiveText) {
            if(context instanceof Activity)
            {
                Activity ac = (Activity)context;
                if(ac.isFinishing())
                {
                    return null;
                }
            }

            View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_input, null, false);
            final Dialog dialog = new Dialog(context, R.style.Transparent_common_dialog);

            TextView titleTv = (TextView) view.findViewById(R.id.tv_tip_title);
            if (!TextUtils.isEmpty(title))
            {
                titleTv.setText(title);
            }
            else
            {
                titleTv.setVisibility(View.GONE);
            }

            final EditText contentTv = (EditText) view.findViewById(R.id.tv_tip_content);

            contentTv.setHint( hit == null ? "":hit);
            contentTv.setText(default_text == null ? "":default_text);



            if(contentTv.getVisibility() == View.VISIBLE)
            {
                contentTv.setFocusableInTouchMode(true);
                contentTv.requestFocus();
                contentTv.setSelection(contentTv.getText().toString().length());
            }

            Button confirmBtn = (Button) view.findViewById(R.id.bt_comfirm);
            if (positiveText != null)
            {
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setText(positiveText);
                confirmBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v)
                    {
                        if (listener != null)
                        {
                            listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE,contentTv.getText().toString());
                        }
                    }
                });
            }
            else
            {
                confirmBtn.setVisibility(View.GONE);
            }

            Button cancelBtn = (Button) view.findViewById(R.id.bt_cancel);
            if (negativeText != null)
            {
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setText(negativeText);
                cancelBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v)
                    {
                        if (listener != null)
                        {
                            listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE,contentTv.getText().toString());
                        }
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                cancelBtn.setVisibility(View.GONE);
            }

            dialog.setContentView(view, new ViewGroup.LayoutParams(getScreenWidth(), ViewGroup.LayoutParams.FILL_PARENT));

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            return dialog;
        }

        @Override
        public Dialog showConfirmDialog(final Context context,final DialogInterface.OnClickListener dialogOnClickListener, String title, String reason, String negativeText, String positiveText) {
            if(context == null){
                return null;
            }

            if(context instanceof Activity)
            {
                if(context instanceof BaseActivity) {
                    BaseActivity ac = (BaseActivity) context;
                    if(ac.isDestroyed_()){
                        return null;
                    }
                }else{
                    Activity ac = (Activity) context;
                    if (ac.isFinishing()) {
                        return null;
                    }
                }
            }
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_confirm, null, false);
            final Dialog dialog = new Dialog(context, R.style.Transparent_common_dialog);

            TextView titleTv = (TextView) view.findViewById(R.id.tv_tip_title);
            if (!TextUtils.isEmpty(title))
            {
                titleTv.setText(title);
            }
            else
            {
                titleTv.setVisibility(View.GONE);
            }

            TextView contentTv = (TextView) view.findViewById(R.id.tv_tip_content);
            if (!TextUtils.isEmpty(reason))
            {
                contentTv.setText(reason);
            }
            else
            {
                contentTv.setVisibility(View.GONE);
            }

            Button confirmBtn = (Button) view.findViewById(R.id.bt_comfirm);
            if (positiveText != null)
            {
                confirmBtn.setVisibility(View.VISIBLE);
                confirmBtn.setText(positiveText);
                confirmBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v)
                    {
                        if (dialogOnClickListener != null)
                        {
                            dialogOnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                confirmBtn.setVisibility(View.GONE);
            }

            Button cancelBtn = (Button) view.findViewById(R.id.bt_cancel);
            if (negativeText != null)
            {
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setText(negativeText);
                cancelBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View v)
                    {
                        if (dialogOnClickListener != null)
                        {
                            dialogOnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                        dialog.dismiss();
                    }
                });
            }
            else
            {
                cancelBtn.setVisibility(View.GONE);
            }

            dialog.setContentView(view, new ViewGroup.LayoutParams(getScreenWidth(), ViewGroup.LayoutParams.FILL_PARENT));

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            return dialog;
        }
    }

}
