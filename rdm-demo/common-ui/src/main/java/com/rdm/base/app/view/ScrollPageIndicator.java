/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rdm.base.app.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;

import com.rdm.base.app.BaseApp;
import com.rdm.common.ui.R;
import com.rdm.common.util.DeviceUtils;
import com.viewpagerindicator.IcsLinearLayout;
import com.viewpagerindicator.PageIndicator;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 新特性：
 * 1、PageIndicator可以不用setViewPager();
 * 2、调用init方法定制Tab的UI；
 *
 *
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class ScrollPageIndicator<TYPE> extends HorizontalScrollView implements PageIndicator {

    public interface OnReachMostRightTabChangeListener {

        void onReachMostRightTabChange(ScrollPageIndicator tabPageIndicator, boolean reachMostRightTab);
    }

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         * @re
         */
        void onTabReselected(int position);

        /**
         *
         * @return 为ture时表示准了这次选择；
         */
        boolean beforeTabReselect(int position);
    }

    public interface OnTabViewSelectListener<T> {

        void onTabeViewSelected(View tab, int position, T data, boolean selected);
    }


    private Rect lastTabVisibleRect = new Rect();
    private Rect tabsVisibleRect = new Rect();

    private Runnable mTabSelector;

    private List<TYPE> mListData;
    private OnTabViewSelectListener<TYPE> onTabViewSelectListener = null;


    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            final int newSelected = (Integer) view.getTag();

            if(mTabReselectedListener!= null){
                boolean allowClick = mTabReselectedListener.beforeTabReselect(newSelected);
                if(!allowClick){
                    return;
                }
            }

            int oldSelected = -1;

            if(mViewPager!= null){
                oldSelected = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(newSelected, false);
            }else{
                setCurrentItem(newSelected);
            }
            if (oldSelected != newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
        }
    };

    private IcsLinearLayout mTabLayout;

    private ViewPager mViewPager;

    private OnPageChangeListener mListener;

    private int mSelectedTabIndex = 0;

    private OnTabReselectedListener mTabReselectedListener;

    private OnReachMostRightTabChangeListener onReachMostRightTabChangeListener;

    private boolean reachMostRightTab;

    public ScrollPageIndicator(Context context) {
        super(context);
        init(context);
    }

    public ScrollPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollPageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

 /*   public void setOnTabViewSelectListener(OnTabViewSelectListener<TYPE> listener){
        this.onTabViewSelectListener = listener;
    }*/

    public void setOnReachMostRightTabChangeListener(OnReachMostRightTabChangeListener listener) {
        this.onReachMostRightTabChangeListener = listener;
    }

    private void init(Context context) {
//        mContext = context;

        setHorizontalScrollBarEnabled(false);

        mTabLayout = new IcsLinearLayout(context, R.attr.vpiTabPageIndicatorStyle);
        mTabLayout.setClipChildren(false);
        mTabLayout.setClipToPadding(false);
        mTabLayout.setGravity(Gravity.CENTER);
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
//        mTabLayout.setPadding(0, 0, DeviceUtils.dip2px(context, 20), 0);
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        if (position == 0)
            return;

        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector =  new Runnable() {
            @Override
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private Integer mTabRescoureId = null;

    public void initUI(int tabRescouseId, OnTabViewSelectListener<TYPE> listener) {
        mTabRescoureId = tabRescouseId;
        onTabViewSelectListener = listener;

    }

    private void addTab(int index) {
        if ( mListData != null) {
            TYPE object =  mListData.get(index);
            View tabView = onGetTabView(mTabLayout, object);

            tabView.setFocusable(true);
            tabView.setTag(index);
            tabView.setOnClickListener(mTabClickListener);
        }
    }

    /**
     * 获取Tab的大小；
     * @return
     */
    public int getTabSize(){
        return mListData != null ? mListData.size() : 0;
    }



    private  View onGetTabView(ViewGroup tabContainer, TYPE data){

        if(mTabRescoureId == null){
            throw  new RuntimeException("you must call init() first.");
        }

        LayoutInflater.from(BaseApp.get()).inflate(mTabRescoureId.intValue(), tabContainer);
        int position = tabContainer.getChildCount() - 1;
        View tabView = tabContainer.getChildAt(position);
        if(data!= null) {
            onTabeViewSelected(tabView, position, data, false);

        }
        return tabView;
    }



    /**
     *  TabView被选中后的效果
     * @param tab
     * @param position
     * @param data
     * @param selected
     */
    private void onTabeViewSelected(View tab, int position, TYPE data, boolean selected) {
        if(onTabViewSelectListener!=null){
            onTabViewSelectListener.onTabeViewSelected(tab,position,data,selected);
        }
       /* Resources res = TApplication.getInstance().getResources();
        tab.findViewById(R.id.tab_sort_icon).setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
        TextView tvName = ((TextView)tab.findViewById(R.id.tab_sort_name));
        tvName.setTextColor(selected ? res.getColor(R.color.common_color_c40) :res.getColor(R.color.common_color_c500));
*/
    }



    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        final int count = mListData != null ? mListData.size() : 0;
        for (int i = 0; i < count; i++) {
            addTab(i);
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }


    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public int getCurrentItem() {
        return mSelectedTabIndex;
    }

    public TYPE getCurrentItemValue(){
        if(mListData!= null && mSelectedTabIndex < mListData.size()){
          return  mListData.get(mSelectedTabIndex);
        }
        return null;
    }


    public TYPE getItemValue(int index){
        if(mListData!= null && index < mListData.size()){
            return  mListData.get(index);
        }
        return null;
    }

    public int getItemSize() {
        return mTabLayout.getChildCount();
    }

    public ViewGroup getTabContainer() {
        return mTabLayout;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        mSelectedTabIndex = item;
        if(mViewPager!= null) {
            mViewPager.setCurrentItem(item);
        }
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);

            if (isSelected) {
                animateToTab(item);
            }

            TYPE data = null;
            if(mListData != null && i < mListData.size()) {
                data = mListData.get(i);
            }

            if(data!= null) {
                onTabeViewSelected(child, i, data, isSelected);
            }
        }

    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hideKeyboard(getContext());
        }
        return super.dispatchTouchEvent(event);
    }

    public static void hideKeyboard(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                if (activity.getCurrentFocus().getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    public void setListData(List<TYPE> list) {

        if(mTabRescoureId == null){
            throw  new RuntimeException("you must call init() first.");
        }

        Object selected =  null;

        if(mListData != null && mSelectedTabIndex  < mListData.size()){
            selected =  mListData.get(mSelectedTabIndex);
        }
        mListData = list;
        if(mListData != null && selected != null) {
            for (int i = 0; i < mListData.size() ;i++) {
                if(mListData.get(i).equals(selected)){
                    mSelectedTabIndex = i;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mTabLayout == null || mTabLayout.getChildCount() == 0) {
            return;
        }

        View lastView = mTabLayout.getChildAt(mTabLayout.getChildCount() - 1);
        boolean lastTabVisible = lastView.getGlobalVisibleRect(lastTabVisibleRect, null);

        getGlobalVisibleRect(tabsVisibleRect, null);

        int lastTabLabelPaddingLeft = DeviceUtils.dip2px(getContext(), 20);

        boolean reachMostRightTab = lastTabVisible && lastTabVisibleRect.left + lastTabLabelPaddingLeft <= tabsVisibleRect.right;

        if (this.reachMostRightTab != reachMostRightTab) {
            this.reachMostRightTab = reachMostRightTab;

            Handler looper = BaseApp.get().getHandler();
            looper.removeCallbacks(reachMostRightTabChangeRunnable);
            looper.postDelayed(reachMostRightTabChangeRunnable, 100);
        }
    }

    private Runnable reachMostRightTabChangeRunnable = new Runnable() {
        @Override
        public void run() {
            if (onReachMostRightTabChangeListener != null) {
                onReachMostRightTabChangeListener.onReachMostRightTabChange(ScrollPageIndicator.this, reachMostRightTab);
            }
        }
    };

}
