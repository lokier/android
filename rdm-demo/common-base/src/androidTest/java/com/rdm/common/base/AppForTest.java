package com.rdm.common.base;
import com.rdm.base.SDKContext;
import com.rdm.base.app.BaseApp;

/**
 * Created by Rao on 2015/1/11.
 */
public class AppForTest extends BaseApp {

    private SDKContext runContext = null;

    public AppForTest(){
        runContext = new BaseRuntimeForTest();
    }

    public void onCreate(){
        super.onCreate();
    }

    @Override
    public SDKContext getSDkContext() {
        return runContext;
    }



}
