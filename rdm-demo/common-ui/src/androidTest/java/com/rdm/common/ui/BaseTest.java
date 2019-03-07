package com.rdm.common.ui;

import android.content.Context;

import com.rdm.base.Base;
import com.rdm.base.BaseSession;
import com.rdm.common.ILog;
import com.rdm.common.util.SdcardUtils;

import java.io.File;

/**
 * Created by Rao on 2015/1/11.
 */
public class BaseTest extends Base {

   public static BaseSession mSession = null;

    public static void init(Context context){
        File dir = new File(SdcardUtils.getSDFile(),"tencent.rdm.test");
        mSession = new BaseSession(context,"com.rdm.test",dir);
        Base.set(new BaseTest());
        ILog.setEnableLog(true);
        ILog.setLevel(ILog.Level.VERBOSE);
    }

    @Override
    public BaseSession[] getNeedKnowEnventSessions() {
        if(mSession!= null){
            return new BaseSession[]{mSession};
        }
        return new BaseSession[0];
    }
}
