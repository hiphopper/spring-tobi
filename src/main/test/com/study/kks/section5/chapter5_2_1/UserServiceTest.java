package com.study.kks.section5.chapter5_2_1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by home on 2017-07-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext_5_2_1.xml")
public class UserServiceTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private List<User> list;

    @Before
    public void setUp(){
        list = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER-1, 0)
                , new User("joytouch", "강명성", "p2", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 0)
                , new User("erwins", "신승한", "p3", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD-1)
                , new User("madnite1", "이상호", "p4", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD)
                , new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        userDao.deleteAll();
        for(User user : list) userService.add(user);

        userService.upgradeLevels();

        checkLevelUpgraded(list.get(0), false);
        checkLevelUpgraded(list.get(1), true);
        checkLevelUpgraded(list.get(2), false);
        checkLevelUpgraded(list.get(3), true);
        checkLevelUpgraded(list.get(4), false);
    }

    @Test
    public void upgradeAllOrNoting() throws Exception{
        UserService testUserService = new TestUserService(list.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setTransactionManager(transactionManager);

        userDao.deleteAll();
        for(User user : list) testUserService.add(user);

        try{
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){}

        checkLevelUpgraded(list.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        if(upgraded){
            assertThat(userDao.get(user.getId()).getLevel(), is(user.getLevel().nextValue()));
        }else{
            assertThat(userDao.get(user.getId()).getLevel(), is(user.getLevel()));
        }
    }

    static class TestUserService extends UserService{
        private String id;
        public TestUserService(String id){
            this.id = id;
        }

        @Override
        public void upgradeLevel(User user){
            if(user.getId().equals(id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{}
}
