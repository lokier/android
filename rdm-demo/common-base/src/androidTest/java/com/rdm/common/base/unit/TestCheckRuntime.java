package com.rdm.common.base.unit;

import com.rdm.base.app.BaseApp;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;

/**
 *
 * 测试基本的运行环境。
 * Created by Rao on 2015/1/11.
 */
public class TestCheckRuntime extends ApplicationTest {

    public void test(){
       // Assert.assertTrue(Looper.myLooper()== Looper.getMainLooper());
        //必须要装备Base实现。
        Assert.assertTrue(BaseApp.get().getSDkContext()!= null);

    }


}
