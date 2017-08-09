package com.study.kks.section10.chapter10_1;

/**
 * Created by Administrator on 2017-08-09.
 */
public class StringPrinter implements Printer {
    private StringBuffer buffer = new StringBuffer();

    public void print(String message){
        this.buffer.append(message);
    }

    @Override
    public String toString(){
        return this.buffer.toString();
    }
}
