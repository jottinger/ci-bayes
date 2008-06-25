package com.enigmastation.neuralnet;

import com.enigmastation.neuralnet.impl.dao.openspaces.model.Linkage;

import java.util.Collection;

public interface NeuralNetDAO extends Resolver {
    Linkage getLinkage(int layer, Integer origin, Integer dest);

    void setStrength(int layer, Integer origin, Integer dest, double v);

    Collection<? extends Integer> getHiddenIds(int i, Integer o);

    Linkage[] getLinkages(int layer);
}

