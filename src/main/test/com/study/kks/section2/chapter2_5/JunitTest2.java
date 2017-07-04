package com.study.kks.section2.chapter2_5;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by home on 2017-07-04.
 */
public class JunitTest2 {
    static Set<JunitTest2> list = new HashSet<JunitTest2>();

    @Test
    public void test1(){
        assertThat(list, not(hasItem(this)));
        list.add(this);
    }

    @Test
    public void test2(){
        assertThat(list, not(hasItem(this)));
        list.add(this);
    }

    @Test
    public void test3(){
        assertThat(list, not(hasItem(this)));
        list.add(this);
    }
}
