package com.study.kks.section6.chapter6_7;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by home on 2017-07-12.
 */
@Transactional
public interface UserService {
    int MIN_LOGCOUNT_FOR_SILVER = 50;
    int MIN_RECOMMEND_FOR_GOLD = 30;

    void add(User user);
    @Transactional(readOnly = true) User get(String id);
    @Transactional(readOnly = true) List<User> getAll();
    void deleteAll();
    void update(User user);

    void upgradeLevels();
}
