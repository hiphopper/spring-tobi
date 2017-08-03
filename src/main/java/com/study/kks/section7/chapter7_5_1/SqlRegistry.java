package com.study.kks.section7.chapter7_5_1;

public interface SqlRegistry {
    void registerSql(String key, String sql);
    String findSql(String key) throws SqlNotFoundException;
}
