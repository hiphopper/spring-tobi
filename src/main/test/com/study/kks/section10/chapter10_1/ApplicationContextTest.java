package com.study.kks.section10.chapter10_1;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Administrator on 2017-08-09.
 */
public class ApplicationContextTest {
    private String basePath;

    @Before
    public void setUp(){
        this.basePath = StringUtils.cleanPath(ClassUtils.classPackageAsResourcePath(getClass())) + File.separator;
    }


    @Test
    public void registerBean(){
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerSingleton("hello1", Hello.class);

        BeanDefinition definition = new RootBeanDefinition(Hello.class);
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
        StaticApplicationContext ac = new StaticApplicationContext();
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().addPropertyValue("name", "Spring");
        helloDef.getPropertyValues().addPropertyValue("printer", new RuntimeBeanReference("printer"));

        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer", Printer.class).toString(), is("Hello Spring"));
    }

    @Test
    public void genericApplicationContextWithXml(){
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions(basePath+"genericApplicationContext.xml");
        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer", Printer.class).toString(), is("Hello Spring"));
        ac.close();
    }

    @Test
    public void genericApplicationContextWithProperties(){
        GenericApplicationContext ac = new GenericApplicationContext();
        PropertiesBeanDefinitionReader reader = new PropertiesBeanDefinitionReader(ac);
        reader.loadBeanDefinitions(basePath+"genericApplicationContext.properties");
        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer", Printer.class).toString(), is("Hello Spring"));
        ac.close();
    }

    @Test
    public void genericXmlApplicationContext(){
        ApplicationContext ac = new GenericXmlApplicationContext(basePath+"genericApplicationContext.xml");

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer", Printer.class).toString(), is("Hello Spring"));
    }

    @Test
    public void contextExtends(){
        ApplicationContext parent = new GenericXmlApplicationContext(basePath+"parentContext.xml");
        GenericApplicationContext child = new GenericApplicationContext(parent);
        (new XmlBeanDefinitionReader(child)).loadBeanDefinitions(basePath+"childContext.xml");
        child.refresh();

        Printer printer = child.getBean("printer", Printer.class);
        assertThat(printer, is(notNullValue()));

        Hello hello = child.getBean("hello", Hello.class);
        assertThat(hello, is(notNullValue()));

        hello.print();
        assertThat(printer.toString(), is("Hello Child"));
    }

    @Test
    public void test(){
        String path = ClassUtils.classPackageAsResourcePath(getClass());
        System.out.println(path);
        System.out.println(StringUtils.cleanPath("com/study/kks/../../../com/study/kks/section10/chapter10_1"));
    }
}
