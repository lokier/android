package com.rdm.common.base.unit;

import com.rdm.base.BaseSession;
import com.rdm.base.loader.Data;
import com.rdm.base.loader.DataAutoSaveLoader;
import com.rdm.base.loader.DataLoader;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lokierao on 2015/7/20.
 */
public class LoaderTester extends ApplicationTest {


    public void testBasic(){

        DataLoaderTest test = new DataLoaderTest();

        LinsterTest listener = new LinsterTest();

        test.load(DataLoader.LoadType.LOCAL_AND_NETWORK, listener);
        Assert.assertTrue(test.isBusy());

        waitToFinished(test);

        Assert.assertTrue(listener.callOnResultCnt == 2);
        Assert.assertTrue(listener.callOnTimeoutCnt == 0);
        Assert.assertTrue(listener.callOnErrorCnt == 0);
        Assert.assertTrue(listener.callOnCancelCnt == 0);

        {
            //第一次从本地回调；
            Data<List<TestData>> result1 = listener.onResultDataList.get(0);
            Assert.assertTrue(result1.isFromLocal());
            Assert.assertTrue(!result1.isLoadMore());
            Assert.assertTrue(!result1.isTemporary());
            Assert.assertTrue(result1.getPageIndex() == 0);

            //  Assert.assertTrue(result1.getBody() != null && result1.getBody().size() > 0);
        }
        {
            //第二次从网络回调；
            Data<List<TestData>> result1 = listener.onResultDataList.get(1);
            Assert.assertTrue(!result1.isFromLocal());
            Assert.assertTrue(!result1.isLoadMore());
            Assert.assertTrue(!result1.isTemporary());
            Assert.assertTrue(result1.get() != null);
            Assert.assertTrue(result1.getPageIndex() == 0);
        }

    }

    public void testAutoSave(){

        AutoSaveLoaderTest test = new AutoSaveLoaderTest(getSession());

        LinsterTest listener = new LinsterTest();

        test.load(DataLoader.LoadType.ONLY_NETWORK);
        waitToFinished(test);

        test.load(DataLoader.LoadType.ONLY_STORE, listener);
        waitToFinished(test);
        Data<List<TestData>> result1 = listener.onResultDataList.get(0);

        Assert.assertTrue(result1.get().size() == 100);

    }



    public void testConflict(){


        LinsterTest listener = new LinsterTest();
        DataLoaderTest test = new DataLoaderTest();

        for(int i = 0; i < 50;i++){
            test.load(DataLoader.LoadType.ONLY_NETWORK, DataLoader.ConflictType.SKIP,listener);
        }

        waitToFinished(test);

        Assert.assertTrue(listener.callOnResultCnt == 1);
        Assert.assertTrue(listener.callOnTimeoutCnt == 0);
        Assert.assertTrue(listener.callOnErrorCnt == 0);
        Assert.assertTrue(listener.callOnCancelCnt == 0);

        listener = new LinsterTest();
        long startTime = System.currentTimeMillis() ;
        for(int i = 0; i < 5;i++){
            test.load(DataLoader.LoadType.ONLY_NETWORK, DataLoader.ConflictType.WAIT,listener);
        }

        waitToFinished(test);
        Assert.assertTrue(listener.callOnResultCnt == 5);
        Assert.assertTrue(listener.callOnTimeoutCnt == 0);
        Assert.assertTrue(listener.callOnErrorCnt == 0);
        Assert.assertTrue(listener.callOnCancelCnt == 0);
        long spendTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue(spendTime > 4900);

        listener = new LinsterTest();

        startTime = System.currentTimeMillis() ;
        for(int i = 0; i < 5;i++){
            test.load(DataLoader.LoadType.ONLY_NETWORK, DataLoader.ConflictType.ALWAYS,listener);
        }

        waitToFinished(test);
        Assert.assertTrue(listener.callOnResultCnt == 5);
        Assert.assertTrue(listener.callOnTimeoutCnt == 0);
        Assert.assertTrue(listener.callOnErrorCnt == 0);
        Assert.assertTrue(listener.callOnCancelCnt == 0);

         spendTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue(spendTime < 1100);



    }

