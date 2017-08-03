package com.study.kks.section7.chapter7_5_1;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Administrator on 2017-07-18.
 */
public class DummyMailSender implements MailSender {
    @Override
    public void send(SimpleMailMessage simpleMailMessage) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

    }
}
