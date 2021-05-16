package com.hmjk.calcbot.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class FbAttachment implements Serializable {
    //types
    public static final String AUDIO = "audio";
    public static final String VIDEO = "video";
    public static final String IMAGE = "image";
    public static final String FILE = "file";
    public static final String TEMPLATE = "template";

    private String type;
    private Object payload;

    public FbAttachment() {
    }

    public FbAttachment(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
