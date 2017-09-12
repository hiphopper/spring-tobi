package com.study.kks.section6.chapter6_7;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test_applicationContext_6_7.xml")
public class UserServiceTest {
    private List<User> list;

    @Autowired
    private UserService userService;
    @Autowired
    private UserService testUserService;

    @Before
    public void setUp(){
        list = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER-1, 0, "a@ksug.org")
                , new User("joytouch", "강명성", "p2", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 0, "b@ksug.org")
                , new User("erwins", "신승한", "p3", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD-1, "c@ksug.org")
                , new User("madnite1", "이상호", "p4", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD, "d@ksug.org")
                , new User("green", "오민규", "p5", Level.GOLD, 100, 100, "e@ksug.org")
        );
    }

    @Test
    public void upgradeLevels() throws Exception{
        userService.deleteAll();
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
        userService.deleteAll();
        for(User user : list) userService.add(user);

        try{
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        checkLevelUpgraded(list.get(1), false);
    }

    @Test(expected = TransientDataAccessResourceException.class)
    public void readOnlyTransactionAttribute(){
        this.testUserService.getAll();
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        if(upgraded){
            assertThat(userService.get(user.getId()).getLevel(), is(user.getLevel().nextValue()));
        }else{
            assertThat(userService.get(user.getId()).getLevel(), is(user.getLevel()));
        }
    }

    static class TestUserService extends UserServiceImpl{
        private String id = "madnite1";

        @Override
        public void upgradeLevel(User user){
            if(user.getId().equals(id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        @Override
        public List<User> getAll(){
            for(User user : super.getAll()){
                super.update(user);
            }
            return null;
        }
    }

    static class TestUserServiceException extends RuntimeException{}
}
