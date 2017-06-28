package com.study.kks.section1.chapter1_4;

/**
 * Created by cp91716 on 2017-06-28.
 */
public class DaoFactory {

    public static UserDao userDao(){
        UserDao userDao = new UserDao(connectionMaker());

        return userDao;
    }

    public static ConnectionMaker connectionMaker(){
        return new NConnectionMaker();
    }
}
