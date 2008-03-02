package com.enigmastation.classifier;

import javolution.util.FastMap;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public final class FeatureMap extends FastMap<String, ClassifierMap> implements Map<String, ClassifierMap> {
    static final ReentrantLock lock = new ReentrantLock();

    public ClassifierMap getFeature(String feature) {
        lock.lock();
        try {
            ClassifierMap cm = get(feature);
            if (cm == null) {
                put(feature, cm = new ClassifierMap());
            }
            lock.unlock();
            return cm;

        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
