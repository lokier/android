package com.rdm.common.base.unit;

import android.os.Handler;
import android.os.Looper;

import com.rdm.base.BaseSession;
import com.rdm.base.BusyFeedback;
import com.rdm.base.protocol.CacheProtocol;
import com.rdm.base.protocol.Protocol;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;

import java.util.Random;

/**
 * Created by lokierao on 2016/5/20.
 */
public class ProtocalTester extends ApplicationTest {

    public void testBasic() {
        ProtocalForTest protocol = new ProtocalForTest();
        final CallbackData data = new CallbackData();
        BusyFeedback feedback = protocol.send(new Param(), new Protocol.Callback<Result>() {
            @Override
            public void onResult(Result result) {
                data.result = result;
            }
        });

        waitToFinished(feedback);
        Assert.assertTrue(data.result.succeed == true);

        Result result = (Result)data.result;
        Assert.assertTrue(result.msg != null);


        final CallbackData data2 = new CallbackData();
        feedback = protocol.send(Protocol.SendType.LOCAL_AND_NETWORK,new Param(), new Protocol.Callback2<Result>() {
            @Override
            public void onResult(boolean fromLocal,Result result) {
                if(data2.result == null) {
                    data2.result = result;
                    data2.fromLocal = fromLocal;
                }else if(data2.result2 == null){
                    data2.result2 = result;
                    data2.fromLocal2 = fromLocal;
                }else{
                    throw new RuntimeException();
                }
            }
        });

        waitToFinished(feedback);
        Assert.assertTrue(data2.fromLocal == true);
        Assert.assertTrue(data2.fromLocal2 == false);
        Assert.assertTrue(data2.result.succeed == false);
        Assert.assertTrue(data2.result2.succeed == true);


        final CallbackData data3 = new CallbackData();
        feedback = protocol.send(Protocol.SendType.LOCAL_FIRST,new Param(), new Protocol.Callback2<Result>() {
            @Override
            public void onResult(boolean fromLocal,Result result) {
                if(data3.result == null) {
                    data3.result = result;
                    data3.fromLocal = fromLocal;
                }else if(data3.result2 == null){
                    data3.result2 = result;
                    data3.fromLocal2 = fromLocal;
                }else{
                    throw new RuntimeException();
                }
            }
        });

        waitToFinished(feedback);
        Assert.assertTrue(data3.result.succeed == true);
        Assert.assertTrue(data3.fromLocal == false);
        Assert.assertTrue(data3.result2 == null);

        // Assert.assertTrue(data.fromLocal2 == true);

    }

   /* public void testBasieeec(){
        Random r = new Random(System.currentTimeMillis());
        Param param = new Param();
        param.index = r.nextInt(100000);

        int index = param.index;

        CacheProtocol<Param,Result> protolcal = new ProtocalForCache(getSession());
        protolcal.clearLocal(param);
        Result result = protolcal.readFromLocal(param);
        Assert.assertTrue(result == null);


        Result saveResult = new Result();
        saveResult.succeed = true;
        saveResult.msg = "wdsjdifjiw";
        saveResult.index = index;
        protolcal.saveResult(param,saveResult);

        sleep(200);

        result = protolcal.readFromLocal(param);
        Assert.assertTrue(result != null);
        Assert.assertTrue(result.succeed == true);
        Assert.assertTrue(result.index == index);

    }*/

    public void testCacheProtocol()  {

        Random r = new Random(System.currentTimeMillis());
        Param param = new Param();
        param.index = r.nextInt(100000);

        CacheProtocol<Param,Result> protolcal = new ProtocalForCache(getSession());
        protolcal.clearLocal(param);
        Result result = protolcal.readFromLocal(param);
        Assert.assertTrue(result == null);

        final CallbackData<Result> callbackDaata = new CallbackData<Result>();
        BusyFeedback feedback = protolcal.send(param, new Protocol.Callback<Result>() {
            @Override
            public void onResult(Result result) {
                callbackDaata.result = result;
            }
        });
        waitToFinished(feedback);
        sleep(100); //等待缓存成功
        result = protolcal.readFromLocal(param); //读取缓存成功。
        Assert.assertTrue(callbackDaata.result!= null);
        Assert.assertTrue(result!= callbackDaata.result);
        Assert.assertTrue(result.index == callbackDaata.result.index);
        Assert.assertTrue(callbackDaata.result.succeed == true);
        Assert.assertTrue(result.succeed == true);


        final CallbackData<Result> localFirstData = new CallbackData<Result>();
        feedback = protolcal.send(Protocol.SendType.LOCAL_FIRST,param, new Protocol.Callback2<Result>() {
            @Override
            public void onResult(boolean fromLocal,Result result) {
                if(localFirstData.result == null) {
                    localFirstData.result = result;
                    localFirstData.fromLocal = fromLocal;
                }else if(localFirstData.result2 == null){
                    localFirstData.result2 = result;
                    localFirstData.fromLocal2 = fromLocal;
                }else{
                    throw new RuntimeException();
                }
            }
        });

        waitToFinished(feedback);

        Assert.assertTrue(localFirstData.result.succeed == true);
        Assert.assertTrue(localFirstData.fromLocal == true);
        Assert.assertTrue(localFirstData.result2 == null);

    }

        public void testTimeout() {

        final long timeOut = 2000L;

        ProtocalForTest timeoutProtocal = new ProtocalForTest(){
            @Override
            public void readFromNetwork(Param param, Commiter<Result> commiter) {
                //doNothing.
            }

            protected long getCommintTimeout(){
                return timeOut;
            }
        };

       final CallbackData data = new CallbackData();
        BusyFeedback feedback = timeoutProtocal.send(new Param(), new Protocol.Callback<Result>() {
            @Override
            public void onResult(Result result) {
                data.result = result;
            }
        });

        long startTime = System.currentTimeMillis();
        waitToFinished(feedback);
        long spendTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue(spendTime >= timeOut);
        Assert.assertTrue(data.result.error == Protocol.ErrorCode.COMMIT_TIMOUT);
        Assert.assertTrue(data.result.succeed == false);


    }

    private static class CallbackData<RESULT extends Protocol.ProtocolResult> {
        RESULT result = null;
        boolean fromLocal = true;
        RESULT result2 = null;
        boolean fromLocal2 = false;


    }

    private static class Param {
        int index;

    }

    public static class Result extends Protocol.ProtocolResult {

        String msg = null;
        int index  = -1;
    }




    public static class ProtocalForTest extends Protocol<Param, Result> {

        @Override
        public String getUniqueId(Param param) {
            return "just-for-test";
        }

        protected Result createResult() {
            return new Result();
        }

            @Override
        public void readFromNetwork(Param param, final Commiter<Result> commiter) {


            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Result result = new Result();
                    result.succeed = true;
                    result.msg = "wdsjdifjiw";
                    commiter.commit(result);
                }
            },200);
        }

       @Override
        protected long getCommintTimeout(){
            return 100000L;
        }


    }


    public static class ProtocalForCache extends CacheProtocol<Param,Result> {

        public ProtocalForCache(BaseSession session) {
            super(session);
        }

        @Override
        public String getUniqueId(Param param) {
            return "just-for-test-cache";
        }

        protected Result createResult() {
            return new Result();
        }

        @Override
        public void readFromNetwork(final Param param, final Commiter<Result> commiter) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Result result = new Result();
                    result.succeed = true;
                    result.msg = "";
                    result.index = param.index;
                    commiter.commit(result);
                }
            },200);
        }
    }
}
