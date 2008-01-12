package com.enigmastation.recommendations;

/**
 * Created by IntelliJ IDEA.
 * User: jottinger
 * Date: Jan 10, 2008
 * Time: 4:02:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tuple {
    Object key;
    Double value;

    public Tuple() {

    }
    public Tuple(Object key, Double value) {
        setKey(key);
        setValue(value);
    }
    
    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    
    public String toString() {
        return "Tuple["+key.toString()+","+value+"]";
    }
}
