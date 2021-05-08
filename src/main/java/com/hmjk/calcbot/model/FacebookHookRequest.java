package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FacebookHookRequest implements Serializable {
    private String object;
    private List<FacebookEntry> entry = new ArrayList<>();

    public FacebookHookRequest() {
    }

    public FacebookHookRequest(String object, List<FacebookEntry> entry) {
        this.object = object;
        this.entry = entry;
    }

    public String getObject() {
        return object;
    }

    public List<FacebookEntry> getEntry() {
        return entry;
    }

    @Override
    public String toString() {
        return "FacebookHookRequest{" +
                "object='" + object + '\'' +
                ", entry=" + entry +
                '}';
    }
}