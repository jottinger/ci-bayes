package com.enigmastation.classifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class FeatureMap extends ConcurrentHashMap<String, ClassifierMap> implements Map<String, ClassifierMap> {
    static final ReentrantLock lock = new ReentrantLock();

    public ClassifierMap getFeature(String feature) {
            ClassifierMap cm = get(feature);
            if (cm == null) {
                put(feature, cm = createEmptyClassifierMap());
            }
            return cm;
    }

    public ClassifierMap createEmptyClassifierMap() {
        return new ClassifierMap();
    }
}
