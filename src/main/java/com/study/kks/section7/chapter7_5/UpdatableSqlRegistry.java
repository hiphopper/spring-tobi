package com.study.kks.section7.chapter7_5;

import java.util.Map;

/**
 * Created by Administrator on 2017-08-02.
 */
public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String value) throws SqlUpdateFailureException;
    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;
}
