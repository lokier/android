package com.rdm.common.ui.demo.view;

import android.app.Activity;
import android.os.Bundle;

import com.rdm.common.ui.demo.R;

public class AbilityDetaileViewDemo extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ability_detail_view);

        AbilityDetailView detailView = (AbilityDetailView) findViewById(R.id.detailView);


        float[] data = new float[]{9,8,15,10,13,1};

         String[] ABILITY_LABELS = new String[]{"胜率", "S8连胜", "MVP", "场次", "战斗力"};
         String[] mValueList = new String[]{"80%", "34", "56", "565", "23434"};
        detailView.setData(ABILITY_LABELS,mValueList,data);

    }
}