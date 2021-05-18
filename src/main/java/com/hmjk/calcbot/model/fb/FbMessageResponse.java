package com.hmjk.calcbot.model.fb;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FbMessageResponse implements Serializable {
    //Message is in response to a received message.
    public static final String MESSAGE_TYPE_RESPONSE = "RESPONSE";
    //  Message is being sent proactively and is not in response to a received message. This includes promotional and non-promotional messages sent inside the the 24-hour standard messaging window.
    public static final String MESSAGE_TPYE_UPDATE = "UPDATE";
    public static final String MESSAGE_TPYE_MESSAGE_TAG = "MESSAGE_TAG";



    private String messaging_type;
    //Any time you send a message, you must identify the message recipient in the body of the request.
    // has a id field.
    private Map<String,String> recipient = new HashMap<>();
    // this is the main body of the message
    private Map<String,Object> message = new HashMap<>();

    public FbMessageResponse() {
    }

    public FbMessageResponse(String messaging_type, Map<String, String> recipient, Map<String, Object> message) {
        this.messaging_type= messaging_type;
        this.recipient = recipient;
        this.message = message;
    }

    public void setMessaging_type(String messaging_type) {
        this.messaging_type = messaging_type;
    }

    public void setRecipient(Map<String, String> recipient) {
        this.recipient = recipient;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }

    public String getMessaging_type() {
        return messaging_type;
    }

    public Map<String, String> getRecipient() {
        return recipient;
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}