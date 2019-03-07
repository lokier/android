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
import com.rdm.common.ILog;
import com.rdm.common.ui.demo.R;

/**
 * Created by lokierao on 2016/12/1.
 */
public class RedPacketRainActvity extends BaseActivity {
    private RedPacketRainView mRainView;
    private ImageView img_red_packet_bag;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        setContentView(R.layout.activity_anim_snow_down);
        initView();
    }
    @Override
    public void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor) {

    }

    @Override
    protected void onLoadDataStatus(boolean loading) {

    }

    private void initView(){

        mRainView = (RedPacketRainView) findViewById(R.id.snowView);
        img_red_packet_bag = (ImageView) findViewById(R.id.img_red_packet_bag);

        mRainView.setRun(true);

        img_red_packet_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApp.get().getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        Animation animation = AnimationUtils.loadAnimation(RedPacketRainActvity.this,R.anim.red_package_bag_anim);
                        img_red_packet_bag.setAnimation(animation );
                        animation.start();
                    }
                });

            }
        });

        mRainView.setOnListener(new RedPacketRainView.OnListener() {
            @Override
            public void onClick(RedPacketFlake flake, int rawX, int rawY) {



            }

            @Override
            public void onBaoFinish(RedPacketFlake flake) {
                //抽中将了。
                mRainView.notifyReward("www",R.drawable.icon_red_packet_rain);
            }

            @Override
            public void onBegin(RedPacketFlake flake) {

            }

            @Override
            public void onEnd(RedPacketFlake flake) {

            }
        });
    }


    private void doBagAnimation(){
    }





    @Override
    protected void onDestroy() {
        mRainView.setRun(false);
        super.onDestroy();
    }

}
