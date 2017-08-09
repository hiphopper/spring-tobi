package com.study.kks.section10.chapter10_1;

import org.junit.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2017-08-09.
 */
public class ApplicationContextTest {

    @Test
    public void registerBean(){
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerSingleton("hello1", Hello.class);

        RootBeanDefinition definition = new RootBeanDefinition(Hello.class);
        definition.getPropertyValues().addPropertyValue("name", "Spring");
        ac.registerBeanDefinition("hello2", definition);

        Hello hello1 = ac.getBean("hello1", Hello.class);
        Hello hello2 = ac.getBean("hello2", Hello.class);

        assertThat(hello1, is(notNullValue()));
        assertThat(hello2.sayHello(), is("Hello Spring"));

        assertThat(hello1, is(not(hello2)));
        assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

    @Test
    public void registerBeanWithDependency(){
    }
}
