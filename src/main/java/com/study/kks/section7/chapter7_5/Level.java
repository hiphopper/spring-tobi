package com.study.kks.section7.chapter7_5;

/**
 * Created by home on 2017-07-12.
 */
public enum Level {
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private int value;
    private Level next;

    Level(int value, Level next){
        this.value = value;
        this.next = next;
    }

    public int intValue(){
        return this.value;
    }

    public Level nextValue(){
        return this.next;
    }

    public static Level valueOf(int value){
        switch (value){
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown value : " + value);
        }
    }
}
