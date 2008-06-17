package com.enigmastation.classifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public final class FeatureMap extends ConcurrentHashMap<String, ClassifierMap> implements Map<String, ClassifierMap> {
    static final ReentrantLock lock = new ReentrantLock();

    public ClassifierMap getFeature(String feature) {
            ClassifierMap cm = get(feature);
            if (cm == null) {
                put(feature, cm = new ClassifierMap());
            }
            return cm;
    }
}
