package com.zl.async;

/**
 * Created by zl on 2016/7/19.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value=0;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
