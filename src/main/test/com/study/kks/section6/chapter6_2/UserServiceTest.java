package com.study.kks.section6.chapter6_2;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
