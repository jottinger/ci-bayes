package com.enigmastation.neuralnet.impl;

import com.enigmastation.neuralnet.model.Linkage;
import com.enigmastation.neuralnet.model.Layer;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Dec 29, 2008
 * Time: 4:24:19 AM
 * <p/>
 * <p>This class is licensed under the Apache Software License, available at
 * <a href="http://www.apache.org/licenses/LICENSE-2.0.html">http://www.apache.org/licenses/LICENSE-2.0.html</a>.
 * No guarantees are made for fitness of use for any purpose whatsoever, and no responsibility is assigned to
 * its author for the results of any use. Note section 7 of the ASL 2.0, please, and if someone dies because of
 * this class, I'm sorry, but it's not my fault. I warned you.
 */
public class BaseLinkage implements Linkage {
    Double strength;
    Integer fromId;
    Integer toId;
    Layer layer;

    public BaseLinkage() {
    }

    public BaseLinkage(Layer layer, Integer fromId, Integer toId, Double strength) {
        this.strength = strength;
        this.fromId = fromId;
        this.toId = toId;
        this.layer = layer;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(Double strength) {
        this.strength = strength;
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public Integer getToId() {
        return toId;
    }

    public void setToId(Integer toId) {
        this.toId = toId;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }
}
