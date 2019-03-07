package com.rdm.common.base.unit;

import com.rdm.base.app.BaseApp;
import com.rdm.base.db.DbPool;
import com.rdm.base.db.EntityManager;
import com.rdm.base.db.Pool;
import com.rdm.base.db.annotation.Column;
import com.rdm.base.db.annotation.GenerationType;
import com.rdm.base.db.annotation.Id;
import com.rdm.base.db.annotation.Table;
import com.rdm.common.base.ApplicationTest;

import junit.framework.Assert;

import java.io.Serializable;

/**
 *
 * 测试基本的运行环境。
 * Created by Rao on 2015/1/11.
 */
public class DBTester extends ApplicationTest {

    public void testEntityManagerSave(){

        EntityManager<User> userEntity = BaseApp.get().getEntityManagerFactory(getSession()).getEntityManager(User.class);

        userEntity.deleteAll();

        User user = new User();
        user.uuid = "xdfsf";
        Assert.assertTrue(userEntity.getCount() == 0 );

        userEntity.save(user);
        Assert.assertTrue(user.id !=-1 );
        Assert.assertTrue(userEntity.getCount() == 1 );

        User user2 =  userEntity.findById(user.id);
        Assert.assertTrue(user.id == user2.id );
        Assert.assertTrue(user.uuid.equals(user2.uuid));



    }

    public void testDBPool(){
        Pool<Serializable> dbPool = getSession().getPool();

        User user2 = dbPool.get("KEY-OK",User.class);


        User user = new User();
        user.uuid = "xdfsf";
        user.id = 10;
        dbPool.put("KEY-OK",user);
         user2 = dbPool.get("KEY-OK",User.class);
        Assert.assertTrue(user.id == user2.id );
        Assert.assertTrue(user.uuid.equals(user2.uuid));

        dbPool.remove("KEY-OK");
        user2 = dbPool.get("KEY-OK",User.class);

       Assert.assertTrue(user2 == null);

    }

    @Table(version = 4,name = "USER_FOR_TEST")
    public static class User implements Serializable{
        @Id(strategy = GenerationType.AUTO_INCREMENT)
        public int id = -1;
        @Column
        public String uuid;
        @Column
        public byte[] suid;
        @Column
        public Long tgpid;
        @Column
        public Long showid;
        @Column
        public String nick;
        @Column
        public String picurl;
        @Column
        public int gender;
        @Column
        public int tgp_level;
        @Column
        public long lastModifyTimestamp;
    }


}
