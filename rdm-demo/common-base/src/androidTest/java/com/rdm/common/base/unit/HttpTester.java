package com.rdm.common.base.unit;

import android.os.Looper;

import com.rdm.base.BusyFeedback;
import com.rdm.base.network.ResultData;
import com.rdm.base.network.CacheManager;
import com.rdm.base.network.DefaultHttpPull;
import com.rdm.base.network.HttpPull;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lokierao on 2015/1/13.
 */
public class HttpTester  extends ApplicationTest {

    private void clearCache() throws IOException{
        CacheManager.getDefault(getSession()).clear();
    }

    @Override
    protected void waitToFinished(BusyFeedback feedback){
       super.waitToFinished(feedback);
        try {
            synchronized (this){
                wait(400);
            }
        } catch (InterruptedException e) {
        }
    }
    public void testEnableCache()throws Exception {

        //清理缓存
        clearCache();

        HttpPull http = HttpPull.Factory.create(getSession());
        String url = "http://www.baidu.com";

        RC_OK_NO_CACHE<String> resultCallback = new RC_OK_NO_CACHE<String>();
        //只拉取缓存数据
        resultCallback.isSuccess = true;
        BusyFeedback feedback = http.pullText(HttpPull.PullType.CACHE_ONLY, url, resultCallback, false);
        waitToFinished(feedback);

        Assert.assertTrue(resultCallback.callResultFromLocatTime == 0);
        Assert.assertTrue(resultCallback.callResultTime == 1);
        Assert.assertTrue(resultCallback.loclaResult == null);
        Assert.assertTrue(resultCallback.result == null);
        Assert.assertTrue(resultCallback.isSuccess == false);


        //只拉取网络
        resultCallback = new RC_OK_NO_CACHE<String>();
        feedback = http.pullText(HttpPull.PullType.NETWORK_ONLY, url, resultCallback, true);
        waitToFinished(feedback);

        Assert.assertTrue(resultCallback.callResultTime == 1);
        Assert.assertTrue(resultCallback.callResultFromLocatTime == 0);
        Assert.assertTrue(resultCallback.result.getBody().contains("百度"));



        //只拉取缓存数据也是没有的，因为callback里面禁止了缓存数据。
        resultCallback = new RC_OK_NO_CACHE<String>();
        //只拉取缓存数据
        resultCallback.isSuccess = true;
        feedback = http.pullText(HttpPull.PullType.CACHE_ONLY, url, resultCallback, false);
        waitToFinished(feedback);

        Assert.assertTrue(resultCallback.callResultFromLocatTime == 0);
        Assert.assertTrue(resultCallback.callResultTime == 1);
        Assert.assertTrue(resultCallback.loclaResult == null);
        Assert.assertTrue(resultCallback.result == null);
        Assert.assertTrue(resultCallback.isSuccess == false);



    }

        public void testPullText()throws Exception{
        //清理缓存
        clearCache();

        HttpPull http = HttpPull.Factory.create(getSession());
        String url = "http://www.baidu.com";

        RC_OK<String> resultCallback = new RC_OK<String>();
        //只拉取缓存数据
        BusyFeedback feedback = http.pullText(HttpPull.PullType.CACHE_ONLY, url, resultCallback, false);
        waitToFinished(feedback);

            Assert.assertTrue(resultCallback.callResultFromLocatTime == 0);
            Assert.assertTrue(resultCallback.callResultTime == 1);
            Assert.assertTrue(resultCallback.loclaResult == null);
            Assert.assertTrue(resultCallback.result == null);
            Assert.assertTrue(resultCallback.isSuccess == false);



        //只拉取网络
        feedback = http.pullText(HttpPull.PullType.NETWORK_ONLY, url, resultCallback, true);
        waitToFinished(feedback);

            Assert.assertTrue(resultCallback.callResultFromLocatTime == 0);
            Assert.assertTrue(resultCallback.callResultTime == 2);
            Assert.assertTrue(resultCallback.loclaResult == null);
            Assert.assertTrue(resultCallback.isSuccess == true);
        Assert.assertTrue(resultCallback.result.getBody().contains("百度"));



        //只拉取缓存数据
        resultCallback = new RC_OK<String>();
        feedback = http.pullText(HttpPull.PullType.CACHE_ONLY, url, resultCallback, false);
        waitToFinished(feedback);

            super.waitToFinished(feedback,500);

        Assert.assertTrue(resultCallback.callResultTime ==1);
        Assert.assertTrue(resultCallback.loclaResult == null);
            Assert.assertTrue(resultCallback.loclaResult == null);

            Assert.assertTrue(resultCallback.result != null);

            Assert.assertTrue(resultCallback.callResultFromLocatTime ==0);
            Assert.assertTrue(resultCallback.isSuccess ==true);

            Assert.assertTrue(resultCallback.result.getBody().contains("百度"));

        //拉取网络数据或者缓存数据。
        feedback = http.pullText(HttpPull.PullType.CACHE_AND_NETWORK, url, resultCallback, false);
        waitToFinished(feedback);

            Assert.assertTrue(resultCallback.isSuccess ==true);
            Assert.assertTrue(resultCallback.callResultTime ==2);
            Assert.assertTrue(resultCallback.callResultFromLocatTime ==1);
            Assert.assertTrue(resultCallback.loclaResult.getBody().contains("百度"));
            Assert.assertTrue(resultCallback.result.getBody().contains("百度"));

    }

