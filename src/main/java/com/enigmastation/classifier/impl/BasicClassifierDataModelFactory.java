package com.enigmastation.classifier.impl;

import com.enigmastation.classifier.ClassifierDataModelFactory;
import com.enigmastation.classifier.ClassifierMap;
import com.enigmastation.classifier.FeatureMap;

public class BasicClassifierDataModelFactory implements ClassifierDataModelFactory {
    public FeatureMap buildFeatureMap() {
        return new FeatureMap();
    }

    public ClassifierMap buildClassifierMap() {
        return new ClassifierMap();
    }
}
