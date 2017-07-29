package com.study.kks.section7.chapter7_2_3;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
