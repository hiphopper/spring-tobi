package com.study.kks.section6.chapter6_3;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test_applicationContext_6_3.xml")
public class UserServiceTest {
    private List<User> list;
    private MockUserDao mockUserDao;
    private MockMailSender mockMailSender;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Before
    public void setUp(){
        list = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER-1, 0, "a@ksug.org")
                , new User("joytouch", "강명성", "p2", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 0, "b@ksug.org")
                , new User("erwins", "신승한", "p3", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD-1, "c@ksug.org")
                , new User("madnite1", "이상호", "p4", Level.SILVER, 60, UserService.MIN_RECOMMEND_FOR_GOLD, "d@ksug.org")
                , new User("green", "오민규", "p5", Level.GOLD, 100, 100, "e@ksug.org")
        );

        mockUserDao = new MockUserDao(list);
        mockMailSender = new MockMailSender();
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
    public void mockUpgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));

        checkUserAndLevel(updated.get(0), list.get(1).getId(), list.get(1).getLevel());
        checkUserAndLevel(updated.get(1), list.get(3).getId(), list.get(3).getLevel());

        List<String> requests = mockMailSender.getRequests();

        assertThat(requests.size(), is(2));
        assertThat(list.get(1).getEmail(), is(requests.get(0)));
        assertThat(list.get(3).getEmail(), is(requests.get(1)));
    }

    @Test
    public void mockitoUpgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        UserDao mockUserDao = mock(UserDao.class);
        MailSender mockMailSender = mock(MailSender.class);

        when(mockUserDao.getAll()).thenReturn(this.list);

        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class)); // update() 파라미터는 무시하고 몇번 호출됐는지 검증하는 코드
        verify(mockUserDao).update(list.get(1));    // update() 호출된 파라미터가 맞는지 검증하는 코드
        assertThat(list.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(list.get(3));
        assertThat(list.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(list.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(list.get(3).getEmail()));
    }

    @Test
    public void upgradeAllOrNoting() throws Exception{
        TestUserService testUserService = new TestUserService(list.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        TransactionHandler transactionHandler = new TransactionHandler();
        transactionHandler.setTarget(testUserService);
        transactionHandler.setTransactionManager(transactionManager);
        transactionHandler.setPattern("upgradeLevels");

        UserService userServiceTx = (UserService) Proxy.newProxyInstance(getClass().getClassLoader()
                , new Class[]{ UserService.class }, transactionHandler);

        userDao.deleteAll();
        for(User user : list) userServiceTx.add(user);

        try{
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        assertThat(list.get(3).getLevel(), is(Level.SILVER));
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNotingWithFactoryBean() throws Exception{
        TestUserService testUserService = new TestUserService(list.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        TxProxyFactoryBean txProxyFactoryBean = applicationContext.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);

        UserService userServiceTx = (UserService)txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for(User user : list) userDao.add(user);
        try{
            userServiceTx.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){

        }

        assertThat(list.get(3).getLevel(), is(Level.SILVER));
    }

    private void checkLevelUpgraded(User user, boolean upgraded){
        if(upgraded){
            assertThat(userDao.get(user.getId()).getLevel(), is(user.getLevel().nextValue()));
        }else{
            assertThat(userDao.get(user.getId()).getLevel(), is(user.getLevel()));
        }
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel){
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    static class TestUserService extends UserServiceImpl{
        private String id;
        public TestUserService(){
            this("madnite1");
        }
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

    static class MockMailSender implements MailSender {
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
