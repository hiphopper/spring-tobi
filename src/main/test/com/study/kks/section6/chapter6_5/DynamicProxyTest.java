package com.study.kks.section6.chapter6_5;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DynamicProxyTest {

    @Test
    public void proxyFactoryBean(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello hello = (Hello) pfBean.getObject();
        assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(hello.sayHi("Toby"), is("HI TOBY"));
        assertThat(hello.sayThankYou("Toby"), is("THANK YOU TOBY"));
    }

    @Test
    public void pointcutAdvisor(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello hello = (Hello) pfBean.getObject();
        assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
        assertThat(hello.sayHi("Toby"), is("HI TOBY"));
        assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
    }

    @Test
    public void classNamePointcutAdvisor(){
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut(){
            @Override
            public ClassFilter getClassFilter() {
                return (Class<?> clazz) -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        pointcut.setMappedName("sayH*");

        checkAdvice(new HelloTarget(), pointcut, true);

        class HelloWorld extends HelloTarget{}
        checkAdvice(new HelloWorld(), pointcut, false);

        class HelloTobi extends HelloTarget{}
        checkAdvice(new HelloTobi(), pointcut, true);
    }

    private void checkAdvice(Object target, Pointcut pointcut, boolean adviced){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello hello = (Hello) pfBean.getObject();
        if(adviced){
            assertThat(hello.sayHello("Toby"), is("HELLO TOBY"));
            assertThat(hello.sayHi("Toby"), is("HI TOBY"));
            assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
        }else{
            assertThat(hello.sayHello("Toby"), is("Hello Toby"));
            assertThat(hello.sayHi("Toby"), is("Hi Toby"));
            assertThat(hello.sayThankYou("Toby"), is("Thank You Toby"));
        }
    }

    class UppercaseAdvice implements MethodInterceptor{
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }

    }

    interface Hello{
        String sayHello(String name);
        String sayHi(String name);
        String sayThankYou(String name);
    }

    class HelloTarget implements Hello{
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
}
