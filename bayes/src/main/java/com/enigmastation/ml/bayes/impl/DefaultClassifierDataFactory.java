package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.bayes.ClassifierDataFactory;
import com.enigmastation.ml.bayes.Feature;
import com.enigmastation.ml.common.collections.MapBuilder;
import com.enigmastation.ml.common.collections.ValueProvider;

import java.util.Map;

public class DefaultClassifierDataFactory implements ClassifierDataFactory {
    @Override
    public Map<Object, Integer> buildCategories() {
        return new MapBuilder().defaultValue(0).build();
    }

    @Override
    public Map<Object, Feature> buildFeatures() {
        return new MapBuilder().valueProvider(new ValueProvider<Object, Feature>() {

            @Override
            public Feature getDefault(Object k) {
                Feature f = new Feature();
                f.setFeature(k);
                f.setCategories(new MapBuilder().defaultValue(0).<Object, Integer>build());
                return f;
            }

            @Override
            public Map<Object, Feature> load() {
                return null;
            }
        }).build();
    }
}
