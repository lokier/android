package com.rdm.common.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 注入形式的ListAdpater。例子：
 *
     public class DataAdapter extends CommonAdapter<DataViewHolder, Object> {

        @Override
        public void refreshItemViewWithData(DataViewHolder holder, final Object item, int position) {
        //DO your list item
        }

        }

         @ContentView(R.layout.listitem_chat)
         public static class DataViewHolder extends BaseViewHolder {

         @ViewInject(R.id.ll_chat_friend_paopao)
         LinearLayout ll_chat_friend_paopao;
     }
 *
 * Created by lokierao on 2015/1/22.
 */
public abstract class CommonAdapter<ViewHolder extends BaseViewHolder, Data> extends BaseAdapter {

    protected List<Data> mList;

    private Class<ViewHolder> clazzOfViewHolder;

    public CommonAdapter()
    {
        clazzOfViewHolder = (Class<ViewHolder>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else
            return 0;
    }

    public List<Data> getList()
    {
        return mList;
    }

    @Override
    public Data getItem(int position) {
        if(mList == null || position < 0 || position >= mList.size()){
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected ViewHolder createViewHolder() {
        try {
            //TimeAnalysisTool.startAnalysis("reflection");
            ViewHolder holder = clazzOfViewHolder.newInstance();
            //TimeAnalysisTool.endAnalysis("reflection");
            return holder;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = createViewHolder();
            convertView = holder.getContentView();
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        refreshItemViewWithData(holder, getItem(position), position);
        return convertView;
    }

    public abstract void refreshItemViewWithData(ViewHolder holder, Data data, int position);

    public void setList(List<Data> list) {
        if(list == null){
            list = new ArrayList<Data>();
        }
        this.mList = list;
        notifyDataSetChanged();
    }

    public void appendList(List<Data> list){
        if(mList == null) {
            mList = new ArrayList<Data>();
        }
        if(list != null && list.size() > 0) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void appendList(Data[] datas){
        if(datas == null || datas.length < 1){
            return;
        }
        appendList(Arrays.asList(datas));
    }

    public void setList(Data[] list) {
        setList(Arrays.asList(list));
    }
}