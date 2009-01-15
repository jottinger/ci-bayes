package com.enigmastation.neuralnet.model;

public interface Linkage {
    Layer getLayer();

    void setLayer(Layer layer);

    Integer getFromId();

    void setFromId(Integer fromId);

    Integer getToId();

    void setToId(Integer toId);

    Double getStrength();

    void setStrength(Double strength);
}
