package com.enigmastation.ml.bayes.persistence;

public class InfinispanCategoryDataStore extends InfinispanDataStore<String, Integer> {
    public InfinispanCategoryDataStore() {
        setCacheName("category");
    }

    @Override
    public Integer getDefault(Object k) {
        Integer i = super.getDefault(k);
        if (i == null) {
            return 0;
        }
        return i;
    }
}
