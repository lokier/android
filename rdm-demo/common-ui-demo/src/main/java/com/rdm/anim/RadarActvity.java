package com.rdm.anim;

import android.os.Bundle;

import com.rdm.base.BusyStatusMonitor;
import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.EventType;
import com.rdm.common.ui.demo.R;

/**
 * Created by lokierao on 2016/12/1.
 */
public class RadarActvity extends BaseActivity {
    private RippleImageView mRainView;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        setContentView(R.layout.activity_anim_radar);
        initView();
    }
    @Override
    public void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor) {

    }

    @Override
    protected void onLoadDataStatus(boolean loading) {

    }

    private void initView(){
        mRainView = (RippleImageView) findViewById(R.id.radarView);
        mRainView.startWaveAnimation();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
