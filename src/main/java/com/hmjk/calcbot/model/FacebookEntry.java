package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FacebookEntry implements Serializable {
    private String id;
    private Long time;
    private List<FacebookMessaging> messaging = new ArrayList<>();

    public FacebookEntry() {
    }

    public FacebookEntry(String id, Long time, List<FacebookMessaging> messaging) {
        this.id = id;
        this.time = time;
        this.messaging = messaging;
    }

    public String getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public List<FacebookMessaging> getMessaging() {
        return messaging;
    }

    @Override
    public String toString() {
        return "FacebookEntry{" +
                "id='" + id + '\'' +
                ", time=" + time +
                ", messaging=" + messaging +
                '}';
    }
}
