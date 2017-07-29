package com.study.kks.section7.chapter7_2_2;

public class SqlRetrievalFailureException extends RuntimeException {
    public SqlRetrievalFailureException(String message){
        super(message);
    }

    public SqlRetrievalFailureException(Throwable cause){
        super(cause);
    }

    public SqlRetrievalFailureException(String message, Throwable cause){
        super(message, cause);
    }
}
