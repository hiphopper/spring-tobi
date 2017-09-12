package com.study.kks.section10.chapter10_3;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ScopeTest {

    @Test
    public void singletoneScope(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SingletoneBean.class, SingletoneBeanClient.class);
        Set<SingletoneBean> beans = new HashSet<SingletoneBean>();
        beans.add(applicationContext.getBean(SingletoneBean.class));
        beans.add(applicationContext.getBean(SingletoneBean.class));
        assertThat(beans.size(), is(1));

        beans.add(applicationContext.getBean(SingletoneBeanClient.class).singletoneBean1);
        beans.add(applicationContext.getBean(SingletoneBeanClient.class).singletoneBean2);
        assertThat(beans.size(), is(1));
    }

    static class SingletoneBean{}
    static class SingletoneBeanClient{
        @Autowired private SingletoneBean singletoneBean1;
        @Autowired private SingletoneBean singletoneBean2;
    }

    @Test
    public void prototypeScope(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(PrototypeBean.class, PrototypeBeanClient.class);
        Set<PrototypeBean> beans = new HashSet<PrototypeBean>();
        beans.add(applicationContext.getBean(PrototypeBean.class));
        assertThat(beans.size(), is(1));
        beans.add(applicationContext.getBean(PrototypeBean.class));
        assertThat(beans.size(), is(2));

        beans.add(applicationContext.getBean(PrototypeBeanClient.class).prototypeBean1);
        assertThat(beans.size(), is(3));
        beans.add(applicationContext.getBean(PrototypeBeanClient.class).prototypeBean2);
        assertThat(beans.size(), is(4));
    }

    @Scope("prototype")
    static class PrototypeBean{}
    static class PrototypeBeanClient{
        @Autowired private PrototypeBean prototypeBean1;
        @Autowired private PrototypeBean prototypeBean2;
    }
}
