package com.study.kks.section1.chapter1_7_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by home on 2017-06-28.
 */
@Configuration
public class CountDaoFactory {

    @Bean
    public UserDao userDao(){
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker(){
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker(){
        return new NConnectionMaker();
    }
}
