package com.hmjk.calcbot.model;

import java.io.Serializable;

public class FacebookMessage implements Serializable {
    private String mid;
    private Long seq;
    private String text;

    public FacebookMessage() {
    }

    public FacebookMessage(String mid, Long seq, String text) {
        this.mid = mid;
        this.seq = seq;
        this.text = text;
    }

    public String getMid() {
        return mid;
    }

    public Long getSeq() {
        return seq;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "FacebookMessage{" +
                "mid='" + mid + '\'' +
                ", seq=" + seq +
                ", text='" + text + '\'' +
                '}';
    }
}