    public void testTimout(){

        EmptyImpletmentLoader test = new EmptyImpletmentLoader();
        LinsterTest listener = new LinsterTest();

        //因为loadMore没有实现，且方法
        test.load(DataLoader.LoadType.ONLY_NETWORK, listener, 2000);
       // test.loadMore(listener, 2000);

        long start = System.currentTimeMillis();
        waitToFinished(test);
        long spend = System.currentTimeMillis() - start;

        Assert.assertTrue(Math.abs(spend - 2000) < 100);  //超时时间

        Assert.assertTrue(listener.callOnResultCnt == 0);
        Assert.assertTrue(listener.callOnTimeoutCnt == 1);
        Assert.assertTrue(listener.callOnErrorCnt == 0);
        Assert.assertTrue(listener.callOnCancelCnt == 0);

    }



    public void testLoadPage(){
        DataLoaderTest test = new DataLoaderTest();

        LinsterTest listener = new LinsterTest();

        int pageIndex = 23;
        int pageSize = 50;
        test.loadPage(pageIndex, pageSize, listener);

        waitToFinished(test);

        Assert.assertTrue(listener.callOnResultCnt == 1);
        Assert.assertTrue(listener.callOnTimeoutCnt == 0);
        Assert.assertTrue(listener.callOnErrorCnt == 0);
        Assert.assertTrue(listener.callOnCancelCnt == 0);

        {
            Data<List<TestData>> result1 = listener.onResultDataList.get(0);
            Assert.assertTrue(!result1.isFromLocal());
            Assert.assertTrue(!result1.isLoadMore());
            Assert.assertTrue(!result1.isTemporary());
            Assert.assertTrue(result1.getPageIndex() == pageIndex);
            Assert.assertTrue(result1.getPageSize() == pageSize);
        }

    }


    public void testCancel(){

    }

    public static class TestData {

    }

    private static class LinsterTest implements  DataLoader.DataListener<List<TestData>>{

        public List<Data<List<TestData>>> onResultDataList = new ArrayList<Data<List<TestData>>>();
        public int callOnResultCnt = 0;
        public int callOnErrorCnt = 0;
        public int callOnCancelCnt = 0;
        public int callOnTimeoutCnt = 0;
        public Exception onError;
        public Data data = null;

        @Override
        public void onResult(DataLoader loader, Data<List<TestData>> data) {
            callOnResultCnt++;
            onResultDataList.add(data);
            this.data = data;
        }

        @Override
        public void onError(DataLoader loader, String message, Exception error) {
            callOnErrorCnt++;
            onError = error;
        }

        @Override
        public void onCancel(DataLoader loader) {
            callOnCancelCnt++;
        }

        @Override
        public void onTimeout(DataLoader loader) {
            callOnTimeoutCnt++;
        }
    }

    public static class AutoSaveLoaderTest extends DataAutoSaveLoader<List<TestData>> {

        public AutoSaveLoaderTest(BaseSession session){
            super(session);
        }

        @Override
        protected byte[] loadOriginData(boolean loadMore) {
            return new byte[100];
        }

        @Override
        protected byte[] loadOriginData(int pageIndex, int pageSize) {
            return new byte[10];
        }

        @Override
        protected List<TestData> parseOriginData(byte[] serialData) {

            //Thread.sleep(1000);
            List<TestData> data = new ArrayList<TestData>();

            for(int i = 0;i < serialData.length;i++){
                data.add(new TestData());
            }
            return data;
        }

        @Override
        public String getSessionId() {
            return "AutoSaveLoaderTest";
        }
    }

    public static class DataLoaderTest extends DataLoader<List<TestData>>{

        @Override
        protected List<TestData> loadFromStore(){
            return null;
        }

        @Override
        protected List<TestData> loadMoreFromStore()  {
            return null;
        }

        @Override
        protected List<TestData> loadPageFromStore(int pageIndex, int pageSize)  {
            return null;
        }


        @Override
        protected void onLoadFromNet(ResultNotifier notifier)  {

            try {
                Thread.sleep(1000);
                List<TestData> data = new ArrayList<TestData>();
                notifier.notifyResult(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onLoadMoreFromNet(ResultNotifier notifier) {

        }

        @Override
        protected void onLoadPageFromNet(ResultNotifier notifier, int pageIndex, int pageSize) {
            try {
                Thread.sleep(1000);
                List<TestData> data = new ArrayList<TestData>();
                notifier.notifyResult(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void saveToStore(com.rdm.base.loader.Data<List<TestData>> data)  {

        }

        @Override
        public String getSessionId() {
            return null;
        }

    }

    public static class EmptyImpletmentLoader extends DataLoaderTest{

        @Override
        protected void onLoadPageFromNet(ResultNotifier notifier, int pageIndex, int pageSize) {

        }

        @Override
        protected void onLoadFromNet(ResultNotifier notifier)  {

        }
    }


}
