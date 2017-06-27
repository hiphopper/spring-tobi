package com.study.kks.section1.chapter1_2_1;

/**
 * Created by cp91716 on 2017-06-27.
 */
public class App {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        UserDao userDao = new NUserDao();
        userDao.add(user);
        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
