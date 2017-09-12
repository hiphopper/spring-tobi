package com.study.kks.section5.chapter5_1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by home on 2017-07-13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext_5_1.xml")
public class UserServiceTest {
    @Autowired private UserDao userDao;
    @Autowired private UserService userService;
    private List<User> list;

    @Before
    public void setUp(){
        list = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0)
                , new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0)
                , new User("erwins", "신승한", "p3", Level.SILVER, 60, 29)
                , new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30)
                , new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels(){
        userDao.deleteAll();
        for(User user : list) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(list.get(0), Level.BASIC);
        checkLevel(list.get(1), Level.SILVER);
        checkLevel(list.get(2), Level.SILVER);
        checkLevel(list.get(3), Level.GOLD);
        checkLevel(list.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel){
        assertThat(userDao.get(user.getId()).getLevel(), is(expectedLevel));
    }

    @Test
    public void add(){
        userDao.deleteAll();

        User userWithLevel = list.get(4);
        User userWithoutLevel = list.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }
}
