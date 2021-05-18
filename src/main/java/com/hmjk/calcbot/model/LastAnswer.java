package com.hmjk.calcbot.model;

import com.google.gson.Gson;

import javax.persistence.*;

/**
 * @author Hasan Masum
 */
@Entity
@Table(schema = "public", name = "last_answer")
public class LastAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userId;
    private double answer;

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }

    public double getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
