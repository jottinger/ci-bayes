package com.enigmastation.ml.bayes.persistence;

import com.enigmastation.ml.bayes.Feature;

public class InfinispanFeatureDataStore extends InfinispanDataStore<String, Feature> {
    public InfinispanFeatureDataStore() {
        setCacheName("feature");
    }

    @Override
    public Feature getDefault(Object k) {
        Feature f = super.getDefault(k);
        if (f == null) {
            f = new Feature();
            f.setFeature(k);
        }
        return f;
    }
}
