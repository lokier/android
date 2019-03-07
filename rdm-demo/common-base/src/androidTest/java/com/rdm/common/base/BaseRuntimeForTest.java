package com.rdm.common.base;

import android.content.Context;

import com.rdm.base.BaseSession;
import com.rdm.base.SDKContext;
import com.rdm.base.db.DefaultUpdateListener;
import com.rdm.base.db.EntityManager;

import java.io.File;

/**
 * Created by Rao on 2015/1/11.
 */
public class BaseRuntimeForTest implements SDKContext {

   public static BaseSession mSession = null;

    private static  File baseDir = null;

    public static void init(Context context){
        baseDir = context.getDir("",Context.MODE_WORLD_READABLE);

        mSession = new BaseSession("session-test");
    }


    /*@Override
    public int getDBVersion() {
        return 0;
    }*/

    @Override
    public Files getFiles() {

        return new Files() {
            @Override
            public File getBaseDir() {
                return baseDir;
            }

            @Override
            public File getSessionDirecotry() {
                return new File(baseDir,"seesion");
            }
        };
    }

    @Override
    public DBInit getDBInit() {
        return new DBInit() {
            @Override
            public int getDBVersion() {
                return 1;
            }

            @Override
            public EntityManager.UpdateListener getUpdateListener() {
                return new DefaultUpdateListener();
            }
        };
    }
}
