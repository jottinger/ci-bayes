package com.enigmastation.neuralnet.model;

import com.gigaspaces.annotation.pojo.SpaceProperty;

/**
 * Created by IntelliJ IDEA.
 * User: joeo
 * Date: Jun 25, 2008
 * Time: 12:29:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Linkage {
    @SpaceProperty
    int getLayer();

    void setLayer(int layer);

    @SpaceProperty
    Integer getFromId();

    void setFromId(Integer fromId);

    @SpaceProperty
    Integer getToId();

    void setToId(Integer toId);

    @SpaceProperty
    Double getStrength();

    void setStrength(Double strength);
}
