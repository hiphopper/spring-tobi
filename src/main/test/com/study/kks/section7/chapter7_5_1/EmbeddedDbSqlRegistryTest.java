package com.study.kks.section7.chapter7_5_1;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by Administrator on 2017-08-03.
 */
public class EmbeddedDbSqlRegistryTest extends AbstractSqlRegistryTest {
    EmbeddedDatabase embeddedDatabase;
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        embeddedDatabase = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
                                                    .addScript("classpath:/com/study/kks/section7/chapter7_5_1/schema.sql").build();

        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase);

        return sqlRegistry;
    }

    @After
    public void tearDown(){
        embeddedDatabase.shutdown();
    }

    @Test
    public void transactionalUpdate(){
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<String, String>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("SQL999!@#$", "Modified2");

        try{
            sqlRegistry.updateSql(sqlmap);
            fail();
        }catch (SqlUpdateFailureException e){}

        checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
