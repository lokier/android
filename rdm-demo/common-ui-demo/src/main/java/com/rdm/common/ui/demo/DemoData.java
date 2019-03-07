package com.rdm.common.ui.demo;

import com.cn.ActivityActivity;
import com.handmark.pulltorefresh.samples.PulltoRefreshDemo;
import com.manuelpeinado.quickreturnheader.demo.ListViewSampleActivity;
import com.manuelpeinado.quickreturnheader.demo.ScrollViewSampleActivity;
import com.rdm.anim.RadarActvity;
import com.rdm.anim.RedPacketRainActvity;
import com.rdm.anim.RotateImageViewActvity;
import com.rdm.anim.ThrowEggActvity;
import com.rdm.common.ui.demo.activity.JazzViewPagerDemo;
import com.rdm.common.ui.demo.activity.RoundedImageViewDemo;
import com.rdm.common.ui.demo.activity.XListViewDemo;
import com.rdm.common.ui.demo.base.BaseActivityDemo;
import com.rdm.common.ui.demo.base.TipsViewDemo;
import com.rdm.common.ui.demo.view.AbilityDetaileViewDemo;
import com.viewpagerindicator.sample.ViewPageIndicatorDemo;

import org.anjoy.gallery.GalleryDemosActivity;
import org.anjoy.gallery.RotateViewTestActivity;

import java.util.HashMap;

import me.grantland.autofittextview.sample.AutoFitTextViewDemo;

/**
 * Created by Rao on 2015/10/17.
 */
public class DemoData {

    private static HashMap<String,Object> gData;


    public static  HashMap<String,Object> getDemoData(){

        if(gData == null){
            gData = create();
        }

        return gData;
    }

    private static HashMap<String,Object> create(){

        HashMap<String,Object> mainList = new HashMap<String,Object>();

        {
            addDemoActivityClss(mainList,JazzViewPagerDemo.class);
            addDemoActivityClss(mainList,ViewPageIndicatorDemo.class);
            addDemoActivityClss(mainList,PulltoRefreshDemo.class);
            addDemoActivityClss(mainList,RoundedImageViewDemo.class);
            addDemoActivityClss(mainList,AutoFitTextViewDemo.class);
            addDemoActivityClss(mainList,XListViewDemo.class);
            addDemoActivityClss(mainList,ListViewSampleActivity.class);
            addDemoActivityClss(mainList,ScrollViewSampleActivity.class);
        }
        {

            HashMap<String,Object> baseViewDemoList = new HashMap<String,Object>();
            mainList.put("Common UI控件列表",baseViewDemoList);

            addDemoActivityClss(baseViewDemoList, BaseActivityDemo.class);
            addDemoActivityClss(baseViewDemoList, TipsViewDemo.class);

        }

        {
            HashMap<String,Object> demolIst = new HashMap<String,Object>();

            mainList.put("动画效果",demolIst);

            addDemoActivityClss(demolIst,RedPacketRainActvity.class);
            addDemoActivityClss(demolIst,ThrowEggActvity.class);
            addDemoActivityClss(demolIst,RadarActvity.class);
            addDemoActivityClss(demolIst,RotateImageViewActvity.class);

            addDemoActivityClss(demolIst,RotateViewTestActivity.class);
            addDemoActivityClss(demolIst,GalleryDemosActivity.class);
            addDemoActivityClss(demolIst,ActivityActivity.class);

        }

        {
            HashMap<String,Object> demolIst = new HashMap<String,Object>();

            mainList.put("自定义View",demolIst);
            addDemoActivityClss(demolIst, AbilityDetaileViewDemo.class);

        }


        return mainList;
    }

    private static void addDemoActivityClss(HashMap<String,Object> data, Class cls){
        data.put(cls.getSimpleName(),cls);
    }

}
