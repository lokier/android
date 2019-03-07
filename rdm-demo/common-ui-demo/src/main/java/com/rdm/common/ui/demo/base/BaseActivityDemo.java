package com.rdm.common.ui.demo.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rdm.base.BusyStatusMonitor;
import com.rdm.base.app.BaseActivity;
import com.rdm.base.app.EventType;
import com.rdm.base.view.ViewUtils;
import com.rdm.base.view.annotation.ViewInject;
import com.rdm.common.ui.UIUtils;
import com.rdm.common.ui.demo.R;

/**
 * Created by lokierao on 2015/10/15.
 */
public class BaseActivityDemo extends BaseActivity {

    @ViewInject(R.id.chk_overlap_footer)
    private CheckBox chk_overlap_footer;

    @ViewInject(R.id.chk_overlap_header)
    private CheckBox chk_overlap_header;

    @ViewInject(R.id.btn_show_hide)
    private CheckBox btn_show_hide;

    @Override
    protected void onCreated(Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        setContentView(R.layout.activity_base_demo);

        init();

    }

    @Override
    public void startLoadData(EventType type, Object eventSource, BusyStatusMonitor monitor) {

    }

    @Override
    protected void onLoadDataStatus(boolean loading) {

    }


    private void init(){

        TextView footerTextView = new TextView(this);
        footerTextView.setText("Footer View \n This is Footer View \n coustom view");
        footerTextView.setGravity(Gravity.CENTER);
        footerTextView.setBackgroundColor(Color.YELLOW);

        getBaseView().getTitleView().setTitle("BaseActivity 演示");
        getBaseView().setFooterView(footerTextView);


        getBaseView().getTitleView().addRightBarButton("右边按钮", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast(v.getContext(), "已点击右边按钮", false);
            }
        });
        CompoundButton.OnCheckedChangeListener checkedChangeListener =  new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doOverlapHeaderFooterAction();
            }
        };
        chk_overlap_footer.setOnCheckedChangeListener(checkedChangeListener);
        chk_overlap_header.setOnCheckedChangeListener(checkedChangeListener);

        btn_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                doHideShowHeaderView();
            }
        });
    }

    private void doHideShowHeaderView(){
        boolean hide =  btn_show_hide.isChecked();
        if(hide){
            getBaseView().hideFooterView(true);
            getBaseView().hideHeaderView(true);
        } else{
            getBaseView().showFooterView(true);
            getBaseView().showHeaderView(true);
        }
    }

    private void doOverlapHeaderFooterAction(){
        boolean overlopFooter = chk_overlap_footer.isChecked();
        boolean overlopHeader = chk_overlap_header.isChecked();
        getBaseView().setOverlapBar(overlopHeader,overlopFooter);
    }
}
