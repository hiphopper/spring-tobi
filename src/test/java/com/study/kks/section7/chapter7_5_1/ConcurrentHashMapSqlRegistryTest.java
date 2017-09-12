package com.study.kks.section7.chapter7_5_1;

/**
 * Created by Administrator on 2017-08-02.
 */
public class ConcurrentHashMapSqlRegistryTest extends AbstractSqlRegistryTest {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
