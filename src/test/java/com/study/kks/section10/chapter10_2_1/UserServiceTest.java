package com.study.kks.section10.chapter10_2_1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "test_applicationContext_10_2_1.xml")
public class UserServiceTest {
    @Autowired
    private SimpleDriverDataSource dataSource;

    @Test
    public void placeHolderTest() throws Exception{
        assertThat(dataSource.getUsername(), is("spring"));
        assertThat(dataSource.getPassword(), is("book"));
    }
}
