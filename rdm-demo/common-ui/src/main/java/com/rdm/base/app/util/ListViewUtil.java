package com.rdm.base.app.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Rao on 2016/5/31.
 */
public class ListViewUtil {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int itemHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            if(itemHeight < 1) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                itemHeight = listItem.getMeasuredHeight();
            }
            totalHeight += itemHeight;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
