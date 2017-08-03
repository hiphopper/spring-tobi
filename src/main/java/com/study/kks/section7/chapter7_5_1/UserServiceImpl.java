package com.study.kks.section7.chapter7_5_1;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

/**
 * Created by home on 2017-07-12.
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private MailSender mailSender;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void upgradeLevels(){
        List<User> userList = userDao.getAll();
        for (User user : userList){
            if(canUpgradeLevel(user)){
                upgradeLevel(user);
            }
        }
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
        this.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 "+user.getLevel().name()+"로 업그레이드되었습니다.");

        mailSender.send(mailMessage);
    }
}
