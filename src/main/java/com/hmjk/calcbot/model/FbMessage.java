package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.List;

public class FbMessage implements Serializable {
    private String mid;
    private Long seq;
    private List<FbAttachment> attachments;
    private String text;
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

    public FbMessage(String mid, Long seq, String text) {
        this.mid = mid;
        this.seq = seq;
        this.text = text;
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
        return "FacebookMessage{" +
                "mid='" + mid + '\'' +
                ", seq=" + seq +
                ", attachments=" + attachments +
                ", text='" + text + '\'' +
                '}';
    }
}