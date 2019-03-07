package com.rdm.common.ui;

import android.test.ApplicationTestCase;
import com.rdm.base.BusyFeedback;
import com.rdm.base.BaseSession;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<AppTest> {

    public ApplicationTest() {
        super(AppTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        BaseTest.init(getApplication());
    }

    public void sleep(long t){
        try {
            Thread.sleep(t);
        }catch (Exception ex){

        }
    }

    @Override
    protected void tearDown() throws Exception {
        terminateApplication();
        super.tearDown();
    }

    public BaseSession getSession(){
        return BaseTest.mSession;
    }


    protected void waitToFinished(BusyFeedback feedback){
        waitToFinished(feedback,50L);
    }

    protected void waitToFinished(BusyFeedback feedback,long timeSpan){
        while(feedback.isBusy()){
            try {
                synchronized (this){
                    wait(timeSpan);
                }
            } catch (InterruptedException e) {
            }
        }
    }
}