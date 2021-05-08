package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FacebookMessaging implements Serializable {
    private Map<String,String> sender;
    private Map<String,String> recipient;
    private Long timestamp;
    private FacebookMessage message;

    public FacebookMessaging() {
    }

    public FacebookMessaging(Map<String, String> sender, Map<String, String> recipient, Long timestamp, FacebookMessage message) {
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Map<String, String> getSender() {
        return sender;
    }

    public Map<String, String> getRecipient() {
        return recipient;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public FacebookMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FacebookMessaging{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", timestamp=" + timestamp +
                ", message=" + message +
                '}';
    }
}