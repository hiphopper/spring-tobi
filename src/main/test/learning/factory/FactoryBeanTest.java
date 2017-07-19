package learning.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Administrator on 2017-07-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration // 같은 package 경로의 resource 또는 디렉토리에 있어야 class이름 + "-context.xml" 로 세팅된다.
public class FactoryBeanTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void getMessageFromFactoryBean(){
        Object message = applicationContext.getBean("message");
        assertThat(message, is(instanceOf(Message.class)));
        assertThat(((Message)message).getText(), is("Factory Bean"));

    }

    @Test
    public void getFactoryBean(){
        Object factoryBean = applicationContext.getBean("&message");
        assertThat(factoryBean, is(instanceOf(MessageFactoryBean.class)));
    }
}
