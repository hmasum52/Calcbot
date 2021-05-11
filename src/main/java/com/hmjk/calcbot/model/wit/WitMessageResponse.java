package com.hmjk.calcbot.model.wit;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.List;

public class WitMessageResponse implements Serializable {
    private String text;
    private List<WitIntent> intents;
    private JsonObject entities;
    private JsonObject traits;

    public WitMessageResponse() {
    }

    public WitMessageResponse(String text, List<WitIntent> intents, JsonObject entities, JsonObject traits) {
        this.text = text;
        this.intents = intents;
        this.entities = entities;
        this.traits = traits;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<WitIntent> getIntents() {
        return intents;
    }

    public void setIntents(List<WitIntent> intents) {
        this.intents = intents;
    }

    public JsonObject getEntities() {
        return entities;
    }

    public void setEntities(JsonObject entities) {
        this.entities = entities;
    }

    public JsonObject getTraits() {
        return traits;
    }

    public void setTraits(JsonObject traits) {
        this.traits = traits;
    }

    @Override
    public String toString() {
        return "WitMessageResponse{" +
                "text='" + text + '\'' +
                ", intents=" + intents +
                ", entities=" + entities +
                ", traits=" + traits +
                '}';
    }
}
