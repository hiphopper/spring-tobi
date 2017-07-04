package com.study.kks.section2.chapter2_5;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Created by home on 2017-07-04.
 */
public class JunitTest {
    static JunitTest testObject;

    @Test
    public void test1(){
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test2(){
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test3(){
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
}
