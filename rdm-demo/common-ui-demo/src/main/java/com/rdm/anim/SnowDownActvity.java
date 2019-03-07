package com.rdm.anim;

import android.os.Bundle;

import com.rdm.base.BusyStatusMonitor;
import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.EventType;
import com.rdm.common.ui.demo.R;

/**
 * Created by lokierao on 2016/12/1.
 */
public class SnowDownActvity extends BaseActivity {

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        setContentView(R.layout.activity_anim_snow_down);
    }


    @Override
    public void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor) {

    }

    @Override
    protected void onLoadDataStatus(boolean loading) {

    }
}
