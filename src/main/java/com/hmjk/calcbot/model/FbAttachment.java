package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.Map;

public class FbAttachment implements Serializable {
    private String type;
    private Map<String,String> payload;

    public FbAttachment() {
    }

    public FbAttachment(String type, Map<String, String> payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "FBAttachment{" +
                "type='" + type + '\'' +
                ", payload=" + payload +
                '}';
    }
}
