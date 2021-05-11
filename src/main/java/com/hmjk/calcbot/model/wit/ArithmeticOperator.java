package com.hmjk.calcbot.model.wit;

import java.io.Serializable;

public class ArithmeticOperator implements Serializable {
    private String type;

    public ArithmeticOperator(String type) {
        this.type = type;
    }

    public ArithmeticOperator() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
