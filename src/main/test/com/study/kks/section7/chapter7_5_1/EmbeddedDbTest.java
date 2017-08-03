package com.study.kks.section7.chapter7_5_1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

/**
 * Created by Administrator on 2017-08-03.
 */
public class EmbeddedDbTest {
    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        this.embeddedDatabase = new EmbeddedDatabaseBuilder().setType(HSQL)
                                    .addScript("classpath:/com/study/kks/section7/chapter7_5_1/schema.sql")
                                    .addScript("classpath:/com/study/kks/section7/chapter7_5_1/data.sql")
                                    .build();

        jdbcTemplate = new JdbcTemplate(this.embeddedDatabase);
    }

    @After
    public void tearDown(){
        this.embeddedDatabase.shutdown();
    }

    @Test
    public void initData(){
        assertThat(jdbcTemplate.queryForObject("select count(*) from sqlmap", null, int.class), is(2));

        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from sqlmap order by key_");
        assertThat(list.get(0).get("KEY_"), is("KEY1"));
        assertThat(list.get(0).get("SQL_"), is("SQL1"));
        assertThat(list.get(1).get("KEY_"), is("KEY2"));
        assertThat(list.get(1).get("SQL_"), is("SQL2"));
    }

    @Test
    public void insert(){
        jdbcTemplate.update("insert into sqlmap(KEY_, SQL_) values('KEY3', 'SQL3')");
        assertThat(jdbcTemplate.queryForObject("select count(*) from sqlmap", null, int.class), is(3));
    }
}
