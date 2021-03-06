package com.study.kks.section7.chapter7_2_3;

import java.util.List;

/**
 * Created by Administrator on 2017-07-12.
 */
public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}
