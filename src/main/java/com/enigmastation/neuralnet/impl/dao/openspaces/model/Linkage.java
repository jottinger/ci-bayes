package com.enigmastation.neuralnet.impl.dao.openspaces.model;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceProperty;

@SpaceClass
public class Linkage implements com.enigmastation.neuralnet.model.Linkage {
    /**
     * can't look for an "undefined layer"
     */
    int layer;
    Integer fromId;
    Integer toId;
    Double strength;

    public String toString() {
        return "Linkage[layer=" + layer + ",fromId=" + fromId + ",toId=" + toId + ",strength=" + strength + "]";
    }

    public Linkage(int layer, Integer origin, Integer dest) {
        setLayer(layer);
        setFromId(origin);
        setToId(dest);
    }

    public Linkage() {
    }

    @SpaceProperty
    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    @SpaceProperty
    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    @SpaceProperty
    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    @SpaceProperty
    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }
}
