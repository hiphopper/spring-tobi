package com.study.kks.section7.chapter7_2_3;

public class DefaultSqlService extends BaseSqlService {
    public DefaultSqlService(){
        setSqlRegistry(new HashMapSqlRegistry());
        setSqlReader(new JaxbXmlSqlReader());
    }
}
