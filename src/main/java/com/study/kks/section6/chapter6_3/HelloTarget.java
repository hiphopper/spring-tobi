package com.study.kks.section6.chapter6_3;

/**
 * Created by Administrator on 2017-07-19.
 */
public class HelloTarget implements Hello {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String sayHi(String name) {
        return "Hi "+name;
    }

    @Override
    public String sayThankYou(String name) {
        return "Thank You "+name;
    }
}
