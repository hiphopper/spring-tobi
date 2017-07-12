package com.study.kks.section4.chapter4_2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2017-07-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext_4_2.xml")
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired private DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        user1 = new User("gyumee", "박성철", "springno1");
        user2 = new User("leegw700", "이길원", "springno2");
        user3 = new User("bumjin", "박범진", "springno3");
    }


    @Test(expected = DuplicateKeyException.class)
    public void duplicateKey(){
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user1);
    }

    @Test
    public void sqlExceptionTranslate(){
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        try{
            userDao.add(user1);
            userDao.add(user1);
        }catch (DataAccessException e){
            SQLException sqlE = (SQLException) e.getRootCause();
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);

            assertThat(translator.translate(null, null, sqlE), is(instanceOf(DuplicateKeyException.class)));
        }
    }
}
