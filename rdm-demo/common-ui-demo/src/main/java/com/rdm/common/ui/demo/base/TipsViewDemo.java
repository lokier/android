package com.rdm.common.ui.demo.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.rdm.base.app.view.TipView;
import com.rdm.common.ui.demo.R;

/**
 * Created by Rao on 2015/10/18.
 */
public class TipsViewDemo extends Activity {

    private boolean showBusyMode;
    // Button btn;
     TipView mTipView;

    CheckBox checkBox;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_tipsview_demo);

       // btn = (Button) findViewById(R.id.btn);
        mTipView = (TipView)  findViewById(R.id.tipView);
        checkBox = (CheckBox)  findViewById(R.id.checkBox);


        mTipView.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBusyModeContent(!showBusyMode);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTipView.showButton(checkBox.isChecked());

            }
        });
        mTipView.showButton(checkBox.isChecked());
        showBusyModeContent(true);
    }

    public void showBusyModeContent(boolean show){
        showBusyMode = show;
        mTipView.getButton().setText(show ? "show normal tips" : "show busy tips");

        if (show){
            mTipView.showLoadingTip("这是忙碌加载提示");
        }else{
            mTipView.setTip("这是noraml提示");
        }


    }
}
