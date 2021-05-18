package com.hmjk.calcbot.model.wit;

import com.google.gson.Gson;

public class WitIntent {
    private String id;
    private String name;
    private double confidence;

    public WitIntent() {
    }

    public WitIntent(String id, String name, double confidence) {
        this.id = id;
        this.name = name;
        this.confidence = confidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
