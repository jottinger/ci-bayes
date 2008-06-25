package com.enigmastation.neuralnet.model;

public interface Linkage {
    int getLayer();

    void setLayer(int layer);

    Integer getFromId();

    void setFromId(Integer fromId);

    Integer getToId();

    void setToId(Integer toId);

    Double getStrength();

    void setStrength(Double strength);
}
