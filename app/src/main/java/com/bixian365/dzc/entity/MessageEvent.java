package com.bixian365.dzc.entity;

/**
 * eventBus 实体类
 * Created by NN on 2017/9/9.
 */

public class MessageEvent {

    private String message;
    private int tag;

    public MessageEvent(int tag,String message) {
        this.message = message;
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
