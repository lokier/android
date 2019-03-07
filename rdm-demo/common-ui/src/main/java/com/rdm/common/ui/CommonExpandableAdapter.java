package com.rdm.common.ui;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Rao on 2016/5/15.
 */
public abstract class CommonExpandableAdapter<GroupViewHolder extends BaseViewHolder, ChildViewHolder extends BaseViewHolder> extends BaseExpandableListAdapter {

    private Class<GroupViewHolder> clazzOfGroupViewHolder;
    private Class<ChildViewHolder> clazzOfChildViewHolder;

    public CommonExpandableAdapter(){
        clazzOfGroupViewHolder = (Class<GroupViewHolder>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        clazzOfChildViewHolder = (Class<ChildViewHolder>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];

    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition << 32 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    protected GroupViewHolder createGroupViewHolder() {
        try {
            GroupViewHolder holder = clazzOfGroupViewHolder.newInstance();
            return holder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected ChildViewHolder creatChildViewHolder() {
        try {
            ChildViewHolder holder = clazzOfChildViewHolder.newInstance();
            return holder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    final public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder holder = null;
        if (convertView == null || !clazzOfGroupViewHolder.isInstance(convertView.getTag())) {
            holder = createGroupViewHolder();
            convertView = holder.getContentView();
            convertView.setTag(holder);
        }
        else
        {
            holder = (GroupViewHolder) convertView.getTag();
        }
        refreshGroupViewWithData(holder, groupPosition);
        return convertView;
    }


    @Override
   final public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null || !clazzOfChildViewHolder.isInstance(convertView.getTag())) {
            holder = creatChildViewHolder();
            convertView = holder.getContentView();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ChildViewHolder) convertView.getTag();
        }
        try {
            refreshChildViewWithData(holder, groupPosition,childPosition);
        }catch (ClassCastException ex){
            Log.i("xxxx",""+ clazzOfChildViewHolder.isInstance(convertView.getTag()));
            throw ex;
        }
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    protected abstract void refreshChildViewWithData(ChildViewHolder holder, int groupPosition, int childPosition);

    protected abstract void refreshGroupViewWithData(GroupViewHolder holder, int groupPosition);


}
