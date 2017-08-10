package com.study.kks.section10.chapter10_2;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class BeanRegisterTest {
    @Test
    public void simpleBeanScanning(){
        // @Component 가 붙은 클래스를 스캔할 패키지를 넣어서 컨텍스트를 만들어준다.
        // 생성과 동시에 자동으로 스캔과 등록이 진행된다.
        ApplicationContext ac = new AnnotationConfigApplicationContext("com.study.kks.section10.chapter10_2");
        AnnotatedHello hello = ac.getBean("annotatedHello", AnnotatedHello.class);

        assertThat(hello, is(notNullValue()));
    }

    @Test
    public void beanScanningWithXml(){
        ApplicationContext ac = new GenericXmlApplicationContext("com/study/kks/section10/chapter10_2/BeanRegisterTest-context.xml");
        AnnotatedHello hello = ac.getBean("annotatedHello", AnnotatedHello.class);

        assertThat(hello, is(notNullValue()));
    }

    @Test
    public void beanRegisterWithCoding(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AnnotatedHelloConfig.class);
        AnnotatedHello hello = ac.getBean("annotatedHello", AnnotatedHello.class);
        assertThat(hello, is(notNullValue()));

        AnnotatedHelloConfig helloConfig = ac.getBean("annotatedHelloConfig", AnnotatedHelloConfig.class);
        assertThat(helloConfig, is(notNullValue()));

        assertThat(helloConfig.annotatedHello(), is(sameInstance(hello))); // @Configuration 클래스로 싱글톤 스코프를 갖는다.
    }
}
