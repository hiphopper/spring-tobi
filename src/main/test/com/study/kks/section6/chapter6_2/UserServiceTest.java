package com.study.kks.section6.chapter6_2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class UserServiceTest {
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
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        MockUserDao userDao = new MockUserDao(this.list);
        MockMailSender mailSender = new MockMailSender();

        userServiceImpl.setUserDao(userDao);
        userServiceImpl.setMailSender(mailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = userDao.getUpdated();
        assertThat(updated.size(), is(2));

        checkUserAndLevel(updated.get(0), list.get(1).getId(), list.get(1).getLevel());
        checkUserAndLevel(updated.get(1), list.get(3).getId(), list.get(3).getLevel());

        List<String> requests = mailSender.getRequests();

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

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel){
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

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
