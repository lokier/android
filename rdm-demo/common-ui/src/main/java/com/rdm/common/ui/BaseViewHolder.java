package com.rdm.common.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.rdm.base.view.ViewUtils;
import com.rdm.base.view.annotation.ContentView;

/**
 *
 *
 *
 * Created by lokierao on 2015/1/22.
 */
public abstract class BaseViewHolder {
    private View mContentView;

    public BaseViewHolder() {
        this(null);
    }

    /**
     * @param context 默认的为null，有些特殊场景是需要Activity类型的context。
     */
    protected BaseViewHolder(Context context) {
        Class<?> handlerType = this.getClass();

        ContentView contentView = handlerType.getAnnotation(ContentView.class);
        if (contentView == null) {
            throw new IllegalStateException("could not find ContentView annoation in :" + handlerType.getCanonicalName());
        }

        int layoutId = contentView.value();
        if (context == null) {
            mContentView = UIUtils.getLayoutInflater().inflate(layoutId, null, false);
        } else {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mContentView = layoutInflater.inflate(layoutId, null, false);
        }
        ViewUtils.inject(this,mContentView);
    }

    public View getContentView() {
        // 只用于获取一次  之后 ViewHolder不持有ContentView的引用
        View contentView = mContentView;
        mContentView = null;
        return contentView;
    }

}