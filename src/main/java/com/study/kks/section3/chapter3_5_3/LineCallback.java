package com.study.kks.section3.chapter3_5_3;

/**
 * Created by Administrator on 2017-07-07.
 */
public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
