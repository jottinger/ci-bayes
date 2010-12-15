package com.enigmastation.dao.model;

import java.io.Serializable;

public enum Visibility implements Serializable {
    VISIBLE(-0.2),
    INVISIBLE(0.0),
    ;

    Visibility(double d) {
        strength=d;
    }
    double strength;

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }
}