    public void testPullBlob()throws Exception{
        //清理缓存
        clearCache();

        HttpPull http = new DefaultHttpPull(getSession(), Looper.getMainLooper(),true);
        String url = "http://www.baidu.com/img/bdlogo.png";

        RC_OK<byte[]> resultCallback = new RC_OK<byte[]>();
        //只拉取缓存数据
        BusyFeedback feedback = http.pullBlob(HttpPull.PullType.CACHE_AND_NETWORK, url, resultCallback, false);

        //拉取网络数据或者缓存数据。
        waitToFinished(feedback);

      //  Assert.assertTrue(resultCallback.fromNetwork ==true);
        Assert.assertTrue(resultCallback.isSuccess ==true);
        Assert.assertTrue(resultCallback.callResultTime ==1);
        Assert.assertTrue(resultCallback.callResultFromLocatTime ==0);
        Assert.assertTrue(resultCallback.result.getBody().length >0);


    }


    public void testPullJSONArray()throws Exception{
        //清理缓存
        clearCache();

        HttpPull http = new DefaultHttpPull(getSession(), Looper.getMainLooper(),true);
        String url = "http://119.147.227.168/dlied1.qq.com/qqtalk/cfapp/config/recent_7_average_data.json?mkey=54b650284b7d03ce&f=a00e&p=.json";

        RC_OK<JSONArray> resultCallback = new RC_OK<JSONArray>();
        //只拉取缓存数据
        BusyFeedback feedback = http.pullJSONArray(HttpPull.PullType.CACHE_AND_NETWORK, url, resultCallback, false);

        waitToFinished(feedback);

        Assert.assertTrue(resultCallback.isSuccess ==true);
        Assert.assertTrue(resultCallback.callResultTime ==1);
        Assert.assertTrue(resultCallback.callResultFromLocatTime ==0);
        Assert.assertTrue(resultCallback.result.getBody().length() >= 7);


    }


    private static class RC_OK<T> implements HttpPull.ResultCallback<T> {

      //  public boolean fromNetwork = false;

        public int callResultTime = 0;
        public int callResultFromLocatTime = 0;
        public HttpPull.ErrorType errorType = null;
        public boolean isSuccess = false;
        ResultData<T> result;
        ResultData<T> loclaResult;


        @Override
        public void onResult(boolean isSuccess, HttpPull.ErrorType errorType, ResultData<T> result) {
            this.isSuccess = isSuccess;
            callResultTime++;
            this.errorType = errorType;
            this.result = result;

        }

        @Override
        public void onResultFromLocal(ResultData<T> result) {
            this.loclaResult = result;
            callResultFromLocatTime++;
        }
    }


    private static class RC_OK_NO_CACHE<T> extends RC_OK<T> {
        @Override
        public void onResult(boolean isSuccess, HttpPull.ErrorType errorType, ResultData<T> result) {
           super.onResult(isSuccess,errorType,result);
            if(result!= null){
                result.enableSaveToCache(false);
            }
        }

    }


}
