package com.study.kks.section7.chapter7_5;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017-08-02.
 */
public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
    private Map<String, String> sqlmap = new ConcurrentHashMap<String, String>();

    @Override
    public void registerSql(String key, String sql) {
        sqlmap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String value = sqlmap.get(key);
        if(StringUtils.isEmpty(value)) throw new SqlNotFoundException(key +"를 이용해서 SQL을 찾을 수 없습니다.");
        return value;
    }

    @Override
    public void updateSql(String key, String value) throws SqlUpdateFailureException {
        if(sqlmap.get(key) == null) throw new SqlUpdateFailureException(key+"에 해당하는 SQL을 찾을 수 없습니다.");

        sqlmap.put(key, value);
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for(Map.Entry<String, String> entry : sqlmap.entrySet()){
            updateSql(entry.getKey(), entry.getValue());
        }
    }
}
