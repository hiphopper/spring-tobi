package com.study.kks.section6.chapter6_4;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {
    private List<User> users = new ArrayList<User>();
    private List<User> updated = new ArrayList<User>();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    public List<User> getUpdated(){
        return updated;
    }

    @Override
    public List<User> getAll() {
        return this.users;
    }

    @Override
    public void update(User user) {
        this.updated.add(user);
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }


}
