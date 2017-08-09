package com.study.kks.section10.chapter10_1;

/**
 * Created by Administrator on 2017-08-09.
 */
public class Hello {
    private String name;
    private Printer printer;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public String sayHello(){
        return "Hello " + name;
    }

    public void print(){
        this.printer.print(sayHello());
    }
}
