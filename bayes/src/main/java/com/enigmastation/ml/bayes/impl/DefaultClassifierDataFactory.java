package com.enigmastation.ml.bayes.impl;

import com.enigmastation.ml.bayes.ClassifierDataFactory;
import com.enigmastation.ml.bayes.Feature;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;

import java.io.IOException;

public class DefaultClassifierDataFactory implements ClassifierDataFactory {
    @Override
    public Cache<Object, Integer> buildCategories() {
        return getCache("categories");
    }

    private <K, V> Cache<K, V> getCache(String name) {
        try {
            return new DefaultCacheManager("bayes-cache.xml").getCache(name, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cache<Object, Feature> buildFeatures() {
        return getCache("features");
    }
}
