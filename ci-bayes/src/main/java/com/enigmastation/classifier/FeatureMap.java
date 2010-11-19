package com.enigmastation.classifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FeatureMap extends ConcurrentHashMap<String, ClassifierMap> implements Map<String, ClassifierMap> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 9171104455176142182L;

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
