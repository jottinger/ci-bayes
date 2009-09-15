package com.enigmastation.classifier;

public interface ClassifierDataModelFactory {
    FeatureMap buildFeatureMap();

    ClassifierMap buildClassifierMap();
}
