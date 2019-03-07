package com.rdm.common.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rdm.base.BaseSession;
import com.rdm.base.app.BaseApplication;

import java.io.File;
import java.util.Map;
import java.util.Set;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by Rao on 2015/1/11.
 */
public class AppTest extends BaseApplication {

    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected AbstractDaoMaster createDaoMaster(BaseSession session) {
        File pojoFile = new File(session.getDirecotry(),"pojo-ui-test.db");
        final DaoMasterForTest.OpenHelper helper;
        helper =  new DBUpgradeForTest(this,pojoFile.getAbsolutePath(),null);
        return new DaoMasterForTest(helper.getWritableDatabase());
    }


    public static class DBUpgradeForTest extends DaoMasterForTest.OpenHelper {

        public DBUpgradeForTest(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            if(newVersion < oldVersion) {
                //暴力升级
                DaoMasterForTest.dropAllTables(db, true);
                DaoMasterForTest.createAllTables(db, false);
                return;
            }
            switch (newVersion){
           /* case 8:{
                HashSet<String> modifiedTableName = new HashSet<String>();
                modifiedTableName.add(FixedIncomeDBDao.TABLENAME);
                upgrade(db,modifiedTableName);
                return;
            }*/
                default:
                    //平滑升级
                    DaoMasterForTest.createAllTables(db, true);
                    return;
            }

        }

        /**
         * 重新建立制定的数据库表
         * @param db
         * @param modifiedTableName
         */
        private void upgrade(SQLiteDatabase db,Set<String> modifiedTableName){

            for(String tableName: modifiedTableName){
                String sql = "DROP TABLE " + (true ? "IF EXISTS " : "") + "'" + tableName + "'";
                db.execSQL(sql);
            }

            DaoMasterForTest.createAllTables(db, true);

        }

    }




    public static class DaoMasterForTest extends AbstractDaoMaster {
        public static final int SCHEMA_VERSION = 12;

        /** Creates underlying database table using DAOs. */
        public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {

        }

        /** Drops underlying database table using DAOs. */
        public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {

        }

        public static abstract class OpenHelper extends SQLiteOpenHelper {

            public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
                super(context, name, factory, SCHEMA_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
                createAllTables(db, false);
            }
        }

        /** WARNING: Drops all table on Upgrade! Use only during development. */
        public static class DevOpenHelper extends OpenHelper {
            public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
                super(context, name, factory);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
                dropAllTables(db, true);
                onCreate(db);
            }
        }

        public DaoMasterForTest(SQLiteDatabase db) {
            super(db, SCHEMA_VERSION);

        }

        public DaoSessionForTest newSession() {
            return new DaoSessionForTest(db, IdentityScopeType.Session, daoConfigMap);
        }

        public DaoSessionForTest newSession(IdentityScopeType type) {
            return new DaoSessionForTest(db, type, daoConfigMap);
        }

    }


    public static class DaoSessionForTest extends AbstractDaoSession {





        public DaoSessionForTest(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
                daoConfigMap) {
            super(db);


        }

        public void clear() {

        }


    }


}
