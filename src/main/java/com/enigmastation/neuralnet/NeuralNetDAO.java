package com.enigmastation.neuralnet;

import com.enigmastation.neuralnet.model.Linkage;
import com.enigmastation.neuralnet.model.Layer;

import java.util.Collection;

public interface NeuralNetDAO extends Resolver {
    Linkage getLinkage(Layer layer, Integer origin, Integer dest);

    void setStrength(Layer layer, Integer origin, Integer dest, double v);

    Collection<? extends Integer> getHiddenIds(Layer layer, Integer o);

    Linkage[] getLinkages(Layer layer);
}

