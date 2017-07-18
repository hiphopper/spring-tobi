package com.study.kks.section5.chapter5_4;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

/**
 * Created by home on 2017-07-12.
 */
public class UserService {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;
    private PlatformTransactionManager transactionManager;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels()  throws Exception{
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try{
            List<User> userList = userDao.getAll();
            for (User user : userList){
                if(canUpgradeLevel(user)){
                    upgradeLevel(user);
                }
            }

            this.transactionManager.commit(transactionStatus);
        }catch (Exception e){
            this.transactionManager.rollback(transactionStatus);
            throw e;
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    private boolean canUpgradeLevel(User user){
        Level level = user.getLevel();

        switch (level){
            case BASIC: return user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER: return user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD: return false;
            default: throw new AssertionError("Unknown Level :"+level);
        }
    }

    protected void upgradeLevel(User user){
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User user) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "mail.ksug.org");
        Session session = Session.getInstance(prop, null);

        MimeMessage message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress("useradmin@ksug.org"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Upgrade 안내");
            message.setText("사용자님의 등급이 "+user.getLevel().name()+"로 업그레이드되었습니다.");

            Transport.send(message);
        }catch (AddressException e){
            throw new RuntimeException(e);
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
    }
}
