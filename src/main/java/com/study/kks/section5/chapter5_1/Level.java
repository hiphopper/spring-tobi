package com.study.kks.section5.chapter5_1;

/**
 * Created by home on 2017-07-12.
 */
public enum Level {
    BASIC(1), SILVER(2), GOLD(3);

    private int value;

    Level(int value){
        this.value = value;
    }

    public int intValue(){
        return this.value;
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
