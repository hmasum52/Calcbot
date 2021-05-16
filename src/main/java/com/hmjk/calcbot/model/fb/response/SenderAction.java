package com.hmjk.calcbot.model.fb.response;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
{
  "recipient":{
    "id":"<PSID>"
  },
  "sender_action":"typing_on"
}
 */
public class SenderAction implements Serializable {
    public static final String MARK_SEEN = "mark_seen"; // Mark last message as read
    public static final String TYPING_ON = "typing_on"; // Turn typing indicators on
    public static final String TYPING_OFF = "typing_off"; // Turn typing indicators off

    private Map<String,String> recipient = new HashMap<>();
    private String sender_action;

    public SenderAction(){

    }

    public SenderAction(Map<String,String> recipient, String sender_action){
        this.recipient = recipient;
        this.sender_action = sender_action;
    }

    public SenderAction(String recipientId, String sender_action){
        recipient.put("id",recipientId);
        this.sender_action = sender_action;
    }

    public void setRecipient(Map<String, String> recipient) {
        this.recipient = recipient;
    }

    public void setRecipientId(String id){
        recipient.put("id",id);
    }

    public void setSender_action(String sender_action) {
        this.sender_action = sender_action;
    }

    public Map<String, String> getRecipient() {
        return recipient;
    }

    public String getSender_action() {
        return sender_action;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
