package com.enigmastation.classifier;

import javolution.util.FastMap;

import java.util.Map;

public final class FeatureMap extends FastMap<String, ClassifierMap> implements Map<String, ClassifierMap> {
    public ClassifierMap getFeature(String feature) {
        ClassifierMap cm=get(feature);
        if(cm==null) {
            put(feature, cm=new ClassifierMap());
        }
        return cm;        
    }
}
