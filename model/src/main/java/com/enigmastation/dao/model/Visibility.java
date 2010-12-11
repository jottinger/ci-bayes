package com.enigmastation.dao.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: 11/5/10
 * Time: 5:16 AM
 * To change this template use File | Settings | File Templates.
 */
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
