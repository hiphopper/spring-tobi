package com.study.kks.section2.chapter2_2;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

/**
 * Created by home on 2017-07-03.
 */
public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException{
        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        ApplicationContext applicationContext = new GenericXmlApplicationContext("test_applicationContext_2_2.xml");
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);

        userDao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());

        if(!user.getName().equals(user2.getName())){
            System.out.println("조회 실패 (Name)");
        }else if(!user.getPassword().equals(user2.getPassword())){
            System.out.println("조회 실패 (Password)");
        }else{
            System.out.println(user2.getId() + " 조회 성공");
        }
    }

    @Test
    public void addAndGet2() throws SQLException{
        ApplicationContext applicationContext = new GenericXmlApplicationContext("test_applicationContext_2_2.xml");
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        userDao.add(user);
        assertThat(userDao.getCount(), is(1));

        User user2 = userDao.get(user.getId());

        assertThat(user.getName(), is(user2.getName()));
        assertThat(user.getPassword(), is(user2.getPassword()));
    }
}
