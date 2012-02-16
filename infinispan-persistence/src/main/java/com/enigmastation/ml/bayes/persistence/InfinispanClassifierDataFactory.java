package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.bayes.ClassifierDataFactory;
import com.enigmastation.ml.bayes.Feature;
import com.enigmastation.ml.common.collections.MapBuilder;

import java.util.Map;

public class InfinispanClassifierDataFactory implements ClassifierDataFactory {
    InfinispanCategoryDataStore categories = new InfinispanCategoryDataStore();
    InfinispanFeatureDataStore features = new InfinispanFeatureDataStore();

    @Override
    public Map<Object, Integer> buildCategories() {
        return new MapBuilder().valueProvider(categories).dataStore(categories).build();
    }

    @Override
    public Map<Object, Feature> buildFeatures() {
        return new MapBuilder().valueProvider(features).dataStore(features).build();
    }
}
