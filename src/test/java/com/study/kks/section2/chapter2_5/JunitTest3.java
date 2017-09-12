package com.study.kks.section2.chapter2_5;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by home on 2017-07-04.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test_applicationContext_2_5.xml"})
public class JunitTest3 {
    @Autowired
    ApplicationContext applicationContext;

    static Set<JunitTest3> list = new HashSet<JunitTest3>();
    static ApplicationContext testApplicationContext;

    @Test
    public void test1(){
        assertThat(list, not(hasItem(this)));
        list.add(this);

        assertThat(testApplicationContext == null || testApplicationContext == applicationContext, is(true));
        testApplicationContext = this.applicationContext;
    }

    @Test
    public void test2(){
        assertThat(list, not(hasItem(this)));
        list.add(this);

        assertTrue(testApplicationContext == null || testApplicationContext == applicationContext);
        testApplicationContext = this.applicationContext;
    }

    @Test
    public void test3(){
        assertThat(list, not(hasItem(this)));
        list.add(this);

        assertThat(testApplicationContext, either(is(nullValue())).or(is(sameInstance(applicationContext))));
        testApplicationContext = this.applicationContext;
    }
}
