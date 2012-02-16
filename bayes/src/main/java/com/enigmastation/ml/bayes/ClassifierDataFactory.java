package com.enigmastation.ml.bayes;

import java.util.Map;

public interface ClassifierDataFactory {
    Map<Object, Integer> buildCategories();

    Map<Object, Feature> buildFeatures();
}
