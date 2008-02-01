package com.enigmastation.classifier;

import javolution.util.FastMap;

import java.util.Map;

public class ClassifierMap extends FastMap<String, Integer> implements Map<String, Integer> {
    private long totalCount;

    public void incrementCategory(String category) {
        int i=1;
        if(containsKey(category)) {
            i=i+get(category).intValue();
        }
        put(category, i);
        totalCount+=1;
    }

    public double getTotalCount() {
        return totalCount;
    }
}
