package com.hmjk.calcbot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FbEntry implements Serializable {
    private String id;
    private Long time;
    private List<FbMessaging> messaging = new ArrayList<>();

    public FbEntry() {
    }

    public FbEntry(String id, Long time, List<FbMessaging> messaging) {
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

    public List<FbMessaging> getMessaging() {
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
/*
{
  "id": "682498302938465",
  "time": 1518479195594,
  "messaging": [
    {
      "sender": {
        "id": "<PSID>"
      },
      "recipient": {
        "id": "<PAGE_ID>"
      },
      "timestamp": 1518479195308,
      "message": {
        "mid": "mid.$cAAJdkrCd2ORnva8ErFhjGm0X_Q_c",
        "attachments": [
          {
            "type": "<image|video|audio|file>",
            "payload": {
              "url": "<ATTACHMENT_URL>"
            }
          }
        ]
      }
    }
  ]
}
 */
