package com.hmjk.calcbot.model.fb.response;

import com.google.gson.Gson;

import java.io.Serializable;

public class QuickReply implements Serializable {
    public static final String TEXT = "text";
    public static final String LOCATION = "location";
    public static final String PHONE_NUMBER = "user_phone_number";
    public static final String USER_MAIL = "user_email";
    private String content_type;
    private String title;
    private String payload;
    private String image_url;

    public QuickReply() {
    }

    public QuickReply(String content_type, String title, String payload, String image_url) {
        this.content_type = content_type;
        this.title = title;
        this.payload = payload;
        this.image_url = image_url;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

/*
{
  "recipient":{
    "id":"<PSID>"
  },
  "messaging_type": "RESPONSE",
  "message":{
    "text": "Pick a color:",
    "quick_replies":[
      {
        "content_type":"text",
        "title":"Red",
        "payload":"<POSTBACK_PAYLOAD>",
        "image_url":"http://example.com/img/red.png"
      },{
        "content_type":"text",
        "title":"Green",
        "payload":"<POSTBACK_PAYLOAD>",
        "image_url":"http://example.com/img/green.png"
      }
    ]
  }
}
 */
