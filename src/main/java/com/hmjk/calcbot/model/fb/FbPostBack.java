package com.hmjk.calcbot.model.fb;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Objects;

/*
"postback":{
    "title": "<TITLE_FOR_THE_CTA>",
    "payload": "<USER_DEFINED_PAYLOAD>",
    "referral": {
      "ref": "<USER_DEFINED_REFERRAL_PARAM>",
      "source": "<SHORTLINK>",
      "type": "OPEN_THREAD",
    }
  }
 */
public class FbPostBack {
    private String title;
    private String payload;
    private Map<String ,Object> referral;

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

    public Map<String, Object> getReferral() {
        return referral;
    }

    public void setReferral(Map<String, Object> referral) {
        this.referral = referral;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FbPostBack that = (FbPostBack) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(referral, that.referral);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, payload, referral);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
