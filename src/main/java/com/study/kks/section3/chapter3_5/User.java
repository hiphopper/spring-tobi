package com.study.kks.section3.chapter3_5;

/**
 * Created by cp91716 on 2017-06-27.
 */
public class User {
    String id;
    String name;
    String password;

    public User(){}
    public User(String id, String name, String password){
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
