package com.study.kks.section2.chapter2_3;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by home on 2017-07-04.
 */
public class UserDaoTest2 {
    private ApplicationContext applicationContext;
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp(){
        applicationContext = new GenericXmlApplicationContext("test_applicationContext_2_3.xml");
        userDao = applicationContext.getBean("userDao", UserDao.class);

        user1 = new User("gyumee", "박성철", "springno1");
        user2 = new User("leegw700", "이길원", "springno2");
        user3 = new User("bumjin", "박범진", "springno3");
    }

    @Test
    public void addAndGet() throws SQLException{
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        User userget1 = userDao.get(user1.getId());
        assertThat(user1.getName(), is(userget1.getName()));
        assertThat(user1.getPassword(), is(userget1.getPassword()));

        User userget2 = userDao.get(user2.getId());
        assertThat(user2.getName(), is(userget2.getName()));
        assertThat(user2.getPassword(), is(userget2.getPassword()));
    }
    @Test
    public void getCount() throws SQLException{
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));
        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws SQLException{
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.get("unknown_id");
    }
}
