package com.rdm.base.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rdm.common.ui.R;

/**
 * Created by asherchen on 2015/9/24.
 */
public class TitleView extends RelativeLayout {

    /**
     * Left Button
     */
    protected TitleButton mLeftButton;

    /**
     * 导航栏仅仅包括高度为45dip的导航栏部分，不包含状态栏
     */
    //protected Re mNavigationBarView;


    /**
     * 公共部分的高度（4.4以上是状态栏+导航栏，4.4以下是导航栏）
     */

    protected Integer mRightButtonColor;

    private BaseActivity mActivity;
    private CharSequence title;

    public TitleView(BaseActivity activity) {
        super(activity);
        init(activity);
        mActivity = activity;
    }

    public void show(){
        if(mActivity.isDestroyed_()){
            return;
        }
        mActivity.getBaseView().showHeaderView(false);
        mActivity.getBaseView().setOverlapBar(false,null);
    }

    public void hide(){
        if(mActivity.isDestroyed_()){
            return;
        }
        mActivity.getBaseView().hideHeaderView(false);
        mActivity.getBaseView().setOverlapBar(true,null);
    }

    private void init(BaseActivity activity) {
        setId(R.id.nav_title_bar);

        inflate(activity, R.layout.common_navigation_bar, this);

        View status_bar_id = findViewById(R.id.status_bar_id);
        if(activity.needFitSystemView()){
            status_bar_id.setVisibility(View.VISIBLE);
        }else{
            status_bar_id.setVisibility(View.GONE);
        }

        //RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();

      /*  if(params == null) {
            params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        if(activity.needFitSystemView()){
            params.height = activity.getResources().getDimensionPixelSize(R.dimen.title_bar_height_with_statusbar);
        }else{
            params.height = activity.getResources().getDimensionPixelSize(R.dimen.title_bar_height);
        }*/
      //  setLayoutParams(params);

        mLeftButton = (TitleButton) findViewById(R.id.nav_left_button);
        enableBackButton();
    }

    public void enableBackButton() {
        View.OnClickListener closeClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mActivity != null) {
                    mActivity.finish();
                }
            }
        };
        setLeftBarButton(R.drawable.nav_back_normal, closeClickListener);

    }

    public void clearRightButtons(){
        ViewGroup ll =  (ViewGroup) findViewById(R.id.nav_right_buttons);
        for(int i = 0; i < ll.getChildCount(); i++){
            View view = ll.getChildAt(i);
            view.setOnClickListener(null);
        }
        ll.removeAllViews();
    }

    public void setRightButtonColor(Integer color) {
        mRightButtonColor = color;
    }


    //和Title相关的操作
    public void setTitleColor(int color) {
        TextView tv = (TextView)findViewById(R.id.nav_title);
        if (tv != null) {
            tv.setTextColor(color);
        }
    }

    public void setTitleColorByResId(int resId) {
        setTitleColor(getResources().getColor(resId));
    }


    public void setTitle(CharSequence title) {
        this.title = title;
        TextView tv = (TextView) findViewById(R.id.nav_title);
        if (tv != null)
        {
            tv.setText(title);
        }
    }


    public void setTitle(int rescouseId) {
        setTitle(getContext().getResources().getString(rescouseId));
    }

    public void setTitleContent(View view) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.nav_content_container);
        if (ll != null) {
            ll.removeAllViews();
            ll.addView(view);
        }
    }

    //和TitleBar相关的逻辑操作
/*    public void showNavigationBarOnly() {
        mNavigationBarView.setVisibility(View.VISIBLE);
    }

    public void hideNavigationBarOnly() {
        mNavigationBarView.setVisibility(View.GONE);
    }*/



    //和LeftButton相关的逻辑操作
    public void setLeftBarButton(int resId, View.OnClickListener listener) {
        if (mLeftButton != null) {
            mLeftButton.setVisibility(View.VISIBLE);
            mLeftButton.setImage(resId);
            mLeftButton.setMode(TitleButton.Mode.left);
            mLeftButton.setOnClickListener(listener);
        }
    }

    public void hideLeftButton() {
        if (mLeftButton != null) {
            mLeftButton.setVisibility(View.GONE);
        }
    }

    //和RightButton相关的逻辑操作
    public TitleButton addRightBarButton(String text, View.OnClickListener listener) {
        TitleButton ib = createRightTitleButton();

        ib.setText(text);
        ib.setMinimumHeight((int) getResources().getDimension(R.dimen.navigation_bar_min_height));
        ib.setMinimumWidth((int) getResources().getDimension(R.dimen.navigation_bar_min_width));
        ib.setOnClickListener(listener);
        if (mRightButtonColor != null) {
            ib.setTextColor(mRightButtonColor);
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.nav_right_buttons);
        ll.addView(ib);
        return ib;
    }

    private TitleButton createRightTitleButton(){
        TitleButton ib = new TitleButton(getContext());
        //默认点击背景
        ib.setBackgroundResource(R.drawable.btn_common_with_white);
        return ib;
    }

    /**
     * 文字按钮的最小尺寸是50dp*30dp
     *
     * @deprecated 统一ui规范不建议使用这个方法了
     */
    public TitleButton addRightBarButton(String text, int bkgResId, View.OnClickListener listener) {
        TitleButton ib = createRightTitleButton();
        ib.setText(text, bkgResId);
        ib.setMinimumHeight((int) getResources().getDimension(R.dimen.navigation_bar_min_height));
        ib.setMinimumWidth((int) getResources().getDimension(R.dimen.navigation_bar_min_width));
        ib.setOnClickListener(listener);
        if (mRightButtonColor != null) {
            ib.setTextColor(mRightButtonColor);
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.nav_right_buttons);
        ll.addView(ib);
        return ib;
    }

    /**
     * 文字按钮的最小尺寸是30dp*30dp
     */
    public TitleButton addRightBarButton(int bkgResId, View.OnClickListener listener) {
        TitleButton ib = createRightTitleButton();
        ib.setImage(bkgResId);
        ib.setMode(TitleButton.Mode.center);
        int padding = (int) getResources().getDimension(R.dimen.navigation_bar_padding_icon);
        ib.setPadding(padding, padding, padding, padding);
        ib.setMinimumHeight((int) getResources().getDimension(R.dimen.navigation_bar_min_height));
        ib.setMinimumWidth((int) getResources().getDimension(R.dimen.navigation_bar_min_height));
        ib.setOnClickListener(listener);
        if (mRightButtonColor != null) {
            ib.setTextColor(mRightButtonColor);
        }

        LinearLayout ll = (LinearLayout) findViewById(R.id.nav_right_buttons);
        ll.addView(ib);

        return ib;
    }

    public void addCustomViewInRight(View view) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.nav_right_buttons);
        ll.addView(view);
    }

    public CharSequence getTitle() {
        return title;
    }
}
