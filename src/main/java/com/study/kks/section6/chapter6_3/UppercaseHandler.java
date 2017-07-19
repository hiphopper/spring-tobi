package com.study.kks.section6.chapter6_3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017-07-19.
 */
public class UppercaseHandler implements InvocationHandler {
    private Object target;
    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if(ret instanceof String){
            return ((String)ret).toUpperCase();
        }else{
            return ret;
        }
    }
}
