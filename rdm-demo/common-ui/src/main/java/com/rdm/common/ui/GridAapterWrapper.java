package com.rdm.common.ui;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 *
 * Created by lokierao on 2015/10/19.
 */
public class GridAapterWrapper extends BaseAdapter {

    private final ListAdapter mListAdapter;
    private int mCloumn = 2;
    private int mColumnPadding = 0;
    private int marginLeft = 0;
    private int marginRight = 0;

    public GridAapterWrapper(ListAdapter adapter){
      this.mListAdapter = adapter;
      mListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                GridAapterWrapper.this.notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                GridAapterWrapper.this.notifyDataSetInvalidated();
            }
        });
    }

    public void setColumn(int cloumn){
        if(cloumn!= mCloumn){
            mCloumn = cloumn;
            notifyDataSetChanged();
        }
    }

    public void setMargin(int left, int right){
        if(left == marginLeft && right == marginRight){
            return;
        }
        marginLeft = left;
        marginRight = right;
        notifyDataSetChanged();
    }

    public void setColumnPadding(int piexl){
        if(mColumnPadding != piexl) {
            mColumnPadding  = piexl;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        int count = mListAdapter.getCount();
        return count / mCloumn + ((count % mCloumn > 0) ? 1 : 0);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        int start = position * mCloumn;
        int end = start + mCloumn;
        int totalCouont = mListAdapter.getCount();
        if (convertView != null) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if(holder.column == mCloumn && holder.columnPadding == mColumnPadding && holder.marginLeft == marginLeft && holder.marginRight == marginRight){

                for(int i = start ;i < end ;i++){

                    int childIndex = i - start;
                    View childView = holder.container.getChildAt(childIndex);

                    View cView = mListAdapter.getView(i,childView,holder.container);
                    if (cView != childView) {
                        holder.container.removeViewAt(childIndex);
                        cView.setLayoutParams(buildLayoutParams(childIndex, mCloumn));
                        holder.container.addView(cView,childIndex);
                    }

                    if( i < totalCouont){
                        cView.setVisibility(View.VISIBLE);
                    }else{
                        cView.setVisibility(View.INVISIBLE);
                    }


                }

                return holder.container;
            }
        }
        ViewHolder holder = new ViewHolder();

        holder.container = new LinearLayout(viewGroup.getContext());
       // AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //params.se
        holder.column = mCloumn;
        holder.columnPadding = mColumnPadding;
        holder.container.setTag(holder);


        for(int i = start ;i < end ;i++){
            int childIndex = i - start;
            View cView = mListAdapter.getView(i,null,holder.container);
            if(cView == null) {
                cView = new View(viewGroup.getContext());
            }
            cView.setLayoutParams(buildLayoutParams(childIndex, mCloumn));
            holder.container.addView(cView,childIndex);
            if( i < totalCouont){
                cView.setVisibility(View.VISIBLE);
            }else{
                cView.setVisibility(View.INVISIBLE);
            }
        }


        return holder.container;
    }

    private LinearLayout.LayoutParams buildLayoutParams(int column, int columnSize){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        boolean isFirstColumn = column == 0;
        boolean isLarstColumn = column == columnSize - 1;
        int leftMargin = isFirstColumn ? marginLeft : mColumnPadding;
        int rightMargin = isLarstColumn ? marginRight : 0;
        params.setMargins(leftMargin,0,rightMargin,0);
        return params;
    }

    private static class ViewHolder{
        int column = 0;
        int columnPadding = 0;
        int marginLeft = 0;
        int marginRight = 0;
        LinearLayout container = null;
    }
}
