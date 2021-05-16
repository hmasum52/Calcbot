package com.hmjk.calcbot.model.fb.response;

import com.google.gson.Gson;

import java.util.Objects;

public class FbButton {
    public static final String TYPE_WEB_URL = "web_url";
    public static final String TYPE_POSTBACK = "postback";

    private String type;
    private String title;
    private String url;
    private String payload;

    public FbButton(){

    }

    //url button
    // or postback
    public FbButton(String type, String title, String urlOrPayload){
        this.type  = type;
        this.title = title;
        if(type.equals(TYPE_POSTBACK))
            this.payload = urlOrPayload;
        else if(type.equals(TYPE_WEB_URL))
            this.url = urlOrPayload;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FbButton fbButton = (FbButton) o;
        return Objects.equals(type, fbButton.type) &&
                Objects.equals(title, fbButton.title) &&
                Objects.equals(url, fbButton.url) &&
                Objects.equals(payload, fbButton.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, url, payload);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
