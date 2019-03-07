package com.rdm.base.app;

import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.rdm.common.ui.view.XListView;

/**
 * Created by lokierao on 2015/10/16.
 */
public abstract class Refresher {

    private View refreshView;

    public void unbindRefreshView(){
        if(refreshView!= null) {
            if(refreshView instanceof  PullToRefreshBase){
                PullToRefreshBase view = (PullToRefreshBase)refreshView;
                view.setOnRefreshListener((PullToRefreshBase.OnRefreshListener2)null);
            }else if(refreshView instanceof  XListView){
                XListView view = (XListView)refreshView;
                view.setXListViewListener(null);
            }
            refreshView = null;
        }
    }

    public void bindRerfeshView( PullToRefreshBase view){
        completeAllRefresh();
        resetListner();
        if(view == null){
            return;
        }
        completeAllRefresh();
        refreshView = view;
        view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                if(refreshView.getState() == PullToRefreshBase.State.REFRESHING) {
                    doRefresh(refreshView);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                doLoadMore(refreshView);
            }
        });

    }

    public void bindRerfeshView(final XListView view){
        completeAllRefresh();
        resetListner();
        if(view == null){
            return;
        }
        refreshView = view;
        view.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                doRefresh(view);

            }

            @Override
            public void onLoadMore() {
                doLoadMore(view);
            }
        });

    }

    private void resetListner(){
        if(refreshView  == null){
            return;
        }
        if(refreshView instanceof  PullToRefreshBase){
            PullToRefreshBase view = (PullToRefreshBase)refreshView;
            PullToRefreshBase.OnRefreshListener2 ls = null;
            view.setOnRefreshListener(ls);
        }else if(refreshView instanceof  XListView) {
            XListView view = (XListView)refreshView;
            view.setXListViewListener(null);
        }
    }



    /**
     * <note>不应该自己调用</note>
     */
    public void completeAllRefresh(){
        if(refreshView  == null){
            return;
        }
        if(refreshView instanceof  PullToRefreshBase){
            PullToRefreshBase view = (PullToRefreshBase)refreshView;
            view.onRefreshComplete();
        }else if(refreshView instanceof  XListView) {
            XListView view = (XListView)refreshView;
            view.stopRefresh();
            view.stopLoadMore();
        }
    }

    protected abstract void doRefresh(View eventSource);


    protected abstract void doLoadMore(View eventSource);


}
