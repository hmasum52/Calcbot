package com.hmjk.calcbot.model.wit;

public class WitIntent {
    private long id;
    private String name;
    private double confidence;

    public WitIntent() {
    }

    public WitIntent(long id, String name, double confidence) {
        this.id = id;
        this.name = name;
        this.confidence = confidence;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        return "WitIntent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", confidence=" + confidence +
                '}';
    }
}
