package com.study.kks.section7.chapter7_5_1;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
