package com.study.kks.section5.chapter5_1;

import java.util.List;

/**
 * Created by home on 2017-07-12.
 */
public class UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> userList = userDao.getAll();
        for (User user : userList){
            boolean changed;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50){
                changed = true;
                user.setLevel(Level.SILVER);
            }else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30){
                changed = true;
                user.setLevel(Level.GOLD);
            }else if(user.getLevel() == Level.GOLD){
                changed = false;
            }else{
                changed = false;
            }

            if(changed) userDao.update(user);
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
