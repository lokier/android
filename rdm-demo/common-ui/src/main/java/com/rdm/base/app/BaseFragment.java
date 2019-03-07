package com.rdm.base.app;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.rdm.base.Abandonable;
import com.rdm.base.BusyFeedback;
import com.rdm.base.BusyStatusMonitor;
import com.rdm.common.ILog;
import com.rdm.common.ui.R;

import java.util.Vector;

/**
 * Created by Rao on 2015/1/10.
 */
public abstract class BaseFragment extends Fragment implements BusyFeedback {

    protected String TAG = getClass().getSimpleName();

    private boolean isAbandon = false;
    private Refresher mRefresh = null;
    private BusyStatusMonitor mBusyStatusMonitor;

    @Override
    public boolean isBusy() {
        BusyStatusMonitor monitor = mBusyStatusMonitor;
        if(monitor!= null){
            return monitor.isBusy();
        }
        return false;
    }

    @Override
    public float getProgress() {
        return 0;
    }

    @Override
    public void cancel() {

    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public String getDescription() {
        return "Fragement busy status";
    }

    @Override
    public void abandon() {
    }

    @Override
    public boolean isAbandon() {
        return isAbandon;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ILog.i(TAG,"onCreate()");
        destroyed = false;
    }

    @Override
    public void onStart() {
        ILog.d(TAG,"onStart()");
        super.onStart();

    }


    @Override
    public void onResume() {
        ILog.d(TAG,"onResume()");
        super.onResume();

    }

    @Override
    public void onPause() {
        ILog.d(TAG,"onPause()");
        super.onPause();
    }


    @Override
    public void onStop() {
        ILog.d(TAG,"onStop()");
        super.onStop();

    }

    public void startLoadByRefresh(){
        this.startLoadData(EventType.Refresh, this, getBusyStatusMonitor());
    }

    /**
     * 刷新系统界面的兼容性问题。
     * xml需要包含以下ID
     <View android:id="@+id/layout_status_bar_id_for_system" android:layout_width="match_parent" android:layout_height="@dimen/status_bar_height"/>
     */
    public void refreshFitSystemView() {
        refreshFitSystemView(getView());
    }

        /**
         * 刷新系统界面的兼容性问题。
         */
    private void refreshFitSystemView(View view){
        if(view == null || isDestroyed_()){
            return;
        }
        //自适应手机的透明状态栏问题。
        Activity activity = getActivity();
        if(activity instanceof BaseActivity) {
            BaseActivity act = (BaseActivity) activity;
            View status_bar_id = view.findViewById(R.id.layout_status_bar_id_for_system);
            if(status_bar_id != null) {
                if (act.needFitSystemView()) {
                    status_bar_id.setVisibility(View.VISIBLE);
                } else {
                    status_bar_id.setVisibility(View.GONE);
                }
            }
        }

    }

    private Vector<Abandonable> abandonableList = new Vector<Abandonable>();

    public void abandonWhileDestroy(Abandonable abandonable){
        abandonableList.add(abandonable);
    }

    public void onDestroy() {
        ILog.i(TAG,"onDestroy()");
        destroyed = true;
        if(mBusyStatusMonitor != null) {
            mBusyStatusMonitor.abandon();
            mBusyStatusMonitor = null;
        }

        for(Abandonable abandonable : abandonableList){
            abandonable.abandon();
        }
        abandonableList.clear();
        if(mRefresh != null){
            mRefresh.unbindRefreshView();
            mRefresh = null;
        }
        super.onDestroy();

    }

    @Override
    public void onAttach(Activity context) {
        ILog.d(TAG, "onAttach():" + context);
        super.onAttach(context);

        isAbandon = false;
        if(context instanceof  BaseActivity) {
           BaseActivity baseActivity = (BaseActivity)context;

           baseActivity.getBusyStatusMonitor().addBusyFeedback(this);
       }
    }

    @Override
    public void onDetach() {
        ILog.d(TAG,"onDetach()");

        Activity act = getActivity();
        isAbandon = true;
        if(act != null && act instanceof  BaseActivity) {
            BaseActivity baseActivity = (BaseActivity)act;
            BusyStatusMonitor monitor = baseActivity.getBusyStatusMonitor();
            if(monitor!= null) {
                monitor.removeFeedback(this);
            }
        }
        super.onDetach();
    }


    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if(isDestroyed_()){
            return;
        }
        ILog.d(TAG,"onViewCreated()");
        BusyStatusMonitor monitor =  getBusyStatusMonitor();
        startLoadData(EventType.Init, this, monitor);
        monitor.refreshBusyStatus();
        if(!monitor.isBusy()){
            onLoadDataStatus(false);
        }

        if(view != null) {
            refreshFitSystemView(view);
        }
    }


    public BusyStatusMonitor getBusyStatusMonitor(){

        if(mBusyStatusMonitor == null) {
            if(isDestroyed_()){
                return null;
            }
            mBusyStatusMonitor = new BusyStatusMonitor();
            mBusyStatusMonitor.setName(getClass().getSimpleName());
            mBusyStatusMonitor.setBusyStatusListener(new BusyStatusMonitor.BusyStatusListener() {
                @Override
                public void onBusyStausChanged(boolean isBusy) {
                    if (!isBusy && mRefresh != null) {
                        mRefresh.completeAllRefresh();
                    }
                    BaseFragment.this.onLoadDataStatus(isBusy);

                }
            });
        }
        return  mBusyStatusMonitor;
    }


    public Refresher getRefresher(){
        if(mRefresh == null) {
            if(isDestroyed_()){
                return null;
            }
            mRefresh = new FragmentRefresher(this);
        }
        return mRefresh;
    }



    /**
     *
     * @param type
     * @param eventSource
     * @param monitor  用于监听耗时工作。监听的工作很重要，因为要知道什么时候工作完成，以便重置下拉刷新工作。
     */
    public abstract void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor);

    /**
     *
     * @param   loading
     */
    protected abstract void onLoadDataStatus(boolean loading);

    private boolean destroyed = false;

    public boolean isDestroyed_() {
        if (destroyed) return true;

        Activity activity = getActivity();

        if (activity == null) {
            return true;
        }

        if (activity instanceof BaseActivity) {
            return ((BaseActivity) activity).isDestroyed_();
        }

        return activity.isFinishing();
    }


    private static class FragmentRefresher extends Refresher{
        private BaseFragment fragment;
        public FragmentRefresher(BaseFragment activity){
            this.fragment = activity;
        }

        @Override
        protected void doRefresh(View eventSource) {
            BusyStatusMonitor monitor = fragment.getBusyStatusMonitor();
            fragment.startLoadData(EventType.PullRefresh, eventSource,monitor);
            monitor.refreshBusyStatus();
            if(!monitor.isBusy()){
                completeAllRefresh();
            }

        }

        @Override
        protected void doLoadMore(View eventSource) {
            BusyStatusMonitor monitor = fragment.getBusyStatusMonitor();
            monitor.refreshBusyStatus();
            fragment.startLoadData(EventType.LoadMore, eventSource,monitor);
            if(!monitor.isBusy()){
                completeAllRefresh();
            }
        }
    }

}
