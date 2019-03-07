package com.rdm.common.base;

import android.test.ApplicationTestCase;

import com.rdm.base.BusyFeedback;
import com.rdm.base.BaseSession;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<AppForTest> {

    public ApplicationTest() {
        super(AppForTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        BaseRuntimeForTest.init(getApplication());
    }

    @Override
    protected void tearDown() throws Exception {
        terminateApplication();
        super.tearDown();
    }

    public void sleep(long t){
        try {
            Thread.sleep(t);
        }catch (Exception ex){

        }
    }

    public BaseSession getSession(){
        return BaseRuntimeForTest.mSession;
    }


    protected void waitToFinished(BusyFeedback feedback){
        waitToFinished(feedback,50L);
    }

    protected void waitToFinished(BusyFeedback feedback,long timeSpan){
        do{
            try {
                synchronized (this){
                    wait(timeSpan);
                }
            } catch (InterruptedException e) {
            }
        }while (feedback.isBusy());

       /* try {
            synchronized (this){
                wait(400);
            }
        } catch (InterruptedException e) {
        }*/
    }
}