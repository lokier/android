package com.rdm.common.base.unit;

import android.os.Bundle;

import com.rdm.base.Publisher;
import com.rdm.base.Subscriber;
import com.rdm.base.app.BaseApp;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;

import java.util.ArrayList;

/**
 * Created by Rao on 2015/1/11.
 */
public class EventPulisherTester extends ApplicationTest {

    /**
     * 测试类对象事件的处理。
     * @throws Exception
     */
    public void testObjectEvent() throws Exception {

        final Publisher publisher = BaseApp.get().getPublisher();

        TestSubcriber eventSubcriber = new TestSubcriber();

        Object event = new TestEventObject();
        Object event2 = new Object();
        Object event3 = new TestEventObject(){};

        Bundle data = new Bundle();
        data.putInt("topic",1);

        //注册事件：
        publisher.subscribe(TestEventObject.class, eventSubcriber);
        try{
            publisher.publish(null);
            Assert.fail();
        }catch(Exception ex){
        }



        publisher.publish(new TestSubcriber());
        publisher.publish(event2); //无法收听
        publisher.publish(event3);//无法收听
        publisher.publish(event);
        publisher.publish(event);


        try {
            publisher.publish(null);
            Assert.fail();
        }catch(Exception ex){

        }




        Thread.sleep(100);

        Assert.assertTrue(eventSubcriber.callTime == 3);
        Assert.assertTrue(eventSubcriber.callObject == event);



        eventSubcriber.callTime = 0;
        eventSubcriber.callObject = null;

        publisher.unsubscribe(eventSubcriber);

        publisher.publish(new TestSubcriber());

        publisher.publish(event);

        Thread.sleep(100);

        Assert.assertTrue(eventSubcriber.callTime == 0);
        Assert.assertTrue(eventSubcriber.callObject == null);




    }

    public void testUnscribeEvent()throws Exception{

        final Publisher publisher = BaseApp.get().getPublisher();

        TestSubcriber eventSubcriber1 = new TestSubcriber();
        TestSubcriber eventSubcriber2 = new TestSubcriber();

        Object event = new TestEventObject();

        //注册事件：
        publisher.subscribe(TestEventObject.class,eventSubcriber1);
        publisher.subscribe(TestEventObject.class,eventSubcriber2);
        publisher.unsubscribe(eventSubcriber1);
        publisher.unsubscribe(eventSubcriber1);
        publisher.unsubscribe(eventSubcriber1);
        publisher.unsubscribe(eventSubcriber1);


        publisher.publish(event);
        publisher.publish(event);
        publisher.publish(event);



        Thread.sleep(100);

        Assert.assertTrue(eventSubcriber1.callTime == 0);
        Assert.assertTrue(eventSubcriber1.callObject == null);

        Assert.assertTrue(eventSubcriber2.callTime == 3);
        Assert.assertTrue(eventSubcriber2.callObject == event);


        publisher.unsubscribe(eventSubcriber2);
        eventSubcriber2.callObject = null;
        publisher.publish(event);
        Thread.sleep(100);

        Assert.assertTrue(eventSubcriber2.callTime == 3);
        Assert.assertTrue(eventSubcriber2.callObject == null);


    }


    public void testUnscribeEventType()throws Exception{

        final Publisher publisher = BaseApp.get().getPublisher();

        TypeSubcriber<Object> eventSubcriber1 = new TypeSubcriber<Object>();
        TypeSubcriber<String> eventSubcriber2 = new TypeSubcriber<String>();


        //注册事件：
        publisher.subscribe(String.class,eventSubcriber2);
        publisher.subscribe(Object.class,eventSubcriber1);


        publisher.publish(new String());
        publisher.publish(new ArrayList());
        publisher.publish(new String());
        publisher.publish(new Cloneable() {
            @Override
            protected Object clone() throws CloneNotSupportedException {
                return super.clone();
            }
        });
        publisher.publish(new String());



        Thread.sleep(100);

        Assert.assertTrue(eventSubcriber1.callTime == 5);
        Assert.assertTrue(eventSubcriber1.callObject.getClass() == String.class);

        Assert.assertTrue(eventSubcriber2.callTime == 3);
        Assert.assertTrue(eventSubcriber2.callObject.getClass() == String.class);


        publisher.unsubscribe(eventSubcriber2);
        publisher.unsubscribe(eventSubcriber1);



    }





    public static class TestEventObject {


    }

    public static class TypeSubcriber<T> implements Subscriber<T> {

        int callTime = 0;

        T callObject = null;

        @Override
        public void onEvent(T event) {
            callTime++;
            callObject = event;
        }
    }

    public static class TestSubcriber implements Subscriber<TestEventObject> {

        int callTime = 0;

        TestEventObject callObject = null;

        @Override
        public void onEvent(TestEventObject event) {
            callTime++;
            callObject = event;
        }
    }

}
