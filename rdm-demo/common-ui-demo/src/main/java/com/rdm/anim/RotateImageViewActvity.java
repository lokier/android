package com.rdm.anim;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

import com.rdm.base.BusyStatusMonitor;
import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.EventType;
import com.rdm.common.ui.demo.R;

/**
 * Created by lokierao on 2016/12/1.
 */
public class RotateImageViewActvity extends BaseActivity {
    private RotateImageView mRainView;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        setContentView(R.layout.activity_anim_rotate);
        initView();
    }
    @Override
    public void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor) {

    }

    @Override
    protected void onLoadDataStatus(boolean loading) {

    }

    private boolean isShow;
    private void initView(){

        mRainView = (RotateImageView) findViewById(R.id.rotateImageView);

        final View button =  findViewById(R.id.button);

        button.setRotationY(-180);
        AnimationDrawable drawable =  (AnimationDrawable)button.getBackground();
        drawable.start();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url1 = "http://182.247.229.157/dlied1.qq.com/qqtalk/wzry/hero/icon/Icon/301163.PNG";
                String url2 = "http://down.qq.com/qqtalk/wzry/hero/icon/Equip/11289.png";
               if(isShow){
                    mRainView.showAndTrunOver(url1,url2);
               }else{
                   mRainView.showAndTrunOver(url2,url1);
              }
                isShow = !isShow;
            }
        });


       /* Rotatable rotatable = new Rotatable.Builder(mRainView)
                .direction(Rotatable.ROTATE_BOTH)
                .build();
        rotatable.rotate(Rotatable.ROTATE_Y, 3600, 3000);*/

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
