package com.study.kks.section7.chapter7_2_2;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
