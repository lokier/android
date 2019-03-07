package com.rdm.base.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.rdm.base.view.ViewUtils;
import com.rdm.common.ui.R;
import com.rdm.common.ui.UIUtils;

/**
 * Created by lokierao on 2015/10/15.
 */
public class BaseActivityView extends RelativeLayout {


    private final boolean needFitSystemView;
    private BaseActivity mActivity;

    private TitleView mTitleView;

    private boolean mOverlapHeader = true;
    private boolean mOverlapFooter = true;


    public BaseActivityView(BaseActivity context, boolean needFitSysytem) {
        super(context);
        mActivity = context;
        this.needFitSystemView = needFitSysytem;
        init(context);
    }

    private  void init(Context context){

        int layoutR = getBaseActivityContentId();
        LayoutInflater.from(context).inflate(layoutR, this);
        setHeaderView(getTitleView());
        setOverlapBar(false,false);
    }

    /**
     * 设置内容区域是否被header，footer覆盖。
     * @param overlapHeader
     * @param overlapFooter
     */
    public void setOverlapBar(Boolean overlapHeader, Boolean overlapFooter){
        if(overlapHeader == null){
            overlapHeader =mOverlapHeader;
        }
        if(overlapFooter == null) {
            overlapFooter = mOverlapFooter;
        }
        if(overlapFooter!= mOverlapFooter || overlapHeader != mOverlapHeader){
            mOverlapFooter = overlapFooter;
            mOverlapHeader = overlapHeader;
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            if(!mOverlapHeader) {
                params.addRule(RelativeLayout.BELOW, R.id.base_view_header);
            }
            if(!mOverlapFooter){
                params.addRule(RelativeLayout.ABOVE, R.id.base_view_footer);
            }

            RelativeLayout group = (RelativeLayout) findViewById(R.id.base_view_content);
            group.setLayoutParams(params);

        }

    }

    public int getContentViewId(){
        return R.id.base_view_content;
    }

    private int getBaseActivityContentId() {
        if (needFitSystemView) {
            return R.layout.base_activity_view_with_fitsystem;
        }
        return R.layout.base_activity_view;

    }

        public void setContentView(View contentView) {
            ViewGroup group = (ViewGroup) findViewById(R.id.base_view_content);
            group.removeAllViews();
            group.addView(contentView);
        }

    public void setContentView(int layoutResID) {
        ViewGroup group = (ViewGroup) findViewById(R.id.base_view_content);
        group.removeAllViews();
        LayoutInflater.from(getContext()).inflate(layoutResID, group);
    }

    public void setContentView(View view, LayoutParams params) {
        ViewGroup group = (ViewGroup) findViewById(R.id.base_view_content);
        group.removeAllViews();
        group.addView(view, params);
    }


    public TitleView getTitleView(){
        if(mTitleView == null) {
            mTitleView = new TitleView(mActivity);
        }
        return mTitleView;
    }

    public void setHeaderView(View view){
        ViewGroup group = getHeaderViewContainer();
        group.removeAllViews();
        group.addView(view);
    }

    protected ViewGroup getHeaderViewContainer(){
        return (ViewGroup) findViewById(R.id.base_view_header);
    }

    protected ViewGroup getFooterViewContainer(){
        return (ViewGroup) findViewById(R.id.base_view_footer);
    }

    public void setFooterView(View view){
        ViewGroup group = getFooterViewContainer();
        group.removeAllViews();
        group.addView(view);
    }

    public void hideFooterView(boolean withAnimation) {
        final ViewGroup group = getFooterViewContainer();

        if(group.getVisibility() == View.INVISIBLE){
            return;
        }

        if (withAnimation) {
            Animation moveUp = AnimationUtils.loadAnimation(getContext(), R.anim.move_down_to_bottom);
            moveUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    group.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            group.startAnimation(moveUp);
        } else {
            group.setVisibility(View.INVISIBLE);
        }
    }

    public void showFooterView(boolean withAnimation) {
        final ViewGroup group = getFooterViewContainer();

        if(group.getVisibility() == View.VISIBLE){
            return;
        }
        if (withAnimation) {
            group.setVisibility(View.VISIBLE);
            group.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_up_from_bottom));
        } else {
            group.setVisibility(View.VISIBLE);
        }
    }


    public void hideHeaderView(boolean withAnimation) {
        final ViewGroup group = getHeaderViewContainer();

        if(group.getVisibility() == View.INVISIBLE){
            return;
        }

        if (withAnimation) {
            Animation moveUp = AnimationUtils.loadAnimation(getContext(), R.anim.move_up_top);
            moveUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    group.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            group.startAnimation(moveUp);
        } else {
            group.setVisibility(View.INVISIBLE);
        }
    }

    public void showHeaderView(boolean withAnimation) {
        final ViewGroup group = getHeaderViewContainer();

        if(group.getVisibility() == View.VISIBLE){
            return;
        }
        if (withAnimation) {
            group.setVisibility(View.VISIBLE);
            group.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.move_down));
        } else {
            group.setVisibility(View.VISIBLE);
        }
    }

}
