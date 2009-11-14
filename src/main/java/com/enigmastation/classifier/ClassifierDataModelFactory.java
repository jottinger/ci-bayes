package com.enigmastation.classifier;

import java.util.Map;

public interface ClassifierDataModelFactory {
    public Map<String,Integer> getCategoryCountMap();
    public Map<String,Integer> getFeatureMap(String feature);
}
