package com.study.kks.section1.chapter1_4_1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cp91716 on 2017-06-28.
 */
@Configuration
public class DaoFactory {

    @Bean
    public static UserDao userDao(){
        UserDao userDao = new UserDao(connectionMaker());

        return userDao;
    }

    @Bean
    public static ConnectionMaker connectionMaker(){
        return new NConnectionMaker();
    }
}
