package com.rdm.anim;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rdm.base.BusyStatusMonitor;
import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.BaseApp;
import com.rdm.base.app.EventType;
import com.rdm.common.ui.demo.R;

/**
 * Created by lokierao on 2016/12/1.
 */
public class ThrowEggActvity extends BaseActivity {
    private ThrowEggView mRainView;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        setContentView(R.layout.activity_anim_throw_egg);
        initView();
    }
    @Override
    public void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor) {

    }

    @Override
    protected void onLoadDataStatus(boolean loading) {

    }

    private void initView(){

        mRainView = (ThrowEggView) findViewById(R.id.throwEgg);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
