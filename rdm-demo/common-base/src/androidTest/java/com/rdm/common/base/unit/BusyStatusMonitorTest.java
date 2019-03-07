package com.rdm.common.base.unit;

import com.rdm.base.BusyFeedback;
import com.rdm.base.BusyStatusMonitor;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;


/**
 *
 * 测试基本的运行环境。
 * Created by Rao on 2015/1/11.
 */
public class BusyStatusMonitorTest extends ApplicationTest {

    private static byte[] DATA = new byte[]{34,45,24,65,35,65,35,3};


    public void testBasic(){

        BusyStatusMonitor monitor = new BusyStatusMonitor(30,100);

        Assert.assertTrue(!monitor.isMonitorning());
        Assert.assertTrue(!monitor.isBusy());

        BusyFeedbackImpl busyFeedback = new BusyFeedbackImpl();

        monitor.addBusyFeedback(busyFeedback);
        Assert.assertTrue(monitor.isMonitorning());
        Assert.assertTrue(!monitor.isBusy());

        busyFeedback.mIsBusy = true;
        monitor.refreshBusyStatus();
        Assert.assertTrue(monitor.isMonitorning());
        Assert.assertTrue(monitor.isBusy());

        busyFeedback.mIsBusy = false;
        sleep(62);
        Assert.assertTrue(monitor.isMonitorning());
        Assert.assertTrue(!monitor.isBusy());

        busyFeedback.mIsAndonle = true;
        sleep(210);
        Assert.assertTrue(!monitor.isMonitorning());
        Assert.assertTrue(!monitor.isBusy());

        super.waitToFinished(monitor);

    }


    public void testStatusChanged(){

        //BusyStatusMonitor对busy状态会有个缓冲时间，200ms
        long maxDelayTime = 100;
        BusyStatusMonitor monitor = new BusyStatusMonitor(maxDelayTime,200);
        MyBusyStatusListener listener = new MyBusyStatusListener();

        monitor.setBusyStatusListener(listener);

        Assert.assertTrue(!monitor.isMonitorning());
        Assert.assertTrue(!monitor.isBusy());

        BusyFeedbackImpl busyFeedback = new BusyFeedbackImpl();
        monitor.addBusyFeedback(busyFeedback);
        sleep(110);

        busyFeedback.mIsBusy = true;
        sleep(30);
        //由于没到缓冲时间，所以状态是不会变得
        Assert.assertTrue(!monitor.isBusy());
        sleep(50);
        Assert.assertTrue(!monitor.isBusy());
        busyFeedback.mIsBusy = false;
        sleep(50);

        //由于在缓冲时间内完成的状态变化，所以根本不会回调BusyStatusListener方法；
        Assert.assertTrue(!monitor.isBusy());
        Assert.assertTrue(listener.busyChangeCount == 0);



        //case 2
        busyFeedback.mIsBusy = true;

        //在[缓冲时间 - 2 * 缓冲时间]之内才知道忙碌状态的变化。
        sleep(210);
        Assert.assertTrue(monitor.isBusy());
        Assert.assertTrue(listener.busyChangeCount == 1);


        busyFeedback.mIsBusy = false;
        //之后再90ms以上在改为busy状态。
        sleep(201);

        Assert.assertTrue(!monitor.isBusy());
        Assert.assertTrue(listener.busyChangeCount == 2);

        super.waitToFinished(monitor);

    }


    private class MyBusyStatusListener implements BusyStatusMonitor.BusyStatusListener {

        private int busyChangeCount  = 0;

        @Override
        public void onBusyStausChanged(boolean isBusy) {
            busyChangeCount++;
        }
    }

    private class BusyFeedbackImpl implements BusyFeedback{

        public boolean mIsBusy = false;
        public boolean mIsAndonle = false;

        @Override
        public boolean isBusy() {
            return mIsBusy;
        }

        @Override
        public float getProgress() {
            return 0;
        }

        @Override
        public void cancel() {

        }

        @Override
        public boolean isCancellable() {
            return false;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public void abandon() {

        }

        @Override
        public boolean isAbandon() {
            return mIsAndonle;
        }
    }





}
