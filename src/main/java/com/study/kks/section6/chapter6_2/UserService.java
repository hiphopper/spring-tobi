package com.study.kks.section6.chapter6_2;

/**
 * Created by home on 2017-07-12.
 */
public interface UserService {
    int MIN_LOGCOUNT_FOR_SILVER = 50;
    int MIN_RECOMMEND_FOR_GOLD = 30;

    void upgradeLevels()  throws Exception;
    void add(User user);
}
