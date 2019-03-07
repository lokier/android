package com.rdm.common.ui.demo;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.samples.PulltoRefreshDemo;
import com.manuelpeinado.quickreturnheader.demo.ListViewSampleActivity;
import com.manuelpeinado.quickreturnheader.demo.ScrollViewSampleActivity;
import com.rdm.common.ui.demo.activity.JazzViewPagerDemo;
import com.rdm.common.ui.demo.activity.RoundedImageViewDemo;
import com.rdm.common.ui.demo.activity.XListViewDemo;
import com.rdm.common.ui.demo.base.BaseActivityDemo;
import com.rdm.common.util.DeviceUtils;
import com.viewpagerindicator.sample.ViewPageIndicatorDemo;

import java.util.Map;

import me.grantland.autofittextview.sample.AutoFitTextViewDemo;

/**
 * Created by Rao on 2015/1/24.
 */
public class DemoActivityListAdpter extends BaseAdapter{


 /*   private static Class[] DEMO_CLASSES = new Class[]{
            JazzViewPagerDemo.class,
            ViewPageIndicatorDemo.class,
            PulltoRefreshDemo.class,
            RoundedImageViewDemo.class,
            AutoFitTextViewDemo.class,
            XListViewDemo.class,
            ListViewSampleActivity.class,
            ScrollViewSampleActivity.class,
            BaseActivityDemo.class,

    };*/

    private Activity mActivity;
    private int padding = 0;

    private Map<String,Object> mDemoData;
    private String[] mNameList = null;

    public DemoActivityListAdpter(Activity activity){
        mActivity = activity;
        padding = DeviceUtils.dip2px(mActivity,10);
    }

    public void setDemoData(Map<String,Object> data){
        mDemoData = data;
        notifyDataSetChanged();
        mNameList = data.keySet().toArray(new String[0]);
    }


    @Override
    public int getCount() {
        return mNameList.length;
    }

    @Override
    public Object getItem(int position) {
        return mDemoData.get(mNameList[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new TextView(mActivity);
        }



        TextView tv = (TextView)convertView;
        tv.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tv.setPadding(padding, padding, padding, padding);

        tv.setText(mNameList[position]);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object ob = getItem(position);
                if (ob instanceof Class) {
                    final Class cls = (Class) ob;
                    Intent intent = new Intent(mActivity, cls);
                    mActivity.startActivity(intent);
                } else if (ob instanceof Map) {
                    MainActivity.launch(mActivity,mNameList[position],(Map)ob);
                }
            }
        });
        return tv;
    }
}
