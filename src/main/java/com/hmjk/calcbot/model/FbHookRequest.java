package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FbHookRequest implements Serializable {
    private String object;
    private List<FbEntry> entry = new ArrayList<>();

    public FbHookRequest() {
    }

    public FbHookRequest(String object, List<FbEntry> entry) {
        this.object = object;
        this.entry = entry;
    }

    public String getObject() {
        return object;
    }

    public List<FbEntry> getEntry() {
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