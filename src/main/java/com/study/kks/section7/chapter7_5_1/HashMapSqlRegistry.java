package com.study.kks.section7.chapter7_5_1;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HashMapSqlRegistry implements SqlRegistry {
    private Map<String, String> sqlMap = new HashMap<String, String>();
    @Override
    public void registerSql(String key, String sql) {
        this.sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = this.sqlMap.get(key);
        if(StringUtils.isEmpty(sql)){
            throw new SqlNotFoundException(key+"에 대한 SQL을 찾을 수 없습니다.");
        }else{
            return sql;
        }
    }
}
