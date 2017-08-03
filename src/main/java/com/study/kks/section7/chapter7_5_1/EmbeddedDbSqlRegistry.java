package com.study.kks.section7.chapter7_5_1;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Administrator on 2017-08-02.
 */
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.transactionTemplate = new TransactionTemplate(new DataSourceTransactionManager(dataSource));
    }

    @Override
    public void registerSql(String key, String sql) {
        jdbcTemplate.update("insert into sqlmap(key_, sql_) values(?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try{
            return jdbcTemplate.queryForObject("select sql_ from sqlmap where key_ = ?", new Object[]{key}, String.class);
        }catch (EmptyResultDataAccessException e){
            throw new SqlNotFoundException(key +"를 이용해서 SQL을 찾을 수 없습니다.");
        }
    }

    @Override
    public void updateSql(String key, String value) throws SqlUpdateFailureException {
        int result = jdbcTemplate.update("update sqlmap set sql_ = ? where key_ = ?", value, key);

        if(result == 0) throw new SqlUpdateFailureException(key+"에 해당하는 SQL을 찾을 수 없습니다.");
    }

    @Override
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
        /*transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                for(Map.Entry<String, String> entry : sqlmap.entrySet()){
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
            아래와 같이 람다식으로 변경하여 사용해도 된다
        */

        transactionTemplate.execute((status) -> {
            for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                updateSql(entry.getKey(), entry.getValue());
            }

            return 0;
        });
    }
}
