package com.study.kks.section6.chapter6_4;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by Administrator on 2017-07-20.
 */
public class TransactionAdvice implements MethodInterceptor {
    private PlatformTransactionManager transactionManager;
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            Object ret = invocation.proceed();
            transactionManager.commit(transactionStatus);
            return ret;
        }catch (RuntimeException e){
            transactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
