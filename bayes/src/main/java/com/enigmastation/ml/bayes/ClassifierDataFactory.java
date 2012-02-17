package com.enigmastation.ml.bayes;

import org.infinispan.Cache;

public interface ClassifierDataFactory {
    Cache<Object, Integer> buildCategories();

    Cache<Object, Feature> buildFeatures();
}
