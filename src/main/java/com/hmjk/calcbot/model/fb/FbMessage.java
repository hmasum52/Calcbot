package com.hmjk.calcbot.model.fb;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FbMessage implements Serializable {
    //always
    private String mid;
    //optional
    private Long seq; // null is no seq
    //optional
    private List<FbAttachment> attachments; //null if no attachments;
    //optional
    private String text; //null is no text message

    // quick_reply has a payload field
    private Map<String,String> quick_reply; //null is no quick_reply
    /*
     "message": {
            "mid": "m_B5-RCRQ7EmEclm-o3FmZt1c8UUhKkaK-sgwsYPnahmXZTKil7IOfC5c-bjuYT-LeQB3zw8x4fvSI9jFGAGjd7A",
            "attachments": [
              {
                "type": "audio",
                "payload": {
                  "url": "https://cdn.fbsbx.com/v/t59.3654-21/178563899_580217470039366_8147828623823212479_n.mp4/audioclip-1620648513000-2276.mp4?_nc_cat=103&ccb=1-3&_nc_sid=7272a8&_nc_ohc=r39FjuchXLQAX-hnV9q&_nc_ht=cdn.fbsbx.com&oh=ea8c9df0a45bd22fe9697aeb6754fd84&oe=609AFD66"
                }
              }
            ]
          }
     */

    public FbMessage() {
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public void setAttachments(List<FbAttachment> attachments) {
        this.attachments = attachments;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, String> getQuick_reply() {
        return quick_reply;
    }

    public void setQuick_reply(Map<String, String> quick_reply) {
        this.quick_reply = quick_reply;
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

    public List<FbAttachment> getAttachments() {
        return attachments;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}