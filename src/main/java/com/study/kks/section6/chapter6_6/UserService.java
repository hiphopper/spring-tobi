package com.study.kks.section6.chapter6_6;

import java.util.List;

/**
 * Created by home on 2017-07-12.
 */
public interface UserService {
    int MIN_LOGCOUNT_FOR_SILVER = 50;
    int MIN_RECOMMEND_FOR_GOLD = 30;

    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    void update(User user);

    void upgradeLevels();
}
