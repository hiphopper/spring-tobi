package com.study.kks.section6.chapter6_1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by home on 2017-07-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test_applicationContext_6_1.xml")
public class UserServiceTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private MailSender mailSender;
    private List<User> list;

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
        userDao.deleteAll();
        for(User user : list) userServiceImpl.add(user);

        userServiceImpl.upgradeLevels();

        checkLevelUpgraded(list.get(0), false);
        checkLevelUpgraded(list.get(1), true);
        checkLevelUpgraded(list.get(2), false);
        checkLevelUpgraded(list.get(3), true);
        checkLevelUpgraded(list.get(4), false);
    }

    @Test
    @DirtiesContext
    public void upgradeLevelsWithMockObject() throws Exception{
        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        upgradeLevels();

        List<String> requests = mockMailSender.getRequests();

        assertThat(requests.size(), is(2));
        assertThat(list.get(1).getEmail(), is(requests.get(0)));
        assertThat(list.get(3).getEmail(), is(requests.get(1)));
    }

    @Test
    public void upgradeAllOrNoting() throws Exception{
        TestUserService testUserService = new TestUserService(list.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        UserServiceTx userServiceTx = new UserServiceTx();
        userServiceTx.setTransactionManager(transactionManager);
        userServiceTx.setUserService(testUserService);

        userDao.deleteAll();
        for(User user : list) userServiceTx.add(user);

        try{
            userServiceTx.upgradeLevels();
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

    static class TestUserService extends UserServiceImpl{
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

    static class MockMailSender implements MailSender{
        List<String> requests = new ArrayList<String>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMailMessage) throws MailException {
            requests.add(simpleMailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

        }
    }
}
