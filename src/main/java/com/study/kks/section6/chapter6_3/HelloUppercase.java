package com.study.kks.section6.chapter6_3;

/**
 * Created by Administrator on 2017-07-19.
 */
public class HelloUppercase implements Hello {
    private Hello target;
    public HelloUppercase(Hello target) {
        this.target = target;
    }

    @Override
    public String sayHello(String name) {
        return this.target.sayHello(name).toUpperCase();
    }

    @Override
    public String sayHi(String name) {
        return this.target.sayHi(name).toUpperCase();
    }

    @Override
    public String sayThankYou(String name) {
        return this.target.sayThankYou(name).toUpperCase();
    }
}
