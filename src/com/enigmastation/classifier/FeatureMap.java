package com.enigmastation.classifier;

import javolution.util.FastMap;

import java.util.Map;

public final class FeatureMap extends FastMap<String, ClassifierMap> implements Map<String, ClassifierMap> {
    public ClassifierMap getFeature(String feature) {
        if(!containsKey(feature)) {
            put(feature, new ClassifierMap());
        }
        return get(feature);
    }
}
